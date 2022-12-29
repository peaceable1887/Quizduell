package gruppe_b.quizduell.persistence.entities;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

    public DbPlayerStats(DbUser player, int gameCount, int gameWonCount, int gameLossCount, int gameDrawCount) {
        super(player, gameCount, gameWonCount, gameLossCount, gameDrawCount);
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

    @OneToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false, updatable = false)
    @Override
    public DbUser getPlayer() {
        return (DbUser) super.getPlayer();
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

    @Column(name = "game_loss_count", nullable = false)
    @Override
    public int getGameLossCount() {
        return super.getGameLossCount();
    }

    @Column(name = "game_draw_count", nullable = false)
    @Override
    public int getGameDrawCount() {
        return super.getGameDrawCount();
    }

    public PlayerStats createEntity() {
        PlayerStats playerStats = new PlayerStats();
        playerStats.setId(getId());
        playerStats.setPlayer(getPlayer());
        playerStats.setGameCount(getGameCount());
        playerStats.setGameWonCount(getGameWonCount());

        return playerStats;
    }
}
