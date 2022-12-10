package gruppe_b.quizduell.common.exceptions;

public class JwtIsExpiredException extends Exception {
    public JwtIsExpiredException(String msg) {
        super(msg);
    }
}
