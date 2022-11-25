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

import gruppe_b.quizduell.lobbyserver.common.AuthHelper;
import gruppe_b.quizduell.lobbyserver.common.LobbyHelper;
import gruppe_b.quizduell.lobbyserver.models.Lobby;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Type;

@SpringBootTest
public class WebSocketLiveTest {

    static String WS_URI = "ws://test.burmeister.hamburg/lobby-websocket";

    CompletableFuture<String> completableFuture;

    @Test
    void webSocketConnectTest() throws Exception {
        // Arrange
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                asList(new WebSocketTransport(new StandardWebSocketClient()))));

        StompSession stompSession = stompClient.connect(WS_URI,
                new StompSessionHandlerAdapter() {
                }).get(10, TimeUnit.SECONDS);

        completableFuture = new CompletableFuture<>();

        stompSession.subscribe("/topic/new-lobby",
                new PublishLobbyStompFrameHandler());

        // Act
        String lobby = completableFuture.get(30, TimeUnit.SECONDS);

        // Assert
    }

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
