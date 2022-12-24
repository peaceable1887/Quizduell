package gruppe_b.quizduell.quizserver.common;

import java.util.List;
import java.util.UUID;

import gruppe_b.quizduell.application.common.GameSessionDto;
import gruppe_b.quizduell.application.enums.QuizStatus;
import gruppe_b.quizduell.application.models.Player;

public class QuizSessionDto {

    private UUID quizId;
    private UUID lobbyId;
    private QuizStatus quizStatus;
    private List<Player> playerList;
    private List<GameSessionDto> roundList;

    public UUID getQuizId() {
        return quizId;
    }

    public void setQuizId(UUID quizId) {
        this.quizId = quizId;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(UUID lobbyId) {
        this.lobbyId = lobbyId;
    }

    public QuizStatus getQuizStatus() {
        return quizStatus;
    }

    public void setQuizStatus(QuizStatus quizStatus) {
        this.quizStatus = quizStatus;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<GameSessionDto> getRoundList() {
        return roundList;
    }

    public void setRoundList(List<GameSessionDto> roundList) {
        this.roundList = roundList;
    }
}
