package gruppe_b.quizduell.application.common;

import java.util.UUID;

import gruppe_b.quizduell.application.enums.PlayerResult;

public class GameSessionPlayerResult {

    private final UUID id;
    private PlayerResult playerResult;
    private int points = 0;
    private String name;

    public GameSessionPlayerResult(UUID id, String name) {
        this.id = id;
        this.playerResult = PlayerResult.NULL;
        this.name = name;
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
