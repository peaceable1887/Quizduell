package gruppe_b.quizduell.application.questions;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.domain.entities.Question;

/**
 * Repository für die Fragen.
 * 
 * @author Christopher Burmeister
 */
@Repository
public interface QuestionRepository {
    Question findByUUID(UUID id);

    Question save(Question question);

    Question update(Question question);

    Question random(List<UUID> excludeQuestions);

    List<? extends Question> findAll();

    void deleteAll();
}
