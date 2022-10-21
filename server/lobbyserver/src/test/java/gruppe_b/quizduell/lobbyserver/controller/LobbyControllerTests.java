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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;

import gruppe_b.quizduell.lobbyserver.Models.Lobby;
import gruppe_b.quizduell.lobbyserver.common.AuthHelper;
import gruppe_b.quizduell.lobbyserver.common.LobbyHelper;
import net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void whenCreateThenCreateNewLobby() throws Exception {
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
    void whenConnectThenAddPlayerToLobby() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();

        JSONObject jconnectRequest = new JSONObject();
        jconnectRequest.put("lobbyId", lobbyId.toString());
        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(post("/lobby/connect")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jconnectRequest.toJSONString()))
                .andReturn();

        // Assert
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("00000000-0000-0000-0000-000000000000",
                lobbyHelper.getLobby(lobbyId).getPlayers().get(1).getUserId().toString());
    }

    @Test
    @WithMockUser
    void whenGetThenReturnLobby() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();

        JSONObject jgetRequest = new JSONObject();
        jgetRequest.put("lobbyId", lobbyId.toString());
        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(get("/lobby/get")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jgetRequest.toJSONString()))
                .andReturn();

        String jsonStringLobby = result.getResponse().getContentAsString();

        // Assert
        assertEquals(200, result.getResponse().getStatus());
        assertTrue(jsonStringLobby.contains(lobbyId.toString()));
    }

    @Test
    @WithMockUser
    void whenAllThenReturnAllLobbies() throws Exception {
        // Arrange
        UUID lobbyId_1 = lobbyHelper.createLobby();
        UUID lobbyId_2 = lobbyHelper.createLobby();
        UUID lobbyId_3 = lobbyHelper.createLobby();

        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(get("/lobby/all")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonStringLobby = result.getResponse().getContentAsString();

        // Assert
        assertEquals(200, result.getResponse().getStatus());
        assertTrue(jsonStringLobby.contains(lobbyId_1.toString()));
        assertTrue(jsonStringLobby.contains(lobbyId_2.toString()));
        assertTrue(jsonStringLobby.contains(lobbyId_3.toString()));
    }
}
