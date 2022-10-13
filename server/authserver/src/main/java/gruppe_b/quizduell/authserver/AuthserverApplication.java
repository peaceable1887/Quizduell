package gruppe_b.quizduell.authserver;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import gruppe_b.quizduell.authserver.config.RsaKeyProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@ComponentScan(basePackages = "gruppe_b.quizduell.application.*")
@ComponentScan(basePackages = "gruppe_b.quizduell.persistence.*")
public class AuthserverApplication implements InitializingBean {

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}

	@Autowired
	TestCases test;

	@Override
	public void afterPropertiesSet() throws Exception {
		test.createTestUserJohn();
	}
}
