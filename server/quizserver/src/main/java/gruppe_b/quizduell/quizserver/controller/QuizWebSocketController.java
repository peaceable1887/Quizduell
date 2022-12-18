package gruppe_b.quizduell.quizserver.controller;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

import gruppe_b.quizduell.application.game.QuizSession;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.common.dto.PlayerStatusDto;
import gruppe_b.quizduell.common.enums.PlayerStatus;
import gruppe_b.quizduell.common.exceptions.AttributeNullException;
import gruppe_b.quizduell.common.exceptions.UnknownPlayerStatusException;
import gruppe_b.quizduell.quizserver.exceptions.AnswerOutOfRangeException;
import gruppe_b.quizduell.quizserver.exceptions.QuizNotFoundException;
import gruppe_b.quizduell.quizserver.exceptions.QuizSessionNotFoundException;
import gruppe_b.quizduell.quizserver.services.QuizService;

@Controller
@ControllerAdvice
public class QuizWebSocketController {

    @Autowired
    QuizService quizService;

    /**
     * Wird der Endpunkt für ein spezifisches Quiz abonniert, wird dieses beim
     * abonnieren zurückgegeben.
     * 
     * @param lobbyId Id der Lobby, über die das Quiz erstellt wurde.
     * @return abonniertes Quiz
     */
    @SubscribeMapping("/quiz/{lobbyId}")
    public Quiz subscribeQuizId(@DestinationVariable String lobbyId) {
        return quizService.getQuiz(UUID.fromString(lobbyId));
    }

    /**
     * Endpunkt zum Updaten des Status eines Users.
     * Wird ein Status-Update an den Endpunkt gesendet, wird der neue Status des
     * Quiz gesendet.
     * 
     * @param lobbyId   id der Lobby, über die das Quiz erstellt wurde.
     * @param status    Status Objekt des Spielers/ Client. Erlaubte werte: 'ready',
     *                  'wait'.
     * @param principal Principal Objekt des Clients/ Senders über das die UserId
     *                  gelesen wird.
     * @return neuer Status des Quiz
     * @throws UnknownPlayerStatusException gesendeter Status des Spielers/ Client
     *                                      unbekannt/ nicht erlaubt
     * @throws AttributeNullException       fehlendes Attribut im 'status' Objekt
     * @throws QuizNotFoundException
     */
    @MessageMapping("/quiz/{lobbyId}/status-player")
    @SendTo("/topic/quiz/{lobbyId}")
    public Quiz playerStatus(@DestinationVariable String lobbyId, PlayerStatusDto status,
            Principal principal)
            throws UnknownPlayerStatusException, AttributeNullException, QuizNotFoundException {

        PlayerStatus playerStatus;

        if (status.status == null) {
            throw new AttributeNullException("status null");
        }

        if (status.status.compareTo("ready") == 0) {
            playerStatus = PlayerStatus.READY;
        } else if (status.status.compareTo("wait") == 0) {
            playerStatus = PlayerStatus.WAIT;
        } else {
            throw new UnknownPlayerStatusException(status.status);
        }

        return quizService.updatePlayerStatus(UUID.fromString(lobbyId),
                UUID.fromString(principal.getName()), playerStatus);
    }

    @MessageMapping("/quiz/session/{lobbyId}/answer")
    public void answer(@DestinationVariable String lobbyId, int answer, Principal principal)
            throws QuizSessionNotFoundException, AnswerOutOfRangeException {
        QuizSession session = quizService.getSession(UUID.fromString(lobbyId));

        if (session == null) {
            throw new QuizSessionNotFoundException("QuizSession not found! LobbyId: " + lobbyId);
        }

        if (answer < 1 || answer > 4) {
            throw new AnswerOutOfRangeException("Answer out of range: " + answer);
        }

        session.playerAnswer(UUID.fromString(principal.getName()), answer);
    }
}
