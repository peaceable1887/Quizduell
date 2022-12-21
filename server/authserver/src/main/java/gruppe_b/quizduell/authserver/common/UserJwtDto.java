package gruppe_b.quizduell.authserver.common;

import java.io.Serializable;
import java.util.UUID;

public class UserJwtDto implements Serializable {

    public UserJwtDto() {

    }

    public UserJwtDto(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }

    public UserJwtDto(String token, UUID userId) {
        this.token = token;
        this.userId = userId.toString();
    }

    public String token;

    public String userId;
}
