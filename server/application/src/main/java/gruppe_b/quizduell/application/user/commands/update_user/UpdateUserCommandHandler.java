package gruppe_b.quizduell.application.user.commands.update_user;

import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.UserRepository;
import gruppe_b.quizduell.domain.entities.User;

@Service
public class UpdateUserCommandHandler implements RequestHandler<UpdateUserCommand, User> {

    private UserRepository repo;

    public UpdateUserCommandHandler(UserRepository repo) {
        this.repo = repo;
    }

    public User handle(UpdateUserCommand command) {
        return repo.update(command.user);
    }
}
