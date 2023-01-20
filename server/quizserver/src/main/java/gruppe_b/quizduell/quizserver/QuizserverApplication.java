package gruppe_b.quizduell.quizserver;

import java.io.Console;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import gruppe_b.quizduell.common.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@ComponentScan(basePackages = "gruppe_b.quizduell.application.*")
@ComponentScan(basePackages = "gruppe_b.quizduell.persistence.*")
public class QuizserverApplication implements InitializingBean {

	public static void main(String[] args) {
		SpringApplication.run(QuizserverApplication.class, args);
	}

	@Autowired
	SeedDb seedDb;

	@Override
	public void afterPropertiesSet() throws Exception {
		seedDb.seed();
	}
}
