package me.wyndev.towerdefense;

import me.lucko.luckperms.common.config.generic.adapter.EnvironmentVariableConfigAdapter;
import me.lucko.luckperms.common.config.generic.adapter.MultiConfigurationAdapter;
import me.lucko.luckperms.minestom.LuckPermsMinestom;
import me.lucko.luckperms.minestom.context.defaults.GameModeContextProvider;
import me.wyndev.towerdefense.files.config.Config;
import me.wyndev.towerdefense.files.maps.Maps;
import me.wyndev.towerdefense.game.GameInstance;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import net.luckperms.api.LuckPerms;
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
import net.minestom.server.timer.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static Instance mainLobby;
    public static SchedulerManager scheduler;

    public static void main(String[] args) throws IOException {
        Config.read();
        Maps.load();

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

        scheduler = MinecraftServer.getSchedulerManager();

        MojangAuth.init();
        minecraftServer.start(Config.configData.getHostname(), Config.configData.getPort());
        log.info("Server started on address {} with port {}", Config.configData.getHostname(), Config.configData.getPort());
    }

    private void setupLuckPerms() {
        Path directory = Path.of("luckperms");
        LuckPerms luckPerms = LuckPermsMinestom.builder(directory)
                .commands(true) // enables registration of LuckPerms commands
                .contextProvider(new GameModeContextProvider()) // provide additional custom contexts
                .configurationAdapter(plugin -> new MultiConfigurationAdapter(plugin, // define the configuration
                        new EnvironmentVariableConfigAdapter(plugin) // use MultiConfigurationAdapter to load from multiple sources, in order
                )).permissionSuggestions("test.permission", "test.other") // add permission suggestions for commands and the web editor
                .dependencyManager(true) // automatically download and classload dependencies
                .enable();
    }
}