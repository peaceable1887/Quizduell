package gruppe_b.quizduell.lobbyserver.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gruppe_b.quizduell.common.models.Player;
import gruppe_b.quizduell.lobbyserver.enums.LobbyStatus;
import gruppe_b.quizduell.lobbyserver.exceptions.LobbyStatusException;

public class Lobby {

    private final UUID id;
    private final List<Player> playerList;
    private final String name;
    private LobbyStatus lobbyStatus;

    public Lobby(String name, Player firstPlayer) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.playerList = new ArrayList<>();
        this.playerList.add(firstPlayer);
        this.lobbyStatus = LobbyStatus.WAIT;
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

    public void addPlayer(Player player) throws LobbyStatusException {
        if (lobbyStatus == LobbyStatus.STARTED) {
            throw new LobbyStatusException("Can't add Player. Lobby already started!");
        }
        playerList.add(player);
    }

    public Player getPlayer(UUID playerId) {
        for (Player player : playerList) {
            if (player.getUserId().compareTo(playerId) == 0) {
                return player;
            }
        }
        return null;
    }

    public void removePlayer(UUID playerId) {
        playerList.removeIf(p -> p.getUserId().equals(playerId));
    }

    /**
     * Gibt die Anzahl der Spieler zurück in der Lobby.
     * 
     * @return Anzahl Spieler
     */
    public int playerCount() {
        return playerList.size();
    }

    /**
     * Gibt zurück, ob alles Spieler in der Lobby bereit sind.
     * 
     * @return true all player ready
     */
    public boolean allPlayersReady() {
        for (Player player : playerList) {
            // Hat der Spieler einen anderne Status als ready?
            if (!player.getStatus().equals("ready")) {
                // Spieler ist nicht ready!
                return false;
            }
        }

        // Alle Spieler ready
        return true;
    }

    public LobbyStatus getLobbyStatus() {
        return this.lobbyStatus;
    }

    public void setLobbyStarted() {
        this.lobbyStatus = LobbyStatus.STARTED;
    }
}
