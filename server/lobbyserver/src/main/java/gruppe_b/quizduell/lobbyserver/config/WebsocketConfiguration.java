package gruppe_b.quizduell.lobbyserver.config;

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
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

    /**
     * Endpunkt für zum Connection der Clients setzen.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/lobby-websocket").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefix für die Endpunkte zum Senden an den Server.
        registry.setApplicationDestinationPrefixes("lobby");
        // Prefix für die Endpunkte zum Abonnieren durch den Client.
        registry.enableSimpleBroker("/topic");
    }
}
