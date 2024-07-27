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

    CHICKEN(EntityType.CHICKEN, "Chicken", ChatColor.GREEN.toColor(), 10, 20, 0.1),
    WOLF(EntityType.WOLF, "Wolfs", ChatColor.GRAY.toColor(), 20, 30, 0.2);

    private final EntityType entityType;
    private final String nameComponentText;
    private final TextColor nameColor;
    private final int cost;
    private final int health;
    /**
     * How fast this entity moves along the tower defense track
     */
    private final double movementSpeed;

    TowerDefenseEnemyType(EntityType entityType, String nameComponentText, TextColor nameColor, int cost, int health, double movementSpeed) {
        this.entityType = entityType;
        this.nameComponentText = nameComponentText;
        this.cost = cost;
        this.nameColor = nameColor;
        this.health = health;
        this.movementSpeed = movementSpeed;
    }
}
