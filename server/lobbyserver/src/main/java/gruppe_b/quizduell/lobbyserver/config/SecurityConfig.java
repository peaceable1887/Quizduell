package gruppe_b.quizduell.lobbyserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import gruppe_b.quizduell.common.config.AbstractSecurityConfig;
import gruppe_b.quizduell.common.config.RsaKeyProperties;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends AbstractSecurityConfig {

    public SecurityConfig(RsaKeyProperties rsaKeys) {
        super(rsaKeys);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // return http
        // .csrf(csrf -> csrf.disable())
        // .authorizeRequests(auth -> auth
        // .antMatchers("/lobby-websocket/info").permitAll())
        // .authorizeRequests(auth -> auth
        // .anyRequest().authenticated())
        // .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        // .sessionManagement(session ->
        // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // .httpBasic(Customizer.withDefaults())
        // .build();

        return http
                .authorizeRequests(auth -> auth
                        .antMatchers("/lobby-websocket/info").permitAll())
                .authorizeRequests(auth -> auth
                        .antMatchers("/topic/*").permitAll())
                .authorizeRequests(auth -> auth
                        .antMatchers("/lobby/*").authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
