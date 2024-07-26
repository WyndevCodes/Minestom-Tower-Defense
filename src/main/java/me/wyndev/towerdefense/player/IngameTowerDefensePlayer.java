package me.wyndev.towerdefense.player;

import lombok.Getter;
import lombok.Setter;
import me.wyndev.towerdefense.sidebar.GameSidebar;
import me.wyndev.towerdefense.tower.Tower;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class for {@link TowerDefensePlayer} that represents a player that
 * is currently in a tower defense game.
 */
@Getter
public class IngameTowerDefensePlayer {

    private @Getter TowerDefensePlayer towerDefensePlayer;
    private @Setter int health = 100;
    private long gold = 8000000; //TODO: set back to 0 for production
    private @Setter int towersPlaced = 0;
    /**
     * Gold income of the player every 10 seconds.
     */
    private @Setter long income = 10;
    /**
     * A list of all towers placed by this player in a game
     */
    private final List<Tower> currentPlacedTowers = new ArrayList<>();
    private final GameSidebar gameSidebar;

    /**
     * Initializes an IngameTowerDefensePlayer.
     *
     * @param towerDefensePlayer The online player who is currently in a game
     */
    public IngameTowerDefensePlayer(@NotNull TowerDefensePlayer towerDefensePlayer) {
        this.towerDefensePlayer = towerDefensePlayer;
        this.gameSidebar = new GameSidebar(this);
    }

    /**
     * Shuts down this IngameTowerDefensePlayer, removing
     * any references it has to UUIDs or online players.
     */
    public void shutdown() {
        towerDefensePlayer = null;
        currentPlacedTowers.clear();
        gameSidebar.removeViewer(towerDefensePlayer);
    }

    public void setGold(long gold) {
        this.gold = gold;
        this.gameSidebar.updateGoldLine(gold);
    }
}
