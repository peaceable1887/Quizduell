package gruppe_b.quizduell.infrastructure.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service zum Speicher von Profilbildern.
 * 
 * @author Christopher Burmeister
 */
@Service
public class ProfilePictureService {

    private static final Logger logger = LoggerFactory.getLogger(ProfilePictureService.class);

    private String picturePath = "/www/static/";

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public boolean savePicture(UUID userId, byte[] picture) {
        Path path = Paths.get(getPicturePath() + userId.toString() + ".jpg");

        try {
            Files.write(path, picture);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }
}
