package gruppe_b.quizduell.authserver.common;

import java.io.Serializable;

import gruppe_b.quizduell.domain.entities.User;

public class UserDetailsDto implements Serializable {

    public UserDetailsDto(User user) {
        this.name = user.getName();
        this.mail = user.getMail();
    }

    public String name;

    public String mail;
}
