package gruppe_b.quizduell.application.playerstats.commands.update_stats;

import gruppe_b.quizduell.domain.entities.PlayerStats;

public class UpdateStatsCommand {

    public final PlayerStats playerStats;

    public UpdateStatsCommand(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }
}
