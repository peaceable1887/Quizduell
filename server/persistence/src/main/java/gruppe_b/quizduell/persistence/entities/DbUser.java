package gruppe_b.quizduell.persistence.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import gruppe_b.quizduell.domain.entities.User;

/**
 * User DbEntity.
 * 
 * @author Christopher Burmeister
 */
@Entity
@Table(name = "users")
public class DbUser extends User {

    public DbUser() {
    }

    public DbUser(String name, String passwordHash, String salt) {
        super(name, passwordHash, salt);
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

    @Column(name = "password", nullable = false)
    @Override
    public String getPasswordHash() {
        return super.getPasswordHash();
    }

    @Column(name = "salt", nullable = false)
    @Override
    public String getSalt() {
        return super.getSalt();
    }

    public User createEntity() {
        User user = new User();
        user.setId(getId());
        user.setName(getName());
        user.setPasswordHash(getPasswordHash());
        user.setSalt(getSalt());

        return user;
    }
}
