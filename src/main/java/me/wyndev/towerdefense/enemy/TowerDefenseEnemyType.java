package me.wyndev.towerdefense.enemy;

import lombok.Getter;
import me.wyndev.towerdefense.ChatColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.entity.EntityType;

/**
 * A tower defense enemy type.
 */
@Getter
public enum TowerDefenseEnemyType {

    CHICKEN(EntityType.CHICKEN, "Chicken", ChatColor.GREEN.toColor(), 10, 0, 20, 0.1, 10),
    WOLF(EntityType.WOLF, "Wolf", ChatColor.GRAY.toColor(), 20, 0, 30, 0.2, 11),
    COW(EntityType.COW, "Cow", ChatColor.WHITE.toColor(), 100, 0, 100, 0.2, 12),
    HORSE(EntityType.HORSE, "Horse", ChatColor.YELLOW.toColor(), 250, 0, 500, 0.3, 13),
    ZOMBIE(EntityType.ZOMBIE, "Zombie", ChatColor.GREEN.toColor(), 500, 0, 2_500, 0.1, 14),
    MAD_COW(EntityType.MOOSHROOM, "Mad Cow", ChatColor.RED.toColor(), 1_000, 0, 5_000, 0.5, 15),
    ;

    private final EntityType entityType;
    private final String nameComponentText;
    private final TextColor nameColor;
    private final int cost;
    private final int incomeRequirement;
    private final int health;
    /**
     * How fast this entity moves along the tower defense track
     */
    private final double movementSpeed;
    private final int guiSlot;

    TowerDefenseEnemyType(EntityType entityType, String nameComponentText, TextColor nameColor, int cost, int incomeRequirement, int health,
                          double movementSpeed, int guiSlot) {
        this.entityType = entityType;
        this.nameComponentText = nameComponentText;
        this.cost = cost;
        this.incomeRequirement = incomeRequirement;
        this.nameColor = nameColor;
        this.health = health;
        this.movementSpeed = movementSpeed;
        this.guiSlot = guiSlot;
    }
}
