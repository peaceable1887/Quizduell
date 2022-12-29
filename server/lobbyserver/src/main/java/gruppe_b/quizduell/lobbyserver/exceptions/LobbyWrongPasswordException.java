package gruppe_b.quizduell.lobbyserver.exceptions;

public class LobbyWrongPasswordException extends Exception {
    public LobbyWrongPasswordException(String msg) {
        super(msg);
    }
}
