package gruppe_b.quizduell.quizserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Konfiguration für die Websockets und deren Endpunkte.
 * 
 * @author Christopher Burmeister
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketBrokerConfig.class);

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/quiz-websocket")
                .withSockJS();

        logger.info("--> Stomp Endpoints registered");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefix für die Endpunkte zum Senden an den Server.
        registry.setApplicationDestinationPrefixes("/quiz");
        logger.info("--> WebSocket prefixes set");
        // Prefix für die Endpunkte zum Abonnieren durch den Client.
        registry.enableSimpleBroker("/topic");
        logger.info("--> Simple Broker enabled");
    }
}
