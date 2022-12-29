package gruppe_b.quizduell.application.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gruppe_b.quizduell.application.enums.PlayerResult;

/**
 * Enthält das Ergebnis eines Quiz.
 * 
 * @author Christopher Burmeister
 */
public class GameSessionResult {

    private final List<GameSessionPlayerResult> players;

    public GameSessionResult() {
        this.players = new ArrayList<>();
    }

    public List<GameSessionPlayerResult> getPlayers() {
        return players;
    }

    /**
     * Einem Spieler einen Punkt hinzufügen, wenn er eine Frage richtig beantwortet
     * hat.
     * 
     * @param playerId UUID des Spielers
     * @param name     Name des Spielers
     */
    public void addOnePoint(UUID playerId) {

        // Ist der Spieler bereits eingefügt? Wenn -1 dann nein
        // Größer -1 ist der Index im Array
        int containsPlayer = playersContainsPlayer(playerId);
        players.get(containsPlayer).addOnePoint();
    }

    public void addPlayer(UUID playerId, String name) {
        GameSessionPlayerResult player = new GameSessionPlayerResult(playerId, name);
        players.add(player);
    }

    /**
     * Endpunktestand berechnen.
     * 
     * @throws Exception
     */
    public void endQuiz() throws Exception {
        int highestPointCount = 0;
        int pointSum = 0;
        boolean draw = false;

        /*
         * Berechne den höchsten Punktewert, den ein Spieler hat und die Summe der
         * Punkte von allen Spielern.
         * Über die Punktesumme wird ermittelt, ob es ein Unentschieden gab.
         */
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPoints() > highestPointCount) {
                highestPointCount = players.get(i).getPoints();
            }
            pointSum = pointSum + players.get(i).getPoints();
        }

        /*
         * Gab es ein Unentschieden?
         */
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

    /**
     * Prüft ob die Spieler UUID im Array enthalten ist.
     * 
     * @param id UUID nach der gesucht werden soll.
     * @return -1 = nein. > -1 = index des Spielers im Array
     */
    private int playersContainsPlayer(UUID id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == id) {
                return i;
            }
        }

        return -1;
    }
}
