package gruppe_b.quizduell.application.playerstats.queries.get_details;

import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.playerstats.PlayerStatsRepository;
import gruppe_b.quizduell.domain.entities.PlayerStats;

/**
 * QueryHandler für Player Statistiken.
 * 
 * @author Christopher Burmeister
 */
@Service
public class GetStatsDetailByPlayerIdQueryHandler
        implements RequestHandler<GetStatsDetailByPlayerIdQuery, PlayerStats> {

    private PlayerStatsRepository repo;

    public GetStatsDetailByPlayerIdQueryHandler(PlayerStatsRepository repo) {
        this.repo = repo;
    }

    public PlayerStats handle(GetStatsDetailByPlayerIdQuery query) {
        return repo.findByPlayerId(query.id);
    }
}
