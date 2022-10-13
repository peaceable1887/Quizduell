package gruppe_b.quizduell.application.user.commands.create_user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.user.UserRepository;
import gruppe_b.quizduell.domain.entities.User;

@Service
public class CreateUserCommandHandler {

    @Autowired
    private UserRepository repo;

    public User handle(CreateUserCommand command) {
        User user = new User(command.name, command.passwordHash, command.salt);

        return repo.save(user);
    }
}
