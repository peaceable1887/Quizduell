package gruppe_b.quizduell.authserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import gruppe_b.quizduell.application.common.UserDetailsDto;
import gruppe_b.quizduell.application.common.UserDetailsUpdateDto;
import gruppe_b.quizduell.authserver.common.AuthHelper;
import gruppe_b.quizduell.domain.entities.User;
import gruppe_b.quizduell.persistence.repository.UserRepositoryAdapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {

    static {
        System.setProperty("DB_PORT", "3306");
        System.setProperty("DB_HOSTNAME", "localhost");
        System.setProperty("DB_USERNAME", "root");
        System.setProperty("DB_PASSWORD", "root");
    }

    @Autowired
    MockMvc mvc;

    @Autowired
    AuthHelper authHelper;

    @Autowired
    UserRepositoryAdapter userRepo;

    String jwtToken;
    String userId;
    User user;

    @BeforeEach
    void setup() {
        userRepo.deleteAll();
        userId = authHelper.generateUser().toString();
        jwtToken = authHelper.generateToken(userId);

        user = userRepo.findByUUID(UUID.fromString(userId));
    }

    @Test
    @WithMockUser
    void whenGetUserDetailsThenReturnUserDetails() throws Exception {
        // Arrange

        // Act
        MvcResult result = this.mvc.perform(get("/v1/details")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertTrue(result.getResponse().getContentAsString().contains("authHelper"));
    }

    @Test
    @WithMockUser
    void whenUpdateUserThenUpdateUser() throws Exception {
        // Arrange

        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto();
        updateDto.name = "update";
        updateDto.mail = "update@update.de";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updateDto);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/update")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertNotNull(result);

        String response = result.getResponse().getContentAsString();
        UserDetailsDto updatedDetails = objectMapper.readValue(response, UserDetailsDto.class);
        assertEquals(updateDto.name, updatedDetails.name);
        assertEquals(updateDto.mail, updatedDetails.mail);
        assertEquals(userId, updatedDetails.userId);
    }

    @Test
    @WithMockUser
    void whenUpdateWithEmptyMailThenNoMailUpdate() throws Exception {
        // Arrange

        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto();
        updateDto.name = "update";
        updateDto.mail = "";
        updateDto.password = "update";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updateDto);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/update")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertNotNull(result);

        String response = result.getResponse().getContentAsString();
        UserDetailsDto updatedDetails = objectMapper.readValue(response, UserDetailsDto.class);
        assertEquals(updateDto.name, updatedDetails.name);
        assertEquals(authHelper.getDEFAULT_USER_MAIL(), updatedDetails.mail);
        assertEquals(userId, updatedDetails.userId);

        User userAfterUpdate = userRepo.findByUUID(UUID.fromString(userId));
        assertNotEquals(user.getPasswordHash(), userAfterUpdate.getPasswordHash());
    }

    @Test
    @WithMockUser
    void whenUpdateWithEmptyNameThenNoUserUpdate() throws Exception {
        // Arrange

        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto();
        updateDto.name = "";
        updateDto.mail = "update@update.de";
        updateDto.password = "update";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updateDto);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/update")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertNotNull(result);

        String response = result.getResponse().getContentAsString();
        UserDetailsDto updatedDetails = objectMapper.readValue(response, UserDetailsDto.class);
        assertEquals(authHelper.getDEFAULT_USER_NAME(), updatedDetails.name);
        assertEquals(updateDto.mail, updatedDetails.mail);
        assertEquals(userId, updatedDetails.userId);

        User userAfterUpdate = userRepo.findByUUID(UUID.fromString(userId));
        assertNotEquals(user.getPasswordHash(), userAfterUpdate.getPasswordHash());
    }

    @Test
    @WithMockUser
    void whenUpdateWithNameNullThenNoUserUpdate() throws Exception {
        // Arrange

        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto();
        updateDto.mail = "update@update.de";
        updateDto.password = "update";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updateDto);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/update")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertNotNull(result);

        String response = result.getResponse().getContentAsString();
        UserDetailsDto updatedDetails = objectMapper.readValue(response, UserDetailsDto.class);
        assertEquals(authHelper.getDEFAULT_USER_NAME(), updatedDetails.name);
        assertEquals(updateDto.mail, updatedDetails.mail);
        assertEquals(userId, updatedDetails.userId);

        User userAfterUpdate = userRepo.findByUUID(UUID.fromString(userId));
        assertNotEquals(user.getPasswordHash(), userAfterUpdate.getPasswordHash());
    }

    @Test
    @WithMockUser
    void whenUpdateWithMailNullThenNoMailUpdate() throws Exception {
        // Arrange

        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto();
        updateDto.name = "update";
        updateDto.password = "update";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updateDto);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/update")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertNotNull(result);

        String response = result.getResponse().getContentAsString();
        UserDetailsDto updatedDetails = objectMapper.readValue(response, UserDetailsDto.class);
        assertEquals(updateDto.name, updatedDetails.name);
        assertEquals(authHelper.getDEFAULT_USER_MAIL(), updatedDetails.mail);
        assertEquals(userId, updatedDetails.userId);

        User userAfterUpdate = userRepo.findByUUID(UUID.fromString(userId));
        assertNotEquals(user.getPasswordHash(), userAfterUpdate.getPasswordHash());
    }

    @Test
    @WithMockUser
    void whenUpdateWithEmptyPasswordThenNoUserUpdate() throws Exception {
        // Arrange
        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto();
        updateDto.name = "update";
        updateDto.mail = "update@update.de";
        updateDto.password = "";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updateDto);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/update")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertNotNull(result);

        String response = result.getResponse().getContentAsString();
        UserDetailsDto updatedDetails = objectMapper.readValue(response, UserDetailsDto.class);
        assertEquals(updateDto.name, updatedDetails.name);
        assertEquals(updateDto.mail, updatedDetails.mail);
        assertEquals(userId, updatedDetails.userId);

        User userAfterUpdate = userRepo.findByUUID(UUID.fromString(userId));
        assertEquals(user.getPasswordHash(), userAfterUpdate.getPasswordHash());
    }

    @Test
    @WithMockUser
    void whenUpdateWithPasswordNullThenNoMailUpdate() throws Exception {
        // Arrange
        UserDetailsUpdateDto updateDto = new UserDetailsUpdateDto();
        updateDto.name = "update";
        updateDto.mail = "update@update.de";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updateDto);

        // Act
        MvcResult result = this.mvc.perform(post("/v1/update")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk()).andReturn();

        // Assert
        assertNotNull(result);

        String response = result.getResponse().getContentAsString();
        UserDetailsDto updatedDetails = objectMapper.readValue(response, UserDetailsDto.class);
        assertEquals(updateDto.name, updatedDetails.name);
        assertEquals(updateDto.mail, updatedDetails.mail);
        assertEquals(userId, updatedDetails.userId);

        User userAfterUpdate = userRepo.findByUUID(UUID.fromString(userId));
        assertEquals(user.getPasswordHash(), userAfterUpdate.getPasswordHash());
    }
}
