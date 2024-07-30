package me.wyndev.towerdefense.sidebar;

import me.wyndev.towerdefense.ChatColor;
import me.wyndev.towerdefense.Utils;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefenseTeam;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

public class GameSidebar extends AbstractSidebar {

    private final TowerDefenseTeam viewers;

    /**
     * Constructs a game sidebar for a team.
     */
    public GameSidebar(TowerDefenseTeam team) {
        super("<green><bold>Tower Defense");
        this.viewers = team;
    }

    public void addPlayer(TowerDefensePlayer plr) {
        addViewer(plr);
    }

    @Override
    public void createLines() {
        sidebar.createLine(new Sidebar.ScoreboardLine("space", Component.empty(), 3));
        sidebar.createLine(new Sidebar.ScoreboardLine("health", getHealthLineFormat(50), 2));
        sidebar.createLine(new Sidebar.ScoreboardLine("gold", getGoldLineFormat(0), 1));
        sidebar.createLine(new Sidebar.ScoreboardLine("space1", Component.empty(), 0));
    }

    public void updateGoldLine(double newGold) {
        sidebar.updateLineContent("gold", getGoldLineFormat(newGold));
    }

    private Component getGoldLineFormat(double gold) {
        return Component.text("Gold: ").color(ChatColor.GRAY.toColor())
                .append(Component.text(Utils.formatWithCommas(gold)).color(ChatColor.GOLD.toColor()));
    }

    public void updateHealthLine(int health) {
        sidebar.updateLineContent("health", getHealthLineFormat(health));
    }

    private Component getHealthLineFormat(int health) {
        return Utils.format("<gray>Health: <red>" + health);
    }

    @Override
    public void addViewer(Player player) {
        if (!(player instanceof TowerDefensePlayer towerDefensePlayer) || !viewers.getTowerDefensePlayers().contains(towerDefensePlayer)) return;
        sidebar.addViewer(player);
    }

    @Override
    public void removeViewer(Player player) {
        if (!(player instanceof TowerDefensePlayer towerDefensePlayer) || !viewers.getTowerDefensePlayers().contains(towerDefensePlayer)) return;
        sidebar.removeViewer(player);
    }
}
