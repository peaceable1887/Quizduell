package gruppe_b.quizduell.lobbyserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;

import gruppe_b.quizduell.lobbyserver.common.AuthHelper;
import gruppe_b.quizduell.lobbyserver.common.ConnectRequest;
import gruppe_b.quizduell.lobbyserver.common.CreateRequest;
import gruppe_b.quizduell.lobbyserver.common.LobbyBuilder;
import gruppe_b.quizduell.lobbyserver.common.LobbyHelper;
import gruppe_b.quizduell.lobbyserver.models.Lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest()
@ComponentScan(basePackageClasses = AuthHelper.class)
@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
@AutoConfigureMockMvc
class LobbyControllerTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    AuthHelper authHelper;

    @Autowired
    LobbyHelper lobbyHelper;

    @Test
    void whenNotAuthenticated() throws Exception {
        // Arrange

        // Act

        // Assert
        this.mvc.perform(get("/v1/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void whenCreateThenCreateNewLobby() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject jcreateRequest = new JSONObject();
        jcreateRequest.put("name", "testLobby");
        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(post("/v1/create")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jcreateRequest.toJSONString()))
                .andExpect(status().isCreated()).andReturn();

        // Assert
        String response = result.getResponse().getContentAsString();
        Lobby responseLobby = objectMapper.readValue(response, Lobby.class);

        assertEquals("", responseLobby.getPassword());
        assertEquals(1, responseLobby.playerCount());
        // assertEquals(authHelper.getDEFAULT_USER_NAME(),
        // responseLobby.getPlayers().get(0).getName());
    }

    @Test
    @WithMockUser
    void whenCreateLobbyWithPasswordThenLobbyHasPassword() throws Exception {
        // Arrange
        CreateRequest request = new CreateRequest();
        request.name = "test";
        request.password = "password";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(post("/v1/create")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated()).andReturn();

        // Assert
        String response = result.getResponse().getContentAsString();
        Lobby responseLobby = objectMapper.readValue(response, Lobby.class);

        assertEquals("", responseLobby.getPassword());
        assertEquals(1, responseLobby.playerCount());

        Lobby lobby = lobbyHelper.getLobby(responseLobby.getId());
        assertEquals("password", lobby.getPassword());
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
        MvcResult result = this.mvc.perform(post("/v1/connect")
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
    void whenConnectToLobbyWithPasswordWithoutPasswordThenBadRequest() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby("password");

        ObjectMapper objectMapper = new ObjectMapper();
        ConnectRequest request = new ConnectRequest();
        request.lobbyId = lobbyId;
        request.password = "pwd";
        String json = objectMapper.writeValueAsString(request);

        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(post("/v1/connect")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        assertEquals("Password für die Lobby falsch: pwd", result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    void whenConnectToLobbyWithPasswordWithPasswordThenAddPlayerToLobby() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby("password");

        ObjectMapper objectMapper = new ObjectMapper();
        ConnectRequest request = new ConnectRequest();
        request.lobbyId = lobbyId;
        request.password = "password";
        String json = objectMapper.writeValueAsString(request);

        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(post("/v1/connect")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn();

        // Assert
        String response = result.getResponse().getContentAsString();
        Lobby lobby = objectMapper.readValue(response, Lobby.class);
        assertEquals(lobbyId, lobby.getId());
        assertEquals(2, lobby.playerCount());
        assertEquals("00000000-0000-0000-0000-000000000000", lobby.getPlayers().get(1).getUserId().toString());
    }

    @Test
    @WithMockUser
    void whenDisconnectThenRemovePlayerFromLobby() throws Exception {
        // Arrange
        LobbyBuilder builder = lobbyHelper.getNewLobbyBuilder();
        UUID playerId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Lobby lobby = builder.buildLobby()
                .addPlayer(playerId, "john")
                .getLobby();

        assertEquals(2, lobby.getPlayers().size());

        JSONObject jdisconnectRequest = new JSONObject();
        jdisconnectRequest.put("lobbyId", lobby.getId().toString());

        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(post("/v1/disconnect")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jdisconnectRequest.toJSONString()))
                .andReturn();

        // Assert
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(1, lobby.getPlayers().size());
    }

    @Test
    @WithMockUser
    void whenLastPlayerDisconnectThenRemoveLobby() throws Exception {
        // Arrange
        LobbyBuilder builder = lobbyHelper.getNewLobbyBuilder();
        UUID playerId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Lobby lobby = builder.buildLobby(playerId)
                .getLobby();

        assertEquals(1, lobby.getPlayers().size());
        assertEquals(lobby, lobbyHelper.getLobby(lobby.getId()));

        JSONObject jdisconnectRequest = new JSONObject();
        jdisconnectRequest.put("lobbyId", lobby.getId().toString());

        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(post("/v1/disconnect")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jdisconnectRequest.toJSONString()))
                .andReturn();

        // Assert
        assertEquals(200, result.getResponse().getStatus());
        assertNull(lobbyHelper.getLobby(lobby.getId()));
    }

    @Test
    @WithMockUser
    void whenGetThenReturnLobby() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createLobby();

        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(get("/v1/get")
                .header("Authorization", "Bearer " + jwtToken)
                .param("lobbyId", lobbyId.toString()))
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
        MvcResult result = this.mvc.perform(get("/v1/all")
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

    @Test
    @WithMockUser
    void whenConnectAndMaxPlayerCountReachedThenThrowException() throws Exception {
        // Arrange
        UUID lobbyId = lobbyHelper.createFullLobby();

        JSONObject jconnectRequest = new JSONObject();
        jconnectRequest.put("lobbyId", lobbyId.toString());
        String jwtToken = authHelper.generateToken();

        // Act
        MvcResult result = this.mvc.perform(post("/v1/connect")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jconnectRequest.toJSONString()))
                .andReturn();

        // Assert
        assertEquals(400, result.getResponse().getStatus());
        assertEquals("Lobby full. Max player count: 2",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser
    void multithreadTest() throws Exception {
        // Arrange
        int threadCount = 10;
        int threadLoopCount = 1_000;
        int preTestLobbyCount = lobbyHelper.getLobbyService().getAllLobbies().size();

        ExecutorService executor = Executors.newFixedThreadPool(100);
        Collection<Callable<Void>> tasks = new ArrayList<>();

        JSONObject jcreateRequest = new JSONObject();
        jcreateRequest.put("name", "testLobby");
        String jwtToken = authHelper.generateToken();

        // Act
        for (int i = 0; i < threadCount; i++) {
            Callable<Void> task = () -> {
                for (int j = 0; j < threadLoopCount; j++) {
                    mvc.perform(post("/v1/create")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jcreateRequest.toJSONString()))
                            .andExpect(status().isCreated());
                }
                return null;
            };

            tasks.add(task);
        }

        executor.invokeAll(tasks);

        // Assert
        assertEquals(threadCount * threadLoopCount + preTestLobbyCount,
                lobbyHelper.getLobbyService().getAllLobbies().size());
    }
}
