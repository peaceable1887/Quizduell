package gruppe_b.quizduell.quizserver.services;

import java.util.TimerTask;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import gruppe_b.quizduell.application.interfaces.StartQuiz;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.quizserver.common.QuizStartDto;

public class QuizStartCountDown extends TimerTask {

    private final Quiz quiz;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private int counter = 5;
    private StartQuiz startQuizCallBack;

    public QuizStartCountDown(Quiz quiz,
            SimpMessagingTemplate simpMessagingTemplate,
            StartQuiz startQuiz) {
        this.quiz = quiz;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.startQuizCallBack = startQuiz;
    }

    @Override
    public void run() {
        // alle Spieler immer noch ready?
        if (!quiz.allPlayersReady()) {
            // nicht alle Spieler ready. Cancel countdown.
            this.cancel();
            sendMessage(createDto("abort", counter));
            return;
        }

        if (counter == 0) {
            this.cancel();
            quiz.setQuizStarted();
            sendMessage(createDto("start", counter));
            startQuizCallBack.startQuiz(quiz);
        } else {
            sendMessage(createDto("start", counter));
        }

        counter--;
    }

    private void sendMessage(QuizStartDto dto) {
        simpMessagingTemplate.convertAndSend(
                "/topic/quiz/" + quiz.getLobbyId().toString() + "/start-quiz", dto);
    }

    private static QuizStartDto createDto(String status, int countdown) {
        QuizStartDto dto = new QuizStartDto();
        dto.status = status;
        dto.countdown = countdown;
        return dto;
    }
}
