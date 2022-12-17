package gruppe_b.quizduell.application.game;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import gruppe_b.quizduell.application.common.GameSessionDto;
import gruppe_b.quizduell.application.common.GameSessionPlayerDto;
import gruppe_b.quizduell.application.common.PlayerRoundStatus;
import gruppe_b.quizduell.application.enums.RoundStatus;
import gruppe_b.quizduell.application.interfaces.SendToPlayerService;
import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.application.models.QuizPlayer;
import gruppe_b.quizduell.domain.entities.Question;

public class QuizSession extends Thread {

    private String threadName;
    private final SendToPlayerService send;

    private final Quiz quiz;
    private boolean cancel = false;

    private final int maxRounds = 6;
    private int currentRoundNum = 0;
    private int maxRoundLength = 20;
    private int maxRoundLengthAfterAnswer = 6;
    private int currentMaxRoundLength;
    private boolean currentRoundClose = false;
    private Instant roundStartTime;

    private final int playerCount;
    private final List<QuizRound> roundList;

    private final Lock lock;

    private boolean sendUpdate = true;

    public QuizSession(Quiz quiz, SendToPlayerService sendToPlayerService) {
        this.send = sendToPlayerService;
        this.threadName = quiz.getId().toString();
        this.quiz = quiz;
        this.playerCount = quiz.getPlayers().size();
        lock = new ReentrantLock(true);
        roundList = new ArrayList<>();
    }

    @Override
    public void run() {
        System.out.println("Running " + threadName);

        int lastSendetCountdown = maxRoundLength + 1;

        // Schleife für 6 Runden
        for (int i = 0; i < maxRounds; i++) {
            // neue Runde starten
            nextRound();

            int countdown = 1;

            // Schleife für eine Runde
            while (countdown > 0) {
                if (cancel) {
                    return;
                }

                try {
                    if (lock.tryLock()) {
                        countdown = calcRemainingSeconds();

                        // Gab es eine Antwort von einem Spieler und die Zeit muss noch verkürzt werden?
                        if (getCurrentRoundNum().getPlayerAnswered().size() == 1 &&
                                countdown > maxRoundLengthAfterAnswer) {
                            countdown = maxRoundLengthAfterAnswer;
                            currentMaxRoundLength = maxRoundLengthAfterAnswer + 1;
                            // Haben alle Spieler geantwortet?
                        } else if (getCurrentRoundNum().getPlayerAnswered().size() >= playerCount) {
                            break;
                        }

                        // Für jede vergangene Sekunde den Countdown an die Spieler senden
                        if (lastSendetCountdown > countdown) {
                            send.sendRoundCountdown(quiz.getLobbyId(), countdown);
                            lastSendetCountdown = countdown;
                        }

                        // Update der aktuellen Runde schicken, wenn ein Update aussteht
                        if (sendUpdate) {
                            send.sendGameSessionUpdate(quiz.getLobbyId(), createGameSessionDto());
                            sendUpdate = false;
                        }
                    }
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            }

            // Ende der aktuellen Runde
            endRound();

            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {

            }
        }

        System.out.println("Thread " + threadName + " exiting.");
    }

    @Override
    public void start() {
        System.out.println("Starting " + threadName);
        super.start();
    }

    public void cancel() {
        cancel = true;
    }

    public void playerAnswer(UUID playerId, int answer) {
        Player playerObj = quiz.getPlayer(playerId);

        if (playerObj == null) {
            return;
        }

        QuizPlayer player = new QuizPlayer(playerObj);

        try {
            if (lock.tryLock()) {
                if (cancel || currentRoundClose) {
                    return;
                }

                player.setAnswer(answer);

                if (getCurrentRoundNum().getPlayerAnswered().containsKey(player.getUserId())) {
                    return;
                }

                getCurrentRoundNum().getPlayerAnswered().put(player.getUserId(), player);

                if (calcRemainingSeconds() > 0) {
                    sendUpdate = true;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void nextRound() {
        try {
            if (lock.tryLock()) {
                currentRoundNum++;
                roundList.add(new QuizRound(getNewQuestion()));
                roundStartTime = Instant.now();
                currentMaxRoundLength = maxRoundLength;
                currentRoundClose = false;
            }
        } finally {
            lock.unlock();
        }
    }

    private void endRound() {
        try {
            if (lock.tryLock()) {
                currentRoundClose = true;
                GameSessionDto dto = createGameSessionDto();
                dto.roundStatus = RoundStatus.CLOSE;
                send.sendGameSessionUpdate(quiz.getLobbyId(), dto);
            }
        } finally {
            lock.unlock();
        }
    }

    private Question getNewQuestion() {
        return new Question(
                UUID.randomUUID(),
                "testText",
                "antwort1",
                "antwort2",
                "antwort3",
                "antwort4",
                2);
    }

    private GameSessionDto createGameSessionDto() {
        GameSessionDto dto = new GameSessionDto();
        QuizRound round = getCurrentRoundNum();

        if (currentRoundClose) {
            dto.roundStatus = RoundStatus.CLOSE;
        } else {
            dto.roundStatus = RoundStatus.OPEN;
        }

        dto.currentRound = currentRoundNum;
        dto.maxRounds = maxRounds;

        dto.questionText = round.getQuestion().getQuestionText();
        dto.answerOne = round.getQuestion().getAnswerOne();
        dto.answerTwo = round.getQuestion().getAnswerTwo();
        dto.answerThree = round.getQuestion().getAnswerThree();
        dto.answerFour = round.getQuestion().getAnswerFour();

        ArrayList<GameSessionPlayerDto> playerDtoList = new ArrayList<>();

        for (Player player : quiz.getPlayers()) {
            GameSessionPlayerDto playerDto = new GameSessionPlayerDto();
            playerDto.playerId = player.getUserId();

            if (getCurrentRoundNum().getPlayerAnswered().containsKey(player.getUserId())) {
                playerDto.playerRoundStatus = PlayerRoundStatus.FINISH;
            } else {
                playerDto.playerRoundStatus = PlayerRoundStatus.GUESS;
            }

            playerDtoList.add(playerDto);
        }

        dto.playerList = playerDtoList;

        return dto;
    }

    private QuizRound getCurrentRoundNum() {
        return roundList.get(roundList.size() - 1);
    }

    public int calcRemainingSeconds() {
        int passSeconds = (int) roundStartTime.until(Instant.now(), ChronoUnit.SECONDS);
        return currentMaxRoundLength - passSeconds;
    }
}
