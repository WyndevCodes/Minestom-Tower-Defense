package me.wyndev.towerdefense.tower;

import lombok.Getter;
import net.minestom.server.entity.EntityType;

/**
 * A tower type.
 */
@Getter
public enum TowerType {

    SKELETON(EntityType.SKELETON, 3, 5, 25)
    ;

    private final EntityType entityType;
    private final int maxLevel;
    private final int baseDamage;
    private final int goldCost;

    TowerType(EntityType entityType, int maxLevel, int baseDamage, int goldCost) {
        this.entityType = entityType;
        this.maxLevel = maxLevel;
        this.baseDamage = baseDamage;
        this.goldCost = goldCost;
    }

}
