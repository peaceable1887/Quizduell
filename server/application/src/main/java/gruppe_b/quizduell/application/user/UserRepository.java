package gruppe_b.quizduell.application.user;

import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.domain.entities.User;

@Repository
public interface UserRepository {
    User findByName(String name);

    User save(User user);

    void deleteAll();
}
