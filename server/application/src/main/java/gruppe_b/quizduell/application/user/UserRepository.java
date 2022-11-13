package gruppe_b.quizduell.application.user;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.domain.entities.User;

/**
 * Repository für die User.
 * 
 * @author Christopher Burmeister
 */
@Repository
public interface UserRepository {
    User findByName(String name);

    User findByUUID(UUID id);

    User save(User user);

    void deleteAll();
}
