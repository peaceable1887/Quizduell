package gruppe_b.quizduell.quizserver.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;

import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.quizserver.common.ConnectRequest;
import gruppe_b.quizduell.quizserver.common.QuizHelper;
import gruppe_b.quizduell.quizserver.common.QuizRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
public class QuizControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    QuizHelper quizHelper;

    static {
        System.setProperty("DB_PORT", "3306");
        System.setProperty("DB_HOSTNAME", "localhost");
        System.setProperty("DB_USERNAME", "root");
        System.setProperty("DB_PASSWORD", "root");
    }

    @Test
    @WithMockUser
    void whenFirstPlayerConnectThenCreateGameAndReturn() throws Exception {
        // Arrange
        Player player = quizHelper.createPlayer();
        Lobby lobby = quizHelper.createLobby(player.getUserId());
        String token = quizHelper.createToken(lobby);

        ConnectRequest connectRequest = new ConnectRequest();
        connectRequest.playerId = player.getUserId();
        connectRequest.lobbyId = lobby.getId();
        connectRequest.gameToken = token;

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(connectRequest);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/connect")
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

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(connectRequest);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/connect")
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
    void whenGetThenReturnQuiz() throws Exception {
        // Arrange
        Quiz quiz = quizHelper.createQuiz();
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.lobbyId = quiz.getLobbyId();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(quizRequest);

        // Act
        MvcResult result = this.mvc.perform(get("/v1/get")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(quiz.getId().toString()));
    }
}
