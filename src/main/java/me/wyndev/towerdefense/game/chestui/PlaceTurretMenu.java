package me.wyndev.towerdefense.game.chestui;

import me.wyndev.towerdefense.Utils;
import me.wyndev.towerdefense.files.config.Towers;
import me.wyndev.towerdefense.files.config.object.TowerObject;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefenseTeam;
import me.wyndev.towerdefense.tower.Tower;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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

    private final TowerDefenseTeam team;

    public PlaceTurretMenu(TowerDefenseTeam team) {
        this.team = team;
    }

    public void open(TowerDefensePlayer player, Pos pos, Instance instance) {
        Inventory inventory = getInventory();
        player.openInventory(inventory);

        EventNode child = EventNode.all("BuyMenuEventNode");
        instance.eventNode().addChild(child);

        EventListener<InventoryPreClickEvent> clickListener = EventListener.of(InventoryPreClickEvent.class, e -> {
            e.setCancelled(true);
            TowerObject towerObj = Towers.getFromUISlot(e.getSlot());
            if (towerObj != null) {
                //pay cost
                int level = Towers.getLevelFromUISlot(e.getSlot());
                float price = towerObj.getPriceFromLevel(level);
                if (team.getGold() < price) {
                    player.sendMessage(Utils.format("<red>Not enough gold to purchase this!"));
                    player.playPlayerSound(Key.key("entity.villager.no"));
                    return;
                }

                team.setGold(team.getGold() - Math.round(price));
                player.sendActionBar(Utils.format("<gray>Purchased a level " + level + " " + towerObj.getName() +
                        " <gray> tower for <gold>" + Math.round(price) + " <gray>gold!"));
                player.playPlayerSound(Key.key("entity.experience_orb.pickup"));

                Tower tower = new Tower(towerObj, team, level);
                Pos spawnPos = pos.add(new Pos(0.5, 1, 0.5));

                tower.setInstance(e.getInstance(), spawnPos);
                team.getCurrentPlacedTowers().add(tower);

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
                Component desc = Utils.formatWithoutItalics(tower.getDesc());
                Component blank = Component.empty();
                Component type = Utils.formatWithoutItalics("<color:#828282><u>Type:</u></color> " + (tower.isSplash() ? "<red><bold>Splash" : "<yellow><bold>Single Target"));
                Component line1 = Utils.formatWithoutItalics("<color:#828282><u>Price:</u></color><gold> " + Utils.formatWithCommas(tower.getPriceFromLevel(i)) + " Gold");
                Component line2 = Utils.formatWithoutItalics("<color:#828282><u>Damage:</u></color><white> " + tower.getAttackDamageFromLevel(i));
                Component line3 = Utils.formatWithoutItalics("<color:#828282><u>Range:</u></color><white> " + tower.getAttackRangeFromLevel(i) + " blocks");
                Component line4 = Utils.formatWithoutItalics("<color:#828282><u>Attack cooldown:</u></color><white> " + tower.getAttackSpeedFromLevel(i) / 1000 + "s/attack");
                ItemStack stack = ItemStack.of(
                                Material.fromNamespaceId(tower.getIconMaterials()))
                        .withCustomName(Utils.formatWithoutItalics(tower.getName() + " <dark_gray>(Lvl. " + i + ")"))
                        .withLore(desc, blank, type, line1, line2, line3, line4)
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
