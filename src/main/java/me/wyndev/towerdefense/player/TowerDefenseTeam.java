package me.wyndev.towerdefense.player;

import lombok.Getter;
import lombok.Setter;
import me.wyndev.towerdefense.files.config.object.TeamObject;
import me.wyndev.towerdefense.sidebar.GameSidebar;
import me.wyndev.towerdefense.tower.Tower;
import net.minestom.server.coordinate.Pos;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a team of {@link TowerDefensePlayer}
 * that are currently in a tower defense game.
 */
@Getter
public class TowerDefenseTeam {

    @Getter private final List<TowerDefensePlayer> towerDefensePlayers = new ArrayList<>();
    private int health = 100;
    private double gold = 100; //TODO: set back to 100 for production
    @Setter private int towersPlaced = 0;
    /**
     * Gold income of the team every 10 seconds.
     */
    @Getter @Setter private double income = 10;
    /**
     * A list of all towers placed by this team in a game
     */
    @Getter private final List<Tower> currentPlacedTowers = new ArrayList<>();
    private final TeamObject teamObject;
    private final GameSidebar gameSidebar;

    /**
     * Initializes a TowerDefenseTeam.
     *
     * @param teamObject ALl the settings for the teams
     */
    public TowerDefenseTeam(TeamObject teamObject) {
        this.teamObject = teamObject;
        this.gameSidebar = new GameSidebar(this);
    }

    /**
     * Shuts down this IngameTowerDefensePlayer, removing
     * any references it has to UUIDs or online players.
     */
    public void dispose() {
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

    public void teleport(Pos pos) {
        for (TowerDefensePlayer plr : towerDefensePlayers) {
            plr.teleport(pos);
        }
    }
}
