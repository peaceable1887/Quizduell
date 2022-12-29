package gruppe_b.quizduell.application.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gruppe_b.quizduell.application.enums.PlayerResult;

public class GameSessionResult {

    private final List<GameSessionPlayerResult> players;

    public GameSessionResult() {
        this.players = new ArrayList<>();
    }

    public List<GameSessionPlayerResult> getPlayers() {
        return players;
    }

    public void addOnePoint(UUID playerId) {

        // Ist der Spieler bereits eingefügt? Wenn -1 dann nein
        // Größer -1 ist der Index im Array
        int containsPlayer = playersContainsPlayer(playerId);
        if (containsPlayer >= 0) {
            players.get(containsPlayer).addOnePoint();
        } else {
            GameSessionPlayerResult player = new GameSessionPlayerResult(playerId);
            player.addOnePoint();
            players.add(player);
        }
    }

    public void endQuiz() throws Exception {
        int highestPointCount = 0;
        int pointSum = 0;
        boolean draw = false;

        for (int i = 0; i < players.size(); i++) {
            if (i == 0) {
                highestPointCount = players.get(i).getPoints();
                pointSum = players.get(i).getPoints();
            } else {
                if (players.get(i).getPoints() > highestPointCount) {
                    highestPointCount = players.get(i).getPoints();
                }
                pointSum = pointSum + players.get(i).getPoints();
            }
        }

        if (pointSum / players.size() == highestPointCount) {
            draw = true;
        }

        for (GameSessionPlayerResult player : players) {
            if (draw) {
                if (player.getPoints() == highestPointCount) {
                    player.setPlayerResult(PlayerResult.DRAW);
                }
            } else if (player.getPoints() == highestPointCount) {
                player.setPlayerResult(PlayerResult.WIN);
            } else if (player.getPoints() < highestPointCount) {
                player.setPlayerResult(PlayerResult.LOSS);
            } else {
                throw new Exception("unexpected condition");
            }
        }
    }

    private int playersContainsPlayer(UUID id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == id) {
                return i;
            }
        }

        return -1;
    }
}
