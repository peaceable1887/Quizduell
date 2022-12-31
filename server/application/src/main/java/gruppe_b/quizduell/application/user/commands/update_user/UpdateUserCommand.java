package gruppe_b.quizduell.application.user.commands.update_user;

import gruppe_b.quizduell.domain.entities.User;

public class UpdateUserCommand {

    public final User user;

    public UpdateUserCommand(User user) {
        this.user = user;
    }
}
