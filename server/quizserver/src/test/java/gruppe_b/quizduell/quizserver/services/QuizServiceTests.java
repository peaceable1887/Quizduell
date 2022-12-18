package gruppe_b.quizduell.quizserver.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.lobbyserver.models.Lobby;
import gruppe_b.quizduell.quizserver.common.QuizHelper;
import gruppe_b.quizduell.quizserver.exceptions.PlayerAlreadyConnectedException;
import gruppe_b.quizduell.quizserver.exceptions.PlayerAlreadyInOtherGameException;

@SpringBootTest
public class QuizServiceTests {

    @Autowired
    QuizHelper quizHelper;

    @Autowired
    QuizService quizService;

    static {
        System.setProperty("DB_PORT", "3306");
        System.setProperty("DB_HOSTNAME", "localhost");
        System.setProperty("DB_USERNAME", "root");
        System.setProperty("DB_PASSWORD", "root");
    }

    @Test
    void whenConnectToGameThenCreateGame() throws Exception {
        assertNotNull(quizHelper.createQuiz());
    }

    @Test
    void whenConnectToTheSameGameThenThrowException() throws Exception {
        // Arrange
        Player player = quizHelper.createPlayer();
        Lobby lobby = quizHelper.createLobby(player.getUserId());
        String token = quizHelper.createToken(lobby);
        quizHelper.createQuiz(player, lobby, token);

        // Act

        // Assert
        assertThrowsExactly(PlayerAlreadyConnectedException.class, () -> {
            quizService.connectToQuiz(lobby.getId(), player.getUserId(), token);
        });
    }

    @Test
    void whenConnectToSecondGameThenThrowException() throws Exception {
        // Arrange
        Quiz firstQuiz = quizHelper.createQuiz();
        Player player = firstQuiz.getPlayers().get(0);

        Lobby secondLobby = quizHelper.createLobby(player.getUserId());
        String token = quizHelper.createToken(secondLobby);

        // Act

        // Assert
        assertThrowsExactly(PlayerAlreadyInOtherGameException.class, () -> {
            quizService.connectToQuiz(secondLobby.getId(), player.getUserId(), token);
        });
    }

    @Test
    void testStartGameSession() throws Exception {
        // Arrange
        Quiz quiz = quizHelper.createQuiz();
        Player player = quizHelper.createPlayer();
        quiz.addPlayer(player.getUserId());

        // Act
        quizService.startQuiz(quiz);

        // Assert
    }
}
