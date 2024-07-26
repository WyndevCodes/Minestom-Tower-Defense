package me.wyndev.towerdefense.sidebar;

import me.wyndev.towerdefense.ChatColor;
import net.kyori.adventure.text.Component;
import net.minestom.server.scoreboard.Sidebar;

public class HubSidebar extends AbstractSidebar {

    public HubSidebar() {
        super("&aTower Defense"); //color subject to change
    }

    @Override
    public void createLines() {
        sidebar.createLine(new Sidebar.ScoreboardLine("space", Component.empty(), 2));
        sidebar.createLine(new Sidebar.ScoreboardLine("players", Component.text("Online: 0").color(ChatColor.WHITE.toColor()), 1));
        sidebar.createLine(new Sidebar.ScoreboardLine("space1", Component.empty(), 0));
    }
}
