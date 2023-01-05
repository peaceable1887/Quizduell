package gruppe_b.quizduell.lobbyserver.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gruppe_b.quizduell.lobbyserver.common.ConnectRequest;
import gruppe_b.quizduell.lobbyserver.common.CreateRequest;
import gruppe_b.quizduell.lobbyserver.common.DisconnectRequest;
import gruppe_b.quizduell.lobbyserver.common.LobbyRequest;
import gruppe_b.quizduell.lobbyserver.exceptions.LobbyFullException;
import gruppe_b.quizduell.lobbyserver.exceptions.LobbyStatusException;
import gruppe_b.quizduell.lobbyserver.exceptions.LobbyWrongPasswordException;
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

    private static final Logger logger = LoggerFactory.getLogger(LobbyController.class);

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
    public ResponseEntity<Lobby> create(@AuthenticationPrincipal Jwt principal, @RequestBody CreateRequest request) {
        logger.info("---/create---");
        logger.info("LobbyName: {}", request.name);
        logger.info("LobbyPassword: {}", request.password);
        logger.info("---------");
        return ResponseEntity.status(HttpStatus.CREATED).body(lobbyService.createLobby(
                request.name, UUID.fromString(principal.getSubject()), principal.getClaim("name"), request.password));
    }

    /**
     * Verbinden mit einer Lobby.
     * 
     * @param principal enthält die UserId und wird durch Spring Security im
     *                  authentication Prozess erzeugt.
     * @param request   enthält die LobbyId, mit der sich der User verbinden möchte.
     * @return Lobby die der User beigetreten ist.
     * @throws LobbyWrongPasswordException
     */
    @PostMapping("/connect")
    public ResponseEntity<Lobby> connect(@AuthenticationPrincipal Jwt principal, @RequestBody ConnectRequest request)
            throws LobbyFullException, LobbyStatusException, LobbyWrongPasswordException {
        logger.info("---/connect---");
        logger.info("LobbyId: {}", request.lobbyId);
        logger.info(request.password);

        Lobby lobby = null;
        try {
            lobby = lobbyService.connectToLobby(
                    UUID.fromString(principal.getSubject()),
                    principal.getClaim("name"),
                    request.lobbyId,
                    request.password);

            return ResponseEntity.ok(lobby);
        } catch (LobbyFullException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (LobbyStatusException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (LobbyWrongPasswordException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
            logger.info("---------");
        }
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
        logger.info("---/disconnect---");
        logger.info("LobbyId: {}", request.lobbyId);
        logger.info("---------");
        lobbyService.disconnectFromLobby(
                UUID.fromString(principal.getName()), request.lobbyId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Gibt eine angefragte Lobby zurück.
     * 
     * @param lobbyId LobbyId zu der Lobby, die angefragt wird.
     * @return Lobby
     */
    @GetMapping("/get")
    public ResponseEntity<Lobby> get(@RequestParam("lobbyId") UUID lobbyId) {
        logger.info("---/get---");
        logger.info("LobbyId: {}", lobbyId);
        logger.info("---------");
        return ResponseEntity.ok(lobbyService.getLobby(lobbyId));
    }

    /**
     * Gibt alle bestehenden Lobbies zurück.
     * 
     * @return Liste mit allen bestehenden Lobbies.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Lobby>> all() {
        logger.info("---/all---");
        logger.info("---------");
        return ResponseEntity.ok(lobbyService.getAllLobbies());
    }
}
