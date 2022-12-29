package gruppe_b.quizduell.quizserver.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import gruppe_b.quizduell.application.enums.QuizStatus;
import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.quizserver.common.AuthHelper;
import gruppe_b.quizduell.quizserver.common.ConnectRequest;
import gruppe_b.quizduell.quizserver.common.QuizHelper;
import gruppe_b.quizduell.quizserver.common.QuizRequest;
import gruppe_b.quizduell.quizserver.common.QuizSessionDto;
import gruppe_b.quizduell.quizserver.common.QuizSessionHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
public class QuizControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    QuizHelper quizHelper;

    @Autowired
    AuthHelper authHelper;

    @Autowired
    QuizSessionHelper quizSessionHelper;

    String jwtToken;

    static {
        System.setProperty("DB_PORT", "3306");
        System.setProperty("DB_HOSTNAME", "localhost");
        System.setProperty("DB_USERNAME", "root");
        System.setProperty("DB_PASSWORD", "root");
    }

    @BeforeEach
    void setup() {
        jwtToken = authHelper.generateToken();
    }

    @Test
    @WithMockUser
    void whenFirstPlayerConnectThenCreateGameAndReturn() throws Exception {
        // Arrange
        Player player = quizHelper.createPlayer();
        Lobby lobby = quizHelper.createLobby(player.getUserId());
        String token = quizHelper.createToken(lobby);

        jwtToken = authHelper.generateToken(player.getUserId().toString());

        ConnectRequest connectRequest = new ConnectRequest();
        connectRequest.playerId = player.getUserId();
        connectRequest.lobbyId = lobby.getId();
        connectRequest.gameToken = token;

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(connectRequest);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/connect")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(player.getUserId().toString()));
        assertTrue(content.contains(lobby.getId().toString()));
    }

    @Test
    @WithMockUser
    void whenSecondPlayerConnectedThenConnectToGameAndReturn() throws Exception {
        // Arrange
        Player player = quizHelper.createPlayer();
        Player player2 = quizHelper.createPlayer();
        Lobby lobby = quizHelper.createLobby(player.getUserId());
        String token = quizHelper.createToken(lobby);
        quizHelper.createQuiz(player, lobby, token);

        ConnectRequest connectRequest = new ConnectRequest();
        connectRequest.playerId = player2.getUserId();
        connectRequest.lobbyId = lobby.getId();
        connectRequest.gameToken = token;

        jwtToken = authHelper.generateToken(player2.getUserId().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(connectRequest);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/connect")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(player.getUserId().toString()));
        assertTrue(content.contains(player2.getUserId().toString()));
        assertTrue(content.contains(lobby.getId().toString()));
    }

    @Test
    @WithMockUser
    void whenPlayerConnectToSecondQuizThenBadRequest() throws Exception {
        // Arrange
        Player player = quizHelper.createPlayer();
        Lobby lobby = quizHelper.createLobby(player.getUserId());
        String token = quizHelper.createToken(lobby);

        jwtToken = authHelper.generateToken(player.getUserId().toString());

        Lobby secondLobby = quizHelper.createLobby(player.getUserId());
        String secondToken = quizHelper.createToken(secondLobby);

        ConnectRequest connectRequest = new ConnectRequest();
        connectRequest.playerId = player.getUserId();
        connectRequest.lobbyId = lobby.getId();
        connectRequest.gameToken = token;

        ConnectRequest connectRequest2 = new ConnectRequest();
        connectRequest2.playerId = player.getUserId();
        connectRequest2.lobbyId = secondLobby.getId();
        connectRequest2.gameToken = secondToken;

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(connectRequest);
        String secondJson = objectMapper.writeValueAsString(connectRequest2);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/connect")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        MvcResult secondResult = this.mvc.perform(post("/v1/connect")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondJson))
                .andExpect(status().isBadRequest()).andReturn();

        // Assert
        assertNotNull(secondResult);
    }

    @Test
    @WithMockUser
    void whenCancelQuizThenCancelQuiz() throws Exception {
        // Arrange
        Player player = quizHelper.createPlayer();
        Player player2 = quizHelper.createPlayer();
        Lobby lobby = quizHelper.createLobby(player.getUserId());
        String token = quizHelper.createToken(lobby);
        quizHelper.createQuiz(player, lobby, token);

        ConnectRequest connectRequest = new ConnectRequest();
        connectRequest.playerId = player2.getUserId();
        connectRequest.lobbyId = lobby.getId();
        connectRequest.gameToken = token;

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(connectRequest);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/cancel")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertEquals("true", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    void whenQuizCancelAndPlayerConnectToSecondQuizThenOk() throws Exception {
        // Arrange
        Player player = quizHelper.createPlayer();
        Lobby lobby = quizHelper.createLobby(player.getUserId());
        String token = quizHelper.createToken(lobby);

        Lobby secondLobby = quizHelper.createLobby(player.getUserId());
        String second = quizHelper.createToken(secondLobby);

        ConnectRequest connectRequest = new ConnectRequest();
        connectRequest.playerId = player.getUserId();
        connectRequest.lobbyId = lobby.getId();
        connectRequest.gameToken = token;

        ConnectRequest connectRequest2 = new ConnectRequest();
        connectRequest2.playerId = player.getUserId();
        connectRequest2.lobbyId = secondLobby.getId();
        connectRequest2.gameToken = second;

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(connectRequest);
        String secondJson = objectMapper.writeValueAsString(connectRequest2);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/connect")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        MvcResult cancelResult = this.mvc.perform(post("/v1/cancel")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        MvcResult secondResult = this.mvc.perform(post("/v1/connect")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondJson))
                .andExpect(status().isOk()).andReturn();

        // Assert
    }

    @Test
    @WithMockUser
    void whenGetThenReturnQuiz() throws Exception {
        // Arrange
        Quiz quiz = quizHelper.createQuiz();
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.lobbyId = quiz.getLobbyId();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(quizRequest);

        // Act
        MvcResult result = this.mvc.perform(get("/v1/get")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(quiz.getId().toString()));
    }

    @Test
    @WithMockUser
    void whenGetSessionThenReturnQuizSession() throws Exception {
        // Arrange
        Quiz quiz = quizSessionHelper.createAndStartQuizSession();
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.lobbyId = quiz.getLobbyId();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(quizRequest);

        Thread.sleep(1_000);

        // Act
        MvcResult result = this.mvc.perform(get("/v1/get-session")
                .header("Authorization", jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertNotNull(result);
        String content = result.getResponse().getContentAsString();

        QuizSessionDto quizSession = objectMapper.readValue(content, QuizSessionDto.class);
        assertEquals(quizSession.getLobbyId(), quiz.getLobbyId());
        assertNotNull(quizSession.getQuizId());
        assertNotNull(quizSession.getPlayerList());
        assertNotNull(quizSession.getRoundList());
        assertNotNull(quizSession.getQuizStatus());
        assertEquals(QuizStatus.STARTED, quizSession.getQuizStatus());
        assertEquals(2, quizSession.getPlayerList().size());
        assertEquals(1, quizSession.getRoundList().size());
    }
}
