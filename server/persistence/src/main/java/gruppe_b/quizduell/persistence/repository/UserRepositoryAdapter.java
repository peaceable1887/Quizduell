package gruppe_b.quizduell.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.application.user.UserRepository;
import gruppe_b.quizduell.domain.entities.User;
import gruppe_b.quizduell.persistence.entities.DbUser;

/**
 * Adapter für das User Repository.
 * Übernimmt das Mapping zwischen dem User Domain Model und dem User DbEntity.
 * 
 * @author Christopher Burmeister
 */
@Repository("UserRepository")
public class UserRepositoryAdapter implements UserRepository {

    @Autowired
    private JpaUserRepository repo;

    @Override
    public User findByName(String name) {
        DbUser user = repo.findByName(name);
        if (user == null) {
            return null;
        }
        return user.createEntity();
    }

    @Override
    public User save(User user) {
        return repo.save(new DbUser(
                user.getName(),
                user.getPasswordHash(),
                user.getSalt()));
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}
