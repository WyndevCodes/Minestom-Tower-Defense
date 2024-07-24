package me.wyndev.game;

import me.wyndev.player.TowerDefensePlayer;
import net.kyori.adventure.text.Component;

import java.util.List;

/**
 * Class that represents an active tower defense game.
 */
public class TowerDefenseGame {

    private final int MAX_PLAYERS = 6; //TODO: support for per-map based player counts?

    // Game information
    private GameState gameState;

    // Map information
    //TODO

    // Player information
    private List<TowerDefensePlayer> players; //TODO: teams later?

    public TowerDefenseGame() {
        this.gameState = GameState.WAITING_FOR_PLAYERS;
    }

    /**
     * Starts the game of Tower Defense.
     */
    public void start() {
        this.gameState = GameState.RUNNING;
    }

    /**
     * Attempts to add a player to this tower defense game.
     * @param player The player to add
     * @return True if the player was successfully added,
     * false otherwise (such as if the game is full)
     */
    public boolean addPlayer(TowerDefensePlayer player) {
        if (players.size() >= MAX_PLAYERS) {
            player.sendMessage(Component.text("The game is full!")); //TODO: color red
            return false;
        }

        players.add(player);

        //TODO: teleport player to map spawn

        // Send join message
        for (TowerDefensePlayer playerInGame : players) {
            //TODO: color + format with rank (if we hook into luckperms)
            playerInGame.sendMessage(Component.text(player.getUsername() + " joined! (" + players.size() + "/" + MAX_PLAYERS + ")"));
        }

        // Check for start threshold
        if (players.size() > (MAX_PLAYERS * 0.8)) {
            // Start game countdown
            this.gameState = GameState.COUNTDOWN;
        }

        return true;
    }

    /**
     * Removes a player from this tower defense game if they
     * are a part of the game.
     * @param player The player to remove
     */
    public void removePlayer(TowerDefensePlayer player) {
        boolean wasRemoved = players.remove(player);

        //TODO: game logic determining auto-shutdown, cancel countdown, etc.

        if (wasRemoved && gameState == GameState.RUNNING) {
            for (TowerDefensePlayer playerInGame : players) {
                //send quit message
                //TODO: color + format with rank (if we hook into luckperms)
                playerInGame.sendMessage(Component.text(player.getUsername() + " left the game."));
            }
        }
    }

    /**
     * @return The current {@link GameState} of this game
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @return A list of all {@link TowerDefensePlayer}s currently in this game.
     */
    public List<TowerDefensePlayer> getPlayers() {
        return players;
    }
}
