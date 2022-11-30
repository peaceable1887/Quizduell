package gruppe_b.quizduell.lobbyserver.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.common.models.Player;
import gruppe_b.quizduell.enums.PlayerStatus;
import gruppe_b.quizduell.lobbyserver.exceptions.UnknownPlayerStatusException;
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

    /**
     * Neue Lobby erstellen.
     * 
     * @param playerId erster Spieler, der die Lobby erstellt.
     * @param name     name der Lobby
     * @return erstellte Lobby
     */
    public Lobby createLobby(UUID playerId, String name) {
        Lobby newLobby = new Lobby(name, new Player(playerId));
        this.lobbyRepo.put(newLobby.getId(), newLobby);

        // Publish new lobby on websocket /topic/new-lobby
        publishLobby(newLobby.getId());
        return newLobby;
    }

    /**
     * Neuen Spieler in eine Lobby einfügen.
     * 
     * @param playerId id des neuen Spielers
     * @param lobbyId  id der Lobby, die der Spieler beitreten möchte.
     * @return Lobby der beigetreten wurde.
     */
    public Lobby connectToLobby(UUID playerId, UUID lobbyId) {
        Lobby lobby = this.lobbyRepo.get(lobbyId);
        lobby.addPlayer(new Player(playerId));
        simpMessagingTemplate.convertAndSend("/topic/lobby/" + lobby.getId().toString(), lobby);
        return lobby;
    }

    /**
     * Spieler aus einer Lobby entfernen.
     * 
     * @param playerId id des Spielers, der die Lobby verlassen möchte.
     * @param lobbyId  id der Lobby, die der Spieler verlassen möchte.
     */
    public void disconnectFromLobby(UUID playerId, UUID lobbyId) {
        Lobby lobby = this.lobbyRepo.get(lobbyId);
        lobby.removePlayer(playerId);

        // Lobby löschen, wenn letzter Spieler disconnected.
        if (lobby.getPlayers().isEmpty()) {
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

    /**
     * Status eines Spielers in einer Lobby updaten.
     * 
     * @param lobbyId  id der Lobby, zu der der Spieler gehört
     * @param playerId id des Spielers
     * @param status   Status der gesetzt werden soll
     * @return Lobby
     * @throws UnknownPlayerStatusException
     */
    public Lobby updatePlayerStatus(UUID lobbyId, UUID playerId, PlayerStatus status)
            throws UnknownPlayerStatusException {
        Lobby lobby = getLobby(lobbyId);
        Player player = lobby.getPlayer(playerId);

        if (status == PlayerStatus.READY) {
            player.setReady();
        } else if (status == PlayerStatus.WAIT) {
            player.setWait();
        } else {
            throw new UnknownPlayerStatusException(status.toString());
        }

        return lobby;
    }
}
