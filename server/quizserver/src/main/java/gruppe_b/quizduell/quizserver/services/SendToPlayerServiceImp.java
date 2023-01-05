package gruppe_b.quizduell.quizserver.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.common.GameSessionDto;
import gruppe_b.quizduell.application.common.GameSessionResult;
import gruppe_b.quizduell.application.interfaces.SendToPlayerService;

@Service
public class SendToPlayerServiceImp implements SendToPlayerService {

    private static final Logger logger = LoggerFactory.getLogger(SendToPlayerServiceImp.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendRoundCountdown(UUID lobbyId, int countdown) {
        String topic = "/topic/quiz/session/" + lobbyId.toString() + "/round-countdown";
        logger.info("send message to topic {} content: {}", topic, countdown);

        simpMessagingTemplate.convertAndSend(topic, countdown);
    }

    @Override
    public void sendGameSessionUpdate(UUID lobbyId, GameSessionDto gameSessionDto) {
        String topic = "/topic/quiz/session/" + lobbyId.toString();
        logger.info("send message to topic {} content: {}", topic, gameSessionDto);

        simpMessagingTemplate.convertAndSend(topic, gameSessionDto);
    }

    @Override
    public void sendQuizAbort(UUID lobbyId, GameSessionDto gameSessionDto) {
        String topic = "/topic/quiz/session/" + lobbyId.toString();
        logger.info("send message to topic {} content: {}", topic, gameSessionDto);

        simpMessagingTemplate.convertAndSend(topic, gameSessionDto);
    }

    @Override
    public void sendQuizResult(UUID lobbyId, GameSessionResult gameSessionResult) {
        String topic = "/topic/quiz/session/" + lobbyId.toString() + "/result";
        logger.info("send message to topic {} content: {}", topic, gameSessionResult);

        simpMessagingTemplate.convertAndSend(topic, gameSessionResult);
    }
}
