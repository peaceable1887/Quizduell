package gruppe_b.quizduell.application.user.queries.get_details;

import java.util.UUID;

/**
 * Query zum Laden eines User, anhand der UUID. Wird an einen QueryHandler
 * übergeben.
 */
public class GetUserDetailByUUIDQuery {

    public final UUID id;

    public GetUserDetailByUUIDQuery(UUID id) {
        this.id = id;
    }
}
