package gruppe_b.quizduell.application.user.commands.create_user;

import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.UserRepository;
import gruppe_b.quizduell.domain.entities.User;

@Service
public class CreateUserCommandHandler implements RequestHandler<CreateUserCommand, User> {

    private UserRepository repo;

    public CreateUserCommandHandler(UserRepository repo) {
        this.repo = repo;
    }

    public User handle(CreateUserCommand command) {
        User user = new User(command.name, command.passwordHash, command.salt);

        return repo.save(user);
    }
}
