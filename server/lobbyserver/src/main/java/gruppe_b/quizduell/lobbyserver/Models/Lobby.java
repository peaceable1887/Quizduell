package gruppe_b.quizduell.lobbyserver.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby {

    private final UUID id;
    private final List<Player> playerList;
    private final String name;

    public Lobby(String name, Player firstPlayer) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.playerList = new ArrayList<>();
        this.playerList.add(firstPlayer);
    }

    public UUID getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return playerList;
    }

    public String getName() {
        return name;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }
}
