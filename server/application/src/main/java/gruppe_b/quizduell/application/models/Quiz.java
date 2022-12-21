package gruppe_b.quizduell.application.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gruppe_b.quizduell.application.enums.QuizStatus;

public class Quiz {

    private final UUID id;
    private final List<Player> playerList;
    private QuizStatus quizStatus;

    private UUID lobbyId;

    public Quiz(UUID lobbyId) {
        this.id = UUID.randomUUID();
        this.lobbyId = lobbyId;
        this.playerList = new ArrayList<>();
        this.quizStatus = QuizStatus.WAIT;
    }

    public UUID getId() {
        return id;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public List<Player> getPlayers() {
        return playerList;
    }

    /**
     * Für dem Spiel einen Spieler hinzu.
     * 
     * @param id Id des Spielers, der hinzugefügt werden soll.
     * @return Objekt des Spielers, der hinzugefügt wurde.
     */
    public Player addPlayer(UUID id) {
        Player player = new Player(id);
        playerList.add(player);
        return player;
    }

    /**
     * Gibt das Objekt eines Spieler zurück.
     * 
     * @param playerId Id des Spielers, der angefordert wird.
     * @return Spieler Objekt. Null wenn nicht gefunden.
     */
    public Player getPlayer(UUID playerId) {
        for (Player player : playerList) {
            if (player.getUserId().compareTo(playerId) == 0) {
                return player;
            }
        }
        return null;
    }

    /**
     * Gibt zurück, ob alles Spieler in der Lobby bereit sind.
     * 
     * @return true all player ready
     */
    public boolean allPlayersReady() {
        for (Player player : playerList) {
            // Hat der Spieler einen anderen Status als ready?
            if (!player.getStatus().equals("ready")) {
                // Spieler ist nicht ready!
                return false;
            }
        }

        // Alle Spieler ready
        return true;
    }

    public void cancel() {
        if (this.quizStatus == QuizStatus.FINISH) {
            return;
        }

        this.quizStatus = QuizStatus.ABORT;
    }

    public QuizStatus getQuizStatus() {
        return this.quizStatus;
    }

    public void setQuizStarted() {
        this.quizStatus = QuizStatus.STARTED;
    }
}
