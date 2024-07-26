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

    CHICKEN(EntityType.CHICKEN, "Chicken", ChatColor.GREEN.toColor(), 20, 0.1),
    WOLF(EntityType.WOLF, "Wolfs", ChatColor.GRAY.toColor(), 30, 0.2);

    private final EntityType entityType;
    private final String nameComponentText;
    private final TextColor nameColor;
    private final int health;
    /**
     * How fast this entity moves along the tower defense track
     */
    private final double movementSpeed;

    TowerDefenseEnemyType(EntityType entityType, String nameComponentText, TextColor nameColor, int health, double movementSpeed) {
        this.entityType = entityType;
        this.nameComponentText = nameComponentText;
        this.nameColor = nameColor;
        this.health = health;
        this.movementSpeed = movementSpeed;
    }
}
