package gruppe_b.quizduell.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.domain.entities.Category;
import gruppe_b.quizduell.persistence.repository.CategoryRepositoryAdapter;

@SpringBootTest
public class CategoryRepositoryAdapterTest {

    @Autowired
    CategoryRepositoryAdapter repo;

    Category c1, c2, c3;

    @BeforeEach
    void setUp() {
        repo.deleteAll();

        c1 = repo.save(new Category("Java", "Java test category"));
        c2 = repo.save(new Category("Network", "Network test category"));
        c3 = repo.save(new Category("PC", "PC test category"));
    }

    @Test
    void whenSaveNewCategoryThenCreateAndReturnNewCategory() {
        // Arrange
        Category category = new Category("Test", "Test description");

        // Act
        Category result = repo.save(category);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(category.getName(), result.getName());
        assertEquals(category.getDescription(), result.getDescription());
    }

    @Test
    void whenFindCategoryByNameThenReturnCategory() {
        // Arrange

        // Act
        Category result = repo.findByName(c1.getName());

        // Assert
        assertNotNull(result);
        assertEquals(c1.getId(), result.getId());
        assertEquals(c1.getName(), result.getName());
        assertEquals(c1.getDescription(), result.getDescription());
    }

    @Test
    void whenFindCategoryByIdThenReturnCategory() {
        // Arrange

        // Act
        Category result = repo.findByUUID(c1.getId());

        // Assert
        assertNotNull(result);
        assertEquals(c1.getId(), result.getId());
        assertEquals(c1.getName(), result.getName());
        assertEquals(c1.getDescription(), result.getDescription());
    }
}
