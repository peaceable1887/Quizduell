package gruppe_b.quizduell.persistence.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import gruppe_b.quizduell.domain.entities.Category;

/**
 * Category DbEntity.
 * 
 * @author Christopher Burmeister
 */
@Entity
@Table(name = "categories")
public class DbCategory extends Category {

    private Category category;

    public DbCategory() {
        category = new Category();
    }

    public DbCategory(Category category) {
        this.category = category;
    }

    public DbCategory(String name, String description) {
        category = new Category(name, description);
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Override
    public UUID getId() {
        return category.getId();
    }

    @Override
    public void setId(UUID id) {
        category.setId(id);
    }

    @Column(name = "name", nullable = false, unique = true)
    @Override
    public String getName() {
        return category.getName();
    }

    @Override
    public void setName(String name) {
        category.setName(name);
    }

    @Column(name = "description")
    @Override
    public String getDescription() {
        return category.getDescription();
    }

    @Override
    public void setDescription(String description) {
        category.setDescription(description);
    }

    @Transient
    public Category createEntity() {
        return category;
    }
}
