package gruppe_b.quizduell.lobbyserver.common;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.lobbyserver.services.LobbyService;

@Service
public class LobbyHelper {

    @Autowired
    LobbyService lobbyService;

    public UUID createLobby() {
        return lobbyService.createLobby(UUID.randomUUID(), "test").getId();
    }
}
