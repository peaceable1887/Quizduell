package gruppe_b.quizduell.lobbyserver.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.lobbyserver.Models.Lobby;
import gruppe_b.quizduell.lobbyserver.Models.Player;

@Service
public class LobbyService {

    private final HashMap<UUID, Lobby> lobbyRepo;

    public LobbyService() {
        this.lobbyRepo = new HashMap<>();
    }

    public Lobby createLobby(UUID playerId, String name) {
        Lobby newLobby = new Lobby(name, new Player(playerId));
        this.lobbyRepo.put(newLobby.getId(), newLobby);
        return newLobby;
    }

    public Lobby connectToLobby(UUID playerId, UUID lobbyId) {
        Lobby lobby = this.lobbyRepo.get(lobbyId);
        lobby.addPlayer(new Player(playerId));
        return lobby;
    }
}
