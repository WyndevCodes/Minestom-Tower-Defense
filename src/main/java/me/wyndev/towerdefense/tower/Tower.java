package me.wyndev.towerdefense.tower;

import lombok.Getter;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemy;
import me.wyndev.towerdefense.files.config.object.TowerObject;
import me.wyndev.towerdefense.player.IngameTowerDefensePlayer;
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
    protected final TowerObject type;
    protected final @Getter IngameTowerDefensePlayer playerWhoSpawned; //TODO: change to Team later, in case we support multiple teams
    protected int towerLevel;
    protected List<TowerDefenseEnemy> targets = new ArrayList<>();

    private long lastAttackTime = System.currentTimeMillis();

    public Tower(@NotNull TowerObject type, @NotNull IngameTowerDefensePlayer playerWhoSpawned, int towerLevel) {
        super(EntityType.fromNamespaceId(type.getEntityType()));
        this.type = type;
        this.playerWhoSpawned = playerWhoSpawned;
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
            //d=√((x2 – x1)² + (y2 – y1)²) <- distance between two 2d points
            AtomicReference<TowerDefenseEnemy> target = new AtomicReference<>();
            getInstance().getEntities().forEach(e -> {
                if (e instanceof TowerDefenseEnemy) {
                    Pos tower = getPosition();
                    Pos enemy = e.getPosition();
                    double d = Math.sqrt(Math.pow((tower.x() - enemy.x()), 2) + Math.pow((tower.y() - enemy.y()), 2));
                    if (d <= type.getAttackRangeFromLevel(towerLevel) && ((TowerDefenseEnemy) e).getHealth() > 0) {
                        if (target.get() == null) {
                            target.set((TowerDefenseEnemy) e);
                        } else if (target.get().getTickAlive() * target.get().getTowerDefenseEnemyType().getMovementSpeed() < ((TowerDefenseEnemy) e).getTickAlive() * ((TowerDefenseEnemy) e).getTowerDefenseEnemyType().getMovementSpeed()) {
                            target.set((TowerDefenseEnemy) e);
                        }
                    }
                }
            });
            if (target.get() != null) {
                lookAt(target.get());
                target.get().damage(this, type.getAttackDamageFromLevel(towerLevel));
            }
            //TODO: find all enemies in x radius function
            // then setup list accordingly if the type does splash damage or not
        }

        if (!targets.isEmpty()) {
            lookAt(targets.getFirst());
        }
    }

    /**
     * Initializes this tower entity based on a level.
     * @param towerLevel The level of the tower to use
     *                   during initialization
     */
    public void initializeEntity(int towerLevel) {
        setItemInMainHand(ItemStack.builder(Material.fromNamespaceId(type.getItemInHandFromLevel(towerLevel))).build());
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
    public void upgrade() {
        // Check if tower can be upgraded
        if (towerLevel == type.getMaxLevel()) {
            playerWhoSpawned.getTowerDefensePlayer().sendMessage(Component.text("This tower is already max level!"));
            return;
        }

        float upgradeCost = type.getPriceFromLevel(towerLevel); //Since level start at one and array start a 0, there is no need to add 1

        // Check if player has enough money to upgrade
        if (playerWhoSpawned.getGold() < upgradeCost) {
            playerWhoSpawned.getTowerDefensePlayer().sendMessage(Component.text("You do not have enough gold to upgrade this tower!").color(TextColor.color(255, 0, 0)));
            return;
        }

        // Remove gold
        //TODO: action bar with remove gold and upgraded tower message
        playerWhoSpawned.setGold(playerWhoSpawned.getGold() - upgradeCost);

        // Upgrade tower
        towerLevel += 1;
        initializeEntity(towerLevel);
        //TODO: more text formatting, maybe add name of tower? Or move this to action bar
        playerWhoSpawned.getTowerDefensePlayer().sendMessage(Component.text("Upgraded tower to level " + towerLevel + " !"));
    }

    /**
     * Sells this tower.
     */
    public void sell() {
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
        playerWhoSpawned.getTowerDefensePlayer().sendMessage(Component.text("Sold tower for " + goldReturn + " gold!"));
        playerWhoSpawned.getTowerDefensePlayer().playPlayerSound(Key.key("entity.silverfish.death"));
        playerWhoSpawned.setGold(playerWhoSpawned.getGold() + goldReturn);
        playerWhoSpawned.getCurrentPlacedTowers().remove(this);
    }
}

