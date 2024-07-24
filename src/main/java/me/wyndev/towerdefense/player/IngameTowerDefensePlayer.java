package me.wyndev.towerdefense.player;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper class for {@link TowerDefensePlayer} that represents a player that
 * is currently in a tower defense game.
 */
@Getter
public class IngameTowerDefensePlayer {

    private final TowerDefensePlayer towerDefensePlayer;
    private @Setter int health = 100;
    private @Setter long gold = 0;
    private @Setter int towersPlaced = 0;
    //private List<Tower> currentPlacedTowers;

    /**
     * Initializes an IngameTowerDefensePlayer.
     *
     * @param towerDefensePlayer The online player who is currently in a game
     */
    public IngameTowerDefensePlayer(@NotNull TowerDefensePlayer towerDefensePlayer) {
        this.towerDefensePlayer = towerDefensePlayer;
    }
}
