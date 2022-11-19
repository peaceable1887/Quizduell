package gruppe_b.quizduell.quizserver.controller;

import java.security.Principal;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gruppe_b.quizduell.quizserver.common.CreateRequest;
import gruppe_b.quizduell.quizserver.common.QuizRequest;
import gruppe_b.quizduell.quizserver.models.Quiz;
import gruppe_b.quizduell.quizserver.services.QuizService;

/**
 * Rest-Controller für das Quiz
 * 
 * @author Christopher Burmeister
 */
@RestController
@RequestMapping("/v1")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // TODO entfernen und im Websocket übernehmen?
    @PostMapping("/answer")
    public ResponseEntity<Void> answer() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Erstellt eine Quiz-Session.
     * 
     * @param request enthält die LobbyId und die PlayerId's.
     * @return Quiz
     */
    @PostMapping("/create")
    public ResponseEntity<Quiz> create(@RequestBody CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                quizService.createQuiz(
                        request.lobbyId,
                        request.playerIdList));
    }

    /**
     * Gib eine angefragte Quiz-Session zurück.
     * 
     * @param request enthält die QuizId
     * @return Quiz
     */
    @GetMapping("/get")
    public ResponseEntity<Quiz> get(@RequestBody QuizRequest request) {
        return ResponseEntity.ok().body(quizService.getQuiz(request.quizId));
    }
}
