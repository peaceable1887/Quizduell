package gruppe_b.quizduell.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.application.playerstats.PlayerStatsRepository;
import gruppe_b.quizduell.domain.entities.PlayerStats;
import gruppe_b.quizduell.persistence.entities.DbPlayerStats;

/**
 * Adapter für das PlayerStats Repository.
 * Übernimmt das Mapping zwischen dem PlayerStats Domain Model und dem
 * PlayerStats DbEntity.
 * 
 * @author Christopher Burmeister
 */
@Repository("PlayerStatsRepository")
public class PlayerStatsRepositoryAdapter implements PlayerStatsRepository {

    @Autowired
    private JpaPlayerStatsRepository repo;

    @Override
    public PlayerStats findByPlayerId(UUID id) {
        DbPlayerStats playerStats = repo.findByPlayerId(id);
        if (playerStats == null) {
            return null;
        }
        return playerStats;
    }

    @Override
    public PlayerStats save(PlayerStats playerStats) {
        return repo.save(new DbPlayerStats(
                playerStats.getPlayerId(),
                playerStats.getGameCount(),
                playerStats.getGameWonCount()));
    }

    @Override
    public PlayerStats update(PlayerStats playerStats) {
        Optional<DbPlayerStats> result = repo.findById(playerStats.getId());

        if (!result.isPresent()) {
            return null;
        }

        DbPlayerStats dbEntity = result.get();

        dbEntity.setPlayerId(playerStats.getPlayerId());
        dbEntity.setGameCount(playerStats.getGameCount());
        dbEntity.setGameWonCount(playerStats.getGameWonCount());

        repo.save(dbEntity);

        return dbEntity;
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}
