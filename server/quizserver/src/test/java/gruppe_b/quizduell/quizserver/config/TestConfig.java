package gruppe_b.quizduell.quizserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import gruppe_b.quizduell.lobbyserver.services.LobbyService;

@Configuration
public class TestConfig {

    @Autowired
    JwtEncoder jwtEncoder;

    @Bean
    public LobbyService getLobbyService() {
        return new LobbyService();
    }
}
