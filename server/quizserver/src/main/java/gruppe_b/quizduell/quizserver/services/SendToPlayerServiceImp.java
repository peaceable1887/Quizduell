package gruppe_b.quizduell.quizserver.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.common.GameSessionDto;
import gruppe_b.quizduell.application.interfaces.SendToPlayerService;

@Service
public class SendToPlayerServiceImp implements SendToPlayerService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendRoundCountdown(UUID lobbyId, int countdown) {
        simpMessagingTemplate.convertAndSend("/topic/quiz/session/" + lobbyId.toString() + "/round-countdown",
                countdown);
    }

    @Override
    public void sendGameSessionUpdate(UUID lobbyId, GameSessionDto gameSessionDto) {
        simpMessagingTemplate.convertAndSend("/topic/quiz/session/" + lobbyId.toString(), gameSessionDto);
    }

    @Override
    public void sendQuizAbort(UUID lobbyId, GameSessionDto gameSessionDto) {
        simpMessagingTemplate.convertAndSend("/topic/quiz/session/" + lobbyId.toString(), gameSessionDto);
    }
}
