package gruppe_b.quizduell.quizserver.services;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import gruppe_b.quizduell.application.interfaces.StartQuiz;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.quizserver.common.QuizStartDto;

/**
 * Service für den Countdown, wenn die Spieler mit dem Quiz verbunden sind und
 * das Quiz starten soll.
 * 
 * @author Christopher Burmeister
 */
public class QuizStartCountDown extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(QuizStartCountDown.class);

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

    /**
     * Loop für den Countdown
     */
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
            // Countdown fertig. Letzten Countdown senden und Spiel starten.
            this.cancel();
            quiz.setQuizStarted();
            sendMessage(createDto("start", counter));
            startQuizCallBack.startQuiz(quiz);
        } else {
            // Countdown senden.
            sendMessage(createDto("start", counter));
        }

        counter--;
    }

    /**
     * Aktuellen Countdown über Websocket an die Spieler senden.
     * 
     * @param dto
     */
    private void sendMessage(QuizStartDto dto) {
        logger.info("start-quiz countdown: {}", dto.countdown);
        simpMessagingTemplate.convertAndSend(
                "/topic/quiz/" + quiz.getLobbyId().toString() + "/start-quiz", dto);
    }

    /**
     * Erstellt das Dto zum Senden des Countdown's an die Spieler.
     * 
     * @param status
     * @param countdown
     * @return
     */
    private static QuizStartDto createDto(String status, int countdown) {
        QuizStartDto dto = new QuizStartDto();
        dto.status = status;
        dto.countdown = countdown;
        return dto;
    }
}
