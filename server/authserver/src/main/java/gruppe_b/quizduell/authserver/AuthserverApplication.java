package gruppe_b.quizduell.authserver;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import gruppe_b.quizduell.common.config.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@ComponentScan(basePackages = "gruppe_b.quizduell.application.*")
@ComponentScan(basePackages = "gruppe_b.quizduell.persistence.*")
@ComponentScan(basePackages = "gruppe_b.quizduell.infrastructure.*")
public class AuthserverApplication implements InitializingBean {

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}

	// @Autowired
	// TestCases test;

	@Override
	public void afterPropertiesSet() throws Exception {
		// test.createTestUserJohn();
	}
}
