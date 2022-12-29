package gruppe_b.quizduell.lobbyserver.services;

import java.util.TimerTask;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import gruppe_b.quizduell.lobbyserver.common.LobbyStartDto;
import gruppe_b.quizduell.lobbyserver.modelss.Lobby;

public class LobbyStartCountDown extends TimerTask {

    private final Lobby lobby;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LobbyService lobbyService;
    private int counter = 5;

    public LobbyStartCountDown(Lobby lobby,
            SimpMessagingTemplate simpMessagingTemplate,
            LobbyService lobbyService) {
        this.lobby = lobby;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.lobbyService = lobbyService;
    }

    @Override
    public void run() {
        // alle Spieler immer noch ready?
        if (!lobby.allPlayersReady()) {
            // nicht alle Spieler ready. Cancel countdown.
            this.cancel();
            sendMessage(createDto("abort", counter));
            return;
        }

        if (counter == 0) {
            this.cancel();
            String token = lobbyService.startGame(lobby);
            sendMessage(createDto("start", counter, token));
        } else {
            sendMessage(createDto("start", counter));
        }

        counter--;
    }

    private void sendMessage(LobbyStartDto dto) {
        simpMessagingTemplate.convertAndSend(
                "/topic/lobby/" + lobby.getId().toString() + "/start-lobby", dto);
    }

    private static LobbyStartDto createDto(String status, int countdown) {
        LobbyStartDto dto = new LobbyStartDto();
        dto.status = status;
        dto.countdown = countdown;
        return dto;
    }

    private static LobbyStartDto createDto(String status, int countdown, String token) {
        LobbyStartDto dto = createDto(status, countdown);
        dto.gameToken = token;
        return dto;
    }
}
