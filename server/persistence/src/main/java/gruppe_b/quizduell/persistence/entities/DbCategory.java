package gruppe_b.quizduell.persistence.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public DbCategory() {

    }

    public DbCategory(String name, String description) {
        super(name, description);
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Override
    public UUID getId() {
        return super.getId();
    }

    @Column(name = "name", nullable = false, unique = true)
    @Override
    public String getName() {
        return super.getName();
    }

    @Column(name = "description")
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    public Category createEntity() {
        Category category = new Category();
        category.setId(getId());
        category.setName(getName());
        category.setDescription(getDescription());

        return category;
    }
}
