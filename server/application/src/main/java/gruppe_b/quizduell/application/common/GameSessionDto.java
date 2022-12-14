package gruppe_b.quizduell.application.common;

import java.util.List;

import gruppe_b.quizduell.application.enums.RoundStatus;

public class GameSessionDto {

    public RoundStatus roundStatus;
    public int maxRounds;
    public int currentRound;
    public List<GameSessionPlayerDto> playerList;
    public String categoryName;
    public String questionText;
    public String answerOne;
    public String answerTwo;
    public String answerThree;
    public String answerFour;
    public int correctAnswer;
}
