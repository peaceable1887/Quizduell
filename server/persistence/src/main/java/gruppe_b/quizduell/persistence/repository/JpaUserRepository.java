package gruppe_b.quizduell.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.persistence.entities.DbUser;

/**
 * User Repository.
 * 
 * @author Christopher Burmeister
 */
@Repository
public interface JpaUserRepository extends JpaRepository<DbUser, UUID> {
    public DbUser findByName(String name);
}
