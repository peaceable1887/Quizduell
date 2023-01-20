package gruppe_b.quizduell.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;

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

        q1 = repo.save(new Question(c1, "Frage 1", "Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4", 1));
        q2 = repo.save(new Question(c2, "Frage 2", "Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4", 2));
        q3 = repo.save(new Question(c3, "Frage 3", "Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4", 3));
    }

    @Test
    void whenSaveNewQuestionThenCreateAndReturnNewQuestion() {
        // Arrange
        Question question = new Question(c1, "Frage 4", "Antwort 1", "Antwort 2", "Antwort 3", "Antwort 4", 4);

        // Act
        Question result = repo.save(question);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(question.getQuestionText(), result.getQuestionText());
        assertEquals(question.getAnswerOne(), result.getAnswerOne());
        assertEquals(question.getAnswerTwo(), result.getAnswerTwo());
        assertEquals(question.getAnswerThree(), result.getAnswerThree());
        assertEquals(question.getAnswerFour(), result.getAnswerFour());
        assertNotNull(result.getCategory());
        assertEquals(q1.getCategory().getId(), result.getCategory().getId());
    }

    @Test
    void whenFindQuestionByIdThenReturnQuestion() {
        // Arrange

        // Act
        Question result = repo.findByUUID(q1.getId());

        // Assert
        assertNotNull(result);
        assertEquals(q1.getId(), result.getId());
        assertEquals(q1.getQuestionText(), result.getQuestionText());
        assertEquals(q1.getAnswerOne(), result.getAnswerOne());
        assertEquals(q1.getAnswerTwo(), result.getAnswerTwo());
        assertEquals(q1.getAnswerThree(), result.getAnswerThree());
        assertEquals(q1.getAnswerFour(), result.getAnswerFour());
        assertNotNull(result.getCategory());
        assertEquals(q1.getCategory().getId(), result.getCategory().getId());
    }

    @Test
    void whenRandomThenReturnRandomQuestion() {
        // Arrange

        // Act
        Question result = repo.random(null);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getCategory());
    }

    @Test
    @Repeat(10)
    void whenMultipleRandomThenReturnUniqueQuestions() {
        // Arrange
        List<UUID> excludedIds = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            repo.save(new Question(c1,
                    "Frage " + String.valueOf(i),
                    "Antwort " + String.valueOf(i),
                    "Antwort " + String.valueOf(i),
                    "Antwort " + String.valueOf(i),
                    "Antwort " + String.valueOf(i),
                    1));
        }

        // Act
        for (int i = 0; i < 10; i++) {
            Question result = repo.random(excludedIds);
            excludedIds.add(result.getId());
        }

        // Assert
        assertThat(excludedIds.size()).isEqualTo(new HashSet<UUID>(excludedIds).size());
    }
}
