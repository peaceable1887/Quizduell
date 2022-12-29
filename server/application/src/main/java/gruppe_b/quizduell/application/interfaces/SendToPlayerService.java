package gruppe_b.quizduell.application.interfaces;

import java.util.UUID;

import gruppe_b.quizduell.application.common.GameSessionDto;
import gruppe_b.quizduell.application.common.GameSessionResult;

public interface SendToPlayerService {

    void sendRoundCountdown(UUID lobbyId, int countdown);

    void sendGameSessionUpdate(UUID lobbyId, GameSessionDto gameSessionDto);

    void sendQuizAbort(UUID lobbyId, GameSessionDto gameSessionDto);

    void sendQuizResult(UUID lobbyId, GameSessionResult gameSessionResult);
}
