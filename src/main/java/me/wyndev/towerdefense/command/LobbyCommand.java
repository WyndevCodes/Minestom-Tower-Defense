package me.wyndev.towerdefense.command;

import me.wyndev.towerdefense.Main;
import me.wyndev.towerdefense.Utils;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;

public class LobbyCommand extends Command {

    public LobbyCommand() {
        super("lobby", "hub");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof TowerDefensePlayer towerDefensePlayer)) return;
            towerDefensePlayer.sendMessage(Utils.format("<gray>Sending you to the lobby..."));
            if (!Main.gameManager.removePlayerFromGame(towerDefensePlayer)) {
                //Player just needs to be teleported since they aren't in a game
                if (!towerDefensePlayer.getInstance().equals(Main.mainLobby)) {
                    towerDefensePlayer.setInstance(Main.mainLobby);
                    return;
                }

                towerDefensePlayer.teleport(new Pos(0, -5, 0)); //TODO: change this to main lobby spawn pos at some point
            }
        });
    }
}
