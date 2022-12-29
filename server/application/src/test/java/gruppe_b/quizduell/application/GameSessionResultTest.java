package gruppe_b.quizduell.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.application.common.GameSessionPlayerResult;
import gruppe_b.quizduell.application.common.GameSessionResult;
import gruppe_b.quizduell.application.enums.PlayerResult;
import gruppe_b.quizduell.application.interfaces.SendToPlayerService;

@SpringBootTest
public class GameSessionResultTest {

    @Mock
    SendToPlayerService sendToPlayerService;

    GameSessionResult gameSessionResult;
    GameSessionPlayerResult p1;
    GameSessionPlayerResult p2;

    @BeforeEach
    void setup() {
        gameSessionResult = new GameSessionResult();
        p1 = new GameSessionPlayerResult(UUID.randomUUID(), "john");
        p2 = new GameSessionPlayerResult(UUID.randomUUID(), "jane");
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

    @Test
    void whenOnlyOneAnswerCorrectThenWin() throws Exception {
        // Arrange
        gameSessionResult.addOnePoint(p1.getId());

        // Arct
        gameSessionResult.endQuiz();

        // Assert
        assertEquals(1, p1.getPoints());
        assertEquals(0, p2.getPoints());
        assertEquals(PlayerResult.WIN, p1.getPlayerResult());
        assertEquals(PlayerResult.LOSS, p2.getPlayerResult());
    }

    @Test
    void testSendGameResult() throws Exception {
        // Arrange
        UUID lobbyId = UUID.randomUUID();

        gameSessionResult.addOnePoint(p1.getId());
        gameSessionResult.addOnePoint(p2.getId());
        gameSessionResult.addOnePoint(p2.getId());
        gameSessionResult.endQuiz();

        // Act
        sendToPlayerService.sendQuizResult(lobbyId, gameSessionResult);

        Thread.sleep(100);

        // Assert
        verify(sendToPlayerService, times(1)).sendQuizResult(eq(lobbyId),
                any(GameSessionResult.class));

        verify(sendToPlayerService, times(1)).sendQuizResult(eq(lobbyId),
                argThat(getGameSessionResultMatcher()));
    }

    ArgumentMatcher<GameSessionResult> getGameSessionResultMatcher() {
        return x -> x.getPlayers().size() == 2 &&
                x.getPlayers().get(0).getPoints() == 1 &&
                x.getPlayers().get(1).getPoints() == 2 &&
                x.getPlayers().get(0).getPlayerResult() == PlayerResult.LOSS &&
                x.getPlayers().get(1).getPlayerResult() == PlayerResult.WIN;
    }
}
