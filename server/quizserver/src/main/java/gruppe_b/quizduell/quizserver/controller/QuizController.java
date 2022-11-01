package gruppe_b.quizduell.quizserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gruppe_b.quizduell.quizserver.common.CreateRequest;
import gruppe_b.quizduell.quizserver.models.Quiz;
import gruppe_b.quizduell.quizserver.services.QuizService;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("answer")
    public ResponseEntity<Void> answer() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("create")
    public ResponseEntity<Quiz> create(@RequestBody CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.createQuiz(request.lobbyId));
    }
}
