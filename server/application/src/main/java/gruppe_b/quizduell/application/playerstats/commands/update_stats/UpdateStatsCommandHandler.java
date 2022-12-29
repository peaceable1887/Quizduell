package gruppe_b.quizduell.application.playerstats.commands.update_stats;

import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.playerstats.PlayerStatsRepository;
import gruppe_b.quizduell.domain.entities.PlayerStats;

@Service
public class UpdateStatsCommandHandler implements RequestHandler<UpdateStatsCommand, PlayerStats> {

    private PlayerStatsRepository repo;

    public UpdateStatsCommandHandler(PlayerStatsRepository repo) {
        this.repo = repo;
    }

    public PlayerStats handle(UpdateStatsCommand command) {
        return repo.update(command.playerStats);
    }
}
