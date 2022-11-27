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
import org.springframework.messaging.simp.stomp.ConnectionLostException;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;

import gruppe_b.quizduell.lobbyserver.common.AuthHelper;
import gruppe_b.quizduell.lobbyserver.common.LobbyHelper;
import gruppe_b.quizduell.lobbyserver.common.PlayerStatusDto;
import gruppe_b.quizduell.lobbyserver.models.Lobby;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    String jwtToken;

    static final String SUBSCRIBE_NEW_LOBBY_ENDPOINT = "/topic/new-lobby";
    static final String SUBSCRIBE_LOBBY_UPDATE_ENDPOINT = "/topic/lobby/";
    static final String SEND_ENDPOINT_PLAYER_STATUS_UPDATE = "/app/lobby/";

    CompletableFuture<String> completableFuture;

    @Autowired
    LobbyHelper lobbyHelper;

    @BeforeEach
    void setup() {
        WS_URI = "ws://localhost:" + port + "/lobby-websocket";
        completableFuture = new CompletableFuture<>();
        jwtToken = authHelper.generateToken();
    }

    StompSession connectAndGetStompSession() throws Exception {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                asList(new WebSocketTransport(new StandardWebSocketClient()))));

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", jwtToken);

        return stompClient.connect(WS_URI, handshakeHeaders, connectHeaders,
                new StompSessionHandlerAdapter() {
                })
                .get(5, TimeUnit.SECONDS);
    }

    @Test
    void whenConnectThenConnect() throws Exception {
        assertNotNull(connectAndGetStompSession());
    }

    @Test
    void whenPublishThenPublishNewLobby() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_NEW_LOBBY_ENDPOINT,
                new PublishLobbyStompFrameHandler());

        completableFuture = new CompletableFuture<>();

        // Act
        lobbyHelper.publishLobby(lobbyId);

        String lobby = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(lobby);
        assertTrue(lobby.contains(lobbyId.toString()));
    }

    @Test
    void whenSubscribeNewLobbyThenReturnLobby() throws Exception {
        // Arrange
        UUID lobbyId1 = lobbyHelper.createLobby();
        UUID lobbyId2 = lobbyHelper.createLobby();
        UUID lobbyId3 = lobbyHelper.createLobby();

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_NEW_LOBBY_ENDPOINT,
                new PublishLobbyStompFrameHandler());

        // Act
        String lobby = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(lobby);
        assertTrue(lobby.contains(lobbyId1.toString()));
        assertTrue(lobby.contains(lobbyId2.toString()));
        assertTrue(lobby.contains(lobbyId3.toString()));
    }

    @Test
    void whenSubscribeLobbyIdThenReturnLobby() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_LOBBY_UPDATE_ENDPOINT + lobbyId.toString(),
                new PublishLobbyStompFrameHandler());

        // Act
        String lobby = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(lobby);
        assertTrue(lobby.contains(lobbyId.toString()));
    }

    @Test
    void whenSendStatusThenSendLobbyUpdate() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();
        String playerId = lobbyHelper.getLobby(
                lobbyId).getPlayers().get(0).getUserId().toString();
        jwtToken = authHelper.generateToken(playerId);

        PlayerStatusDto dto = new PlayerStatusDto();
        dto.status = "ready";
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_LOBBY_UPDATE_ENDPOINT + lobbyId.toString(),
                new PublishLobbyStompFrameHandler());

        Thread.sleep(2000);

        // Act
        completableFuture = new CompletableFuture<>();

        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status",
                json.getBytes());

        String lobby = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(lobby);
        assertTrue(lobby.contains(lobbyId.toString()));
        assertTrue(lobby.contains(playerId + "\",\"status\":\"ready"));
    }

    @Test
    void whenTokenExpiredThenThrowExpiredException() throws Exception {
        // Arrange
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                asList(new WebSocketTransport(new StandardWebSocketClient()))));

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", authHelper.generateExpiredToken());

        // Act
        try {
            StompSession stompSession = stompClient.connect(WS_URI, handshakeHeaders, connectHeaders,
                    new StompSessionHandlerAdapter() {
                    }).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Assert
            assertEquals(ConnectionLostException.class, e.getCause().getClass());
        }
    }

    @Test
    void whenMissingAttributesThenThrowException() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();
        String playerId = lobbyHelper.getLobby(
                lobbyId).getPlayers().get(0).getUserId().toString();
        jwtToken = authHelper.generateToken(playerId);

        PlayerStatusDto dto = new PlayerStatusDto();
        // dto.playerId = playerId;
        // dto.status = "ready";
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe("/user/queue/errors",
                new PublishLobbyStompFrameHandler());

        Thread.sleep(2000);

        // Act
        completableFuture = new CompletableFuture<>();

        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status",
                json.getBytes());

        String lobby = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(lobby);
        assertTrue(lobby.contains("status null"));
    }

    @Test
    void sendToTestMessageMapping() throws Exception {
        // Arrange
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                asList(new WebSocketTransport(new StandardWebSocketClient()))));

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", jwtToken);

        StompSession stompSession = stompClient.connect(WS_URI, handshakeHeaders, connectHeaders,
                new StompSessionHandlerAdapter() {
                }).get(5, TimeUnit.SECONDS);

        stompSession.subscribe("/topic/test",
                new PublishLobbyStompFrameHandler());

        Thread.sleep(2000);

        // Act
        completableFuture = new CompletableFuture<>();

        stompSession.send("/app/test", "test".getBytes());

        String lobby = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(lobby);
        assertTrue(lobby.contains("hello from @MessageMapping /test"));
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
