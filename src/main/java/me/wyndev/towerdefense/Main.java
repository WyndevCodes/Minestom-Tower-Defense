package me.wyndev.towerdefense;

import me.wyndev.towerdefense.game.CustomEntity.Cursor;
import me.wyndev.towerdefense.game.GameInstance;
import me.wyndev.towerdefense.game.PlaceTurretMenu;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import net.hollowcube.schem.Rotation;
import net.hollowcube.schem.Schematic;
import net.hollowcube.schem.SchematicReader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.listener.ChatMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static Instance mainLobby;

    public static void main(String[] args) {
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
           if (e.getMessage().equals("start")) {
               ArrayList<Player> players = new ArrayList<>();
               players.addAll(mainLobby.getPlayers());
               GameInstance gameInstance = new GameInstance(players);
               gameInstance.start();
           }
        });

        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);
    }
}