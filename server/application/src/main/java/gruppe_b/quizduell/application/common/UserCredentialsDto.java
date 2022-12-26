package gruppe_b.quizduell.application.common;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Datentransfer-Objekt für Login-Daten.
 */
public class UserCredentialsDto implements Serializable {

    public UserCredentialsDto() {

    }

    public UserCredentialsDto(String name, String mail, String password) {
        this.name = name;
        this.mail = mail;
        this.password = password;
    }

    @NotEmpty(message = "Name can not be empty")
    @NotBlank(message = "Name is mandatory")
    public String name;

    @Email
    public String mail;

    @NotEmpty(message = "Password can not be empty")
    @NotBlank(message = "Password is mandatory")
    public String password;
}
