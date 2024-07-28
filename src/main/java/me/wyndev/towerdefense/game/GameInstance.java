package me.wyndev.towerdefense.game;

import lombok.Getter;
import me.wyndev.towerdefense.ChatColor;
import me.wyndev.towerdefense.Main;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemy;
import me.wyndev.towerdefense.files.maps.Maps;
import me.wyndev.towerdefense.game.chestui.ModifyTurret;
import me.wyndev.towerdefense.game.chestui.PlaceTurretMenu;
import me.wyndev.towerdefense.game.customentity.Cursor;
import me.wyndev.towerdefense.player.IngameTowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
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
import java.util.function.UnaryOperator;

/**
 * Class that represents an active tower defense game.
 */
@SuppressWarnings("UnstableApiUsage")
public class GameInstance {

    private final int MAX_PLAYERS = 6; //TODO: support for per-map based player counts?
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

    // Player information
    /**
     * A list of all {@link TowerDefensePlayer}s currently in this game.
     */
    private final @Getter List<TowerDefensePlayer> players;
    /**
     * A map of all {@link IngameTowerDefensePlayer}s associated with
     * players currently in this game.
     */
    private final @Getter Map<UUID, IngameTowerDefensePlayer> ingamePlayers;

    /**
     * Creates a new GameInstance with no default players.
     */
    public GameInstance() {
        // Create instance here to keep it as a final, non-changing variable (because it is)
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();

        gameLoop = new GameLoop(this);
        players = new ArrayList<>();
        ingamePlayers = new HashMap<>();
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
        ingamePlayers = new HashMap<>();


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
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                instance.loadChunk(x, y);
            }
        }
        SchematicReader reader = new SchematicReader();
        Schematic map = reader.read(Maps.getRandomMap());
        map.build(Rotation.NONE, UnaryOperator.identity()).apply(instance, () -> {
            log.info("Schematic built");
        });
        schemSize = map.size();

        //Execute on player join
        instance.eventNode().addListener(PlayerSpawnEvent.class, event -> {
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            event.getPlayer().setAllowFlying(true);

            // Clear inventory (just in case)
            event.getPlayer().getInventory().clear();
            event.getPlayer().addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 1, -1));

            //TODO: remove this, this is for testing purposes only
            this.gameState = GameState.COUNTDOWN;
            gameLoop.startCountdown(); //default 30 seconds for now
        });

        //Remove player on quit
        instance.eventNode().addListener(PlayerDisconnectEvent.class, event -> Main.gameManager.removePlayerFromGame((TowerDefensePlayer) event.getPlayer(), true));
    }

    /**
     * Starts the game of Tower Defense, setting up
     * every queued player in the game.
     */
    public void start() {
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


        new WavesManager().startWave(this, schemSpawnTranslation);

        this.gameState = GameState.RUNNING;

        for (TowerDefensePlayer player : players) {
            ingamePlayers.put(player.getUuid(), new IngameTowerDefensePlayer(player));

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

                IngameTowerDefensePlayer towerDefensePlayer = ingamePlayers.get(event.getPlayer().getUuid());
                if (towerDefensePlayer == null) return;

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
                IngameTowerDefensePlayer towerDefensePlayer = ingamePlayers.get(event.getPlayer().getUuid());
                if (towerDefensePlayer == null) throw new IllegalStateException("A player in the tower defense game does not have an associated tower defense player wrapper!");
                Tower tower = towerAt(Pos.fromPoint(block));
                if (tower != null) {
                    if (tower.getPlayerWhoSpawned().equals(towerDefensePlayer)) {
                        new ModifyTurret(towerDefensePlayer, tower).open(Pos.fromPoint(block), instance);
                    } else {
                        //Player cannot open another player's tower menu
                    }
                } else {
                    new PlaceTurretMenu(towerDefensePlayer).open(new Pos(block.x(), block.y(), block.z()), event.getInstance());
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

        ingamePlayers.forEach((uuid, towerPlayer) -> towerPlayer.shutdown());
        players.forEach(p -> Main.gameManager.removePlayerFromGame(p));

        MinecraftServer.getInstanceManager().unregisterInstance(instance);

        players.clear();
        ingamePlayers.clear();
    }

    /**
     * Gets the tower at a position
     * @param pos The position to check
     * @return The tower if found, otherwise null if there is no tower at the position
     */
    public @Nullable Tower towerAt(Pos pos) {
        List<Tower> towers = new ArrayList<>();
        for (IngameTowerDefensePlayer player1 : ingamePlayers.values()) {
            towers.addAll(player1.getCurrentPlacedTowers());
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

}
