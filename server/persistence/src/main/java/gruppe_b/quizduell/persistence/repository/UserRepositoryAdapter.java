package gruppe_b.quizduell.persistence.repository;

import java.util.Optional;
import java.util.UUID;

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
        return user.getUserModel();
    }

    public User findByUUID(UUID id) {
        Optional<DbUser> user = repo.findById(id);
        if (!user.isPresent()) {
            return null;
        }
        return user.get().getUserModel();
    }

    @Override
    public User save(User user) {
        return repo.save(new DbUser(user));
    }

    @Override
    public User update(User user) {
        Optional<DbUser> result = repo.findById(user.getId());

        if (!result.isPresent()) {
            return null;
        }

        DbUser dbEntity = result.get();

        dbEntity.setName(user.getName());
        dbEntity.setMail(user.getMail());
        dbEntity.setPasswordHash(user.getPasswordHash());
        dbEntity.setSalt(user.getSalt());

        repo.save(dbEntity);

        return dbEntity;
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}
