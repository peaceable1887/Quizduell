package gruppe_b.quizduell.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import gruppe_b.quizduell.application.user.commands.create_user.CreateUserCommand;
import gruppe_b.quizduell.application.user.commands.create_user.CreateUserCommandHandler;

public class TestCases {

    @Autowired
    CreateUserCommandHandler createUser;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void createTestUserJohn() {
        String endcodePswd = passwordEncoder.encode("password");
        CreateUserCommand command = new CreateUserCommand("john", endcodePswd, "salz");
        createUser.handle(command);
    }
}
