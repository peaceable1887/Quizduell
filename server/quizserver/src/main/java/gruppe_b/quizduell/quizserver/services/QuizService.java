package gruppe_b.quizduell.quizserver.services;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.common.models.Player;
import gruppe_b.quizduell.quizserver.models.Quiz;

@Service
public class QuizService {

    private final HashMap<UUID, Quiz> quizRepo;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public QuizService() {
        this.quizRepo = new HashMap<>();
    }

    public Quiz createQuiz(UUID lobbyId) {
        Quiz newQuiz = new Quiz(lobbyId);
        quizRepo.put(newQuiz.getId(), newQuiz);
        return newQuiz;
    }
}
