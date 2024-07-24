package me.wyndev.towerdefense.game;

import me.wyndev.towerdefense.tower.TowerType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
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

    public void open(Player player, Pos pos, Instance instance) {
        Inventory inventory = getInventory();
        player.openInventory(inventory);

        EventNode child = EventNode.all("BuyMenuEventNode");
        instance.eventNode().addChild(child);

        EventListener clickListener = EventListener.of(InventoryPreClickEvent.class, e -> {
            //TODO: Replace this with a for loop of every TowerType to find which match
            InventoryPreClickEvent event = (InventoryPreClickEvent) e;
            if (event.getSlot() == 10) {
                Entity entity = new Entity(TowerType.SKELETON.getEntityType());
                entity.setInstance(event.getInstance(), pos.add(new Pos(0.5, 1, 0.5)));
                event.setCancelled(true);
                event.getPlayer().closeInventory();
                deleteChild(child);
            }
        });

        EventListener closeMenuListener = EventListener.of(InventoryCloseEvent.class, e -> {
            deleteChild(child);
        });

        listeners.add(clickListener);
        listeners.add(closeMenuListener);

        child.addListener(clickListener);
        child.addListener(closeMenuListener);
    }

    public void deleteChild(EventNode node) {
        listeners.forEach(l -> {
            node.removeListener(l);
        });
    }

    public Inventory getInventory() {
        //TODO: Generate that from
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, Component.text("Tower shop").color(TextColor.color(0, 181, 5)));
        inventory.setItemStack(10, ItemStack.of(Material.STONE).withCustomName(Component.text("Skeleton tower").color(TextColor.color(123, 123, 123))));
        return inventory;
    }
}
