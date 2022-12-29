package gruppe_b.quizduell.quizserver.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import gruppe_b.quizduell.application.common.GameSessionPlayerResult;
import gruppe_b.quizduell.application.common.GameSessionResult;
import gruppe_b.quizduell.application.services.StatsService;
import gruppe_b.quizduell.domain.entities.PlayerStats;
import gruppe_b.quizduell.domain.entities.User;
import gruppe_b.quizduell.persistence.repository.UserRepositoryAdapter;
import gruppe_b.quizduell.persistence.repository.PlayerStatsRepositoryAdapter;

@SpringBootTest
@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
public class SaveQuizResultInDbTest {

    @Autowired
    StatsService statsService;

    @Autowired
    UserRepositoryAdapter userRepo;

    @Autowired
    PlayerStatsRepositoryAdapter statsRepo;

    static {
        System.setProperty("DB_PORT", "3306");
        System.setProperty("DB_HOSTNAME", "localhost");
        System.setProperty("DB_USERNAME", "root");
        System.setProperty("DB_PASSWORD", "root");
    }

    PlayerStats s1, s2, s3;

    User john, dave, marc, newUser;

    GameSessionResult gameSessionResult;
    GameSessionPlayerResult p1;
    GameSessionPlayerResult p2;

    @BeforeEach
    void setup() {
        statsRepo.deleteAll();
        userRepo.deleteAll();

        john = userRepo.save(new User("John", "JohnDoe@doe.com", "password", "salt"));
        dave = userRepo.save(new User("Dave", "exampleusermailname@exmaplemailhost.hamburg", "password", "salt"));
        marc = userRepo.save(new User("Marc", "u@u.de", "password", "salt"));
        newUser = userRepo.save(new User("newUser", "newUser@mail.de", "password", "salt"));

        s1 = statsRepo.save(new PlayerStats(john, 15, 2, 10, 3));
        s2 = statsRepo.save(new PlayerStats(dave, 10, 5, 5, 0));
        s3 = statsRepo.save(new PlayerStats(marc, 0, 0, 0, 0));

        gameSessionResult = new GameSessionResult();
        p1 = new GameSessionPlayerResult(john.getId());
        p2 = new GameSessionPlayerResult(newUser.getId());
        gameSessionResult.getPlayers().add(p1);
        gameSessionResult.getPlayers().add(p2);
    }

    @Test
    void test() throws Exception {
        // Arrange
        gameSessionResult.addOnePoint(p1.getId());
        gameSessionResult.addOnePoint(p1.getId());
        gameSessionResult.addOnePoint(p1.getId());
        gameSessionResult.addOnePoint(newUser.getId());
        gameSessionResult.addOnePoint(newUser.getId());

        gameSessionResult.endQuiz();

        // Act
        statsService.createOrUpdatePlayerStats(gameSessionResult);

        // Assert
        PlayerStats p1Stats = statsRepo.findByPlayerId(p1.getId());
        PlayerStats p2Stats = statsRepo.findByPlayerId(p2.getId());

        assertEquals(s1.getGameCount() + 1, p1Stats.getGameCount());
        assertEquals(s1.getGameWonCount() + 1, p1Stats.getGameWonCount());
        assertEquals(s1.getGameLossCount(), p1Stats.getGameLossCount());
        assertEquals(s1.getGameDrawCount(), p1Stats.getGameDrawCount());

        assertEquals(1, p2Stats.getGameCount());
        assertEquals(0, p2Stats.getGameWonCount());
        assertEquals(1, p2Stats.getGameLossCount());
        assertEquals(0, p2Stats.getGameDrawCount());
    }
}
