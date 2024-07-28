package me.wyndev.towerdefense.game.chestui;

import me.wyndev.towerdefense.Utils;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefenseTeam;
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
import java.util.List;

public class ModifyTurret {
    //TODO: add the icon in the center of the screen and show all the stats change as lore
    List<EventListener> listeners = new ArrayList<>(); //I hate doing that, but I have no idea how to do this

    private final TowerDefensePlayer player;
    private final Tower tower;

    public ModifyTurret(TowerDefensePlayer player, Tower tower) {
        this.player = player;
        this.tower = tower;
    }

    public void open(Pos pos, Instance instance) {
        Inventory inventory = getInventory(tower);
        player.openInventory(inventory);

        EventNode child = EventNode.all("EditMenuEventNode");
        instance.eventNode().addChild(child);

        EventListener<InventoryPreClickEvent> clickListener = EventListener.of(InventoryPreClickEvent.class, e -> {
            e.setCancelled(true);

            if (e.getSlot() == 0) {
                tower.sell(player);

                e.getPlayer().closeInventory();
                deleteChild(child);
            }

            if (e.getSlot() == 4) {
                tower.upgrade(player);

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

    public Inventory getInventory(Tower tower) {
        //TODO: Generate tower items from TowerType.java values
        Inventory inventory = new Inventory(InventoryType.HOPPER, Component.text("Tower editor").color(TextColor.color(172, 179, 0)));

        //Sell lore
        Component sellLore = MiniMessage.miniMessage().deserialize("<color:#828282><u>Refund:</u></color><white> " + tower.getType().getPriceFromLevel(tower.getTowerLevel()) * 0.8);

        //Upgrade lore
        Component upgradeLore;
        if (tower.getType().getMaxLevel() != tower.getTowerLevel()) {
            upgradeLore = MiniMessage.miniMessage().deserialize("<color:#828282><u>Cost:</u></color><white> " + tower.getType().getPriceFromLevel(tower.getTowerLevel() + 1));
        } else {
            upgradeLore = MiniMessage.miniMessage().deserialize("<red>Max level !");
        }

        //Tower component
        Component line1;
        Component line2;
        Component line3;
        Component line4;

        if (tower.getType().getMaxLevel() != tower.getTowerLevel()) {
            line1 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Damage:</u></color><white> " + tower.getType().getAttackDamageFromLevel(tower.getTowerLevel()) + "<dark_gray> -> <white>" + tower.getType().getAttackDamageFromLevel(tower.getTowerLevel() + 1));
            line2 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Range:</u></color><white> " + tower.getType().getAttackRangeFromLevel(tower.getTowerLevel()) + " blocks<dark_gray> -> <white>" + tower.getType().getAttackRangeFromLevel(tower.getTowerLevel() + 1) + " blocks");
            line3 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Attack cooldown:</u></color><white> " + tower.getType().getAttackSpeedFromLevel(tower.getTowerLevel()) / 1000 + "s <dark_gray> -> <white>" + tower.getType().getAttackSpeedFromLevel(tower.getTowerLevel() + 1) / 1000 + "s");
            line4 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Splash:</u></color><white> " + tower.getType().isSplash());
        } else {
            line1 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Damage:</u></color><white> " + tower.getType().getAttackDamageFromLevel(tower.getTowerLevel()));
            line2 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Range:</u></color><white> " + tower.getType().getAttackRangeFromLevel(tower.getTowerLevel()) + " blocks");
            line3 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Attack cooldown:</u></color><white> " + tower.getType().getAttackSpeedFromLevel(tower.getTowerLevel()) / 1000);
            line4 = MiniMessage.miniMessage().deserialize("<color:#828282><u>Splash:</u></color><white> " + tower.getType().isSplash());

        }

        inventory.setItemStack(0, ItemStack.of(Material.REDSTONE_BLOCK).withCustomName(Component.text("Sell tower").color(TextColor.color(255, 0, 0))).withLore(sellLore));
        inventory.setItemStack(4, ItemStack.of(Material.EMERALD_BLOCK).withCustomName(Component.text("Upgrade tower").color(TextColor.color(0, 255, 27))).withLore(upgradeLore));
        inventory.setItemStack(2, ItemStack.of(Material.fromNamespaceId(tower.getType().getIconMaterials())).withCustomName(Utils.format(tower.getType().getName())).withAmount(tower.getTowerLevel()).withMaxStackSize(128).withLore(line1, line2, line3, line4));

        for (int i = 0; i < UICommon.HopperDelimiter.length; i++) {
            if (UICommon.HopperDelimiter[i] == 1) {
                inventory.setItemStack(i, UICommon.delimiter);
            }
        }
        return inventory;
    }
}
