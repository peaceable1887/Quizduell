package gruppe_b.quizduell.domain.entities;

import java.util.UUID;

/**
 * Object für die Statik eines Spielers.
 * 
 * @author Christopher Burmeister
 */
public class PlayerStats {

    UUID id;
    User player;
    int gameCount;
    int gameWonCount;
    int gameLossCount;
    int gameDrawCount;

    public PlayerStats() {

    }

    public PlayerStats(User player, int gameCount, int gameWonCount, int gameLossCount, int gameDrawCount) {
        this.player = player;
        this.gameCount = gameCount;
        this.gameWonCount = gameWonCount;
        this.gameLossCount = gameLossCount;
        this.gameDrawCount = gameDrawCount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
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

    public int getGameLossCount() {
        return gameLossCount;
    }

    public void setGameLossCount(int gameLossCount) {
        this.gameLossCount = gameLossCount;
    }

    public int getGameDrawCount() {
        return gameDrawCount;
    }

    public void setGameDrawCount(int gameDrawCount) {
        this.gameDrawCount = gameDrawCount;
    }
}
