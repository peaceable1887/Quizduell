package gruppe_b.quizduell.quizserver.websocket;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.databind.ObjectMapper;

import gruppe_b.quizduell.common.dto.PlayerStatusDto;
import gruppe_b.quizduell.common.models.Player;
import gruppe_b.quizduell.lobbyserver.common.LobbyStartDto;
import gruppe_b.quizduell.quizserver.common.AuthHelper;
import gruppe_b.quizduell.quizserver.common.QuizHelper;
import gruppe_b.quizduell.quizserver.common.QuizStartDto;
import gruppe_b.quizduell.quizserver.models.Quiz;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuizWebSocketTest {

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
    QuizHelper quizHelper;

    static final String SUBSCRIBE_QUIZ_UPDATE_ENDPOINT = "/topic/quiz/";
    static final String SUBSCRIBE_QUIZ_START = "/topic/quiz/";
    static final String SEND_ENDPOINT_PLAYER_STATUS_UPDATE = "/app/quiz/";

    @BeforeEach
    void setup() {
        WS_URI = "ws://localhost:" + port + "/quiz-websocket";
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
    void whenSubscribeQuizThenReturnQuiz() throws Exception {
        // Arrange
        UUID lobbyId = quizHelper.createQuiz().getLobbyId();

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_QUIZ_UPDATE_ENDPOINT + lobbyId.toString(),
                new PublishQuizStompFrameHandler());

        // Act
        String quiz = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(quiz);
        assertTrue(quiz.contains(lobbyId.toString()));
    }

    @Test
    void whenSendStatusUpdateThenSendLobbyUpdate() throws Exception {
        // Arrange
        Quiz quiz = quizHelper.createQuiz();
        UUID lobbyId = quiz.getLobbyId();
        UUID playerId = quiz.getPlayers().get(0).getUserId();
        jwtToken = authHelper.generateToken(playerId.toString());

        PlayerStatusDto dto = new PlayerStatusDto();
        dto.status = "ready";
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_QUIZ_UPDATE_ENDPOINT + lobbyId.toString(),
                new PublishQuizStompFrameHandler());

        Thread.sleep(2000);

        completableFuture = new CompletableFuture<>();

        // Act
        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status-player",
                json.getBytes());

        String result = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(lobbyId.toString()));
        assertTrue(result.contains(playerId.toString() + "\",\"status\":\"ready"));
    }

    @Test
    void whenAllPlayerReadyThenStartCountdown() throws Exception {
        // Arrange
        Quiz quiz = quizHelper.createQuiz();
        UUID lobbyId = quiz.getLobbyId();
        quiz.getPlayers().get(0).setReady();

        UUID player2Id = UUID.randomUUID();
        quiz.addPlayer(player2Id);
        jwtToken = authHelper.generateToken(player2Id.toString());

        PlayerStatusDto dto = new PlayerStatusDto();
        dto.status = "ready";
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_QUIZ_START + lobbyId.toString() + "/start-quiz",
                new PublishQuizStompFrameHandler());

        completableFuture = new CompletableFuture<>();

        // Act
        stompSession.send(SEND_ENDPOINT_PLAYER_STATUS_UPDATE + lobbyId.toString() + "/status-player",
                json.getBytes());

        String status;
        QuizStartDto quizStartDto;

        // Countdown 3
        status = completableFuture.get(5, TimeUnit.SECONDS);
        completableFuture = new CompletableFuture<>();
        quizStartDto = objectMapper.readValue(status, QuizStartDto.class);
        assertEquals(3, quizStartDto.countdown);
        assertEquals("start", quizStartDto.status);

        // Countdown 2
        status = completableFuture.get(5, TimeUnit.SECONDS);
        completableFuture = new CompletableFuture<>();
        quizStartDto = objectMapper.readValue(status, QuizStartDto.class);
        assertEquals(2, quizStartDto.countdown);
        assertEquals("start", quizStartDto.status);

        // Countdown 1
        status = completableFuture.get(5, TimeUnit.SECONDS);
        completableFuture = new CompletableFuture<>();
        quizStartDto = objectMapper.readValue(status, QuizStartDto.class);
        assertEquals(1, quizStartDto.countdown);
        assertEquals("start", quizStartDto.status);

        // Start
        status = completableFuture.get(5, TimeUnit.SECONDS);
        completableFuture = new CompletableFuture<>();
        quizStartDto = objectMapper.readValue(status, QuizStartDto.class);

        // Assert
        assertNotNull(quizStartDto);
        assertEquals(0, quizStartDto.countdown);
        assertEquals("start", quizStartDto.status);
    }

    private class PublishQuizStompFrameHandler implements StompFrameHandler {

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
