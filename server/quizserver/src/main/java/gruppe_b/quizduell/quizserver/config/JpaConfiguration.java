package gruppe_b.quizduell.quizserver.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Konfigurations-Klasse für JpaRepo's aus dem "persistence" Layer.
 * Konfiguration erfolgt über annotation.
 * 
 * @author Christopher Burmeister
 */
@Configuration
@EnableJpaRepositories("gruppe_b.quizduell.persistence.repository")
@EntityScan("gruppe_b.quizduell.persistence.entities")
public class JpaConfiguration {

}
