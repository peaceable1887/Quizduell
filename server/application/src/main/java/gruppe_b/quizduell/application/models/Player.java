package gruppe_b.quizduell.application.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {

    private final UUID userId;
    private final String name;
    private String status = "wait";

    @JsonCreator
    public Player(@JsonProperty("userId") UUID userId,
            @JsonProperty("name") String name) {
        this.name = name;
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
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
