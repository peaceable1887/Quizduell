package gruppe_b.quizduell.application.playerstats;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.domain.entities.PlayerStats;

/**
 * Repository für die Spieler-Statistik.
 * 
 * @author Christopher Burmeister
 */
@Repository
public interface PlayerStatsRepository {
    PlayerStats findByPlayerId(UUID id);

    PlayerStats save(PlayerStats playerStats);

    PlayerStats update(PlayerStats playerStats);

    void deleteAll();
}
