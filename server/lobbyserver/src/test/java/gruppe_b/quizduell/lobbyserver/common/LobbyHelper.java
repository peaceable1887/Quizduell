package gruppe_b.quizduell.lobbyserver.common;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;

@Service
public class LobbyHelper {

    @Autowired
    LobbyService lobbyService;

    public LobbyService getLobbyService() {
        return lobbyService;
    }

    public UUID createLobby() {
        return lobbyService.createLobby(UUID.randomUUID(), "test").getId();
    }

    public UUID createFullLobby() throws Exception {
        Lobby lobby = lobbyService.createLobby(UUID.randomUUID(), "test");
        lobby.addPlayer(new Player(UUID.randomUUID(), "john"));
        return lobby.getId();
    }

    public Lobby getLobby(UUID lobbyId) {
        return lobbyService.getLobby(lobbyId);
    }

    public void publishLobby(UUID lobbyId) {
        lobbyService.publishLobby(lobbyId);
    }

    public LobbyBuilder getNewLobbyBuilder() {
        return new LobbyBuilder(lobbyService);
    }
}
