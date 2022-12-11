package gruppe_b.quizduell.domain.entities;

import java.util.UUID;

/**
 * Kategorie Objekt für Fragen.
 * 
 * @author Christopher Burmeister
 */
public class Category {

    UUID id;
    String name;
    String description;

    public Category() {

    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
