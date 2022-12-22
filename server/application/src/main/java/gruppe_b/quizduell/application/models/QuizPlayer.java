package gruppe_b.quizduell.application.models;

import java.util.UUID;

public class QuizPlayer extends Player {

    private int answer;

    public QuizPlayer(UUID userId, String name) {
        super(userId, name);
    }

    public QuizPlayer(Player player) {
        super(player.getUserId(), player.getName());
        setReady();
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
