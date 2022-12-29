package gruppe_b.quizduell.application.game;

import java.util.HashMap;
import java.util.UUID;

import gruppe_b.quizduell.application.models.QuizPlayer;
import gruppe_b.quizduell.domain.entities.Question;

public class QuizRound {

    private final Question question;
    private final HashMap<UUID, QuizPlayer> playerAnswered;

    public QuizRound(Question question) {
        this.question = question;
        this.playerAnswered = new HashMap<>();
    }

    public Question getQuestion() {
        return question;
    }

    public HashMap<UUID, QuizPlayer> getPlayerAnswered() {
        return playerAnswered;
    }
}
