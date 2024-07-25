package me.wyndev.towerdefense.tower;

import lombok.Getter;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemy;
import me.wyndev.towerdefense.player.IngameTowerDefensePlayer;
import me.wyndev.towerdefense.tower.attribute.MultiEntityTower;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract framework for towers that are placed to defend
 * a player's base during a tower defense game.
 */
public abstract class Tower extends EntityCreature {
    protected final TowerType type;
    protected final @Getter IngameTowerDefensePlayer playerWhoSpawned; //TODO: change to Team later, in case we support multiple teams
    protected int towerLevel;
    protected List<TowerDefenseEnemy> targets = new ArrayList<>();

    private long lastAttackTime = System.currentTimeMillis();

    public Tower(@NotNull TowerType type, @NotNull IngameTowerDefensePlayer playerWhoSpawned, int towerLevel) {
        super(type.getEntityType());
        this.type = type;
        this.playerWhoSpawned = playerWhoSpawned;
        this.towerLevel = towerLevel;
        initializeEntity(towerLevel);
    }

    /**
     * Ticks this tower.
     */
    public void tick() {
        if (System.currentTimeMillis() - lastAttackTime > getAttackSpeed(towerLevel)) {
            lastAttackTime = System.currentTimeMillis();
            targets.clear();
            //target search and then attack
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
    public abstract void initializeEntity(int towerLevel);

    /**
     * Gets the attack speed of this tower based on its level.
     * @param towerLevel The current level of the tower
     * @return The attack speed of the tower based on its
     * level, in the time unit of milliseconds between attacks
     */
    public abstract long getAttackSpeed(int towerLevel);

    /**
     * Gets the attack range of this tower based on its level.
     * @param towerLevel The current level of the tower
     * @return The attack range of the tower based on its
     * level, in blocks
     */
    public abstract int getAttackRange(int towerLevel);

    /**
     * Attempts to upgrade this tower.
     */
    public void upgrade() {
        int upgradeCost = type.getGoldCost() * TowerUpgradeMultiplier.getMultiplierForLevel(towerLevel + 1);

        // Check if player has enough money to upgrade
        if (playerWhoSpawned.getGold() < upgradeCost) {
            playerWhoSpawned.getTowerDefensePlayer().sendMessage(Component.text("You do not have enough gold to upgrade this tower!").color(TextColor.color(255, 0, 0)));
            return;
        }

        // Check if tower can be upgraded
        if (towerLevel == type.getMaxLevel()) {
            playerWhoSpawned.getTowerDefensePlayer().sendMessage(Component.text("This tower is already max level!"));
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
        int goldReturn = (int)(type.getGoldCost() * 0.2) * TowerUpgradeMultiplier.getMultiplierForLevel(towerLevel);

        // Send message and play sound (add more formatting later)
        playerWhoSpawned.getTowerDefensePlayer().sendMessage(Component.text("Sold tower for " + goldReturn + " gold!"));
        playerWhoSpawned.getTowerDefensePlayer().playPlayerSound(Key.key("entity.silverfish.death"));
        playerWhoSpawned.setGold(playerWhoSpawned.getGold() + goldReturn);
        playerWhoSpawned.getCurrentPlacedTowers().remove(this);
    }

    /**
     * Tower upgrade cost multipliers.
     */
    private enum TowerUpgradeMultiplier {
        LEVEL_1(1, 1),
        LEVEL_2(2, 20),
        LEVEL_3(3, 100),
        LEVEL_4(4, 250);

        /**
         * The level of the tower to apply this multiplier to
         */
        private final int level;
        /**
         * The multiplier that should be used when calculating the cost
         * to upgrade to its associated level
         */
        private final int multiplier;

        TowerUpgradeMultiplier(int level, int multiplier) {
            this.level = level;
            this.multiplier = multiplier;
        }

        /**
         * Gets the upgrade cost multiplier for an upgrade level.
         * @param level The level to get the upgrade cost multiplier for
         * @return The upgrade cost multiplier for the level, if found,
         * otherwise the upgrade cost for {@link TowerUpgradeMultiplier#LEVEL_4}
         */
        public static int getMultiplierForLevel(int level) {
            for (TowerUpgradeMultiplier m : values()) {
                if (m.level == level) return m.multiplier;
            }
            return LEVEL_4.multiplier; //DEFAULT
        }
    }
}

