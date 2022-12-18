package gruppe_b.quizduell.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.persistence.entities.DbCategory;
import gruppe_b.quizduell.persistence.repository.JpaCategoryRepository;

@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    JpaCategoryRepository repo;

    DbCategory c1, c2, c3;

    @BeforeEach
    void setup() {
        repo.deleteAll();

        c1 = repo.save(new DbCategory("test1", "testText1"));
        c2 = repo.save(new DbCategory("test2", "testText2"));
        c3 = repo.save(new DbCategory("test3", "testText3"));
    }

    @Test
    void setsIdOnSave() {
        // Arrange
        DbCategory category = new DbCategory("test", "test");

        // Act
        DbCategory result = repo.save(category);

        // Assert
        assertNotNull(result.getId());
    }
}
