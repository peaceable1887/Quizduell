package gruppe_b.quizduell.authserver.common;

import java.io.Serializable;
import java.util.UUID;

public class UserJwtDto implements Serializable {

    public UserJwtDto() {

    }

    public UserJwtDto(String token, String userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    public UserJwtDto(String token, UUID userId, String username) {
        this.token = token;
        this.userId = userId.toString();
        this.username = username;
    }

    public String token;

    public String userId;

    public String username;
}
