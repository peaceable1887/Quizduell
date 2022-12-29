package gruppe_b.quizduell.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.application.common.GameSessionPlayerResult;
import gruppe_b.quizduell.application.common.GameSessionResult;
import gruppe_b.quizduell.application.enums.PlayerResult;

@SpringBootTest
public class GameSessionResultTest {

    GameSessionResult gameSessionResult;
    GameSessionPlayerResult p1;
    GameSessionPlayerResult p2;

    @BeforeEach
    void setup() {
        gameSessionResult = new GameSessionResult();
        p1 = new GameSessionPlayerResult(UUID.randomUUID());
        p2 = new GameSessionPlayerResult(UUID.randomUUID());
        gameSessionResult.getPlayers().add(p1);
        gameSessionResult.getPlayers().add(p2);
    }

    @Test
    void whenAddPointThenIncreasePlayerScore() {
        // Arrange

        // Act
        gameSessionResult.addOnePoint(p1.getId());

        // Assert
        assertEquals(1, p1.getPoints());
    }

    @Test
    void whenPlayerOneHadMorePointsThenPlayerOneWinsAndTwoLoss() throws Exception {
        // Arrange
        gameSessionResult.addOnePoint(p1.getId());
        gameSessionResult.addOnePoint(p1.getId());
        gameSessionResult.addOnePoint(p2.getId());

        // Act
        gameSessionResult.endQuiz();

        // Assert
        assertEquals(2, p1.getPoints());
        assertEquals(1, p2.getPoints());
        assertEquals(PlayerResult.WIN, p1.getPlayerResult());
        assertEquals(PlayerResult.LOSS, p2.getPlayerResult());
    }

    @Test
    void whenPlayerTwoHadMorePointsThenPlayerTwoWinsAndOneLoss() throws Exception {
        // Arrange
        gameSessionResult.addOnePoint(p1.getId());
        gameSessionResult.addOnePoint(p2.getId());
        gameSessionResult.addOnePoint(p2.getId());

        // Act
        gameSessionResult.endQuiz();

        // Assert
        assertEquals(1, p1.getPoints());
        assertEquals(2, p2.getPoints());
        assertEquals(PlayerResult.LOSS, p1.getPlayerResult());
        assertEquals(PlayerResult.WIN, p2.getPlayerResult());
    }

    @Test
    void whenPointsEqualThenThenDraw() throws Exception {
        // Arrange
        gameSessionResult.addOnePoint(p1.getId());
        gameSessionResult.addOnePoint(p1.getId());
        gameSessionResult.addOnePoint(p2.getId());
        gameSessionResult.addOnePoint(p2.getId());

        // Act
        gameSessionResult.endQuiz();

        // Assert
        assertEquals(2, p1.getPoints());
        assertEquals(2, p2.getPoints());
        assertEquals(PlayerResult.DRAW, p1.getPlayerResult());
        assertEquals(PlayerResult.DRAW, p2.getPlayerResult());
    }
}
