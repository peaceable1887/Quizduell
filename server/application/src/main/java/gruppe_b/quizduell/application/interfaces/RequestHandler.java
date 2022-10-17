package gruppe_b.quizduell.application.interfaces;

/**
 * Generisches Interface für Query und Command handler.
 * 
 * @author Christopher Burmeister
 */
public interface RequestHandler<T, E> {
    E handle(T command);
}
