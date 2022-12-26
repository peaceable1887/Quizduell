package gruppe_b.quizduell.application.playerstats.queries.get_details;

import java.util.UUID;

/**
 * Query zum Laden der Statistiken eines Players, anhand der UUID. Wird an einen
 * QueryHandler übergeben.
 */
public class GetStatsDetailByPlayerIdQuery {

    public final UUID id;

    public GetStatsDetailByPlayerIdQuery(UUID id) {
        this.id = id;
    }
}
