package gruppe_b.quizduell.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.persistence.entities.DbUser;
import gruppe_b.quizduell.persistence.repository.JpaUserRepository;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    JpaUserRepository repo;

    DbUser john, dave, marc;

    @BeforeEach
    void setUp() {
        repo.deleteAll();

        john = repo.save(new DbUser("John", "JohnDoe@doe.com", "password", "salt"));
        dave = repo.save(new DbUser("Dave", "exampleusermailname@exmaplemailhost.hamburg", "password", "salt"));
        marc = repo.save(new DbUser("Marc", "u@u.de", "password", "salt"));
    }

    @Test
    void setsIdOnSave() {
        // Arrange
        DbUser user = new DbUser("Doe", "test", "password", "salt");

        // Act
        DbUser result = repo.save(user);

        // Assert
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void addDuplicateShouldThrowException() {
        // Arrange
        repo.save(new DbUser("Duplicate", "test", "password", "salt"));

        // Act

        // Assert
        Throwable exception = assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class, () -> {
                    repo.save(new DbUser("Duplicate", "test", "PASSWORD", "PASSWORD"));
                });
        assertEquals(
                "could not execute statement; SQL [n/a]; constraint [UK_jhck7kjdogc7yia7qamc89ypv]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
                exception.getMessage());
    }
}
