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

    // @Override
    // public void configureClientInboundChannel(ChannelRegistration registration) {
    // registration.interceptors(new ChannelInterceptor() {
    // @Override
    // public Message<?> preSend(Message<?> message, MessageChannel channel) {
    // StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
    // StompHeaderAccessor.class);
    // if (StompCommand.CONNECT.equals(accessor.getCommand())) {
    // List<String> authorization = accessor.getNativeHeader("X-Authorization");
    // logger.debug("X-Authorization: {}", authorization);

    // String accessToken = authorization.get(0).split(" ")[1];
    // Jwt jwt = jwtDecoder.decode(accessToken);
    // JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    // Authentication authentication = converter.convert(jwt);
    // accessor.setUser(authentication);
    // }
    // return message;
    // }
    // });
    // }

    // @Override
    // public void configureClientInboundChannel(ChannelRegistration registration) {
    // logger.info("--> Set Interceptor for inbound client messages");

    // registration.interceptors(new ChannelInterceptor() {
    // @Override
    // public Message<?> preSend(Message<?> message, MessageChannel channel) {
    // logger.info("--> Check JWT for inbound client message");

    // StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
    // message, StompHeaderAccessor.class);
    // logger.info("--> Inbound command: {}", accessor.getCommand());
    // logger.info("--> Inbound command equals connect: {}",
    // StompCommand.CONNECT.equals(accessor.getCommand()));
    // if (StompCommand.CONNECT.equals(accessor.getCommand())) {
    // List<String> authorization = accessor.getNativeHeader("X-Authorization");
    // Map<String, Object> token = accessor.getSessionAttributes();
    // if (authorization == null) {
    // logger.warn("Missing X-Authorization in header!");
    // return message;
    // }

    // logger.info("X-Authorization: {}", authorization);

    // String accessToken = authorization.get(0).split(" ")[1];

    // logger.info("JWT: {}", accessToken);

    // Jwt jwt = jwtDecoder.decode(accessToken);
    // JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    // Authentication authentication = converter.convert(jwt);
    // accessor.setUser(authentication);
    // }
    // Jwt jwt = jwtDecoder.decode(
    // "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJxdWl6ZHVlbGxfYXV0aHNlcnZlciIsInN1YiI6IjJkNmE5NTUyLTdhNmMtNGNjYi04MTg1LTkwOTM3MzE2YTUyZCIsImV4cCI6MTY2NzQwNzM1MCwiaWF0IjoxNjY3NDAzNzUwLCJzY29wZSI6IiJ9.kpZiVGYRKbG1Vy2dihQKlBZSYQ-QlmukCNKUQhNWmG3PBPTXFQmeQFGAI9c7fiD9TWJTa347uNTjjZcs3EpR-csRf5_-DgzB9evfOpupi78_ScyQGxh5tXUXCBf2ita0nMXj9MEBacTaOp7VucB1kYBXhZh3qUSvhfeMzzKNosDO4ESGWXpNxqJIOxh-vMFpHenhX-ZRgaLKk1HDSDBEawltoaFT4t9m5kA9WoF5qpkUpjtjw80khC8LT_rDbl6iT76gk8ms3xtfCBgX0H3mTdc8NVVC_8tSX0Zk8Pj6aA5Osw8YnDymqbOf1ZA1XuZs7Wmjw8tX9uyUqPhQlL4ZAg");
    // JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    // Authentication authentication = converter.convert(jwt);
    // accessor.setUser(authentication);
    // return message;
    // }
    // });
    // }
}
