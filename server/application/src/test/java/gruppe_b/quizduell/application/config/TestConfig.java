package gruppe_b.quizduell.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import gruppe_b.quizduell.application.categories.CategoryRepository;
import gruppe_b.quizduell.application.game.QuizSession;
import gruppe_b.quizduell.application.interfaces.SendToPlayerService;
import gruppe_b.quizduell.application.models.Quiz;
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
}
