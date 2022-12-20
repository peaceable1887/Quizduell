package gruppe_b.quizduell.lobbyserver.websocket;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;

import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.common.dto.PlayerStatusDto;
import gruppe_b.quizduell.lobbyserver.common.AuthHelper;
import gruppe_b.quizduell.lobbyserver.common.LobbyHelper;
import gruppe_b.quizduell.lobbyserver.common.LobbyStartDto;
import gruppe_b.quizduell.lobbyserver.enums.LobbyStatus;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Type;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class LobbyWebSocketTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    AuthHelper authHelper;

    @Value("${local.server.port}")
    private int port;

    static String WS_URI;

    String jwtToken;

    CompletableFuture<String> completableFuture;

    @Autowired
    LobbyHelper lobbyHelper;

    static final String SUBSCRIBE_NEW_LOBBY_ENDPOINT = "/topic/new-lobby";
    static final String SUBSCRIBE_LOBBY_UPDATE_ENDPOINT = "/topic/lobby/";
    static final String SUBSCRIBE_DELETE_LOBBY_ENDPOINT = "/topic/lobby/delete-lobby";
    static final String SUBSCRIBE_LOBBY_START = "/topic/lobby/";
    static final String SEND_ENDPOINT_PLAYER_STATUS_UPDATE = "/app/lobby/";

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
    void whenSendStatusUpdateThenSendLobbyUpdate() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();
        String playerId = lobbyHelper.getLobby(
                lobbyId).getPlayers().get(0).getUserId().toString();
        jwtToken = authHelper.generateToken(playerId);

        PlayerStatusDto dto = new PlayerStatusDto();
        dto.status = "wait";
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_LOBBY_UPDATE_ENDPOINT + lobbyId.toString(),
                new PublishLobbyStompFrameHandler());

        Thread.sleep(2000);

        completableFuture = new CompletableFuture<>();

        // Act
        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status-player",
                json.getBytes());

        String lobby = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(lobby);
        assertTrue(lobby.contains(lobbyId.toString()));
        assertTrue(lobby.contains(playerId + "\",\"status\":\"wait"));
    }

    @Test
    void whenLastPlayerDisconnectThenSendDeleteLobby() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();
        String playerId = lobbyHelper.getLobby(
                lobbyId).getPlayers().get(0).getUserId().toString();
        jwtToken = authHelper.generateToken(playerId);

        JSONObject jdisconnectRequest = new JSONObject();
        jdisconnectRequest.put("lobbyId", lobbyId.toString());

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_DELETE_LOBBY_ENDPOINT,
                new PublishLobbyStompFrameHandler());

        completableFuture = new CompletableFuture<>();

        // Act
        this.mvc.perform(post("/v1/disconnect")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jdisconnectRequest.toJSONString()))
                .andReturn();

        String result = completableFuture.get(500, TimeUnit.SECONDS);

        // Assert
        assertNotNull(result);
        assertEquals("\"" + lobbyId.toString() + "\"", result);
    }

    @Test
    void whenOnePlayerInLobbyAndUpdateToReadyWhenNoUpdate() throws Exception {
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

        stompSession.subscribe(SUBSCRIBE_LOBBY_START + lobbyId.toString() + "/start-lobby",
                new PublishLobbyStompFrameHandler());

        completableFuture = new CompletableFuture<>();

        // Act
        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status-player",
                json.getBytes());

        // Assert
        assertThrows(TimeoutException.class, () -> {
            completableFuture.get(5, TimeUnit.SECONDS);
        });
    }

    @Test
    void whenAllPlayerReadyThenStartCountdown() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createFullLobby();
        lobbyHelper.getLobby(lobbyId).getPlayers().get(1).setReady();
        String playerId = lobbyHelper.getLobby(
                lobbyId).getPlayers().get(0).getUserId().toString();
        jwtToken = authHelper.generateToken(playerId);

        PlayerStatusDto dto = new PlayerStatusDto();
        dto.status = "ready";
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_LOBBY_START + lobbyId.toString() + "/start-lobby",
                new PublishLobbyStompFrameHandler());

        completableFuture = new CompletableFuture<>();

        // Act
        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status-player",
                json.getBytes());

        String status;
        LobbyStartDto lobbyStartDto;

        // Countdown 3
        status = completableFuture.get(5, TimeUnit.SECONDS);
        completableFuture = new CompletableFuture<>();
        lobbyStartDto = objectMapper.readValue(status, LobbyStartDto.class);
        assertEquals(3, lobbyStartDto.countdown);
        assertEquals("start", lobbyStartDto.status);
        assertNull(lobbyStartDto.gameToken);

        // Countdown 2
        status = completableFuture.get(5, TimeUnit.SECONDS);
        completableFuture = new CompletableFuture<>();
        lobbyStartDto = objectMapper.readValue(status, LobbyStartDto.class);
        assertEquals(2, lobbyStartDto.countdown);
        assertEquals("start", lobbyStartDto.status);
        assertNull(lobbyStartDto.gameToken);

        // Countdown 1
        status = completableFuture.get(5, TimeUnit.SECONDS);
        completableFuture = new CompletableFuture<>();
        lobbyStartDto = objectMapper.readValue(status, LobbyStartDto.class);
        assertEquals(1, lobbyStartDto.countdown);
        assertEquals("start", lobbyStartDto.status);
        assertNull(lobbyStartDto.gameToken);

        // Start
        status = completableFuture.get(5, TimeUnit.SECONDS);
        completableFuture = new CompletableFuture<>();
        lobbyStartDto = objectMapper.readValue(status, LobbyStartDto.class);

        // Assert
        assertNotNull(lobbyStartDto);
        assertEquals(0, lobbyStartDto.countdown);
        assertEquals("start", lobbyStartDto.status);
        assertNotNull(lobbyStartDto.gameToken);
    }

    @Test
    void whenCountdownIsRunningAndPlayerSendStatusWaitThenAbortCountdown() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createFullLobby();
        lobbyHelper.getLobby(lobbyId).getPlayers().get(1).setReady();
        String playerId = lobbyHelper.getLobby(
                lobbyId).getPlayers().get(0).getUserId().toString();
        jwtToken = authHelper.generateToken(playerId);

        PlayerStatusDto dto = new PlayerStatusDto();
        dto.status = "ready";
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_LOBBY_START + lobbyId.toString() + "/start-lobby",
                new PublishLobbyStompFrameHandler());

        completableFuture = new CompletableFuture<>();

        // Act
        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status-player",
                json.getBytes());

        String status;
        LobbyStartDto lobbyStartDto;

        // Countdown 3
        status = completableFuture.get(500, TimeUnit.SECONDS);
        completableFuture = new CompletableFuture<>();
        lobbyStartDto = objectMapper.readValue(status, LobbyStartDto.class);
        assertEquals(3, lobbyStartDto.countdown);
        assertEquals("start", lobbyStartDto.status);
        assertNull(lobbyStartDto.gameToken);

        // Abort
        dto.status = "wait";
        json = objectMapper.writeValueAsString(dto);
        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status-player",
                json.getBytes());

        int counter = 0;
        while (true) {
            status = completableFuture.get(500, TimeUnit.SECONDS);
            completableFuture = new CompletableFuture<>();
            lobbyStartDto = objectMapper.readValue(status, LobbyStartDto.class);
            if (lobbyStartDto.status.equals("abort")) {
                break;
            }
            counter++;
            assertTrue(counter < 10);
        }

        // Assert
        assertNotNull(lobbyStartDto);
        assertEquals("abort", lobbyStartDto.status);
        assertNull(lobbyStartDto.gameToken);
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

        completableFuture = new CompletableFuture<>();

        // Act
        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status-player",
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
