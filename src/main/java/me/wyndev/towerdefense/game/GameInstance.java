package me.wyndev.towerdefense.game;

import lombok.Data;
import me.wyndev.towerdefense.Main;
import me.wyndev.towerdefense.game.ChestUI.PlaceTurretMenu;
import me.wyndev.towerdefense.game.CustomEntity.Cursor;
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
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.UnaryOperator;

@Data
public class GameInstance {
    private static final Logger log = LoggerFactory.getLogger(GameInstance.class);
    private Instance instance;
    public final List<Player> players;

    /**
     * This will start a new instance of GameInstance,
     * then it will set up the environment for a game
     * and teleport every player
     * */
    public void start() {
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();

        //Load chunk to spawn schematic
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                instance.loadChunk(x, y);
            }
        }
        SchematicReader reader = new SchematicReader();
        //todo: replace that with IOUtils in the future + load them from a folder outside of the jar
        Schematic map = reader.read(Main.class.getResourceAsStream("/map.schem"));
        map.build(Rotation.NONE, UnaryOperator.identity()).apply(instance, () -> {
            log.info("Schematic built");
        });

        players.forEach(p -> {
            p.setInstance(instance);
        });

        //Execute on player join
        instance.eventNode().addListener(PlayerSpawnEvent.class, event -> {
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            event.getPlayer().setAllowFlying(true);

            //TODO: Replace this item with a "Tower manager" item that also allow for deleting and upgrading turret
            event.getPlayer().setItemInMainHand(ItemStack.of(Material.ENDER_EYE)
                    .withCustomName(Component.text("Buy turret").color(TextColor.color(0, 145, 73)))
            );
        });

        //Player move event
        instance.eventNode().addListener(PlayerMoveEvent.class, event -> {
            Point block = event.getPlayer().getTargetBlockPosition(10);
            if (block != null && instance.getBlock(block).name().equals("minecraft:grass_block") && event.getPlayer().getItemInMainHand().material().equals(Material.ENDER_EYE)) {
                if (Cursor.cursorHashMap.containsKey(event.getPlayer())) {
                    Cursor entity = Cursor.cursorHashMap.get(event.getPlayer());
                    entity.teleport(new Pos(block.x(), block.y(), block.blockZ()));
                    Cursor.cursorHashMap.get(event.getPlayer()).getEntityMeta().setHasGlowingEffect(true);
                } else {
                    Cursor entity = new Cursor(event.getPlayer());
                    entity.setInstance(instance, new Pos(block.x() -0.05, block.y() -0.05, block.blockZ() -0.05));
                }
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
                event.getPlayer().sendMessage("Open a menu to buy tower");
                new PlaceTurretMenu().open(event.getPlayer(), new Pos(block.x(), block.y(), block.z()), event.getInstance());
            }
        });
    }

    /**
     * This will delete the GameInstance and send every player to the hub
     * */
    public void end() {
        players.forEach(p -> {
            p.setInstance(Main.mainLobby);
        });
        MinecraftServer.getInstanceManager().unregisterInstance(instance);
    }

}
