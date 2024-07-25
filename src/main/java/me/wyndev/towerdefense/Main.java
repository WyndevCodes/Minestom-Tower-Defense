package me.wyndev.towerdefense;

import me.wyndev.towerdefense.files.config.Config;
import me.wyndev.towerdefense.game.GameInstance;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static Instance mainLobby;

    public static void main(String[] args) throws IOException {
        Config.read();

        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.getConnectionManager().setPlayerProvider((arg1, arg2, arg3) -> new TowerDefensePlayer(arg1, arg2, arg3, (byte) 1));

        // Register Events
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        //Generate a new minestom instance
        mainLobby = MinecraftServer.getInstanceManager().createInstanceContainer();
        mainLobby.setBlock(0, -5, 0, Block.STONE);

        //Handle player login in
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            e.setSpawningInstance(mainLobby);
        });

        // Skin handling for players that join (using Mojang auth for now)
        globalEventHandler.addListener(PlayerSkinInitEvent.class, event -> {
            PlayerSkin skin = PlayerSkin.fromUuid(event.getPlayer().getUuid().toString());
            event.setSkin(skin);
        });

        globalEventHandler.addListener(PlayerChatEvent.class, e -> {
            //TODO: change this to a command or an NPC that uses the GameManager class (supports queueing)
            if (e.getMessage().equals("start")) {
                ArrayList<Player> players = new ArrayList<>(mainLobby.getPlayers());
                me.wyndev.towerdefense.game.GameInstance gameInstance = new GameInstance(players);
                gameInstance.setup();
            }
        });

        MojangAuth.init();
        minecraftServer.start(Config.configData.getHostname(), Config.configData.getPort());
        log.info("Server started on address {} with port {}", Config.configData.getHostname(), Config.configData.getPort());
    }
}