package me.wyndev.towerdefense.sidebar;

import me.wyndev.towerdefense.ChatColor;
import me.wyndev.towerdefense.Utils;
import me.wyndev.towerdefense.player.IngameTowerDefensePlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

public class GameSidebar extends AbstractSidebar {

    private final IngameTowerDefensePlayer viewer;

    /**
     * Constructs a game sidebar for a player.
     */
    public GameSidebar(IngameTowerDefensePlayer viewer) {
        super("Tower Defense");
        this.viewer = viewer;
        addViewer(viewer.getTowerDefensePlayer());
    }

    @Override
    public void createLines() {
        sidebar.createLine(new Sidebar.ScoreboardLine("space", Component.empty(), 2));
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

    @Override
    public void addViewer(Player player) {
        if (!player.equals(viewer.getTowerDefensePlayer())) return;
        sidebar.addViewer(player);
    }

    @Override
    public void removeViewer(Player player) {
        if (!player.equals(viewer.getTowerDefensePlayer())) return;
        sidebar.removeViewer(player);
    }
}
