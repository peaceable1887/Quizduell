package gruppe_b.quizduell.quizserver.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gruppe_b.quizduell.common.models.Player;

public class Quiz {

    private final UUID id;
    private final List<Player> playerList;

    private UUID lobbyId;

    public Quiz(UUID lobbyId) {
        this.id = UUID.randomUUID();
        this.lobbyId = lobbyId;
        this.playerList = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public List<Player> getPlayers() {
        return playerList;
    }
}
