package gruppe_b.quizduell.lobbyserver.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.common.enums.PlayerStatus;
import gruppe_b.quizduell.common.exceptions.UnknownPlayerStatusException;
import gruppe_b.quizduell.lobbyserver.exceptions.LobbyFullException;
import gruppe_b.quizduell.lobbyserver.exceptions.LobbyStatusException;
import gruppe_b.quizduell.lobbyserver.exceptions.LobbyWrongPasswordException;
import gruppe_b.quizduell.lobbyserver.models.Lobby;

/**
 * Service zum Managen von Lobbies.
 * 
 * TODO: QuizService und LobbyService teilen sich viel Code, den man generisch
 * umbauen kann...
 * 
 * @author Christopher Burmeister
 */
@Service
public class LobbyService {

    static final int MAX_PLAYER_COUNT = 2;

    // Thread-Safe HashMap
    private final ConcurrentHashMap<UUID, Lobby> lobbyRepo;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    JwtEncoder jwtEncoder;

    public LobbyService() {
        this.lobbyRepo = new ConcurrentHashMap<>();
    }

    /**
     * Neue Lobby erstellen.
     * 
     * @param playerId erster Spieler der die Lobby erstellt.
     * @param name     name der Lobby
     * @param password password für die Lobby. Wenn keins gesetzt werden soll ""
     *                 oder null setzen.
     * @return erstellte Lobby
     */
    public Lobby createLobby(UUID playerId, String name, String password) {
        Lobby newLobby = new Lobby(name, new Player(playerId, name), password);
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
     * @param password password für die Lobby. Wenn die Lobby kein password hat ""
     *                 oder null setzen
     * @return Lobby der beigetreten wurde.
     * @throws LobbyWrongPasswordException
     */
    public Lobby connectToLobby(UUID playerId, String playerName, UUID lobbyId, String password)
            throws LobbyFullException, LobbyStatusException, LobbyWrongPasswordException {
        Lobby lobby = this.lobbyRepo.get(lobbyId);

        if (lobby.hasPassword()) {
            if (!lobby.getPassword().equals(password)) {
                throw new LobbyWrongPasswordException("Password für die Lobby falsch: " + password);
            }
        }

        if (MAX_PLAYER_COUNT <= lobby.playerCount()) {
            throw new LobbyFullException("Lobby full. Max player count: " + MAX_PLAYER_COUNT);
        }

        lobby.addPlayer(new Player(playerId, playerName));
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
            deleteLobby(lobby);
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

        if (status == PlayerStatus.READY && lobby.getPlayers().size() > 1) {
            player.setReady();
        } else if (status == PlayerStatus.WAIT) {
            player.setWait();
        } else {
            throw new UnknownPlayerStatusException(status.toString());
        }

        // Alle Spieler ready?
        if (lobby.allPlayersReady()) {
            // Spieler sind ready. Start countdown.
            startLobbyCountdown(lobby);
        }

        return lobby;
    }

    private void deleteLobby(Lobby lobby) {
        this.lobbyRepo.remove(lobby.getId());
        simpMessagingTemplate.convertAndSend(
                "/topic/lobby/delete-lobby", lobby.getId());
    }

    private void startLobbyCountdown(Lobby lobby) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new LobbyStartCountDown(
                lobby,
                simpMessagingTemplate,
                this), 1_000, 1_000);
    }

    public String startGame(Lobby lobby) {
        lobby.setLobbyStarted();
        deleteLobby(lobby);
        return createGameToken(lobby);
    }

    /**
     * Erzeugt ein JWT über den die Spieler eine Game-Session auf dem Quiz-Server
     * erstellen können
     * 
     * @param lobby Lobby für die ein Token erstellt werden soll.
     * @return JWT
     */
    public String createGameToken(Lobby lobby) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("quizduell_lobbyserver")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(lobby.getId().toString())
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
