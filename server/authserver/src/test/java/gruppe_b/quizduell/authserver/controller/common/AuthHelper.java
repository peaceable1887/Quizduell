package gruppe_b.quizduell.authserver.controller.common;

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

    @Autowired
    private final JwtEncoder jwtEncoder;

    @Autowired
    private RequestHandler<CreateUserCommand, User> createUserHandler;

    public AuthHelper(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
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
        return generateToken("00000000-0000-0000-0000-000000000000");
    }

    public UUID generateUser() {
        User user = createUserHandler.handle(new CreateUserCommand("authHelper", "john@john.de", "password", "salt"));
        return user.getId();
    }
}
