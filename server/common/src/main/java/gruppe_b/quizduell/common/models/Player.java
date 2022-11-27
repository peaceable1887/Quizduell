package gruppe_b.quizduell.common.models;

import java.util.UUID;

public class Player {

    private final UUID userId;
    private String status = "wait";

    public Player(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public void setReady() {
        status = "ready";
    }

    public void setWait() {
        status = "wait";
    }
}
