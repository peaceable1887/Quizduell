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

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;

import gruppe_b.quizduell.quizserver.common.QuizHelper;
import gruppe_b.quizduell.quizserver.models.Quiz;

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

    @Test
    @WithMockUser
    void whenCreateThenCreateNewQuiz() throws Exception {
        // Arrange
        JSONObject jcreateRequest = new JSONObject();
        jcreateRequest.put("lobbyId", UUID.randomUUID().toString());

        String player1Id = UUID.randomUUID().toString();
        String player2Id = UUID.randomUUID().toString();

        JSONArray jPlayerArray = new JSONArray();
        jPlayerArray.add(player1Id);
        jPlayerArray.add(player2Id);

        jcreateRequest.put("playerIdList", jPlayerArray);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jcreateRequest.toJSONString()))
                .andExpect(status().isCreated()).andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(player1Id));
        assertTrue(content.contains(player2Id));
    }

    @Test
    @WithMockUser
    void whenGetThenReturnQuiz() throws Exception {
        // Arrange
        Quiz quiz = quizHelper.createQuiz();

        JSONObject jquizRequest = new JSONObject();
        jquizRequest.put("quizId", quiz.getId().toString());

        // Act
        MvcResult result = this.mvc.perform(get("/v1/get")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jquizRequest.toJSONString()))
                .andExpect(status().isOk()).andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(quiz.getId().toString()));
    }
}
