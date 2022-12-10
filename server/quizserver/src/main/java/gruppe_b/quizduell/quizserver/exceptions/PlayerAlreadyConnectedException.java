package gruppe_b.quizduell.quizserver.exceptions;

public class PlayerAlreadyConnectedException extends Exception {
    public PlayerAlreadyConnectedException(String msg) {
        super(msg);
    }
}
