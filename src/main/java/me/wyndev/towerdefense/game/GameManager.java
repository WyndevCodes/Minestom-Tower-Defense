package me.wyndev.towerdefense.game;

import me.wyndev.towerdefense.player.TowerDefensePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Manager class that handles game instances
 * and the players associated with them.
 */
public class GameManager {

    private final HashMap<UUID, GameInstance> playersWithGames = new HashMap<>();
    private final List<GameInstance> activeGames = new ArrayList<>();

    /**
     * Adds a player to the next available Tower Defense game, or
     * creates a new game if there are none available to join.
     * @param playerToAdd The player to add to a game
     */
    public void addPlayerToGame(TowerDefensePlayer playerToAdd) {
        // Check if player is already in a game first
        if (playersWithGames.containsKey(playerToAdd.getUuid())) {
            playerToAdd.sendMessage(Component.text("You are already in a game! You must leave your current game to join a new one!").color(TextColor.color(255, 0, 0)));
            return;
        }

        // Find first game that player can join
        if (!activeGames.isEmpty()) {
            // Is there a better way to do this? It's not a large performance hit (I assume no more than 10 games will ever be active at once)
            for (GameInstance game : activeGames) {
                if (!game.getGameState().isJoinable()) continue; //skip if game is not joinable
                if (!game.addPlayer(playerToAdd)) continue; //skip if game is full

                // Associate game with player
                playersWithGames.put(playerToAdd.getUuid(), game);
                return;
            }
        }
        // Create a new game
        GameInstance newGame = new GameInstance();
        activeGames.add(newGame);

        // Add player to the new game
        newGame.addPlayer(playerToAdd);
        playersWithGames.put(playerToAdd.getUuid(), newGame);
    }

    /**
     * Removes a player from their current game, if they have one.
     * @param playerToRemove The player to remove from their game
     * @return True if the player was removed from a game, false
     * if the player was not in a game during this attempted removal
     */
    public boolean removePlayerFromGame(TowerDefensePlayer playerToRemove) {
        if (!playersWithGames.containsKey(playerToRemove.getUuid())) return false;

        GameInstance game = playersWithGames.get(playerToRemove.getUuid());
        game.removePlayer(playerToRemove);
        //TODO: remove game from activeGames list when completed, empty, or after the ended state resolves
        playersWithGames.remove(playerToRemove.getUuid());
        return true;
    }

}
