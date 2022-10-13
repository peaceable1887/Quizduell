package gruppe_b.quizduell.domain.entities;

import java.util.UUID;

public class User {
    UUID id;
    String name;
    String passwordHash;
    String salt;

    public User() {
    }

    public User(String name, String passwordHash, String salt) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
