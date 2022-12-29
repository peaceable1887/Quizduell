package gruppe_b.quizduell.application.common;

import java.util.UUID;

import gruppe_b.quizduell.application.enums.PlayerResult;

public class GameSessionPlayerResult {

    private final UUID id;
    private PlayerResult playerResult;
    private int points = 0;

    public GameSessionPlayerResult(UUID id) {
        this.id = id;
        this.playerResult = PlayerResult.NULL;
    }

    public UUID getId() {
        return id;
    }

    public PlayerResult getPlayerResult() {
        return playerResult;
    }

    public void setPlayerResult(PlayerResult playerResult) {
        this.playerResult = playerResult;
    }

    public int getPoints() {
        return this.points;
    }

    public void addOnePoint() {
        this.points++;
    }
}
