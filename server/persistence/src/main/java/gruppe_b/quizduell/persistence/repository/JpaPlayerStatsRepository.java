package gruppe_b.quizduell.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gruppe_b.quizduell.persistence.entities.DbPlayerStats;

/**
 * Player stats Repository.
 * 
 * @author Christopher Burmeister
 */
@Repository
public interface JpaPlayerStatsRepository extends JpaRepository<DbPlayerStats, UUID> {
    public DbPlayerStats findByPlayerId(UUID id);
}
