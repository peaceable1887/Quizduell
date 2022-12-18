package gruppe_b.quizduell.quizserver.common;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.game.QuizSession;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.quizserver.services.QuizService;

@Service
public class QuizSessionHelper {

    @Autowired
    QuizHelper quizHelper;

    @Autowired
    QuizService quizService;

    public Quiz createAndStartQuizSession() throws Exception {
        Quiz quiz = quizHelper.createFullQuiz();
        quiz.getPlayers().get(0).setReady();
        quiz.getPlayers().get(1).setReady();
        quizService.startQuiz(quiz);

        return quiz;
    }
}
