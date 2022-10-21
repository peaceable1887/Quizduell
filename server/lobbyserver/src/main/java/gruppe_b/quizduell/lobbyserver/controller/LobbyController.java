package gruppe_b.quizduell.lobbyserver.controller;

import java.security.Principal;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gruppe_b.quizduell.lobbyserver.Models.Lobby;
import gruppe_b.quizduell.lobbyserver.common.ConnectRequest;
import gruppe_b.quizduell.lobbyserver.common.CreateRequest;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PostMapping("create")
    public ResponseEntity<Lobby> create(Principal principal, @RequestBody CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lobbyService.createLobby(
                UUID.fromString(principal.getName()), request.name));
    }

    @PostMapping("connect")
    public ResponseEntity<Lobby> connect(Principal principal, @RequestBody ConnectRequest request) {
        return ResponseEntity.ok(lobbyService.connectToLobby(
                UUID.fromString(principal.getName()), request.lobbyId));
    }
}
