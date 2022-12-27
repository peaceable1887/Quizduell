package gruppe_b.quizduell.statsserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import gruppe_b.quizduell.application.common.UserCredentialsDto;
import gruppe_b.quizduell.application.services.StatsService;
import gruppe_b.quizduell.application.services.UserRegisterService;
import gruppe_b.quizduell.application.services.UserService;
import gruppe_b.quizduell.domain.entities.PlayerStats;
import gruppe_b.quizduell.domain.entities.User;
import gruppe_b.quizduell.persistence.repository.PlayerStatsRepositoryAdapter;
import gruppe_b.quizduell.persistence.repository.UserRepositoryAdapter;
import gruppe_b.quizduell.statsserver.common.AuthHelper;
import gruppe_b.quizduell.statsserver.common.PlayerStatsDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
public class StatsControllerTests {

    @Autowired
    MockMvc mvc;

    @Autowired
    AuthHelper authHelper;

    @Autowired
    StatsService statsService;

    @Autowired
    UserRegisterService userRegisterService;

    @Autowired
    UserService userService;

    @Autowired
    PlayerStatsRepositoryAdapter playerStatsRepo;

    @Autowired
    UserRepositoryAdapter userRepo;

    static {
        System.setProperty("DB_PORT", "3306");
        System.setProperty("DB_HOSTNAME", "localhost");
        System.setProperty("DB_USERNAME", "root");
        System.setProperty("DB_PASSWORD", "root");
    }

    User u1, u2, u3;
    PlayerStats s1, s2, s3;

    @BeforeEach
    void setup() {
        playerStatsRepo.deleteAll();
        userRepo.deleteAll();

        userRegisterService.saveUser(new UserCredentialsDto("user1", "user1@mail.com", "password"));
        userRegisterService.saveUser(new UserCredentialsDto("user2", "user2@mail.com", "password"));
        userRegisterService.saveUser(new UserCredentialsDto("user3", "user3@mail.com", "password"));

        u1 = userService.getUserByName("user1");
        u2 = userService.getUserByName("user2");
        u3 = userService.getUserByName("user3");

        s1 = statsService.createStats(u1);
        s2 = statsService.createStats(u2);
        s3 = statsService.createStats(u3);
    }

    @Test
    void whenNotAuthenticated() throws Exception {
        // Arrange

        // Act

        // Assert
        this.mvc.perform(get("/v1/get"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void whenRequestPlayerStatsThenResponsePlayerStats() throws Exception {
        // Arrange
        String jwtToken = authHelper.generateToken(u1.getId().toString());

        // Act
        MvcResult result = this.mvc.perform(get("/v1/get")
                .header("Authorization", jwtToken))
                .andReturn();

        // Assert
        assertNotNull(result);
        String response = result.getResponse().getContentAsString();
        assertTrue(!response.isEmpty());

        ObjectMapper objectMapper = new ObjectMapper();
        PlayerStatsDto stats = objectMapper.readValue(response, PlayerStatsDto.class);

        assertEquals(u1.getId(), stats.playerId);
        assertEquals(s1.getId(), stats.id);
    }
}
