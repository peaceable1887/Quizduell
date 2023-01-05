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
        return createLobby("");
    }

    public UUID createLobby(String password) {
        return lobbyService.createLobby("test", UUID.randomUUID(), "testUser", password).getId();
    }

    public UUID createFullLobby() throws Exception {
        return createFullLobby("");
    }

    public UUID createFullLobby(String password) throws Exception {
        Lobby lobby = lobbyService.createLobby("test", UUID.randomUUID(), "testUser", password);
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
