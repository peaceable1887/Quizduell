package gruppe_b.quizduell.lobbyserver.common;

import java.util.UUID;

import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;

public class LobbyBuilder {

    LobbyService lobbyService;
    Lobby lobby;

    public LobbyBuilder(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    public LobbyBuilder buildLobby(UUID userId) {
        lobby = lobbyService.createLobby("buildTestLobby", userId, "testUser", "");
        return this;
    }

    public LobbyBuilder buildLobby(UUID userId, String password) {
        lobby = lobbyService.createLobby("buildTestLobby", userId, "testUser", password);
        return this;
    }

    public LobbyBuilder buildLobby() {
        lobby = lobbyService.createLobby("buildTestLobby", UUID.randomUUID(), "testUser", "");
        return this;
    }

    public LobbyBuilder buildLobby(String password) {
        lobby = lobbyService.createLobby("buildTestLobby", UUID.randomUUID(), "testUser", password);
        return this;
    }

    public LobbyBuilder addPlayer() throws Exception {
        return addPlayer(UUID.randomUUID(), "john");
    }

    public LobbyBuilder addPlayer(UUID playerId, String name) throws Exception {
        lobby.addPlayer(new Player(playerId, name));
        return this;
    }

    public Lobby getLobby() {
        return lobby;
    }
}
