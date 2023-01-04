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

    private final String DEFAULT_USER_UUID = "00000000-0000-0000-0000-000000000000";
    private final String DEFAULT_USER_NAME = "authHelper";
    private final String DEFAULT_USER_MAIL = "john@john.de";
    private final String DEFAULT_USER_PASSWORD = "password";
    private final String DEFAULT_USER_SALT = "salt";

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
    public String getDEFAULT_USER_UUID() {
        return DEFAULT_USER_UUID;
    }

    public String getDEFAULT_USER_NAME() {
        return DEFAULT_USER_NAME;
    }

    public String getDEFAULT_USER_MAIL() {
        return DEFAULT_USER_MAIL;
    }

    public String getDEFAULT_USER_PASSWORD() {
        return DEFAULT_USER_PASSWORD;
    }

    public String getDEFAULT_USER_SALT() {
        return DEFAULT_USER_SALT;
    }

    public String generateToken(String id) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("quizduell_authserver")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(id)
                .claim("name", getDEFAULT_USER_NAME())
                .claim("scope", "")
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateExpiredToken(String id) throws Exception {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("quizduell_authserver")
                .issuedAt(now.minus(2, ChronoUnit.HOURS))
                .expiresAt(now.minus(1, ChronoUnit.HOURS))
                .subject(id)
                .claim("name", getDEFAULT_USER_NAME())
                .claim("scope", "")
                .build();

        return "Bearer " + this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateExpiredToken() throws Exception {
        return generateExpiredToken("00000000-0000-0000-0000-000000000000");
    }

    public String generateToken() {
        return generateToken(DEFAULT_USER_UUID);
    }

    public UUID generateUser() {
        User user = createUserHandler.handle(new CreateUserCommand(getDEFAULT_USER_NAME(), getDEFAULT_USER_MAIL(),
                getDEFAULT_USER_PASSWORD(), getDEFAULT_USER_SALT()));
        return user.getId();
    }
}
