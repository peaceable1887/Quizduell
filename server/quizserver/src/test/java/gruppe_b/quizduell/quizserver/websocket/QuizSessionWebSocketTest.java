package gruppe_b.quizduell.quizserver.websocket;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.databind.ObjectMapper;

import gruppe_b.quizduell.application.common.GameSessionDto;
import gruppe_b.quizduell.application.common.PlayerRoundStatus;
import gruppe_b.quizduell.application.enums.RoundStatus;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.common.dto.PlayerStatusDto;
import gruppe_b.quizduell.common.enums.PlayerStatus;
import gruppe_b.quizduell.quizserver.common.AuthHelper;
import gruppe_b.quizduell.quizserver.common.QuizHelper;
import gruppe_b.quizduell.quizserver.common.QuizSessionHelper;
import gruppe_b.quizduell.quizserver.services.QuizService;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
public class QuizSessionWebSocketTest {

    @Autowired
    AuthHelper authHelper;

    @Autowired
    QuizHelper quizHelper;

    @Autowired
    QuizSessionHelper sessionHelper;

    @Autowired
    QuizService quizService;

    static {
        System.setProperty("DB_PORT", "3306");
        System.setProperty("DB_HOSTNAME", "localhost");
        System.setProperty("DB_USERNAME", "root");
        System.setProperty("DB_PASSWORD", "root");
    }

    @Value("${local.server.port}")
    private int port;

    static String WS_URI;

    String jwtToken;

    CompletableFuture<String> completableFuture;

    static final String SUBSCRIBE_QUIZ_SESSION = "/topic/quiz/session/";
    static final String SUBSCRIBE_QUIZ_SESSION_ROUND_COUNTDOWN = "/topic/quiz/session/";
    static final String SEND_QUIZ_SESSION_ANSWER = "/app/quiz/session/";

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
    void whenAllPlayerReadyToStartSessionThenStartQuizSession() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        GameSessionDto gameSessionDto;

        Quiz quiz = quizHelper.createFullQuiz();
        UUID lobbyId = quiz.getLobbyId();
        quiz.getPlayers().get(0).setReady();
        UUID player2Id = quiz.getPlayers().get(1).getUserId();

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_QUIZ_SESSION + lobbyId.toString(),
                new PublishQuizStompFrameHandler());

        // Act
        quizService.updatePlayerStatus(lobbyId, player2Id, PlayerStatus.READY);

        String result = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(result);

        gameSessionDto = objectMapper.readValue(result, GameSessionDto.class);
        assertEquals(RoundStatus.OPEN, gameSessionDto.roundStatus);
        assertEquals(1, gameSessionDto.currentRound);
    }

    @Test
    void whenFirstRoundStartedThenSendCountdown() throws Exception {
        // Arrange
        Quiz quiz = quizHelper.createFullQuiz();
        UUID lobbyId = quiz.getLobbyId();
        quiz.getPlayers().get(0).setReady();
        UUID player2Id = quiz.getPlayers().get(1).getUserId();

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_QUIZ_SESSION_ROUND_COUNTDOWN + lobbyId.toString() + "/round-countdown",
                new PublishQuizStompFrameHandler());

        // Act
        quizService.updatePlayerStatus(lobbyId, player2Id, PlayerStatus.READY);

        String result = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(result);
        assertEquals("20", result);
    }

    @Test
    void whenSendAnswerThenSendUpdate() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        GameSessionDto gameSessionDto;

        Quiz quiz = sessionHelper.createAndStartQuizSession();
        UUID lobbyId = quiz.getLobbyId();
        UUID playerId = quiz.getPlayers().get(0).getUserId();
        jwtToken = authHelper.generateToken(playerId.toString());

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_QUIZ_SESSION + lobbyId.toString(),
                new PublishQuizStompFrameHandler());

        // Act
        stompSession.send(SEND_QUIZ_SESSION_ANSWER + lobbyId.toString() + "/answer", "2".getBytes());

        String result = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(result);

        gameSessionDto = objectMapper.readValue(result, GameSessionDto.class);
        assertEquals(RoundStatus.OPEN, gameSessionDto.roundStatus);
        assertEquals(1, gameSessionDto.currentRound);
        assertEquals(playerId, gameSessionDto.playerList.get(0).playerId);
        assertEquals(PlayerRoundStatus.FINISH, gameSessionDto.playerList.get(0).playerRoundStatus);
        assertEquals(0, gameSessionDto.playerList.get(0).chosenAnswer);
        assertEquals(2,
                quizService.getSession(lobbyId).getCurrentRound().getPlayerAnswered().get(playerId).getAnswer());
    }

    @Test
    void whenSendAnswerThenSendReducedCountdown() throws Exception {
        // Arrange
        Quiz quiz = sessionHelper.createAndStartQuizSession();
        UUID lobbyId = quiz.getLobbyId();
        UUID playerId = quiz.getPlayers().get(0).getUserId();
        jwtToken = authHelper.generateToken(playerId.toString());

        StompSession stompSession = connectAndGetStompSession();

        stompSession.subscribe(SUBSCRIBE_QUIZ_SESSION_ROUND_COUNTDOWN + lobbyId.toString() + "/round-countdown",
                new PublishQuizStompFrameHandler());

        // Act
        stompSession.send(SEND_QUIZ_SESSION_ANSWER + lobbyId.toString() + "/answer", "1".getBytes());

        String result = completableFuture.get(5, TimeUnit.SECONDS);

        // Assert
        assertNotNull(result);
        assertTrue(Integer.parseInt(result) <= 6);
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
