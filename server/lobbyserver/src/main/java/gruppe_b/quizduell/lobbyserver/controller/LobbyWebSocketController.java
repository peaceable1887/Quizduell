package gruppe_b.quizduell.lobbyserver.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpAttributes;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpSessionScope;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.socket.WebSocketMessage;

import gruppe_b.quizduell.common.models.Player;
import gruppe_b.quizduell.lobbyserver.common.LobbyStartDto;
import gruppe_b.quizduell.lobbyserver.common.PlayerStatusDto;
import gruppe_b.quizduell.lobbyserver.exceptions.AttributeNullException;
import gruppe_b.quizduell.lobbyserver.exceptions.UnknownPlayerStatusException;
import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;

/**
 * WebSocket-Controller für den Datenaustausch zu Lobbies.
 * Datenaustausch erfolgt über STOMP als Message-Broker.
 * 
 * @author Christopher Burmeister
 */
@Controller
@ControllerAdvice
public class LobbyWebSocketController {

    @Autowired
    LobbyService lobbyService;

    /**
     * Wird der Endpunkt abonniert, werden alle Lobbies an den Client gesendet.
     * 
     * @return alle Lobbies
     */
    @SubscribeMapping("/new-lobby")
    public Collection<Lobby> subscribeLobby() {
        return lobbyService.getAllLobbies();
    }

    /**
     * Wird der Endpunkt für eine spezifische Lobby abonniert, wird diese beim
     * abonnierten zurückgegeben.
     * 
     * @param lobbyId id für die zu abonnierende Lobby
     * @return abonnierte Lobby
     */
    @SubscribeMapping("/lobby/{lobbyId}")
    public Lobby subscribeLobbyId(@DestinationVariable String lobbyId) {
        return lobbyService.getLobby(UUID.fromString(lobbyId));
    }

    /**
     * Endpunkt zum Updaten des Status eines User.
     * Wird ein Status-Update an den Endpunkt gesendet, wird der neue Status der
     * Lobby gesendet.
     * 
     * @param lobbyId   id der Lobby
     * @param status    Status Objekt des Spielers/ Client. Erlaubte werte: 'ready',
     *                  'wait'.
     * @param principal Principal Objekt des Clients/ Senders über das die UserId
     *                  gelesen wird.
     * @return neuer Status der Lobby
     * @throws UnknownPlayerStatusException gesendeter Status des Spielers/ Client
     *                                      unbekannt/ nicht erlaubt
     * @throws AttributeNullException       fehlendes Attribut im 'status' Objekt
     */
    @MessageMapping("/lobby/{lobbyId}/status")
    @SendTo("/topic/lobby/{lobbyId}")
    public Lobby playerStatus(@DestinationVariable String lobbyId, PlayerStatusDto status,
            Principal principal)
            throws UnknownPlayerStatusException, AttributeNullException {

        if (status.status == null) {
            throw new AttributeNullException("status null");
        }

        Lobby lobby = lobbyService.getLobby(UUID.fromString(lobbyId));
        Player player = lobby.getPlayer(UUID.fromString(principal.getName()));

        if (status.status.compareTo("ready") == 0) {
            player.setReady();
        } else if (status.status.compareTo("wait") == 0) {
            player.setWait();
        } else {
            throw new UnknownPlayerStatusException(status.status);
        }

        return lobby;
    }

    @SendTo("/topic/lobby/{lobbyId}/start")
    public LobbyStartDto lobbyStart() {
        LobbyStartDto dto = new LobbyStartDto();
        dto.status = "start";
        return dto;
    }

    @SendTo("/topic/lobby/{lobbyId}/start")
    public LobbyStartDto lobbyAbort() {
        LobbyStartDto dto = new LobbyStartDto();
        dto.status = "abort";
        return dto;
    }

    @SubscribeMapping("/test")
    public String subTest() {
        return "hello form @SubscribeMapping /test";
    }

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String test(String msg) {
        return "hello from @MessageMapping /test";
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Exception e) {
        return e.getMessage();
    }
}