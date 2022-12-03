// package gruppe_b.quizduell.lobbyserver.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.ProviderManager;
// import
// org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.oauth2.jwt.JwtEncoder;
// import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

// import com.nimbusds.jose.jwk.JWK;
// import com.nimbusds.jose.jwk.JWKSet;
// import com.nimbusds.jose.jwk.RSAKey;
// import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
// import com.nimbusds.jose.jwk.source.JWKSource;
// import com.nimbusds.jose.proc.SecurityContext;

// import gruppe_b.quizduell.common.config.AbstractSecurityConfig;
// import gruppe_b.quizduell.common.config.RsaKeyProperties;

// @Configuration
// public class TestSecurityConfig extends AbstractSecurityConfig {

// protected TestSecurityConfig(RsaKeyProperties rsaKeys) {
// super(rsaKeys);
// }

// @Bean
// JwtEncoder jwtEncoder() {
// JWK jwk = new
// RSAKey.Builder(getRsaKeys().publicKey()).privateKey(getRsaKeys().privateKey()).build();
// JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
// return new NimbusJwtEncoder(jwks);
// }
// }
