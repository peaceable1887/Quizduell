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
                        "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJxdWl6ZHVlbGxfYXV0aHNlcnZlciIsInN1YiI6IjFhMDIxMWY2LWRiOGYtNGIxYS1hMGFmLWM0NDhjZWIzNTBiNyIsImV4cCI6MTY2NjQ2MTYwNywiaWF0IjoxNjY2NDU4MDA3LCJzY29wZSI6IiJ9.EhGoea8ECMsq9LQ2WvLd3ShrENbQQxC3ePZ14zbGAn-2XajZpcHFofzndaJM2RBJ-oPWwT6RlQWHsNXBb0TzTjYQ0UkWW0hjehpq_4uQwwblqXwVsjgxWBEHaim53vsNYBzx9jGbKePi46KObiISwBIFTBmYg68m6EbJ0RCFEElrZ3lWLvcZXEOYlcAbXKbKda13_EmZaQ6DrRZNyyzaMX2exotX6Q8qSID2IeRGl8lsWEbLdR5ZrZBttZCAaO8SbDWTChxBTpL6oy1Y5bS-3CBn3VyhCyc8sGVnMoRv0FPwS6apKGQm2-1vyNcwmu5WRyI4rSM7AmWc1PbpgxpxCA");
                JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
                Authentication authentication = converter.convert(jwt);
                accessor.setUser(authentication);
                return message;
            }
        });
    }
}
