package gruppe_b.quizduell.application.user.commands.create_user;

/**
 * Command zum erstellen eines neuen Users. Wird an einen CommandHandler
 * übergeben.
 * 
 * @author Christopher Burmeister
 */
public class CreateUserCommand {

    public final String name;
    public final String passwordHash;
    public final String salt;

    public CreateUserCommand(String name, String passwordHash, String salt) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }
}
