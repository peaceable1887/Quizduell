package gruppe_b.quizduell.lobbyserver.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.common.models.Player;
import gruppe_b.quizduell.lobbyserver.models.Lobby;

/**
 * Service zum Managen von Lobbies.
 * 
 * @author Christopher Burmeister
 */
@Service
public class LobbyService {

    // Thread-Safe HashMap
    private final ConcurrentHashMap<UUID, Lobby> lobbyRepo;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public LobbyService() {
        this.lobbyRepo = new ConcurrentHashMap<>();
    }

    public Lobby createLobby(UUID playerId, String name) {
        Lobby newLobby = new Lobby(name, new Player(playerId));
        this.lobbyRepo.put(newLobby.getId(), newLobby);

        // Publish new lobby on websocket /topic/new-lobby
        publishLobby(newLobby.getId());
        return newLobby;
    }

    public Lobby connectToLobby(UUID playerId, UUID lobbyId) {
        Lobby lobby = this.lobbyRepo.get(lobbyId);
        lobby.addPlayer(new Player(playerId));
        simpMessagingTemplate.convertAndSend("/topic/lobby/" + lobby.getId().toString(), lobby);
        return lobby;
    }

    public void disconnectFromLobby(UUID playerId, UUID lobbyId) {
        Lobby lobby = this.lobbyRepo.get(lobbyId);
        lobby.removePlayer(playerId);

        // Lobby löschen, wenn letzter Spieler disconnected.
        if (lobby.getPlayers().size() == 0) {
            this.lobbyRepo.remove(lobby.getId());
        }

        simpMessagingTemplate.convertAndSend("/topic/lobby/" + lobby.getId().toString(), lobby);
    }

    public Lobby publishLobby(UUID lobbyId) {
        Lobby lobby = this.lobbyRepo.get(lobbyId);
        simpMessagingTemplate.convertAndSend("/topic/new-lobby", lobby);
        return lobby;
    }

    public Lobby getLobby(UUID lobbyId) {
        return this.lobbyRepo.get(lobbyId);
    }

    public List<Lobby> getAllLobbies() {
        return new ArrayList<>(this.lobbyRepo.values());
    }
}
