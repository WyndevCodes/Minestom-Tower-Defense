package me.wyndev.towerdefense.tower;

import lombok.Getter;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemy;
import me.wyndev.towerdefense.files.config.object.TowerObject;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefenseTeam;
import me.wyndev.towerdefense.tower.attribute.MultiEntityTower;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Tower extends EntityCreature {
    @Getter protected final TowerObject type;
    @Getter protected final TowerDefenseTeam teamWhoSpawned;
    @Getter protected int towerLevel;
    protected List<TowerDefenseEnemy> targets = new ArrayList<>();

    private long lastAttackTime = System.currentTimeMillis();

    public Tower(@NotNull TowerObject type, @NotNull TowerDefenseTeam teamWhoSpawned, int towerLevel) {
        super(EntityType.fromNamespaceId(type.getEntityType()));
        this.type = type;
        this.teamWhoSpawned = teamWhoSpawned;
        this.towerLevel = towerLevel;
        initializeEntity(towerLevel);
    }

    /**
     * Ticks this tower.
     */
    public void tick() {
        if (System.currentTimeMillis() - lastAttackTime > type.getAttackSpeedFromLevel(towerLevel)) {
            lastAttackTime = System.currentTimeMillis();
            targets.clear();

            AtomicReference<TowerDefenseEnemy> target = new AtomicReference<>();
            getInstance().getEntities().forEach(e -> {
                if (e instanceof TowerDefenseEnemy towerDefenseEnemy && towerDefenseEnemy.getHealth() > 0) {
                    Pos tower = getPosition();
                    Pos enemy = e.getPosition();
                    double d2 = Math.pow((tower.x() - enemy.x()), 2) + Math.pow((tower.y() - enemy.y()), 2); //2D distance squared
                    double atk2 = (type.getAttackRangeFromLevel(towerLevel) * type.getAttackRangeFromLevel(towerLevel)); //attack range squared
                    if (d2 <= atk2) {
                        if (target.get() == null) {
                            target.set((TowerDefenseEnemy) e);
                        } else if (target.get().getTickAlive() * target.get().getEnemieObject().getSpeed() < towerDefenseEnemy.getTickAlive() * towerDefenseEnemy.getEnemieObject().getSpeed()) {
                            target.set((TowerDefenseEnemy) e);
                        }
                    }
                }
            });

            if (type.isSplash() && !targets.isEmpty()) {
                //Splash damage
                if (target.get() != null) lookAt(target.get()); else lookAt(targets.getFirst());
                for (TowerDefenseEnemy t : targets) {
                    t.damage(this, type.getAttackDamageFromLevel(towerLevel));
                }
            } else if (target.get() != null) {
                //Single target
                target.get().damage(this, type.getAttackDamageFromLevel(towerLevel));
                lookAt(target.get());
            }
        }
    }

    /**
     * Initializes this tower entity based on a level.
     * @param towerLevel The level of the tower to use
     *                   during initialization
     */
    public void initializeEntity(int towerLevel) {
        setItemInMainHand(ItemStack.builder(Material.fromNamespaceId(type.getItemInHandFromLevel(towerLevel -1))).build());
        String setName = type.getArmorFromLevel(towerLevel);
        switch (setName) {
            case "leather": {
                if (type.getArmorPieceFromID(0)) setHelmet(ItemStack.builder(Material.LEATHER_HELMET).build());
                if (type.getArmorPieceFromID(1)) setChestplate(ItemStack.builder(Material.LEATHER_CHESTPLATE).build());
                if (type.getArmorPieceFromID(2)) setLeggings(ItemStack.builder(Material.LEATHER_LEGGINGS).build());
                if (type.getArmorPieceFromID(3)) setBoots(ItemStack.builder(Material.LEATHER_BOOTS).build());
                break;
            }
            case "chainmail": {
                if (type.getArmorPieceFromID(0)) setHelmet(ItemStack.builder(Material.CHAINMAIL_HELMET).build());
                if (type.getArmorPieceFromID(1)) setChestplate(ItemStack.builder(Material.CHAINMAIL_CHESTPLATE).build());
                if (type.getArmorPieceFromID(2)) setLeggings(ItemStack.builder(Material.CHAINMAIL_LEGGINGS).build());
                if (type.getArmorPieceFromID(3)) setBoots(ItemStack.builder(Material.CHAINMAIL_BOOTS).build());
                break;
            }
            case "iron": {
                if (type.getArmorPieceFromID(0)) setHelmet(ItemStack.builder(Material.IRON_HELMET).build());
                if (type.getArmorPieceFromID(1)) setChestplate(ItemStack.builder(Material.IRON_CHESTPLATE).build());
                if (type.getArmorPieceFromID(2)) setLeggings(ItemStack.builder(Material.IRON_LEGGINGS).build());
                if (type.getArmorPieceFromID(3)) setBoots(ItemStack.builder(Material.IRON_BOOTS).build());
                break;
            }
            case "gold": {
                if (type.getArmorPieceFromID(0)) setHelmet(ItemStack.builder(Material.GOLDEN_HELMET).build());
                if (type.getArmorPieceFromID(1)) setChestplate(ItemStack.builder(Material.GOLDEN_CHESTPLATE).build());
                if (type.getArmorPieceFromID(2)) setLeggings(ItemStack.builder(Material.GOLDEN_LEGGINGS).build());
                if (type.getArmorPieceFromID(3)) setBoots(ItemStack.builder(Material.GOLDEN_BOOTS).build());
                break;
            }
            case "diamond": {
                if (type.getArmorPieceFromID(0)) setHelmet(ItemStack.builder(Material.DIAMOND_HELMET).build());
                if (type.getArmorPieceFromID(1)) setChestplate(ItemStack.builder(Material.DIAMOND_CHESTPLATE).build());
                if (type.getArmorPieceFromID(2)) setLeggings(ItemStack.builder(Material.DIAMOND_LEGGINGS).build());
                if (type.getArmorPieceFromID(3)) setBoots(ItemStack.builder(Material.DIAMOND_BOOTS).build());
                break;
            }
            case "netherite": {
                if (type.getArmorPieceFromID(0)) setHelmet(ItemStack.builder(Material.NETHERITE_HELMET).build());
                if (type.getArmorPieceFromID(1)) setChestplate(ItemStack.builder(Material.NETHERITE_CHESTPLATE).build());
                if (type.getArmorPieceFromID(2)) setLeggings(ItemStack.builder(Material.NETHERITE_LEGGINGS).build());
                if (type.getArmorPieceFromID(3)) setBoots(ItemStack.builder(Material.NETHERITE_BOOTS).build());
                break;
            }
            default: {
                setHelmet(ItemStack.builder(Material.AIR).build());
                setChestplate(ItemStack.builder(Material.AIR).build());
                setLeggings(ItemStack.builder(Material.AIR).build());
                setBoots(ItemStack.builder(Material.AIR).build());
            }
        }
    }

    /**
     * Attempts to upgrade this tower.
     */
    public void upgrade(TowerDefensePlayer playerWhoUpgraded) {
        // Check if tower can be upgraded
        if (towerLevel == type.getMaxLevel()) {
            playerWhoUpgraded.sendMessage(Component.text("This tower is already max level!"));
            return;
        }

        float upgradeCost = type.getPriceFromLevel(towerLevel + 1); //Since level start at one and array start a 0, there is no need to add 1

        // Check if player has enough money to upgrade
        if (teamWhoSpawned.getGold() < upgradeCost) {
            playerWhoUpgraded.sendMessage(Component.text("You do not have enough gold to upgrade this tower!").color(TextColor.color(255, 0, 0)));
            return;
        }

        // Remove gold
        //TODO: action bar with remove gold and upgraded tower message
        teamWhoSpawned.setGold(teamWhoSpawned.getGold() - Math.round(upgradeCost));

        // Upgrade tower
        towerLevel += 1;
        initializeEntity(towerLevel);
        //TODO: more text formatting, maybe add name of tower? Or move this to action bar
        playerWhoUpgraded.sendMessage(Component.text("Upgraded tower to level " + towerLevel + " !"));
    }

    /**
     * Sells this tower.
     */
    public void sell(TowerDefensePlayer playerWhoSold) {
        // Remove the entities in-game
        if (this instanceof MultiEntityTower multiEntityTower) {
            for (Entity entity : multiEntityTower.getTowerEntities()) {
                entity.remove();
            }
        }
        this.remove();

        // Calculate gold return
        double goldReturn = (type.getPriceFromLevel(towerLevel) * 0.8);

        // Send message and play sound (add more formatting later)
        playerWhoSold.sendMessage(Component.text("Sold tower for " + goldReturn + " gold!"));
        playerWhoSold.playPlayerSound(Key.key("entity.silverfish.death"));
        teamWhoSpawned.setGold(teamWhoSpawned.getGold() + Math.round(goldReturn));
        teamWhoSpawned.getCurrentPlacedTowers().remove(this);
    }
}

