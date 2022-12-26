package gruppe_b.quizduell.authserver.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.services.UserDetailsImp;
import gruppe_b.quizduell.authserver.common.UserJwtDto;

/**
 * Service zum Erstellen eines JWT.
 * 
 * @author Christopher Burmeister
 */
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public UserJwtDto generateToken(Authentication authentication) {

        UserDetailsImp user = (UserDetailsImp) authentication.getPrincipal();

        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("quizduell_authserver")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(user.getId().toString()) // UserId zum Identifizieren setzen
                .claim("name", user.getUsername())
                .claim("scope", scope)
                .build();
        String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new UserJwtDto(token, user.getId(), user.getUsername());
    }
}
