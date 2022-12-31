package gruppe_b.quizduell.authserver.controller;

import java.security.Principal;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gruppe_b.quizduell.application.common.UserCredentialsDto;
import gruppe_b.quizduell.application.common.UserDetailsDto;
import gruppe_b.quizduell.application.services.UserRegisterService;
import gruppe_b.quizduell.application.services.UserService;
import gruppe_b.quizduell.authserver.common.UserJwtDto;
import gruppe_b.quizduell.authserver.service.TokenService;

/**
 * Rest-Controller zum Registrieren und für den Login bzw. das Erzeugen eines
 * JWT.
 * 
 * @author Christopher Burmeister
 */
@RestController
@RequestMapping("/v1")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final UserRegisterService userRegisterService;

    @Autowired
    JwtDecoder jwtDecoder;

    public AuthController(
            TokenService tokenService,
            UserRegisterService userRegisterService) {
        this.tokenService = tokenService;
        this.userRegisterService = userRegisterService;
    }

    /**
     * Erzeugt bei erfolgreicher Authentifizierung ein JWT.
     * 
     * @param authentication Spring Security Objekt mit Username und Passwort
     * @return JWT
     */
    @GetMapping("/token")
    public ResponseEntity<UserJwtDto> token(Authentication authentication) {
        logger.debug("Token requested for user: '{}'", authentication.getName());
        UserJwtDto dto = tokenService.generateToken(authentication);
        logger.debug("Token granted {}", dto.token);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    /**
     * Registriert einen neuen User.
     * 
     * @param userCredentialsDto request enthält Username, Mail (optional), Password
     * @return Http Status
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {
        userRegisterService.saveUser(userCredentialsDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
