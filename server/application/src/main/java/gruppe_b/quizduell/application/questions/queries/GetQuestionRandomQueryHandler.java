package gruppe_b.quizduell.application.questions.queries;

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

    public GetQuestionRandomQueryHandler(QuestionRepository repo) {
        this.repo = repo;
    }

    public Question handle(GetQuestionRandomQuery query) {
        Question question = repo.random();
        return repo.random();
    }
}
