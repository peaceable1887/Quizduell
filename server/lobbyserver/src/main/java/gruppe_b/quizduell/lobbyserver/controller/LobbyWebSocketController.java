package gruppe_b.quizduell.lobbyserver.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpAttributes;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpSessionScope;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import gruppe_b.quizduell.common.models.Player;
import gruppe_b.quizduell.lobbyserver.common.LobbyStartDto;
import gruppe_b.quizduell.lobbyserver.common.PlayerStatusDto;
import gruppe_b.quizduell.lobbyserver.exceptions.UnknownPlayerStatusException;
import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;

@Controller
public class LobbyWebSocketController {

    @Autowired
    LobbyService lobbyService;

    @SubscribeMapping("/new-lobby")
    // @SendTo("/topic/new-lobby")
    public Collection<Lobby> subscribeLobby() {
        return lobbyService.getAllLobbies();
    }

    @SubscribeMapping("/lobby/{lobbyId}")
    // @SendTo("/topic/lobby/{lobbyId}")
    public Lobby subscribeLobbyId(@DestinationVariable String lobbyId) {
        return lobbyService.getLobby(UUID.fromString(lobbyId));
    }

    @MessageMapping("/lobby/{lobbyId}/status")
    @SendTo("/topic/lobby/{lobbyId}")
    public Lobby playerStatus(@DestinationVariable String lobbyId, PlayerStatusDto status,
            Principal principal)
            throws UnknownPlayerStatusException {

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
}