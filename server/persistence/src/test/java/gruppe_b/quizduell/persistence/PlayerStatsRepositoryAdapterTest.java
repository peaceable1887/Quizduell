package gruppe_b.quizduell.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import gruppe_b.quizduell.domain.entities.PlayerStats;
import gruppe_b.quizduell.domain.entities.User;
import gruppe_b.quizduell.persistence.repository.PlayerStatsRepositoryAdapter;
import gruppe_b.quizduell.persistence.repository.UserRepositoryAdapter;

@SpringBootTest
public class PlayerStatsRepositoryAdapterTest {

    @Autowired
    PlayerStatsRepositoryAdapter repo;

    @Autowired
    UserRepositoryAdapter userRepo;

    PlayerStats s1, s2, s3;

    User john, dave, marc;

    @BeforeEach
    void setUp() {
        repo.deleteAll();
        userRepo.deleteAll();

        john = userRepo.save(new User("John", "JohnDoe@doe.com", "password", "salt"));
        dave = userRepo.save(new User("Dave", "exampleusermailname@exmaplemailhost.hamburg", "password", "salt"));
        marc = userRepo.save(new User("Marc", "u@u.de", "password", "salt"));

        s1 = repo.save(new PlayerStats(john.getId(), 15, 2, 10, 3));
        s2 = repo.save(new PlayerStats(dave.getId(), 10, 5, 5, 0));
        s3 = repo.save(new PlayerStats(marc.getId(), 0, 0, 0, 0));
    }

    @Test
    void whenSaveNewPlayerStatsThenCreateAndReturnNewPlayerStats() {
        // Arrange
        User player = userRepo.save(new User("TestUser", "test@test.de", "password", "salt"));
        PlayerStats playerStats = new PlayerStats(player.getId(), 100, 66, 4, 30);

        // Act
        PlayerStats result = repo.save(playerStats);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(playerStats.getPlayerId(), result.getPlayerId());
        assertEquals(playerStats.getGameCount(), result.getGameCount());
        assertEquals(playerStats.getGameWonCount(), result.getGameWonCount());
    }

    @Test
    void whenSaveNewPlayerStatsWithDuplicatedPlayerIdThenNotCreateNewPlayerStats() {
        // Arrange
        PlayerStats playerStats = new PlayerStats(john.getId(), 100, 66, 4, 30);

        // Act

        // Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            PlayerStats result = repo.save(playerStats);
        });
    }

    @Test
    void whenFindPlayerStatsByPlayerIdThenReturnPlayerStats() {
        // Arrange

        // Act
        PlayerStats result = repo.findByPlayerId(john.getId());

        // Assert
        assertNotNull(result);
        assertEquals(s1.getId(), result.getId());
        assertEquals(s1.getPlayerId(), result.getPlayerId());
        assertEquals(s1.getGameCount(), result.getGameCount());
        assertEquals(s1.getGameWonCount(), result.getGameWonCount());
    }
}
