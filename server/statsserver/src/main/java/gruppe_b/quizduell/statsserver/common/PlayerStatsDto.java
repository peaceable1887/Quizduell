package gruppe_b.quizduell.statsserver.common;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gruppe_b.quizduell.domain.entities.PlayerStats;

public class PlayerStatsDto {

    public UUID id;
    public UUID playerId;
    public int gameCount;
    public int gameWonCount;
    public int gameLossCount;
    public int gameDrawCount;

    public PlayerStatsDto(PlayerStats playerStats) {
        this.id = playerStats.getId();
        this.playerId = playerStats.getPlayer().getId();
        this.gameCount = playerStats.getGameCount();
        this.gameWonCount = playerStats.getGameWonCount();
        this.gameLossCount = playerStats.getGameLossCount();
        this.gameDrawCount = playerStats.getGameDrawCount();
    }

    @JsonCreator
    public PlayerStatsDto(@JsonProperty("id") UUID id,
            @JsonProperty("playerId") UUID playerId,
            @JsonProperty("gameCount") int gameCount,
            @JsonProperty("gameWonCount") int gameWonCount,
            @JsonProperty("gameLossCount") int gameLossCount,
            @JsonProperty("gameDrawCount") int gameDrawCount) {
        this.id = id;
        this.playerId = playerId;
        this.gameCount = gameCount;
        this.gameWonCount = gameWonCount;
        this.gameLossCount = gameLossCount;
        this.gameDrawCount = gameDrawCount;
    }
}
