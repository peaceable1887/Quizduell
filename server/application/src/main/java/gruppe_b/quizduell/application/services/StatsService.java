package gruppe_b.quizduell.application.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.playerstats.commands.create_stats.CreateStatsCommand;
import gruppe_b.quizduell.application.playerstats.queries.get_details.GetStatsDetailByPlayerIdQuery;
import gruppe_b.quizduell.domain.entities.PlayerStats;
import gruppe_b.quizduell.domain.entities.User;
import gruppe_b.quizduell.application.exceptions.PlayerStatsNotFoundException;

/**
 * Service zum Managen von Stats.
 * 
 * @author Christopher Burmeister
 */
@Service
public class StatsService {

    private static final Logger logger = LoggerFactory.getLogger(StatsService.class);

    @Autowired
    RequestHandler<GetStatsDetailByPlayerIdQuery, PlayerStats> getStatsHandler;

    @Autowired
    RequestHandler<CreateStatsCommand, PlayerStats> createStatsHandler;

    public PlayerStats getStatsByUserId(UUID id) throws PlayerStatsNotFoundException {
        PlayerStats stats = getStatsHandler.handle(new GetStatsDetailByPlayerIdQuery(id));
        if (stats == null) {
            throw new PlayerStatsNotFoundException("Stats not found! PlayerId: " + id.toString());
        }

        return stats;
    }

    public PlayerStats createStats(User user) {
        return createStatsHandler.handle(new CreateStatsCommand(user,
                0, 0, 0, 0));
    }
}
