package gruppe_b.quizduell.application.playerstats.commands.create_stats;

import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.interfaces.RequestHandler;
import gruppe_b.quizduell.application.playerstats.PlayerStatsRepository;
import gruppe_b.quizduell.domain.entities.PlayerStats;

/**
 * CommandHandler zum Erstellen eines Statistik-Daten-Satzes.
 * 
 * @author Christopher Burmeister
 */
@Service
public class CreateStatsCommandHandler implements RequestHandler<CreateStatsCommand, PlayerStats> {

    private PlayerStatsRepository repo;

    public CreateStatsCommandHandler(PlayerStatsRepository repo) {
        this.repo = repo;
    }

    public PlayerStats handle(CreateStatsCommand command) {
        PlayerStats stats = new PlayerStats(command.player,
                command.gameCount,
                command.gameWonCount,
                command.gameLossCount,
                command.gameDrawCount);

        return repo.save(stats);
    }
}
