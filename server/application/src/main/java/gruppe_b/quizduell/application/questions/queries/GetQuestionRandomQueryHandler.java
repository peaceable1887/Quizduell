package gruppe_b.quizduell.application.questions.queries;

import java.util.Random;

import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.questions.QuestionRepository;
import gruppe_b.quizduell.domain.entities.Question;

/**
 * QueryHandler für eine Zufällige Frage.
 * 
 * @author Christopher Burmeister
 */
@Service
public class GetQuestionRandomQueryHandler implements RequestHandler<GetQuestionRandomQuery, Question> {

    private QuestionRepository repo;

    private Random random;

    public GetQuestionRandomQueryHandler(QuestionRepository repo, Random random) {
        this.repo = repo;
        this.random = random;
    }

    public Question handle(GetQuestionRandomQuery query) {
        return repo.random(random);
    }
}
