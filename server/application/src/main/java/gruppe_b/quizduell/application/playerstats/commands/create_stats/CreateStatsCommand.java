package gruppe_b.quizduell.application.playerstats.commands.create_stats;

import gruppe_b.quizduell.domain.entities.User;

/**
 * Command zum Erstellen eines neuen Statistik-Daten-Satz. Wir an einen
 * CommandHandler übergeben.
 * 
 * @author Christopher Burmeister
 */
public class CreateStatsCommand {

    public final User player;
    public final Integer gameCount;
    public final Integer gameWonCount;
    public final Integer gameLossCount;
    public final Integer gameDrawCount;

    public CreateStatsCommand(User player, Integer gameCount, Integer gameWonCount, Integer gameLossCount,
            Integer gameDrawCount) {
        this.player = player;
        this.gameCount = gameCount;
        this.gameWonCount = gameWonCount;
        this.gameLossCount = gameLossCount;
        this.gameDrawCount = gameDrawCount;
    }
}
