package gruppe_b.quizduell.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import gruppe_b.quizduell.application.game.QuizSession;
import gruppe_b.quizduell.application.interfaces.SendToPlayerService;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.application.user.UserRepository;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    // @Bean
    // public SendToPlayerService getSentToPlayerService() {
    // return mock(SendToPlayerService.class);
    // }

    @Bean
    public UserRepository UserRepository() {
        return mock(UserRepository.class);
    }
}
