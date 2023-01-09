package gruppe_b.quizduell.application.game;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gruppe_b.quizduell.application.common.GameSessionDto;
import gruppe_b.quizduell.application.common.GameSessionPlayerDto;
import gruppe_b.quizduell.application.common.GameSessionResult;
import gruppe_b.quizduell.application.common.PlayerRoundStatus;
import gruppe_b.quizduell.application.enums.QuizStatus;
import gruppe_b.quizduell.application.enums.RoundStatus;
import gruppe_b.quizduell.application.interfaces.SendToPlayerService;
import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.application.models.QuizPlayer;
import gruppe_b.quizduell.application.questions.queries.GetQuestionRandomQuery;
import gruppe_b.quizduell.application.questions.queries.GetQuestionRandomQueryHandler;
import gruppe_b.quizduell.application.services.StatsService;
import gruppe_b.quizduell.domain.entities.Question;

/**
 * Enthält die Spiellogik für ein Quiz über mehrere Runden.
 * 
 * @author Christopher Burmeister
 */
public class QuizSession extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(QuizSession.class);

    private static final int MAX_ROUNDS = 6;
    private static final int MAX_ROUND_LENGTH = 20;
    private static final int MAX_ROUND_LENGTH_AFTER_ANSWER = 6;
    private static final long ROUND_END_PAUSE_DURATION_MILLIS = 5_000;

    private final String threadName; // Für debugging Zwecke
    private final SendToPlayerService send;
    private final StatsService statsService;

    private final Quiz quiz;
    private boolean cancel = false;

    private int currentRoundNum = 0;
    private int currentMaxRoundLength;
    private boolean currentRoundClose = false;
    private Instant roundStartTime;

    private final int playerCount;
    private final List<QuizRound> roundList;

    private final Lock lock;

    private boolean sendUpdate = true;

    private GameSessionResult gameSessionResult;

    private GetQuestionRandomQueryHandler questionRandomHandler;

    /**
     * Erzeugt eine Quiz-Session aus einem Quiz Objekt.
     * 
     * @param quiz                          Quiz für das eine Quiz-Session erstellt
     *                                      werden soll.
     * @param sendToPlayerService           Service über den mit den Spielern
     *                                      Kommuniziert wird (Websocket)
     * @param getQuestionRandomQueryHandler Handler über den zufällige Quizfragen
     *                                      aus der Datenbank geholt werden.
     * @param statsService                  Service zum Updaten der Statistik der
     *                                      Spieler
     * 
     * @author Christopher Burmeister
     */
    public QuizSession(Quiz quiz,
            SendToPlayerService sendToPlayerService,
            GetQuestionRandomQueryHandler getQuestionRandomQueryHandler,
            StatsService statsService) {
        this.send = sendToPlayerService;
        this.threadName = quiz.getId().toString();
        this.quiz = quiz;
        this.questionRandomHandler = getQuestionRandomQueryHandler;
        this.playerCount = quiz.getPlayers().size();
        lock = new ReentrantLock(true);
        roundList = new ArrayList<>();
        this.statsService = statsService;
        initQuizSessionResult();
    }

    /**
     * Quiz starten
     */
    @Override
    public synchronized void start() {
        logger.info("starting session {} on thread: {}", quiz.getLobbyId(), threadName);
        super.start();
    }

    /**
     * Hauptschleife die über die Anzahl der Runden iteriert.
     */
    @Override
    public void run() {
        logger.info("running session {} on thread: {}", quiz.getLobbyId(), threadName);

        int lastSendetCountdown = MAX_ROUND_LENGTH + 1;

        // Schleife für 6 Runden
        for (int i = 0; i < MAX_ROUNDS; i++) {
            // neue Runde starten
            startNewRound();

            // Update-Schleife innerhalb einer Runde
            quizLoop(lastSendetCountdown);

            // Ende der aktuellen Runde
            endRound();

            try {
                // Warten bis zum Start der nächsten Runde.
                Thread.sleep(ROUND_END_PAUSE_DURATION_MILLIS);
            } catch (InterruptedException e) {

            }
        }

        endGame();
    }

    /**
     * Neue Runde starten
     */
    private void startNewRound() {
        logger.info("start new round in session {} on thread: {}", quiz.getLobbyId(), threadName);

        lock.lock();
        try {
            currentRoundNum++;
            roundList.add(new QuizRound(getNewQuestion()));
            roundStartTime = Instant.now();
            currentMaxRoundLength = MAX_ROUND_LENGTH;
            currentRoundClose = false;
            sendUpdate = true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Schleife für eine Einzelne Quizrunde. Prüft alle 100ms, ob es eine Änderung
     * gab, reagiert auf diese und sendet Updates an die Spieler.
     * 
     * @param lastSendetCountdown Variable für den letzten Countdown-Wert, der an
     *                            die Spiele gesendet wurde. Die Klassenvariable
     *                            wird von weiteren Methoden genutzt.
     */
    private void quizLoop(int lastSendetCountdown) {
        logger.info("loop in session {} on thread: {}", quiz.getLobbyId(), threadName);

        int countdown = 1;

        // Schleife für eine Runde
        while (countdown > 0) {
            lock.lock();

            try {
                if (cancel) {
                    return;
                }

                countdown = calcRemainingSeconds();

                // Gab es eine Antwort von einem Spieler und die Zeit muss noch verkürzt werden?
                if (getCurrentRound().getPlayerAnswered().size() == 1 &&
                        countdown > MAX_ROUND_LENGTH_AFTER_ANSWER) {
                    countdown = MAX_ROUND_LENGTH_AFTER_ANSWER;
                    currentMaxRoundLength = MAX_ROUND_LENGTH_AFTER_ANSWER + 1;
                    // Haben alle Spieler geantwortet?
                } else if (getCurrentRound().getPlayerAnswered().size() >= playerCount) {
                    break;
                }

                // Für jede vergangene Sekunde den Countdown an die Spieler senden
                if (lastSendetCountdown > countdown) {
                    logger.info("countdown {} in session {} on thread: {}", countdown, quiz.getLobbyId(), threadName);
                    send.sendRoundCountdown(quiz.getLobbyId(), countdown);
                    lastSendetCountdown = countdown;
                }

                // Update der aktuellen Runde schicken, wenn ein Update aussteht
                if (sendUpdate) {
                    send.sendGameSessionUpdate(quiz.getLobbyId(), createGameSessionDto());
                    sendUpdate = false;
                }
            } finally {
                lock.unlock();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * Aktuelle runde abschließen
     */
    private void endRound() {
        logger.info("session {} on thread {} end round {}", quiz.getLobbyId(), threadName, currentRoundNum);

        lock.lock();
        try {
            if (cancel) {
                return;
            }

            currentRoundClose = true;
            GameSessionDto dto = createGameSessionDto();
            dto.roundStatus = RoundStatus.CLOSE;
            dto.correctAnswer = getCurrentRound().getQuestion().getCorrectAnswer();

            // Antworten der Spieler in das dto übernehmen
            for (GameSessionPlayerDto playerDto : dto.playerList) {
                QuizPlayer quizPlayer = getCurrentRound().getPlayerAnswered().get(playerDto.playerId);
                if (quizPlayer != null) {
                    playerDto.chosenAnswer = quizPlayer.getAnswer();
                }
            }

            send.sendGameSessionUpdate(quiz.getLobbyId(), dto);
        } finally {
            lock.unlock();
        }
    }

    private void endGame() {
        quiz.setQuizFinish();

        updateStatsInDb();

        logger.info("finish session {} on thread: {}", quiz.getLobbyId(), threadName);
    }

    /**
     * Quiz abbrechen
     */
    public void cancel() {
        logger.info("cancel session {} on thread: {}", quiz.getLobbyId(), threadName);

        lock.lock();

        try {
            if (cancel || quiz.getQuizStatus() == QuizStatus.FINISH) {
                return;
            }

            cancel = true;

            GameSessionDto dto = createGameSessionDto();
            dto.roundStatus = RoundStatus.ABORT;

            quiz.setQuizAbort();

            send.sendQuizAbort(quiz.getLobbyId(), dto);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Antwort eines Spielers an die QuizSession übergeben.
     * Antwort wird in der quizLoop verarbeitet.
     * 
     * @param playerId Id des Spielers
     * @param answer   Nummer der Antwort, die der Spieler gewählt hat.
     */
    public void playerAnswer(UUID playerId, int answer) {
        logger.info("player {} send answer {}", playerId, answer);

        Player playerObj = quiz.getPlayer(playerId);

        if (playerObj == null) {
            return;
        }

        QuizPlayer player = new QuizPlayer(playerObj);

        lock.lock();

        try {
            // Wurde das Quiz abgebrochen? Läuft die aktuelle Runde noch?
            if (cancel || currentRoundClose) {
                return;
            }

            // Antwort des Spielers setzen
            player.setAnswer(answer);

            // hat der Spieler bereits geantwortet?
            if (getCurrentRound().getPlayerAnswered().containsKey(player.getUserId())) {
                return;
            }

            // Ist die Antwort correct? Dann bekommt der Spieler einen Punkt...
            if (getCurrentRound().getQuestion().getCorrectAnswer() == answer) {
                gameSessionResult.addOnePoint(playerId);
            }

            // Spieler in das Array (der aktuellen Runde) der Spieler einfügen die
            // geantwortet haben.
            getCurrentRound().getPlayerAnswered().put(player.getUserId(), player);

            if (calcRemainingSeconds() > 0) {
                sendUpdate = true;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Zufällige Frage aus der Datenbank holen
     * 
     * @return Quizfrage
     */
    private Question getNewQuestion() {
        return questionRandomHandler.handle(new GetQuestionRandomQuery());
    }

    /**
     * Datenobjekt mit den aktuellen Daten einer Runde erstellen.
     * Objekt wird genutzt um es an die Spieler zu schicken.
     * 
     * @return Datenobjekt mit den aktuellen Spieldaten
     */
    private GameSessionDto createGameSessionDto() {
        QuizRound round = getCurrentRound();
        return createGameSessionDto(round);
    }

    /**
     * Gibt die aktuelle Runde zurück, in dem sich das Quiz befindet.
     * 
     * @return Aktuelle Runde
     */
    public QuizRound getCurrentRound() {
        return roundList.get(roundList.size() - 1);
    }

    /**
     * Berechnet die verbleibenden Sekunden, bis die aktuelle Runde zuende ist.
     * 
     * @return Sekunden bis zum Ende der Runde
     */
    public int calcRemainingSeconds() {
        int passSeconds = (int) roundStartTime.until(Instant.now(), ChronoUnit.SECONDS);
        return currentMaxRoundLength - passSeconds;
    }

    /**
     * Spielerstatistik in der Datenbank aktualisieren.
     */
    public void updateStatsInDb() {
        try {
            gameSessionResult.endQuiz();

            send.sendQuizResult(quiz.getLobbyId(), gameSessionResult);

            statsService.createOrUpdatePlayerStats(gameSessionResult);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void initQuizSessionResult() {
        gameSessionResult = new GameSessionResult();
        for (Player player : quiz.getPlayers()) {
            gameSessionResult.addPlayer(player.getUserId(), player.getName());
        }
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public List<GameSessionDto> createGameSessionDtoList() {
        List<GameSessionDto> list = new ArrayList<>();

        for (QuizRound round : roundList) {
            list.add(createGameSessionDto(round));
        }

        return list;
    }

    /**
     * Datenobjekt mit den Daten einer Runde erstellen.
     * 
     * @return Datenobjekt mit den Spieldaten
     */
    private GameSessionDto createGameSessionDto(QuizRound round) {
        GameSessionDto dto = new GameSessionDto();

        if (currentRoundClose) {
            dto.roundStatus = RoundStatus.CLOSE;
        } else {
            dto.roundStatus = RoundStatus.OPEN;
        }

        dto.currentRound = currentRoundNum;
        dto.maxRounds = MAX_ROUNDS;

        dto.questionText = round.getQuestion().getQuestionText();
        dto.answerOne = round.getQuestion().getAnswerOne();
        dto.answerTwo = round.getQuestion().getAnswerTwo();
        dto.answerThree = round.getQuestion().getAnswerThree();
        dto.answerFour = round.getQuestion().getAnswerFour();

        ArrayList<GameSessionPlayerDto> playerDtoList = new ArrayList<>();

        for (Player player : quiz.getPlayers()) {
            GameSessionPlayerDto playerDto = new GameSessionPlayerDto();
            playerDto.playerId = player.getUserId();
            playerDto.name = player.getName();

            if (getCurrentRound().getPlayerAnswered().containsKey(player.getUserId())) {
                playerDto.playerRoundStatus = PlayerRoundStatus.FINISH;
            } else {
                playerDto.playerRoundStatus = PlayerRoundStatus.GUESS;
            }

            playerDtoList.add(playerDto);
        }

        dto.playerList = playerDtoList;

        return dto;
    }
}
