package gruppe_b.quizduell.quizserver.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Home Controller. Hier kann der JWT getestet werden.
 * 
 * @author Christopher Burmeister
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public String home(Principal principal) {
        return "Hallo, " + principal.getName();
    }
}
