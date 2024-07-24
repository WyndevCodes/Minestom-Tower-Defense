package me.wyndev.towerdefense.tower;

import lombok.Getter;
import net.minestom.server.entity.EntityType;

/**
 * A tower type.
 */
@Getter
public enum TowerType {

    //TODO: give them a slot attribute for the gui (short 0-53)
    //TODO: give them an items stack icon
    //TODO: Give them a name and a description
    //TODO: Store all towerType in a list (List<TowerType>) -> see PlaceTurretMenu.java:36

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
