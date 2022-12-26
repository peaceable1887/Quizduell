package gruppe_b.quizduell.statsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import gruppe_b.quizduell.common.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@ComponentScan(basePackages = "gruppe_b.quizduell.application.*")
@ComponentScan(basePackages = "gruppe_b.quizduell.persistence.*")
public class StatsserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatsserverApplication.class, args);
    }
}
