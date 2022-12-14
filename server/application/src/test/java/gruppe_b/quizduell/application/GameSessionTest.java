package gruppe_b.quizduell.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.application.common.GameSessionDto;
import gruppe_b.quizduell.application.game.QuizSession;
import gruppe_b.quizduell.application.interfaces.SendToPlayerService;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.application.user.UserRepository;

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

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

@SpringBootTest
class GameSessionTest {

    @Mock
    SendToPlayerService sendToPlayerService;

    QuizSession spySession;

    Quiz quiz;
    QuizSession session;

    @BeforeEach
    void setUp() {
        quiz = new Quiz(UUID.randomUUID());
        quiz.addPlayer(UUID.randomUUID());
        quiz.addPlayer(UUID.randomUUID());
        session = new QuizSession(quiz, sendToPlayerService);
    }

    @Test
    void simpleTest() throws Exception {
        // Arrange
        Quiz quizLocal = new Quiz(UUID.randomUUID());

        // Act
        QuizSession sessionLocal = new QuizSession(quizLocal, sendToPlayerService);

        // Assert
        assertNotNull(sessionLocal);
    }

    @Test
    void testCountdown() throws Exception {
        // Arrange

        // Act
        session.start();

        Thread.sleep(23_000);

        // Assert
        verify(sendToPlayerService, never()).sendRoundCountdown(quiz.getLobbyId(), 21);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 20);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 19);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 18);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 17);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 16);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 15);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 14);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 13);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 12);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 11);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 10);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 9);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 8);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 7);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 6);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 5);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 4);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 3);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 2);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 1);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 0);
        verify(sendToPlayerService, never()).sendRoundCountdown(quiz.getLobbyId(), -1);
    }

    @Test
    void whenStartGameThenSendGameDtoOnlyOnce() throws Exception {
        // Arrange

        // Act
        session.start();

        Thread.sleep(1_000);

        verify(sendToPlayerService).sendGameSessionUpdate(eq(quiz.getLobbyId()),
                any(GameSessionDto.class));

        verify(sendToPlayerService).sendGameSessionUpdate(eq(quiz.getLobbyId()),
                argThat(x -> x.maxRounds == 6 &&
                        x.currentRound == 1 &&
                        x.correctAnswer == 0 &&
                        x.questionText.length() > 0 &&
                        x.answerOne.length() > 0 &&
                        x.answerTwo.length() > 0 &&
                        x.answerThree.length() > 0 &&
                        x.answerFour.length() > 0));
    }

    @Test
    void whenPlayerSendAnswerThenReduceCountdown() throws Exception {
        // Arrange
        UUID playerId = quiz.getPlayers().get(0).getUserId();

        // Act
        session.start();

        Thread.sleep(2_000);

        session.playerAnswer(playerId, 1);

        Thread.sleep(3_000);

        // Assert
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 20);
        verify(sendToPlayerService, never()).sendRoundCountdown(quiz.getLobbyId(), 17);
        verify(sendToPlayerService, never()).sendRoundCountdown(quiz.getLobbyId(), 7);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 6);
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 5);
    }

    @Test
    void whenAllPlayerSendAnswerThenEndRound() throws Exception {
        // Arrange
        UUID playerId1 = quiz.getPlayers().get(0).getUserId();
        UUID playerId2 = quiz.getPlayers().get(0).getUserId();

        // Act
        session.start();

        Thread.sleep(1_000);

        session.playerAnswer(playerId1, 2);
        session.playerAnswer(playerId2, 2);

        Thread.sleep(3_000);

        // Assert
        verify(sendToPlayerService, times(1)).sendRoundCountdown(quiz.getLobbyId(), 20);
        verify(sendToPlayerService, never()).sendRoundCountdown(quiz.getLobbyId(), 17);
        verify(sendToPlayerService, never()).sendRoundCountdown(quiz.getLobbyId(), 6);
        verify(sendToPlayerService, never()).sendRoundCountdown(quiz.getLobbyId(), 5);
    }

    @Test
    void calcRemainingSecondsTest() throws Exception {
        // Arrange

        // Act
        session.start();

        Thread.sleep(2000);

        int passSeconds = session.calcRemainingSeconds();

        // Assert
        assertTrue(passSeconds > 15 && passSeconds < 20);
    }
}
