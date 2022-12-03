package gruppe_b.quizduell.lobbyserver.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gruppe_b.quizduell.lobbyserver.common.ConnectRequest;
import gruppe_b.quizduell.lobbyserver.common.CreateRequest;
import gruppe_b.quizduell.lobbyserver.common.DisconnectRequest;
import gruppe_b.quizduell.lobbyserver.common.LobbyRequest;
import gruppe_b.quizduell.lobbyserver.exceptions.LobbyFullException;
import gruppe_b.quizduell.lobbyserver.exceptions.LobbyStatusException;
import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;

/**
 * Rest-Controller zum Erstellen, Betreten und Auflisten von Lobbies.
 * 
 * @author Christopher Burmeister
 */
@RestController
@RequestMapping("/v1")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    /**
     * Erstellt eine neue Lobby.
     * 
     * @param principal enthält die UserId und wird durch Spring Security im
     *                  authentication Prozess erzeugt.
     * @param request   enthält den Namen der zu erstellenden Lobby.
     * @return neu erstellte Lobby.
     */
    @PostMapping("/create")
    public ResponseEntity<Lobby> create(Principal principal, @RequestBody CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lobbyService.createLobby(
                UUID.fromString(principal.getName()), request.name));
    }

    /**
     * Verbinden mit einer Lobby.
     * 
     * @param principal enthält die UserId und wird durch Spring Security im
     *                  authentication Prozess erzeugt.
     * @param request   enthält die LobbyId, mit der sich der User verbinden möchte.
     * @return Lobby die der User beigetreten ist.
     */
    @PostMapping("/connect")
    public ResponseEntity<Lobby> connect(Principal principal, @RequestBody ConnectRequest request)
            throws LobbyFullException, LobbyStatusException {
        Lobby lobby = null;
        try {
            lobby = lobbyService.connectToLobby(
                    UUID.fromString(principal.getName()), request.lobbyId);
        } catch (LobbyFullException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (LobbyStatusException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(lobby);
    }

    /**
     * Eine Lobby verlassen.
     * 
     * @param principal enthält die UserId und wird durch Spring Security im
     *                  authentication Prozess erzeugt.
     * @param request   enthält die LobbyId, die der User verlassen möchte.
     */
    @PostMapping("/disconnect")
    public ResponseEntity<Void> disconnect(Principal principal, @RequestBody DisconnectRequest request) {
        lobbyService.disconnectFromLobby(
                UUID.fromString(principal.getName()), request.lobbyId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Gibt eine angefragte Lobby zurück.
     * 
     * @param request enthält die LobbyId
     * @return Lobby
     */
    @GetMapping("/get")
    public ResponseEntity<Lobby> get(@RequestBody LobbyRequest request) {
        return ResponseEntity.ok(lobbyService.getLobby(request.lobbyId));
    }

    /**
     * Gibt alle bestehenden Lobbies zurück.
     * 
     * @return Liste mit allen bestehenden Lobbies.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Lobby>> all() {
        return ResponseEntity.ok(lobbyService.getAllLobbies());
    }
}
