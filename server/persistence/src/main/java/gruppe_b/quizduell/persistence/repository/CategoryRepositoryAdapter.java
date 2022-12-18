package gruppe_b.quizduell.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.application.categories.CategoryRepository;
import gruppe_b.quizduell.domain.entities.Category;
import gruppe_b.quizduell.persistence.entities.DbCategory;

/**
 * Adapter für das Category Repository.
 * Übernimmt das Mapping zwischen dem Category Domain Model und dem Category
 * DbEntity.
 * 
 * @author Christopher Burmeister
 */
@Repository("CategoryRepository")
public class CategoryRepositoryAdapter implements CategoryRepository {

    @Autowired
    private JpaCategoryRepository repo;

    @Override
    public Category findByName(String name) {
        DbCategory category = repo.findByName(name);
        if (category == null) {
            return null;
        }
        return category.createEntity();
    }

    @Override
    public Category findByUUID(UUID id) {
        Optional<DbCategory> category = repo.findById(id);
        if (!category.isPresent()) {
            return null;
        }
        return category.get().createEntity();
    }

    @Override
    public Category save(Category category) {
        return repo.save(new DbCategory(
                category.getName(),
                category.getDescription()));
    }

    @Override
    public Category update(Category category) {
        Optional<DbCategory> result = repo.findById(category.getId());

        if (!result.isPresent()) {
            return null;
        }

        DbCategory dbEntity = result.get();

        dbEntity.setName(category.getName());
        dbEntity.setDescription(category.getDescription());

        repo.save(dbEntity);

        return dbEntity;
    }

    @Override
    public List<? extends Category> findAll() {
        return repo.findAll();
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}
