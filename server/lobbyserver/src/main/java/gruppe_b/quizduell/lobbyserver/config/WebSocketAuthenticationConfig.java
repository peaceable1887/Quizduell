package gruppe_b.quizduell.lobbyserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 50)
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthenticationConfig.class);

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        logger.info("--> Set Interceptor for inbound client messages");

        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                logger.info("--> Check JWT for inbound client message");

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                        message, StompHeaderAccessor.class);
                logger.info("--> Inbound command: {}", accessor.getCommand());
                logger.info("--> Inbound command equals connect: {}",
                        StompCommand.CONNECT.equals(accessor.getCommand()));
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("X-Authorization");
                    Map<String, Object> token = accessor.getSessionAttributes();
                    if (authorization == null) {
                        logger.warn("Missing X-Authorization in header!");
                        return message;
                    }

                    logger.info("X-Authorization: {}", authorization);

                    String accessToken = authorization.get(0).split(" ")[1];

                    logger.info("JWT: {}", accessToken);

                    Jwt jwt = jwtDecoder.decode(accessToken);
                    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
                    Authentication authentication = converter.convert(jwt);
                    accessor.setUser(authentication);
                }
                Jwt jwt = jwtDecoder.decode(
                        "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJxdWl6ZHVlbGxfYXV0aHNlcnZlciIsInN1YiI6IjgzOTg0MWQ1LTgzMWMtNGNkMS1iOTExLTI3YThmZDA2ZTZiMSIsImV4cCI6MTY2NjUyMTQ2OCwiaWF0IjoxNjY2NTE3ODY4LCJzY29wZSI6IiJ9.oeiRkQAWBLwsiqIicH_U651IQOCQTSNknKBatoq_62fhNN77H8FP_AdMKHFjX4bqc4Rfu7KaYKOrIWCq288A6ocUz0mcdkO-hPBWpqYN4PvU0KQXIDjmzEdczDLeel4zf3OvjLQo3sSaW95frOMno48QKTJBasFpY_dLGOhOEBDrTjQCiyQIM9bNM3r6OZBjiip_MZGVuhcNDiB9kTzPC23IXIJqYdahsd3jvbKT6udSBe6Poj9fmpZaC_LJ3pOd9t--23WK9j87WUafnYqok5YTozUwu22QxSsYgnuZDDTSMIs6Mu43cRvhZqBZZoPWgJSq3syhlAKbSXEfLn0BrA");
                JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
                Authentication authentication = converter.convert(jwt);
                accessor.setUser(authentication);
                return message;
            }
        });
    }
}
