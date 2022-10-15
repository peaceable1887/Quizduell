package gruppe_b.quizduell.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.user.commands.create_user.CreateUserCommand;
import gruppe_b.quizduell.domain.entities.User;

public class TestCases {

    @Autowired
    RequestHandler<CreateUserCommand, User> createUserHandler;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void createTestUserJohn() {
        String endcodePswd = passwordEncoder.encode("password");
        CreateUserCommand command = new CreateUserCommand("john", endcodePswd, "salz");
        createUserHandler.handle(command);
    }
}
