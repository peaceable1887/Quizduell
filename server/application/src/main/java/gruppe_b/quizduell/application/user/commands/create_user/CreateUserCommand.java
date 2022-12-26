package gruppe_b.quizduell.application.user.commands.create_user;

/**
 * Command zum Erstellen eines neuen Users. Wird an einen CommandHandler
 * übergeben.
 * 
 * @author Christopher Burmeister
 */
public class CreateUserCommand {

    public final String name;
    public final String mail;
    public final String passwordHash;
    public final String salt;

    public CreateUserCommand(String name, String mail, String passwordHash, String salt) {
        this.name = name;
        this.mail = mail;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }
}
