package gruppe_b.quizduell.lobbyserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import gruppe_b.quizduell.common.config.AbstractSecurityConfig;
import gruppe_b.quizduell.common.config.RsaKeyProperties;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends AbstractSecurityConfig {

    public SecurityConfig(RsaKeyProperties rsaKeys) {
        super(rsaKeys);
    }
}
