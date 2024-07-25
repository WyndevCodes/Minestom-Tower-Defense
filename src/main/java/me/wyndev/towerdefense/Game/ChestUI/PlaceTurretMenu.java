package me.wyndev.towerdefense.Game.ChestUI;

import me.wyndev.towerdefense.Player.IngameTowerDefensePlayer;
import me.wyndev.towerdefense.Tower.Tower;
import me.wyndev.towerdefense.Tower.base.SkeletonTower;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.ArrayList;
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

            //TODO: Replace this with a for loop of every TowerType to find which match
            if (e.getSlot() == 10) {
                Tower tower = new SkeletonTower(player);
                Pos spawnPos = pos.add(new Pos(0.5, 1, 0.5));

                tower.setInstance(e.getInstance(), spawnPos);
                player.getCurrentPlacedTowers().put(spawnPos, tower);

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
        inventory.setItemStack(10, ItemStack.of(Material.STONE).withCustomName(Component.text("Skeleton tower").color(TextColor.color(123, 123, 123))));
        return inventory;
    }
}
