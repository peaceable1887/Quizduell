package gruppe_b.quizduell.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import gruppe_b.quizduell.authserver.TestCases;
import gruppe_b.quizduell.common.config.AbstractSecurityConfig;
import gruppe_b.quizduell.common.config.RsaKeyProperties;

/**
 * Konfigurations-Klasse für die Security Einstellungen.
 * 
 * @author Christopher Burmeister
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends AbstractSecurityConfig {

    public SecurityConfig(RsaKeyProperties rsaKeys) {
        super(rsaKeys);
    }

    /**
     * Temp Bean für die Erstellung von Testdaten
     * 
     * @return
     */
    @Bean
    public TestCases getTestCasesClass() {
        return new TestCases();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(getRsaKeys().publicKey()).privateKey(getRsaKeys().privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
