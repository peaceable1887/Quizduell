package gruppe_b.quizduell.quizserver.common;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gruppe_b.quizduell.application.models.Player;
import gruppe_b.quizduell.application.models.Quiz;
import gruppe_b.quizduell.lobbyserver.modelss.Lobby;
import gruppe_b.quizduell.lobbyserver.services.LobbyService;
import gruppe_b.quizduell.quizserver.services.QuizService;

@Service
public class QuizHelper {

    @Autowired
    QuizService quizService;

    @Autowired
    LobbyService lobbyService;

    public Quiz createQuiz() throws Exception {
        Player player = createPlayer();
        Lobby lobby = createLobby(player.getUserId());
        String token = createToken(lobby);

        return createQuiz(player, lobby, token);
    }

    public Quiz createFullQuiz() throws Exception {
        Quiz quiz = createQuiz();
        quiz.addPlayer(createPlayer().getUserId(), "john");

        return quiz;
    }

    public Quiz createQuiz(Player player, Lobby lobby, String token) throws Exception {
        Quiz quiz = quizService.connectToQuiz(lobby.getId(), player.getUserId(), player.getName(), token);

        return quiz;
    }

    public Player createPlayer() {
        return new Player(UUID.randomUUID(), "john");
    }

    public Lobby createLobby(UUID playerId) {
        return createLobby(playerId, "");
    }

    public Lobby createLobby(UUID playerId, String password) {
        return lobbyService.createLobby(playerId, "testLobby", password);
    }

    public String createToken(Lobby lobby) {
        return lobbyService.createGameToken(lobby);
    }
}
