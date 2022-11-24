package gruppe_b.quizduell.lobbyserver.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;

@Controller
public class LobbyWebSocketController {

    @Autowired
    LobbyService lobbyService;

    @SubscribeMapping("/new-lobby")
    public Collection<Lobby> subscribeLobby() {
        return lobbyService.getAllLobbies();
    }

    @SubscribeMapping("/lobby/{lobbyId}")
    public Lobby subscribeLobbyId(@DestinationVariable String lobbyId) {
        return lobbyService.getLobby(UUID.fromString(lobbyId));
    }
}