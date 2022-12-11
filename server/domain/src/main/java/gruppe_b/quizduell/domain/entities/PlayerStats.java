package gruppe_b.quizduell.domain.entities;

import java.util.UUID;

/**
 * Object für die Statik eines Spielers.
 * 
 * @author Christopher Burmeister
 */
public class PlayerStats {

    UUID id;
    UUID playerId;
    int gameCount;
    int gameWonCount;

    public PlayerStats() {

    }

    public PlayerStats(UUID playerId, int gameCount, int gameWonCount) {
        this.playerId = playerId;
        this.gameCount = gameCount;
        this.gameWonCount = gameWonCount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public int getGameCount() {
        return gameCount;
    }

    public void setGameCount(int gameCount) {
        this.gameCount = gameCount;
    }

    public int getGameWonCount() {
        return gameWonCount;
    }

    public void setGameWonCount(int gameWonCount) {
        this.gameWonCount = gameWonCount;
    }
}
