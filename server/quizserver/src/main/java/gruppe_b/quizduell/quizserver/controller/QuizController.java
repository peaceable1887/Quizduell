package gruppe_b.quizduell.quizserver.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.common.exceptions.JwtIsExpiredException;
import gruppe_b.quizduell.quizserver.common.ConnectRequest;
import gruppe_b.quizduell.quizserver.common.QuizRequest;
import gruppe_b.quizduell.quizserver.common.QuizSessionDto;
import gruppe_b.quizduell.quizserver.exceptions.JwtNotIssuedByLobbyServerException;
import gruppe_b.quizduell.quizserver.exceptions.PlayerAlreadyConnectedException;
import gruppe_b.quizduell.quizserver.exceptions.PlayerAlreadyInOtherGameException;
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

    /**
     * Mit einem Quiz verbinden.
     * Gibt es das Quiz noch nicht, wird es erstellt und der User mit diesem
     * Verbunden.
     * 
     * @param request
     * @return
     * @throws PlayerAlreadyConnectedException
     * @throws PlayerAlreadyInOtherGameException
     * @throws JwtNotIssuedByLobbyServerException
     * @throws JwtIsExpiredException
     */
    @PostMapping("/connect")
    public ResponseEntity<Quiz> connect(@AuthenticationPrincipal Jwt principal, @RequestBody ConnectRequest request)
            throws PlayerAlreadyConnectedException,
            PlayerAlreadyInOtherGameException,
            JwtNotIssuedByLobbyServerException,
            JwtIsExpiredException {
        Quiz quiz;

        try {
            quiz = quizService.connectToQuiz(
                    request.lobbyId,
                    UUID.fromString(principal.getSubject()),
                    principal.getClaimAsString("name"),
                    request.gameToken);
        } catch (PlayerAlreadyConnectedException | PlayerAlreadyInOtherGameException
                | JwtNotIssuedByLobbyServerException | JwtIsExpiredException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.status(HttpStatus.OK).body(quiz);
    }

    /**
     * Gib eine angefragtes Quiz anhand der LobbyId zurück.
     * 
     * @param request enthält die LobbyId
     * @return Quiz
     */
    @GetMapping("/get")
    public ResponseEntity<Quiz> get(@RequestBody QuizRequest request) {
        return ResponseEntity.ok().body(quizService.getQuiz(request.lobbyId));
    }

    /**
     * Gibt die Runden einer angefragten Quiz Session anhand der LobbyId zurück.
     * 
     * @param request enthält die LobbyId
     * @return Quiz Session
     */
    @GetMapping("get-session")
    public ResponseEntity<QuizSessionDto> getSession(@RequestBody QuizRequest request) {
        return ResponseEntity.ok().body(quizService.getSessionDtoList(request.lobbyId));
    }

    /**
     * Bricht ein Quiz ab.
     * 
     * @param request
     * @return
     */
    @PostMapping("/cancel")
    public ResponseEntity<Boolean> cancel(@RequestBody QuizRequest request) {
        return ResponseEntity.ok().body(quizService.cancelQuiz(request.lobbyId));
    }
}
