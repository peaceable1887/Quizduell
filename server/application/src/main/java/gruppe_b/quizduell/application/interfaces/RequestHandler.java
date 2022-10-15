package gruppe_b.quizduell.application.interfaces;

public interface RequestHandler<T, E> {
    E handle(T command);
}
