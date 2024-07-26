package me.wyndev.towerdefense.sidebar;

import me.wyndev.towerdefense.Utils;
import net.kyori.adventure.text.Component;
import net.minestom.server.scoreboard.Sidebar;

public class HubSidebar extends AbstractSidebar {

    public HubSidebar() {
        super("<green><bold>Tower Defense"); //color subject to change
    }

    @Override
    public void createLines() {
        sidebar.createLine(new Sidebar.ScoreboardLine("space", Component.empty(), 2));
        sidebar.createLine(new Sidebar.ScoreboardLine("players", getPlayerLineFormat(0), 1));
        sidebar.createLine(new Sidebar.ScoreboardLine("space1", Component.empty(), 0));
    }

    public void updatePlayerCount(int newPlayerCount) {
        sidebar.updateLineContent("players", getPlayerLineFormat(newPlayerCount));
    }

    private Component getPlayerLineFormat(int players) {
        return Utils.format("<gray>Players: <yellow>" + Utils.formatWithCommas(players));
    }
}
