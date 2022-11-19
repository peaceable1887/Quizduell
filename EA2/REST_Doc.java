/**
 * Rest-Controller zum Registrieren und für den Login bzw. das Erzeugen eines
 * JWT.
 */
@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {

    /**
     * Erzeugt bei erfolgreicher Authentifizierung ein JWT.
     * 
     * @param authentication Spring Security Objekt mit Username und Passwort
     * @return JWT
     */
    @GetMapping("/token")
    public ResponseEntity<String> token(Authentication authentication) {
    }

    /**
     * Registriert einen neuen User.
     * 
     * @param userCredentialsDto request enthält Username, Mail (optional), Password
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {
    }

    /**
     * Gibt die Details zu einem User zurück.
     * 
     * @param principal enthält die UserId und wird durch Spring Security im
     *                  authentication Prozess erzeugt.
     * @return Details zum User
     */
    @GetMapping("/details")
    public ResponseEntity<UserDetailsDto> details(Principal principal) {
    }
}

/**
 * Rest-Controller zum Erstellen, Betreten und Auflisten von Lobbies.
 */
@RestController
@RequestMapping("/api/lobby/v1")
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
    public ResponseEntity<Lobby> connect(Principal principal, @RequestBody ConnectRequest request) {
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
    }

    /**
     * Gibt eine angefragte Lobby zurück.
     * 
     * @param request enthält die LobbyId
     * @return Lobby
     */
    @GetMapping("/get")
    public ResponseEntity<Lobby> get(@RequestBody LobbyRequest request) {
    }

    /**
     * Gibt alle bestehenden Lobbies zurück.
     * 
     * @return Liste mit allen bestehenden Lobbies.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Lobby>> all() {
    }
}

/**
 * Rest-Controller für das Quiz
 */
@RestController
@RequestMapping("/v1")
public class QuizController {

    /**
     * Erstellt eine Quiz-Session.
     * 
     * @param request enthält die LobbyId und die PlayerId's.
     * @return Quiz
     */
    @PostMapping("/create")
    public ResponseEntity<Quiz> create(@RequestBody CreateRequest request) {
    }

    /**
     * Gib eine angefragte Quiz-Session zurück.
     * 
     * @param request enthält die QuizId
     * @return Quiz
     */
    @GetMapping("/get")
    public ResponseEntity<Quiz> get(@RequestBody QuizRequest request) {
    }
}