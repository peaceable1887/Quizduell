package gruppe_b.quizduell.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.persistence.entities.DbCategory;

/**
 * Category Repository.
 * 
 * @author Christopher Burmeister
 */
@Repository
public interface JpaCategoryRepository extends JpaRepository<DbCategory, UUID> {
    public DbCategory findByName(String name);
}
