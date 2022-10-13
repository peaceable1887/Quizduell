package gruppe_b.quizduell.authserver.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gruppe_b.quizduell.authserver.common.UserCredentialsDto;
import gruppe_b.quizduell.authserver.service.TokenService;
import gruppe_b.quizduell.authserver.service.UserRegisterService;

@RestController
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;
    private final UserRegisterService userRegisterService;

    public AuthController(
            TokenService tokenService,
            UserRegisterService userRegisterService) {
        this.tokenService = tokenService;
        this.userRegisterService = userRegisterService;
    }

    @PostMapping("/token")
    public ResponseEntity<String> token(Authentication authentication) {
        LOG.debug("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        LOG.debug("Token granted {}", token);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {
        userRegisterService.saveUser(userCredentialsDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
