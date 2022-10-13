package gruppe_b.quizduell.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.domain.entities.User;
import gruppe_b.quizduell.persistence.repository.UserRepositoryAdapter;

@SpringBootTest
class UserRepositoryAdapterTest {

    @Autowired
    UserRepositoryAdapter repo;

    User john, dave, marc;

    @BeforeEach
    void setUp() {
        repo.deleteAll();

        john = repo.save(new User("John", "password", "salt"));
        dave = repo.save(new User("Dave", "password", "salt"));
        marc = repo.save(new User("Marc", "password", "salt"));
    }

    @Test
    void createsNewUserOnSave() {
        // Arrange
        User user = new User("test", "test", "test");

        // Act
        User result = repo.save(user);

        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    void findsUser() {
        // Arrange

        // Act
        User result = repo.findByName(john.getName());

        // Assert
        assertThat(result).isNotNull();
    }
}
