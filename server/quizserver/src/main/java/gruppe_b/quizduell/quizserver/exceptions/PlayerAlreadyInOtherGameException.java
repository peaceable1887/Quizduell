package gruppe_b.quizduell.quizserver.exceptions;

public class PlayerAlreadyInOtherGameException extends Exception {
    public PlayerAlreadyInOtherGameException(String msg) {
        super(msg);
    }
}
