package me.wyndev.towerdefense;

import me.wyndev.towerdefense.game.CustomEntity.Cursor;
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
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.UnaryOperator;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.getConnectionManager().setPlayerProvider((arg1, arg2, arg3) -> new TowerDefensePlayer(arg1, arg2, arg3, (byte) 1));

        // Register Events
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        //Generate a new minestom instance
        InstanceContainer container = MinecraftServer.getInstanceManager().createInstanceContainer();
        container.setBlock(0, -5, 0, Block.STONE);

        //Handle player login in
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            e.setSpawningInstance(container);
            e.getPlayer().setGameMode(GameMode.CREATIVE);
            //Load chunk to spawn schematic
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    container.loadChunk(x, y);
                }
            }
            SchematicReader reader = new SchematicReader();
            //todo: replace that with IOUtils in the future + load them from a folder outside of the jar
            Schematic map = reader.read(Main.class.getResourceAsStream("/map.schem"));
            map.build(Rotation.NONE, UnaryOperator.identity()).apply(container, () -> {
                log.info("Schematic built");
            });
        });

        //Player spawn event
        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            event.getPlayer().setItemInMainHand(ItemStack.of(Material.ENDER_EYE)
                    .withCustomName(Component.text("Buy turret").color(TextColor.color(0, 145, 73)))
            );
        });

        //Player move event
        globalEventHandler.addListener(PlayerMoveEvent.class, event -> {
            Point block = event.getPlayer().getTargetBlockPosition(10);
            if (block != null && container.getBlock(block).name().equals("minecraft:grass_block") && event.getPlayer().getItemInMainHand().material().equals(Material.ENDER_EYE)) {
                if (Cursor.cursorHashMap.containsKey(event.getPlayer())) {
                    Cursor entity = Cursor.cursorHashMap.get(event.getPlayer());
                    entity.teleport(new Pos(block.x(), block.y(), block.blockZ()));
                    Cursor.cursorHashMap.get(event.getPlayer()).getEntityMeta().setHasGlowingEffect(true);
                } else {
                    Cursor entity = new Cursor(event.getPlayer());
                    entity.setInstance(container, new Pos(block.x() -0.05, block.y() -0.05, block.blockZ() -0.05));
                }
            } else {
                if (Cursor.cursorHashMap.containsKey(event.getPlayer())) {
                    Cursor.cursorHashMap.get(event.getPlayer()).remove();
                    Cursor.cursorHashMap.remove(event.getPlayer());
                }
            }
        });

        //Detect the player's click
        globalEventHandler.addListener(PlayerUseItemEvent.class, event -> {
            Point block = event.getPlayer().getTargetBlockPosition(10);
            if (block == null) return;
            if (event.getItemStack().material().equals(Material.ENDER_EYE) && container.getBlock(block).name().equals("minecraft:grass_block")) {
                event.getPlayer().sendMessage("Open a menu to buy tower");
                new PlaceTurretMenu().open(event.getPlayer(), new Pos(block.x(), block.y(), block.z()), event.getInstance());
            }
        });

        // Skin handling for players that join (using Mojang auth for now)
        globalEventHandler.addListener(PlayerSkinInitEvent.class, event -> {
            PlayerSkin skin = PlayerSkin.fromUuid(event.getPlayer().getUuid().toString());
            event.setSkin(skin);
        });

        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);
    }
}