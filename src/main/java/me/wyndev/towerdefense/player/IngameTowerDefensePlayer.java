package me.wyndev.towerdefense.player;

import lombok.Getter;
import lombok.Setter;
import me.wyndev.towerdefense.tower.Tower;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A wrapper class for {@link TowerDefensePlayer} that represents a player that
 * is currently in a tower defense game.
 */
@Getter
public class IngameTowerDefensePlayer {

    private TowerDefensePlayer towerDefensePlayer;
    private @Setter int health = 100;
    private @Setter long gold = 8000000;
    private @Setter int towersPlaced = 0;
    /**
     * Gold income of the player every 10 seconds.
     */
    private @Setter long income = 10;
    /**
     * A map of all towers placed by this player in a game in respect to the
     * position each tower is placed at
     * @deprecated : NEED TO BE MOVED TO GameInstance.java TO PREVENT ANY ISSUES WITH MULTIPLAYER
     */
    @Deprecated(forRemoval = true)
    private final List<EntityCreature> currentPlacedTowers = new ArrayList<>();

    /**
     * Initializes an IngameTowerDefensePlayer.
     *
     * @param towerDefensePlayer The online player who is currently in a game
     */
    public IngameTowerDefensePlayer(@NotNull TowerDefensePlayer towerDefensePlayer) {
        this.towerDefensePlayer = towerDefensePlayer;
    }

    /**
     * Shuts down this IngameTowerDefensePlayer, removing
     * any references it has to UUIDs or online players.
     */
    public void shutdown() {
        towerDefensePlayer = null;
        currentPlacedTowers.clear();
    }
}
