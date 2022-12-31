package gruppe_b.quizduell.authserver.common;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.commands.create_user.CreateUserCommand;
import gruppe_b.quizduell.domain.entities.User;

@Service
public class AuthHelper {

    private final String DEFAULT_UUID = "00000000-0000-0000-0000-000000000000";

    @Autowired
    private final JwtEncoder jwtEncoder;

    @Autowired
    private RequestHandler<CreateUserCommand, User> createUserHandler;

    public AuthHelper(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Gibt die UUID zurück, die verwendet wird, um einen Token ohne vorgegebene
     * UUID zu erstellen.
     * 
     * @return default UUID
     */
    public String getDefaultUUID() {
        return DEFAULT_UUID;
    }

    public String generateToken(String id) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("quizduell_authserver")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(id)
                .claim("scope", "")
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateToken() {
        return generateToken(DEFAULT_UUID);
    }

    public UUID generateUser() {
        User user = createUserHandler.handle(new CreateUserCommand("authHelper", "john@john.de", "password", "salt"));
        return user.getId();
    }
}
