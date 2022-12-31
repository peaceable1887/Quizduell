package gruppe_b.quizduell.authserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import gruppe_b.quizduell.authserver.common.AuthHelper;
import gruppe_b.quizduell.infrastructure.services.ProfilePictureService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfilePictureControllerTest {

    static {
        System.setProperty("DB_PORT", "3306");
        System.setProperty("DB_HOSTNAME", "localhost");
        System.setProperty("DB_USERNAME", "root");
        System.setProperty("DB_PASSWORD", "root");
    }

    private final String PATH_TEST_PICTURE = "src/test/java/gruppe_b/quizduell/authserver/test.jpg";
    private final String PATH_TEST_SAVE_FOLDER = "src/test/java/gruppe_b/quizduell/authserver/test_data/";

    @Autowired
    MockMvc mvc;

    @Autowired
    ProfilePictureService profilePictureService;

    @Autowired
    AuthHelper authHelper;

    @BeforeEach
    void setup() {
        profilePictureService.setPicturePath(PATH_TEST_SAVE_FOLDER);
    }

    @Test
    void whenSendPictureThenSavePicture() throws Exception {
        // Arrange
        String jwtToken = authHelper.generateToken();

        Path pathTestPicture = Paths.get(PATH_TEST_PICTURE);
        byte[] picture = Files.readAllBytes(pathTestPicture);

        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", picture);

        // Act
        this.mvc.perform(MockMvcRequestBuilders.multipart("/images")
                .file(mockFile)
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        // Assert
        File file = new File(PATH_TEST_SAVE_FOLDER + authHelper.getDefaultUUID() + ".jpg");
        assertTrue(file.exists());
        file.delete();
        assertTrue(!file.exists());
    }
}
