package gruppe_b.quizduell.persistence.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import gruppe_b.quizduell.domain.entities.PlayerStats;

/**
 * Spieler-Statistik DbEntity
 * 
 * @author Christopher Burmeister
 */
@Entity
@Table(name = "player_stats")
public class DbPlayerStats extends PlayerStats {

    public DbPlayerStats() {

    }

    public DbPlayerStats(UUID playerId, int gameCount, int gameWonCount) {
        super(playerId, gameCount, gameWonCount);
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Override
    public UUID getId() {
        return super.getId();
    }

    @Column(name = "playerId", nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Override
    public UUID getPlayerId() {
        return super.getPlayerId();
    }

    @Column(name = "game_count", nullable = false)
    @Override
    public int getGameCount() {
        return super.getGameCount();
    }

    @Column(name = "game_won_count", nullable = false)
    @Override
    public int getGameWonCount() {
        return super.getGameWonCount();
    }

    public PlayerStats createEntity() {
        PlayerStats playerStats = new PlayerStats();
        playerStats.setId(getId());
        playerStats.setPlayerId(getPlayerId());
        playerStats.setGameCount(getGameCount());
        playerStats.setGameWonCount(getGameWonCount());

        return playerStats;
    }
}
