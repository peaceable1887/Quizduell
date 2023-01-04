package gruppe_b.quizduell.application.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.playerstats.commands.create_stats.CreateStatsCommand;
import gruppe_b.quizduell.application.playerstats.commands.update_stats.UpdateStatsCommand;
import gruppe_b.quizduell.application.playerstats.queries.get_details.GetStatsDetailByPlayerIdQuery;
import gruppe_b.quizduell.application.user.queries.get_details.GetUserDetailByUUIDQuery;
import gruppe_b.quizduell.domain.entities.PlayerStats;
import gruppe_b.quizduell.domain.entities.User;
import gruppe_b.quizduell.application.common.GameSessionPlayerResult;
import gruppe_b.quizduell.application.common.GameSessionResult;
import gruppe_b.quizduell.application.enums.PlayerResult;
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

    @Autowired
    RequestHandler<UpdateStatsCommand, PlayerStats> updateStatsHandler;

    @Autowired
    RequestHandler<GetUserDetailByUUIDQuery, User> getUserHandler;

    public PlayerStats getStatsByUserId(UUID id) {
        PlayerStats stats = getStatsHandler.handle(new GetStatsDetailByPlayerIdQuery(id));
        if (stats == null) {
            stats = new PlayerStats(null, new User(), 0, 0, 0, 0);
        }

        return stats;
    }

    public PlayerStats createStats(User user) {
        return createStatsHandler.handle(new CreateStatsCommand(user,
                0, 0, 0, 0));
    }

    public PlayerStats createStats(User user, int gameCount, int winCount, int lossCount, int drawCount) {
        return createStatsHandler.handle(new CreateStatsCommand(user, gameCount, winCount, lossCount, drawCount));
    }

    public void createOrUpdatePlayerStats(GameSessionResult gameSessionResult) {
        for (GameSessionPlayerResult player : gameSessionResult.getPlayers()) {

            int win = 0;
            int loss = 0;
            int draw = 0;

            if (player.getPlayerResult() == PlayerResult.WIN) {
                win++;
            } else if (player.getPlayerResult() == PlayerResult.LOSS) {
                loss++;
            } else if (player.getPlayerResult() == PlayerResult.DRAW) {
                draw++;
            }

            PlayerStats stats = getStatsHandler.handle(new GetStatsDetailByPlayerIdQuery(player.getId()));
            if (stats != null) {
                stats.addGameCount(1);
                stats.addGameWonCount(win);
                stats.addGameLossCount(loss);
                stats.addGameDrawCount(draw);

                updateStatsHandler.handle(new UpdateStatsCommand(stats));
            } else {
                createStats(getUserHandler.handle(new GetUserDetailByUUIDQuery(player.getId())), 1, win, loss, draw);
            }
        }
    }
}
