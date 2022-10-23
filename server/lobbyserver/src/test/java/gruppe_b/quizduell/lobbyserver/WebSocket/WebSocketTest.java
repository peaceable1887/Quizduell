package gruppe_b.quizduell.lobbyserver.WebSocket;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.transport.handler.WebSocketTransportHandler;

import gruppe_b.quizduell.lobbyserver.Models.Lobby;
import gruppe_b.quizduell.lobbyserver.common.AuthHelper;
import gruppe_b.quizduell.lobbyserver.common.LobbyHelper;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Type;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketTest {

    @Autowired
    AuthHelper authHelper;

    @Value("${local.server.port}")
    private int port;

    static String WS_URI;

    static final String SUBSCRIBE_NEW_LOBBY_ENDPOINT = "/topic/new-lobby";
    static final String SUBSCRIBE_NEW_PLAYER_IN_LOBBY_ENDPOINT = "/topic/lobby/";

    CompletableFuture<String> completableFuture;

    @Autowired
    LobbyHelper lobbyHelper;

    @BeforeEach
    void setup() {
        WS_URI = "ws://localhost:" + port + "/lobby-websocket";
        completableFuture = new CompletableFuture<>();
    }

    @Test
    void whenPublishThenPublishNewLobby() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();
        String jwtToken = authHelper.generateToken();

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                asList(new WebSocketTransport(new StandardWebSocketClient()))));

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add("X-Authorization", jwtToken);
        StompHeaders connectHeaders = new StompHeaders();

        StompSession stompSession = stompClient.connect(WS_URI, handshakeHeaders,
                connectHeaders,
                new StompSessionHandlerAdapter() {
                })
                // "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJxdWl6ZHVlbGxfYXV0aHNlcnZlciIsInN1YiI6IjgzOTg0MWQ1LTgzMWMtNGNkMS1iOTExLTI3YThmZDA2ZTZiMSIsImV4cCI6MTY2NjUyMTQ2OCwiaWF0IjoxNjY2NTE3ODY4LCJzY29wZSI6IiJ9.oeiRkQAWBLwsiqIicH_U651IQOCQTSNknKBatoq_62fhNN77H8FP_AdMKHFjX4bqc4Rfu7KaYKOrIWCq288A6ocUz0mcdkO-hPBWpqYN4PvU0KQXIDjmzEdczDLeel4zf3OvjLQo3sSaW95frOMno48QKTJBasFpY_dLGOhOEBDrTjQCiyQIM9bNM3r6OZBjiip_MZGVuhcNDiB9kTzPC23IXIJqYdahsd3jvbKT6udSBe6Poj9fmpZaC_LJ3pOd9t--23WK9j87WUafnYqok5YTozUwu22QxSsYgnuZDDTSMIs6Mu43cRvhZqBZZoPWgJSq3syhlAKbSXEfLn0BrA")
                .get(1, TimeUnit.SECONDS);

        stompSession.subscribe(SUBSCRIBE_NEW_LOBBY_ENDPOINT,
                new PublishLobbyStompFrameHandler());

        // Act
        lobbyHelper.publishLobby(lobbyId);

        String lobby = completableFuture.get(10, TimeUnit.SECONDS);

        // Assert
        assertNotNull(lobby);
        assertTrue(lobby.contains(lobbyId.toString()));
    }

    // TODO: Test with expired jwt token

    private class PublishLobbyStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders headers, @Nullable Object payload) {
            // TODO Auto-generated method stub
            String msg = new String((byte[]) payload);
            completableFuture.complete(msg);
        }

    }
}
