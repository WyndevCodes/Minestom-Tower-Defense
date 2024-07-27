package me.wyndev.towerdefense.game.chestui;

import me.wyndev.towerdefense.player.IngameTowerDefensePlayer;
import me.wyndev.towerdefense.tower.Tower;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
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
import java.util.List;

public class ModifyTurret {
    //TODO: add the icon in the center of the screen and show all the stats change as lore
    List<EventListener> listeners = new ArrayList<>(); //I hate doing that, but I have no idea how to do this

    private final IngameTowerDefensePlayer player;
    private final Tower tower;

    public ModifyTurret(IngameTowerDefensePlayer player, Tower tower) {
        this.player = player;
        this.tower = tower;
    }

    public void open(Pos pos, Instance instance) {
        Inventory inventory = getInventory();
        player.getTowerDefensePlayer().openInventory(inventory);

        EventNode child = EventNode.all("EditMenuEventNode");
        instance.eventNode().addChild(child);

        EventListener<InventoryPreClickEvent> clickListener = EventListener.of(InventoryPreClickEvent.class, e -> {
            e.setCancelled(true);

            if (e.getSlot() == 0) {
                tower.sell();

                e.getPlayer().closeInventory();
                deleteChild(child);
            }

            if (e.getSlot() == 4) {
                tower.upgrade();

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
        Inventory inventory = new Inventory(InventoryType.HOPPER, Component.text("Tower shop").color(TextColor.color(0, 181, 5)));
        inventory.setItemStack(0, ItemStack.of(Material.REDSTONE_BLOCK).withCustomName(Component.text("Sell tower").color(TextColor.color(255, 0, 0))));
        inventory.setItemStack(4, ItemStack.of(Material.EMERALD_BLOCK).withCustomName(Component.text("Upgrade tower").color(TextColor.color(255, 0, 0))));
        return inventory;
    }
}
