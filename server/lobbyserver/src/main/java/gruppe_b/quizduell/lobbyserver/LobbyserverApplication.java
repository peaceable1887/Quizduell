package gruppe_b.quizduell.lobbyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import gruppe_b.quizduell.common.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class LobbyserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(LobbyserverApplication.class, args);
	}

}
