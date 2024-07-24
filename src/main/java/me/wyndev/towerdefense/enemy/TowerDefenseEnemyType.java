package me.wyndev.towerdefense.enemy;

import net.minestom.server.entity.EntityType;

/**
 * A tower defense enemy type.
 */
public enum TowerDefenseEnemyType {

    CHICKEN(EntityType.CHICKEN, "<green>Chicken", 20, 0.1f)
    ;

    private final EntityType entityType;
    private final String nameComponentText;
    private final int health;
    private final float movementSpeed;

    TowerDefenseEnemyType(EntityType entityType, String nameComponentText, int health, float movementSpeed) {
        this.entityType = entityType;
        this.nameComponentText = nameComponentText;
        this.health = health;
        this.movementSpeed = movementSpeed;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getNameComponentText() {
        return nameComponentText;
    }

    public int getHealth() {
        return health;
    }

    /**
     * @return How fast this entity moves along the tower defense track
     */
    public float getMovementSpeed() {
        return movementSpeed;
    }
}
