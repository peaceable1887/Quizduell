package gruppe_b.quizduell.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        john = repo.save(new User("John", "JohnDoe@doe.com", "password", "salt"));
        dave = repo.save(new User("Dave", "exampleusermailname@exmaplemailhost.hamburg", "password", "salt"));
        marc = repo.save(new User("Marc", "u@u.de", "password", "salt"));
    }

    @Test
    void whenSaveNewUserThenCreateAndReturnNewUser() {
        // Arrange
        User user = new User("test", "test", "test", "test");

        // Act
        User result = repo.save(user);

        // Assert
        assertNotNull(result);
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getMail(), result.getMail());
        assertEquals(user.getPasswordHash(), result.getPasswordHash());
        assertEquals(user.getSalt(), result.getSalt());
    }

    @Test
    void whenFindUserByNameThenReturnUser() {
        // Arrange

        // Act
        User result = repo.findByName(john.getName());

        // Assert
        assertNotNull(result);
        assertEquals(john.getId(), result.getId());
        assertEquals(john.getName(), result.getName());
    }

    @Test
    void whenFindUserByIdThenReturnUser() {
        // Arrange

        // Act
        User result = repo.findByUUID(john.getId());

        // Assert
        assertNotNull(result);
        assertEquals(john.getId(), result.getId());
        assertEquals(john.getName(), result.getName());
    }
}
