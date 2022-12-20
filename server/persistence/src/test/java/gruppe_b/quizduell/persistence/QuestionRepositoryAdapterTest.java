package gruppe_b.quizduell.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.domain.entities.Category;
import gruppe_b.quizduell.domain.entities.Question;
import gruppe_b.quizduell.persistence.repository.CategoryRepositoryAdapter;
import gruppe_b.quizduell.persistence.repository.QuestionRepositoryAdapter;

@SpringBootTest
public class QuestionRepositoryAdapterTest {

    @Autowired
    QuestionRepositoryAdapter repo;

    @Autowired
    CategoryRepositoryAdapter categoryRepo;

    Question q1, q2, q3;

    Category c1, c2, c3;

    @BeforeEach
    void setUp() {
        repo.deleteAll();
        categoryRepo.deleteAll();

        c1 = categoryRepo.save(new Category("Java", "Java test category"));
        c2 = categoryRepo.save(new Category("Network", "Network test category"));
        c3 = categoryRepo.save(new Category("PC", "PC test category"));

        q1 = repo.save(new Question(c1.getId(), "Frage 1", "Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4", 1));
        q2 = repo.save(new Question(c2.getId(), "Frage 2", "Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4", 2));
        q2 = repo.save(new Question(c3.getId(), "Frage 3", "Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4", 3));
    }

    @Test
    void whenSaveNewQuestionThenCreateAndReturnNewQuestion() {
        // Arrange
        Question question = new Question(c1.getId(), "Frage 4", "Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4", 4);

        // Act
        Question result = repo.save(question);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(question.getCategoryId(), result.getCategoryId());
        assertEquals(question.getQuestionText(), result.getQuestionText());
        assertEquals(question.getAnswerOne(), result.getAnswerOne());
        assertEquals(question.getAnswerTwo(), result.getAnswerTwo());
        assertEquals(question.getAnswerThree(), result.getAnswerThree());
        assertEquals(question.getAnswerFour(), result.getAnswerFour());
        assertEquals(question.getCategoryId(), result.getCategoryId());
    }

    @Test
    void whenFindQuestionByIdThenReturnQuestion() {
        // Arrange

        // Act
        Question result = repo.findByUUID(q1.getId());

        // Assert
        assertNotNull(result);
        assertEquals(q1.getId(), result.getId());
        assertEquals(q1.getCategoryId(), result.getCategoryId());
        assertEquals(q1.getQuestionText(), result.getQuestionText());
        assertEquals(q1.getAnswerOne(), result.getAnswerOne());
        assertEquals(q1.getAnswerTwo(), result.getAnswerTwo());
        assertEquals(q1.getAnswerThree(), result.getAnswerThree());
        assertEquals(q1.getAnswerFour(), result.getAnswerFour());
        assertEquals(q1.getCategoryId(), result.getCategoryId());
    }

    @Test
    void whenRandomThenReturnRandomQuest() {
        // Arrange

        // Act
        Question result = repo.random(new Random());

        // Assert
        assertNotNull(result);
    }
}
