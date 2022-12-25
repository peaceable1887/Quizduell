package gruppe_b.quizduell.statsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import gruppe_b.quizduell.common.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class StatsserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatsserverApplication.class, args);
    }
}
