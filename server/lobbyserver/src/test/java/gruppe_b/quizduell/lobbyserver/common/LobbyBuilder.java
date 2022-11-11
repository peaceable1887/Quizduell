package gruppe_b.quizduell.lobbyserver.common;

import java.util.UUID;

import gruppe_b.quizduell.common.models.Player;
import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;

public class LobbyBuilder {

    LobbyService lobbyService;
    Lobby lobby;

    public LobbyBuilder(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    public LobbyBuilder buildLobby(UUID userId) {
        lobby = lobbyService.createLobby(userId, "buildTestLobby");
        return this;
    }

    public LobbyBuilder buildLobby() {
        lobby = lobbyService.createLobby(UUID.randomUUID(), "buildTestLobby");
        return this;
    }

    public LobbyBuilder addPlayer() {
        return addPlayer(UUID.randomUUID());
    }

    public LobbyBuilder addPlayer(UUID playerId) {
        lobby.addPlayer(new Player(playerId));
        return this;
    }

    public Lobby getLobby() {
        return lobby;
    }
}
