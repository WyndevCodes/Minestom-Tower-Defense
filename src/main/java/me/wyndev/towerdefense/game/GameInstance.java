package me.wyndev.towerdefense.game;

import lombok.Getter;
import me.wyndev.towerdefense.ChatColor;
import me.wyndev.towerdefense.Main;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemy;
import me.wyndev.towerdefense.files.config.Teams;
import me.wyndev.towerdefense.files.maps.Maps;
import me.wyndev.towerdefense.game.chestui.ModifyTurret;
import me.wyndev.towerdefense.game.chestui.PlaceTurretMenu;
import me.wyndev.towerdefense.game.customentity.Cursor;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefenseTeam;
import me.wyndev.towerdefense.tower.Tower;
import net.hollowcube.schem.Rotation;
import net.hollowcube.schem.Schematic;
import net.hollowcube.schem.SchematicReader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Class that represents an active tower defense game.
 */
@SuppressWarnings("UnstableApiUsage")
public class GameInstance {

    private final int MAX_TEAM_COUNT = 6; //TODO: add that in the constructor to allow for bigger game
    private final int PLAYER_PER_TEAM = 1; //TODO: add that in the constructor to allow for bigger game
    private final int MAX_PLAYERS = MAX_TEAM_COUNT * PLAYER_PER_TEAM;
    private static final Logger log = LoggerFactory.getLogger(GameInstance.class);

    // Game information
    /**
     * The current {@link GameState} of this game
     */
    private final GameLoop gameLoop;
    private @Getter GameState gameState = GameState.WAITING_FOR_PLAYERS;

    // Map information
    //TODO: multiple maps with different schematics and player counts?

    @Getter private final Instance instance;
    @Getter private final List<TowerDefenseEnemy> enemies = Collections.synchronizedList(new ArrayList<>());
    @Getter private Pos schemSpawnTranslation;
    @Getter private Point schemSize;
    @Getter private List<TowerDefenseTeam> teams = new ArrayList<>();

    // Player information
    /**
     * A list of all {@link TowerDefensePlayer}s currently in this game.
     */
    private final @Getter List<TowerDefensePlayer> players;

    /**
     * Creates a new GameInstance with no default players.
     */
    public GameInstance() {
        // Create instance here to keep it as a final, non-changing variable (because it is)
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        gameLoop = new GameLoop(this);
        players = new ArrayList<>();
    }

    /**
     * Creates a game instance with more than 0 initial players.
     * @param initialPlayers The initial players to add to this GameInstance
     */
    public GameInstance(List<Player> initialPlayers) {
        // Create instance here to keep it as a final, non-changing variable (because it is)
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();

        gameLoop = new GameLoop(this);
        players = new ArrayList<>();


        for (Player player : initialPlayers) {
            addPlayer((TowerDefensePlayer) player);
        }
    }

    /**
     * This will start a new instance of GameInstance,
     * then it will set up the environment for a game.
     * */
    public void setup() {
        //Load chunk to spawn schematic
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                instance.loadChunk(x, y);
            }
        }

        //Load the map
        SchematicReader reader = new SchematicReader();
        Schematic map = reader.read(Maps.getRandomMap());
        int z = 0;
        int x = 0;
        int sizeX = map.size().blockX() + 5;
        int sizeZ = map.size().blockZ() + 5;
        for (int i = 0; i < MAX_TEAM_COUNT; i++) { //TODO: replace that with max team
            map.build(Rotation.NONE, true).apply(instance, new Pos(x * sizeX, 0, z * sizeZ), () -> {
                log.info("Schematic built");
            });
            x++;
            if (x >= 3) {
                z++;
                x = 0;
            }
        }

        //Create every teams
        if (MAX_TEAM_COUNT <= Teams.teamsData.getTeams().length) {
            for (int i = 0; i < MAX_TEAM_COUNT; i++) {
                teams.add(new TowerDefenseTeam(Teams.teamsData.getTeams()[i]));
            }
        } else {
            throw new IllegalStateException("Error while creating teams:", new Exception("MAX_TEAM_COUNT cannot be higher than the number of team registered in the config!"));
        }

        schemSize = map.size().add(5, 0, 5);

        for (int i = 0; i < players.size(); i++) {

        }

        //Execute on player join
        instance.eventNode().addListener(PlayerSpawnEvent.class, event -> {
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            event.getPlayer().setAllowFlying(true);

            // Clear inventory (just in case)
            event.getPlayer().getInventory().clear();
            event.getPlayer().addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 1, -1, Potion.BLEND_FLAG));

            //TODO: remove this, this is for testing purposes only
            this.gameState = GameState.COUNTDOWN;
            gameLoop.startCountdown(); //default 30 seconds for now

            for (TowerDefenseTeam t : teams) {
                if (!(t.getTowerDefensePlayers().size() >= PLAYER_PER_TEAM)) { //Don't replace that, idea is stupid
                    t.addPlayer((TowerDefensePlayer) event.getPlayer());
                    event.getPlayer().sendMessage(Component.text("You joined the " + t.getTeamObject().displayName + " team").color(TextColor.color(t.getTeamObject().color.toTextColor())));
                    break;
                }
            }
        });

        //Remove player on quit
        instance.eventNode().addListener(PlayerDisconnectEvent.class, event -> Main.gameManager.removePlayerFromGame((TowerDefensePlayer) event.getPlayer(), true));
        //Remove player on world change
        instance.eventNode().addListener(RemoveEntityFromInstanceEvent.class, event -> {
            if (event.getEntity() instanceof TowerDefensePlayer player && players.contains(player) && event.getInstance().equals(instance)) {
                Main.gameManager.removePlayerFromGame(player);
            }
        });
    }

    /**
     * Starts the game of Tower Defense, setting up
     * every queued player in the game.
     */
    public void start() {
        List<TowerDefenseTeam> emptyTeams = new ArrayList<>();
        for (TowerDefenseTeam team : teams) {
            if (team.getTowerDefensePlayers().isEmpty()) {
                emptyTeams.remove(team);
            }
        }

        emptyTeams.forEach(t -> teams.remove(t));

        for (int x = 0; x < schemSize.blockX(); x++) {
            for (int y = 0; y < schemSize.blockX(); y++) {
                for (int z = 0; z < schemSize.blockZ(); z++) {
                    if (instance.getBlock(x, y, z).name().equals("minecraft:red_glazed_terracotta")) {
                        this.schemSpawnTranslation = new Pos(x, y, z);
                        break;
                    }
                    if (this.schemSpawnTranslation != null) break;
                }
                if (this.schemSpawnTranslation != null) break;
            }
            if (this.schemSpawnTranslation != null) break;
        }
        int z = 0;
        int x = 0;
        int sizeX = schemSize.blockX();
        int sizeZ = schemSize.blockZ();
        for (int i = 0; i < MAX_TEAM_COUNT; i++) { //TODO: replace that with max team
            if (!teams.get(i).getTowerDefensePlayers().isEmpty()) {
                Pos mapPos = new Pos(x * sizeX, 0, z * sizeZ);
                teams.get(i).teleport(mapPos.add(schemSpawnTranslation));
                new WavesManager().startWave(this, mapPos.add(schemSpawnTranslation), teams.get(i)); //TODO: assign a team to waveManager to allow for life drain
                x++;
                if (x >= 3) {
                    z++;
                    x = 0;
                }
            }
        }

        this.gameState = GameState.RUNNING;

        for (TowerDefensePlayer player : players) {
            //TODO: Replace this item with a "Tower manager" item that also allow for deleting and upgrading turret
            player.setItemInMainHand(ItemStack.of(Material.ENDER_EYE)
                    .withCustomName(Component.text("Buy turret").color(TextColor.color(0, 145, 73)))
            );

            //TODO: assign plot, etc. (send message is already in GameLoop.java)
        }

        //---- Add game listeners ----
        //Player move event
        instance.eventNode().addListener(PlayerMoveEvent.class, event -> {
            Point block = event.getPlayer().getTargetBlockPosition(10);
            if (block != null && instance.getBlock(block).name().equals("minecraft:grass_block") && event.getPlayer().getItemInMainHand().material().equals(Material.ENDER_EYE)) {
                Cursor entity;
                if (Cursor.cursorHashMap.containsKey(event.getPlayer())) {
                    entity = Cursor.cursorHashMap.get(event.getPlayer());
                    entity.teleport(new Pos(block.x(), block.y(), block.blockZ()));
                } else {
                    entity = new Cursor(event.getPlayer());
                    entity.setInstance(instance, new Pos(block.x() -0.05, block.y() -0.05, block.blockZ() -0.05));
                }

                // Check if player has a turret placed, and if so, change block type because tower space is not available
                if (towerAt(Pos.fromPoint(block)) != null) {
                    entity.getMeta().setBlockState(Block.EMERALD_BLOCK);
                    return;
                }

                entity.getMeta().setBlockState(entity.getDefaultBlockTexture());
            } else {
                if (Cursor.cursorHashMap.containsKey(event.getPlayer())) {
                    Cursor.cursorHashMap.get(event.getPlayer()).remove();
                    Cursor.cursorHashMap.remove(event.getPlayer());
                }
            }
        });

        //Detect the player's click
        instance.eventNode().addListener(PlayerUseItemEvent.class, event -> {
            Point block = event.getPlayer().getTargetBlockPosition(10);
            if (block == null) return;
            if (event.getItemStack().material().equals(Material.ENDER_EYE) && instance.getBlock(block).name().equals("minecraft:grass_block")) {
                TowerDefenseTeam team = teamFromPlayer((TowerDefensePlayer) event.getPlayer());
                if (team == null) throw new IllegalStateException("A player in the tower defense game does not have an associated tower defense team!");
                Tower tower = towerAt(Pos.fromPoint(block));
                if (tower != null) {
                    if (tower.getTeamWhoSpawned().equals(tower)) {
                        new ModifyTurret((TowerDefensePlayer) event.getPlayer(), tower).open(Pos.fromPoint(block), instance);
                    } else {
                        //Player cannot open another player's tower menu
                    }
                } else {
                    new PlaceTurretMenu(team).open((TowerDefensePlayer) event.getPlayer(), new Pos(block.x(), block.y(), block.z()), event.getInstance());
                }
            }
        });

        // Start main game loop
        gameLoop.startMainLoop();
    }

    /**
     * Attempts to add a player to this tower defense game.
     * @param player The player to add
     * @return True if the player was successfully added,
     * false otherwise (such as if the game is full)
     */
    public boolean addPlayer(TowerDefensePlayer player) {
        if (players.size() >= MAX_PLAYERS) {
            player.sendMessage(Component.text("The game is full!").color(ChatColor.RED.toColor()));
            return false;
        }

        players.add(player);


        // Add player to map world
        player.setInstance(instance);
        Main.hubSidebar.removeViewer(player);

        // Send join message
        for (TowerDefensePlayer playerInGame : players) {
            //TODO: color + format with rank (if we hook into luckperms)
            playerInGame.sendMessage(Component.text(player.getUsername() + " joined! (" + players.size() + "/" + MAX_PLAYERS + ")"));
        }

        // Check for start threshold
        if (players.size() >= (MAX_PLAYERS * 0.8)) {
            // Start game countdown
            this.gameState = GameState.COUNTDOWN;
            gameLoop.startCountdown(); //default 30 seconds for now
        }

        return true;
    }

    /**
     * Removes a player from this tower defense game if they
     * are a part of the game.
     * @param player The player to remove
     */
    public void removePlayer(TowerDefensePlayer player) {
        boolean wasRemoved = players.remove(player);

        //TODO: game logic determining auto-shutdown, cancel countdown, etc.
        if (players.size() < (MAX_PLAYERS * 0.8) && gameState == GameState.COUNTDOWN) {
            // Cancel countdown
            gameLoop.cancelCountdown();
            gameState = GameState.WAITING_FOR_PLAYERS;
        }

        if (wasRemoved && gameState == GameState.RUNNING) {
            for (TowerDefensePlayer playerInGame : players) {
                //send quit message
                //TODO: color + format with rank (if we hook into luckperms)
                playerInGame.sendMessage(Component.text(player.getUsername() + " left the game."));
            }
            if (players.size() < 2) {
                //game cannot run anymore with less than 2 people playing
                end();
            }
        }
    }

    /**
     * This will delete the GameInstance and send every player to the hub
     * */
    public void end() {
        gameState = GameState.ENDED;
        gameLoop.stopMainLoop();
        gameLoop.cancelCountdown(); //just in case

        teams.forEach(TowerDefenseTeam::dispose);
        players.forEach(p -> Main.gameManager.removePlayerFromGame(p));

        MinecraftServer.getInstanceManager().unregisterInstance(instance);

        players.clear();
        teams.clear();
    }

    /**
     * Gets the tower at a position
     * @param pos The position to check
     * @return The tower if found, otherwise null if there is no tower at the position
     */
    public @Nullable Tower towerAt(Pos pos) {
        List<Tower> towers = new ArrayList<>();
        for (TowerDefenseTeam towerDefenseTeam : teams) {
            towers.addAll(towerDefenseTeam.getCurrentPlacedTowers());
        }

        // Check if there is a turret at the position
        // Is there any way to make this more efficient? I'm not sure if Pos.java has a hashCode, so I'm not using HashMap#containsKey
        for (Tower check : towers) {
            if (check.getPosition().samePoint(pos.blockX() + 0.5, pos.blockY() + 1, pos.blockZ() + 0.5)) {
                return check;
            }
        }
        return null;
    }

    public @Nullable TowerDefenseTeam teamFromPlayer(TowerDefensePlayer player) {
        for (TowerDefenseTeam t : teams) {
            if (t.getTowerDefensePlayers().contains(player)) {
                return t;
            }
        }
        return null;
    }

}
