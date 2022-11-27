package gruppe_b.quizduell.lobbyserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.*;

/**
 * Konfiguration für die Websockets und deren Endpunkte.
 * 
 * @author Christopher Burmeister
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketBrokerConfig.class);

    /**
     * Endpunkt für zum Connection der Clients setzen.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/lobby-websocket")
                .withSockJS();

        logger.info("--> Stomp Endpoints registered");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefix für die Endpunkte zum Senden an den Server.
        registry.setApplicationDestinationPrefixes("/app", "/topic");
        logger.info("--> WebSocket prefixes set");
        // Prefix für die Endpunkte bzw. den Message-Broker, der an die Clients sendet.
        registry.enableSimpleBroker("/topic");
        logger.info("--> Simple Broker enabled");
    }
}
