package gruppe_b.quizduell.application.categories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.domain.entities.Category;

/**
 * Repository für die Kategorien.
 * 
 * @author Christopher Burmeister
 */
@Repository
public interface CategoryRepository {
    Category findByName(String name);

    Category findByUUID(UUID id);

    Category save(Category category);

    Category update(Category category);

    void deleteAll();
}
