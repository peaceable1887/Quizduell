package gruppe_b.quizduell.quizserver.common;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.quizserver.models.Quiz;
import gruppe_b.quizduell.quizserver.services.QuizService;

@Service
public class QuizHelper {

    @Autowired
    QuizService quizService;

    public Quiz createQuiz() {
        ArrayList<UUID> playerList = new ArrayList<>();
        playerList.add(UUID.randomUUID());
        playerList.add(UUID.randomUUID());

        return quizService.createQuiz(
                UUID.randomUUID(),
                playerList);
    }
}
