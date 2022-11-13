package gruppe_b.quizduell.application.user.queries.get_details;

/**
 * Query zum Laden eines Users, anhand eines Usernames. Wird an einen
 * QueryHandler übergeben.
 * 
 * @author Christopher Burmeister
 */
public class GetUserDetailQuery {

    public final String name;

    public GetUserDetailQuery(String name) {
        this.name = name;
    }
}
