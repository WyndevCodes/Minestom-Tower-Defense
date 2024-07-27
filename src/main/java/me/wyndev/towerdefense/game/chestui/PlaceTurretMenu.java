package me.wyndev.towerdefense.game.chestui;

import me.wyndev.towerdefense.files.config.Towers;
import me.wyndev.towerdefense.files.config.object.TowerObject;
import me.wyndev.towerdefense.files.config.pojo.TowersPojo;
import me.wyndev.towerdefense.player.IngameTowerDefensePlayer;
import me.wyndev.towerdefense.tower.Tower;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceTurretMenu {

    List<EventListener> listeners = new ArrayList<>(); //I hate doing that, but I have no idea how to do this

    private final IngameTowerDefensePlayer player;

    public PlaceTurretMenu(IngameTowerDefensePlayer player) {
        this.player = player;
    }

    public void open(Pos pos, Instance instance) {
        Inventory inventory = getInventory();
        player.getTowerDefensePlayer().openInventory(inventory);

        EventNode child = EventNode.all("BuyMenuEventNode");
        instance.eventNode().addChild(child);

        EventListener<InventoryPreClickEvent> clickListener = EventListener.of(InventoryPreClickEvent.class, e -> {
            e.setCancelled(true);
            if (Towers.getFromUISlot(e.getSlot()) != null) {
                Tower tower = new Tower(Towers.getFromUISlot(e.getSlot()),  player, Towers.getLevelFromUISlot(e.getSlot()));
                Pos spawnPos = pos.add(new Pos(0.5, 1, 0.5));

                tower.setInstance(e.getInstance(), spawnPos);
                player.getCurrentPlacedTowers().add(tower);

                e.getPlayer().closeInventory();
                deleteChild(child);
            }
        });

        EventListener<InventoryCloseEvent> closeMenuListener = EventListener.of(InventoryCloseEvent.class, e -> {
            deleteChild(child);
        });

        listeners.add(clickListener);
        listeners.add(closeMenuListener);

        child.addListener(clickListener);
        child.addListener(closeMenuListener);
    }

    public void deleteChild(EventNode<?> node) {
        listeners.forEach(l -> {
            node.removeListener(l);
        });
    }

    public Inventory getInventory() {
        //TODO: Generate tower items from TowerType.java values
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, Component.text("Tower shop").color(TextColor.color(0, 181, 5)));
        List<TowerObject> towers = Arrays.asList(Towers.towerData.getTowers());
        for (TowerObject tower : towers) {
            for (int i = 1; i <= tower.getMaxLevel(); i++) {
                Component desc = MiniMessage.miniMessage().deserialize(tower.getDesc());
                Component line1 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Price:</u></color><white> " + tower.getPriceFromLevel(i));
                Component line2 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Damage:</u></color><white> " + tower.getAttackDamageFromLevel(i));
                Component line3 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Range:</u></color><white> " + tower.getAttackRangeFromLevel(i) + " blocks");
                Component line4 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Attack cooldown:</u></color><white> " + tower.getAttackSpeedFromLevel(i) / 1000 + "s");
                Component line5 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Splash:</u></color><white> " + tower.isSplash());
                ItemStack stack = ItemStack.of(
                                Material.fromNamespaceId(tower.getIconMaterials()))
                        .withCustomName(MiniMessage.miniMessage().deserialize(tower.getName()))
                        .withLore(desc, line1, line2, line3, line4, line5)
                        .withMaxStackSize(128).withAmount(i);
                inventory.setItemStack(tower.getGUIPosLevel(i), stack);
            }
        }

        for (int i = 0; i < UICommon.ChestBorder.length; i++) {
            if (UICommon.ChestBorder[i] == 1) {
                inventory.setItemStack(i, UICommon.border);
            }
        }

        return inventory;
    }
}
