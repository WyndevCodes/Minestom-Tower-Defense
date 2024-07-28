package me.wyndev.towerdefense.command;

import me.wyndev.towerdefense.Main;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import net.minestom.server.command.builder.Command;

public class PlayCommand extends Command {

    public PlayCommand() {
        super("play");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof TowerDefensePlayer towerDefensePlayer)) return;
            Main.gameManager.addPlayerToGame(towerDefensePlayer);
        });
    }
}
