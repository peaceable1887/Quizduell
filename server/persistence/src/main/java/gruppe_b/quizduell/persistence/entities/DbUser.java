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

import gruppe_b.quizduell.domain.entities.User;

/**
 * User DbEntity.
 * 
 * @author Christopher Burmeister
 */
@Entity
@Table(name = "users")
public class DbUser extends User {

    private User user;

    public DbUser() {
        user = new User();
    }

    public DbUser(User user) {
        this.user = user;
    }

    public DbUser(String name, String mail, String passwordHash, String salt) {
        user = new User(name, mail, passwordHash, salt);
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Override
    public UUID getId() {
        return user.getId();
    }

    @Override
    public void setId(UUID id) {
        user.setId(id);
    }

    @Column(name = "name", nullable = false, unique = true)
    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public void setName(String name) {
        user.setName(name);
    }

    @Column(name = "mail", nullable = true, unique = true)
    @Override
    public String getMail() {
        return user.getMail();
    }

    @Override
    public void setMail(String mail) {
        user.setMail(mail);
    }

    @Column(name = "password", nullable = false)
    @Override
    public String getPasswordHash() {
        return user.getPasswordHash();
    }

    @Override
    public void setPasswordHash(String passwordHash) {
        user.setPasswordHash(passwordHash);
    }

    @Column(name = "salt", nullable = false)
    @Override
    public String getSalt() {
        return user.getSalt();
    }

    public void setSalt(String salt) {
        user.setSalt(salt);
    }

    @Transient
    public User getUserModel() {
        return user;
    }
}
