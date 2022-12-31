package gruppe_b.quizduell.authserver.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gruppe_b.quizduell.infrastructure.services.ProfilePictureService;

/**
 * Rest-Controller zum Speichern von Profilbildern.
 * 
 * @author Christopher Burmeister
 */
@RestController
@RequestMapping("/v1")
public class PictureController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    @Autowired
    ProfilePictureService profilePictureService;

    @PostMapping("/image")
    public ResponseEntity<Void> uploadImage(Principal principal, @RequestParam("file") MultipartFile file) {
        String[] fileNameParts = file.getOriginalFilename().split("\\.");

        if (fileNameParts.length < 2) {
            String errorMsg = "Fehler im Dateinamen. Dateiendung konnte nicht ermittelt werden: "
                    + file.getOriginalFilename();
            logger.error(errorMsg);
            return new ResponseEntity(errorMsg, HttpStatus.BAD_REQUEST);
        }

        String fileExtension = fileNameParts[fileNameParts.length - 1];

        if (!fileExtension.equals("jpg")) {
            String errorMsg = "Falsche Dateiendung: " + fileExtension;
            logger.error(errorMsg);
            return new ResponseEntity(errorMsg, HttpStatus.BAD_REQUEST);
        }

        byte[] fileBytes;

        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        profilePictureService.savePicture(UUID.fromString(principal.getName()), fileBytes);
        return ResponseEntity.ok().build();
    }
}