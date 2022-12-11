package gruppe_b.quizduell.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.application.questions.QuestionRepository;
import gruppe_b.quizduell.domain.entities.Question;
import gruppe_b.quizduell.persistence.entities.DbQuestion;

/**
 * Adapter für das Question Repository.
 * Übernimmt das Mapping zwischen dem Question Domain Model und dem Question
 * DbEntity.
 * 
 * @author Christopher Burmeister
 */
@Repository("QuestionRepository")
public class QuestionRepositoryAdapter implements QuestionRepository {

    @Autowired
    private JpaQuestionRepository repo;

    @Override
    public Question findByUUID(UUID id) {
        Optional<DbQuestion> question = repo.findById(id);
        if (!question.isPresent()) {
            return null;
        }
        return question.get().createEntity();
    }

    @Override
    public Question save(Question question) {
        return repo.save(new DbQuestion(
                question.getCategoryId(),
                question.getQuestionText(),
                question.getAnswerOne(),
                question.getAnswerTwo(),
                question.getAnswerThree(),
                question.getAnswerFour(),
                question.getCorrectAnswer()));
    }

    @Override
    public Question update(Question question) {
        Optional<DbQuestion> result = repo.findById(question.getId());

        if (!result.isPresent()) {
            return null;
        }

        DbQuestion dbEntity = result.get();

        dbEntity.setCategoryId(question.getCategoryId());
        dbEntity.setQuestionText(question.getQuestionText());
        dbEntity.setAnswerOne(question.getAnswerOne());
        dbEntity.setAnswerTwo(question.getAnswerTwo());
        dbEntity.setAnswerThree(question.getAnswerThree());
        dbEntity.setAnswerFour(question.getAnswerFour());
        dbEntity.setCorrectAnswer(question.getCorrectAnswer());

        repo.save(dbEntity);

        return dbEntity;
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}
