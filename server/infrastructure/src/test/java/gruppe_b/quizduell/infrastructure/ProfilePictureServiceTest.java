package gruppe_b.quizduell.infrastructure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.infrastructure.services.ProfilePictureService;

@SpringBootTest
public class ProfilePictureServiceTest {

    private final String PATH_TEST_PICTURE = "src/test/java/gruppe_b/quizduell/infrastructure/test.jpg";
    private final String PATH_TEST_SAVE_FOLDER = "src/test/java/gruppe_b/quizduell/infrastructure/test_data/";

    @Autowired
    ProfilePictureService profilePictureService;

    @BeforeEach
    void setup() {
        profilePictureService.setPicturePath(PATH_TEST_SAVE_FOLDER);
    }

    @Test
    void getPicture() throws Exception {

        Path path = Paths.get(PATH_TEST_PICTURE);
        Files.readAllBytes(path);
    }

    @Test
    void savePicture() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        Path pathTestPicture = Paths.get(PATH_TEST_PICTURE);
        byte[] picture = Files.readAllBytes(pathTestPicture);

        // Act
        profilePictureService.savePicture(userId, picture);

        // Assert
        File file = new File(PATH_TEST_SAVE_FOLDER + userId.toString() + ".jpg");
        assertTrue(file.exists());
        file.delete();
        assertTrue(!file.exists());
    }
}
