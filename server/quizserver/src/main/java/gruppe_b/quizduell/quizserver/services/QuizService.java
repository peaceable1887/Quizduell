package gruppe_b.quizduell.quizserver.services;

import java.time.Instant;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.game.QuizSession;
import gruppe_b.quizduell.application.interfaces.SendToPlayerService;
import gruppe_b.quizduell.application.interfaces.StartQuiz;
import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.application.questions.queries.GetQuestionRandomQueryHandler;
import gruppe_b.quizduell.common.enums.PlayerStatus;
import gruppe_b.quizduell.common.exceptions.JwtIsExpiredException;
import gruppe_b.quizduell.common.exceptions.UnknownPlayerStatusException;
import gruppe_b.quizduell.quizserver.exceptions.JwtNotIssuedByLobbyServerException;
import gruppe_b.quizduell.quizserver.exceptions.PlayerAlreadyConnectedException;
import gruppe_b.quizduell.quizserver.exceptions.PlayerAlreadyInOtherGameException;
import gruppe_b.quizduell.quizserver.exceptions.QuizNotFoundException;

/**
 * Service zum Managen von Quiz-Games.
 * 
 * TODO: QuizService und LobbyService teilen sich viel Code, den man generisch
 * umbauen kann...
 * 
 * @author Christopher Burmeister
 */
@Service
public class QuizService implements StartQuiz {

    private final ConcurrentHashMap<UUID, Quiz> quizRepo;

    private final ConcurrentHashMap<UUID, Player> playerRepo;

    private final ConcurrentHashMap<UUID, QuizSession> sessionRepo;

    @Autowired
    private SendToPlayerService sendToPlayerService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    GetQuestionRandomQueryHandler getQuestionRandomQueryHandler;

    @Autowired
    JwtDecoder jwtDecoder;

    public QuizService() {
        this.quizRepo = new ConcurrentHashMap<>();
        this.playerRepo = new ConcurrentHashMap<>();
        this.sessionRepo = new ConcurrentHashMap<>();
    }

    /**
     * Spieler mit einem Spiel verbinden.
     * Existiert das Spiel noch nicht, wird es erstellt und der Spieler verbunden.
     * 
     * @param lobbyId  id der Lobby, aus der das Spiel gestartet wird.
     * @param playerId id des Spieler, der sich verbinden möchte.
     * @param token    JWT zum Nachweis, dass das Spiel über eine Lobby erstellt
     *                 wurde.
     * @return das Spiel.
     * @throws PlayerAlreadyConnectedException
     * @throws PlayerAlreadyInOtherGameException
     */
    public Quiz connectToQuiz(UUID lobbyId, UUID playerId, String playerName, String token)
            throws PlayerAlreadyConnectedException,
            PlayerAlreadyInOtherGameException,
            JwtNotIssuedByLobbyServerException,
            JwtIsExpiredException {

        /* Prüfen ob der Token vom Lobby-Server erstellt wurde und gültig ist. */
        Jwt jwt = jwtDecoder.decode(token);
        if (jwt.getExpiresAt() == null) {
            throw new JwtIsExpiredException("Missing expires at date!");
            /*
             * Der Token ist gültig bis 12:05 und es ist jetzt 12:02,
             * dann muss geprüft werden, ob die Gültigkeit vor der
             * aktuellen Zeit liegt. Ist es 12:06, ist der Token nicht
             * mehr gültig.
             */
        } else if (jwt.getExpiresAt().isBefore(Instant.now())) {
            throw new JwtIsExpiredException("Token is expired: " + jwt.getExpiresAt());
        }
        String issuer = jwt.getClaimAsString("iss");
        if (!issuer.equals("quizduell_lobbyserver")) {
            throw new JwtNotIssuedByLobbyServerException("Wrong JWT issuer! JWT issuer: " + jwt.getIssuer());
        }

        Quiz quiz;

        // Gibt es das Spiel schon?
        if (quizRepo.containsKey(lobbyId)) {
            // Ja.
            quiz = quizRepo.get(lobbyId);
        } else {
            // Nein. Spiel erstellen.
            quiz = new Quiz(lobbyId);
            quizRepo.put(lobbyId, quiz);
        }

        Player player = playerRepo.get(playerId);

        // Ist der Spieler schon in dem Spiel oder einem anderen?
        if (player != null && quiz.getPlayers().contains(player)) {
            // Spieler ist bereits in dem Spiel.
            throw new PlayerAlreadyConnectedException("Player already connected to the game!");
        } else if (player != null) {
            // Spieler ist in einem anderen Spiel.
            throw new PlayerAlreadyInOtherGameException("Player already connected to other game!");
        } else {
            // Nein. Spieler dem Spiel hinzufügen.
            Player newPlayer = quiz.addPlayer(playerId, playerName);
            playerRepo.put(playerId, newPlayer);
        }

        return quiz;
    }

    /**
     * Eine Quiz oder eine laufende QuizSession abbrechen.
     * 
     * @param lobbyId LobbyId zu der QuizSession
     * @return true wenn das Quiz gefunden und abgebrochen wurde.
     */
    public boolean cancelQuiz(UUID lobbyId) {
        boolean found = false;

        Quiz quiz = quizRepo.get(lobbyId);
        QuizSession session = sessionRepo.get(lobbyId);

        if (quiz != null) {
            quiz.cancel();
            found = true;

            for (Player player : quiz.getPlayers()) {
                playerRepo.remove(player.getUserId());
            }

            simpMessagingTemplate.convertAndSend("/topic/quiz/" + quiz.getLobbyId(), quiz);
        }

        if (session != null) {
            session.cancel();
            return true;
        }

        return found;
    }

    public Quiz getQuiz(UUID lobbyId) {
        return quizRepo.get(lobbyId);
    }

    public QuizSession getSession(UUID lobbyId) {
        return sessionRepo.get(lobbyId);
    }

    public Quiz updatePlayerStatus(UUID lobbyId, UUID playerId, PlayerStatus status)
            throws UnknownPlayerStatusException, QuizNotFoundException {
        Quiz quiz = getQuiz(lobbyId);

        if (quiz == null) {
            throw new QuizNotFoundException("Quiz not Found! LobbyId:" + lobbyId.toString());
        }

        Player player = quiz.getPlayer(playerId);

        if (status == PlayerStatus.READY) {
            player.setReady();
        } else if (status == PlayerStatus.WAIT) {
            player.setWait();
        } else {
            throw new UnknownPlayerStatusException(status.toString());
        }

        // Alle Spieler ready?
        if (quiz.allPlayersReady()) {
            // Spieler sind ready. Start countdown.
            startQuizCountdown(quiz);
        }

        return quiz;
    }

    private void startQuizCountdown(Quiz quiz) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new QuizStartCountDown(
                quiz,
                simpMessagingTemplate,
                this), 1_000, 1_000);
    }

    /**
     * Ein Quiz starten.
     * Es wird eine QuizSession erstellt und gestartet.
     * 
     * @param quiz Quiz für das eine Session gestartet werden soll.
     */
    public void startQuiz(Quiz quiz) {
        QuizSession session = new QuizSession(quiz, sendToPlayerService, getQuestionRandomQueryHandler);
        sessionRepo.put(quiz.getLobbyId(), session);
        session.start();
    }
}
