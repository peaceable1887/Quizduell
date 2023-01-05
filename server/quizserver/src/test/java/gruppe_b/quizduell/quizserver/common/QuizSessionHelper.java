package gruppe_b.quizduell.quizserver.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.quizserver.services.QuizService;

@Service
public class QuizSessionHelper {

    @Autowired
    QuizHelper quizHelper;

    @Autowired
    QuizService quizService;

    public Quiz createAndStartQuizSession() throws Exception {
        Quiz quiz = createQuizSession();
        quiz.getPlayers().get(0).setReady();
        quiz.getPlayers().get(1).setReady();
        quiz.setQuizStarted();
        quizService.startQuiz(quiz);

        return quiz;
    }

    public Quiz createQuizSession() throws Exception {
        Quiz quiz = quizHelper.createFullQuiz();

        return quiz;
    }
}
