package me.wyndev.towerdefense.sidebar;

import me.wyndev.towerdefense.Utils;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

public abstract class AbstractSidebar {

    protected final Sidebar sidebar;

    /**
     * Constructs a sidebar with a name.
     * @param sidebarName The name of the sidebar
     *                    using adventure text formatting
     */
    public AbstractSidebar(String sidebarName) {
        sidebar = new Sidebar(Utils.format(sidebarName));
        createLines();
    }

    /**
     * Creates the lines of the sidebar.
     */
    public abstract void createLines();

    public void addViewer(Player viewer) {
        sidebar.addViewer(viewer);
    }

    public void removeViewer(Player viewer) {
        sidebar.removeViewer(viewer);
    }

}
