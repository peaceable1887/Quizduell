package gruppe_b.quizduell.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import gruppe_b.quizduell.application.categories.CategoryRepository;
import gruppe_b.quizduell.application.playerstats.PlayerStatsRepository;
import gruppe_b.quizduell.application.questions.QuestionRepository;
import gruppe_b.quizduell.application.user.UserRepository;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    @Bean
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    public CategoryRepository categoryRepository() {
        return mock(CategoryRepository.class);
    }

    @Bean
    public QuestionRepository questionRepository() {
        return mock(QuestionRepository.class);
    }

    @Bean
    public PlayerStatsRepository playerStatsRepository() {
        return mock(PlayerStatsRepository.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return mock(PasswordEncoder.class);
    }
}
