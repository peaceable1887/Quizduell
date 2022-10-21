package gruppe_b.quizduell.lobbyserver.Models;

import java.util.UUID;

public class Player {

    private final UUID user_id;

    public Player(UUID user_id) {
        this.user_id = user_id;
    }

    public UUID getUser_id() {
        return user_id;
    }
}
