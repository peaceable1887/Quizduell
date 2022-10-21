package gruppe_b.quizduell.lobbyserver.Models;

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
