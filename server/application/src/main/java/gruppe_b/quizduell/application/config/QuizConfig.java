package gruppe_b.quizduell.application.config;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuizConfig {

    @Bean
    public Random getRandom() {
        return new Random();
    }
}
