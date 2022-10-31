package gruppe_b.quizduell.lobbyserver.models;

import java.util.UUID;

public class Player {

    private final UUID userId;

    public Player(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
