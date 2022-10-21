package gruppe_b.quizduell.lobbyserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.nimbusds.jose.shaded.json.JSONObject;

import gruppe_b.quizduell.lobbyserver.common.AuthHelper;
import gruppe_b.quizduell.lobbyserver.common.LobbyHelper;
import net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@SpringBootTest()
@ComponentScan(basePackageClasses = AuthHelper.class)
@AutoConfigureMockMvc
class LobbyControllerTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    AuthHelper authHelper;

    @Autowired
    LobbyHelper lobbyHelper;

    @Test
    @WithMockUser
    void createTest() throws Exception {
        // Arrange
        JSONObject jcreateRequest = new JSONObject();
        jcreateRequest.put("name", "testLobby");
        String jwtToken = authHelper.generateToken();

        // Act

        // Assert
        this.mvc.perform(post("/lobby/create")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jcreateRequest.toJSONString()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void connectTest() throws Exception {
        // Arrange
        UUID gameId = lobbyHelper.createLobby();

        JSONObject jconnectRequest = new JSONObject();
        jconnectRequest.put("lobbyId", gameId.toString());
        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(post("/lobby/connect")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jconnectRequest.toJSONString()))
                .andReturn();

        // Assert
        assertEquals(200, result.getResponse().getStatus());
    }
}
