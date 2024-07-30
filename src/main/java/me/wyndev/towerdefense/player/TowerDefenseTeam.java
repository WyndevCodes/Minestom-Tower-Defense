package me.wyndev.towerdefense.player;

import lombok.Getter;
import lombok.Setter;
import me.wyndev.towerdefense.sidebar.GameSidebar;
import me.wyndev.towerdefense.tower.Tower;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class that represents a team of {@link TowerDefensePlayer}
 * that are currently in a tower defense game.
 */
@Getter
public class TowerDefenseTeam {

    private final @Getter List<TowerDefensePlayer> towerDefensePlayers;
    private int health = 100;
    private double gold = 100; //TODO: set back to 100 for production
    private @Setter int towersPlaced = 0;
    /**
     * Gold income of the team every 10 seconds.
     */
    private @Setter double income = 10;
    /**
     * A list of all towers placed by this team in a game
     */
    private final List<Tower> currentPlacedTowers = new ArrayList<>();
    private final GameSidebar gameSidebar;

    /**
     * Initializes a TowerDefenseTeam.
     *
     * @param towerDefensePlayers All players to be a part of this team
     */
    public TowerDefenseTeam(@NotNull TowerDefensePlayer... towerDefensePlayers) {
        this.towerDefensePlayers = new ArrayList<>(Arrays.stream(towerDefensePlayers).toList());
        this.gameSidebar = new GameSidebar(this);
    }

    /**
     * Shuts down this IngameTowerDefensePlayer, removing
     * any references it has to UUIDs or online players.
     */
    public void shutdown() {
        for (TowerDefensePlayer towerDefensePlayer : towerDefensePlayers) {
            gameSidebar.removeViewer(towerDefensePlayer);
        }
        currentPlacedTowers.clear();
    }

    public void setGold(double gold) {
        this.gold = gold;
        this.gameSidebar.updateGoldLine(gold);
    }

    public void setHealth(int health) {
        this.health = health;
        if (this.health > 50) this.health = 50;
        if (this.health <= 0) {
            this.health = 0;
            //TODO: death logic
        }
        this.gameSidebar.updateHealthLine(health);
    }
}
