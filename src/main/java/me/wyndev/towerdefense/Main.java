package me.wyndev.towerdefense;

import me.lucko.luckperms.common.config.generic.adapter.EnvironmentVariableConfigAdapter;
import me.lucko.luckperms.common.config.generic.adapter.MultiConfigurationAdapter;
import me.lucko.luckperms.minestom.CommandRegistry;
import me.lucko.luckperms.minestom.LuckPermsMinestom;
import me.wyndev.towerdefense.command.LobbyCommand;
import me.wyndev.towerdefense.command.PlayCommand;
import me.wyndev.towerdefense.files.config.Config;
import me.wyndev.towerdefense.files.config.Enemies;
import me.wyndev.towerdefense.files.config.Towers;
import me.wyndev.towerdefense.files.config.Waves;
import me.wyndev.towerdefense.files.maps.Maps;
import me.wyndev.towerdefense.game.GameManager;
import me.wyndev.towerdefense.npc.JoinGameNPC;
import me.wyndev.towerdefense.npc.NPCManager;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.sidebar.HubSidebar;
import net.luckperms.api.LuckPerms;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.utils.mojang.MojangUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static Instance mainLobby;
    public static SchedulerManager scheduler;
    public static LuckPerms luckPerms;

    public static HubSidebar hubSidebar;

    public static GameManager gameManager;

    public static NPCManager npcManager;

    public static void main(String[] args) {
        loadConfig();

        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.getConnectionManager().setPlayerProvider((arg1, arg2, arg3) -> new TowerDefensePlayer(arg1, arg2, arg3, (byte) 1));

        //Dependency loading
        setupLuckPerms();

        //Register Events
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        //Generate a new minestom instance
        mainLobby = MinecraftServer.getInstanceManager().createInstanceContainer();
        mainLobby.setBlock(0, -5, 0, Block.STONE);

        //Setup hub sidebar
        hubSidebar = new HubSidebar();

        //Setup games
        gameManager = new GameManager();

        //NPCs
        npcManager = new NPCManager();
        npcManager.spawnNPC(new JoinGameNPC(), mainLobby, new Pos(0, -5, 1));

        //Setup commands
        MinecraftServer.getCommandManager().register(new PlayCommand());
        MinecraftServer.getCommandManager().register(new LobbyCommand());

        //Handle player login
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            e.setSpawningInstance(mainLobby);
        });
        globalEventHandler.addListener(PlayerSpawnEvent.class, e -> {
            if (e.isFirstSpawn()) {
                //player just joined
                hubSidebar.addViewer(e.getPlayer());
            }
            hubSidebar.updatePlayerCount(mainLobby.getPlayers().size());
        });

        //Handle player leave
        globalEventHandler.addListener(PlayerDisconnectEvent.class, e -> {
            hubSidebar.removeViewer(e.getPlayer());
        });

        // Skin handling for players that join (using Mojang auth for now)
        globalEventHandler.addListener(PlayerSkinInitEvent.class, event -> {
            try {
                UUID mojangUUID = MojangUtils.getUUID(event.getPlayer().getUsername());
                PlayerSkin skin = PlayerSkin.fromUuid(mojangUUID.toString());
                event.setSkin(skin);
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("Player {}'s skin failed to load!", event.getPlayer().getUsername());
            }
        });

        globalEventHandler.addListener(PlayerChatEvent.class, e -> {
            //TODO: change this to a command or an NPC that uses the GameManager class (supports queueing)
            if (e.getMessage().equals("start")) {
                ArrayList<Player> players = new ArrayList<>(mainLobby.getPlayers());
                players.forEach(player -> gameManager.addPlayerToGame((TowerDefensePlayer) player));
            }
        });

        scheduler = MinecraftServer.getSchedulerManager();

        minecraftServer.start(Config.configData.getHostname(), Config.configData.getPort());
        log.info("Server started on address {} with port {}", Config.configData.getHostname(), Config.configData.getPort());
    }

    private static void setupLuckPerms() {
        Path directory = Path.of("luckperms");
        luckPerms = LuckPermsMinestom.builder(directory)
                .commandRegistry(CommandRegistry.minestom()) // enables registration of LuckPerms commands
                .configurationAdapter(plugin -> new MultiConfigurationAdapter(plugin, // define the configuration
                        new EnvironmentVariableConfigAdapter(plugin) // use MultiConfigurationAdapter to load from multiple sources, in order
                )).permissionSuggestions("test.permission", "test.other") // add permission suggestions for commands and the web editor
                .dependencyManager(true) // automatically download and classload dependencies
                .enable();
    }

    public static void loadConfig() {
        Config.read();
        log.info("Loaded config.yml");
        Waves.read();
        log.info("Registered {} waves entries", Waves.waveData.getWaves().length);
        Towers.read();
        log.info("Registered {} towers entries", Towers.towerData.getTowers().length);
        Enemies.read();
        log.info("Registered {} enemy entries", Enemies.enemiesData.getEnemies().length);
        Maps.load();
        log.info("Registered {} maps", Maps.getMaps().size());
    }
}