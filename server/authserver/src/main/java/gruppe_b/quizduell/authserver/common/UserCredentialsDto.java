package gruppe_b.quizduell.authserver.common;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Datentransfer-Objekt für Login-Daten.
 */
public class UserCredentialsDto implements Serializable {

    @NotEmpty(message = "Name can not be empty")
    @NotBlank(message = "Name is mandatory")
    public String name;

    @NotEmpty(message = "Password can not be empty")
    @NotBlank(message = "Password is mandatory")
    public String password;
}
