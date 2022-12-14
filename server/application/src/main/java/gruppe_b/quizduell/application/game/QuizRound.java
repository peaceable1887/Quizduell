package gruppe_b.quizduell.application.game;

import java.util.ArrayList;
import java.util.List;

import gruppe_b.quizduell.application.models.QuizPlayer;
import gruppe_b.quizduell.domain.entities.Question;

public class QuizRound {

    private final Question question;
    private final List<QuizPlayer> playerAnswered;

    public QuizRound(Question question) {
        this.question = question;
        this.playerAnswered = new ArrayList<>();
    }

    public Question getQuestion() {
        return question;
    }

    public List<QuizPlayer> getPlayerAnswered() {
        return playerAnswered;
    }
}
