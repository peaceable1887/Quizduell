package gruppe_b.quizduell.lobbyserver.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Home Controller. Hier kann der JWT getestet werden.
 * 
 * @author Christopher Burmeister
 */
@RestController
@RequestMapping("/v1")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String home(Principal principal) {
        logger.info("Request from: {}", principal.getName());
        return "Hallo, " + principal.getName();
    }
}
