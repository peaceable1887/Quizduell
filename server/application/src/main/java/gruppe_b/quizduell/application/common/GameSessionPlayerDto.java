package gruppe_b.quizduell.application.common;

import java.util.UUID;

public class GameSessionPlayerDto {

    public UUID playerId;
    public String name;
    public PlayerRoundStatus playerRoundStatus;
    public int chosenAnswer;
}
