package gruppe_b.quizduell.quizserver.exceptions;

public class JwtNotIssuedByLobbyServerException extends Exception {
    public JwtNotIssuedByLobbyServerException(String msg) {
        super(msg);
    }
}
