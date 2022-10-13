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

        john = repo.save(new DbUser("John", "password", "salt"));
        dave = repo.save(new DbUser("Dave", "password", "salt"));
        marc = repo.save(new DbUser("Marc", "password", "salt"));
    }

    @Test
    void setsIdOnSave() {
        // Arrange
        DbUser user = new DbUser("Doe", "password", "salt");

        // Act
        DbUser result = repo.save(user);

        // Assert
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void addDuplicateShouldThrowException() {
        // Arrange
        repo.save(new DbUser("Duplicate", "password", "salt"));

        // Act

        // Assert
        Throwable exception = assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class, () -> {
                    repo.save(new DbUser("Duplicate", "PASSWORD", "PASSWORD"));
                });
        assertEquals(
                "could not execute statement; SQL [n/a]; constraint [UK_3g1j96g94xpk3lpxl2qbl985x]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
                exception.getMessage());
    }
}
