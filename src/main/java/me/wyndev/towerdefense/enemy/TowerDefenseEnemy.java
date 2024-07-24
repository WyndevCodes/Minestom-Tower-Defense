package me.wyndev.towerdefense.enemy;

import me.wyndev.towerdefense.player.TowerDefensePlayer;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.attribute.Attribute;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract framework for enemies that can be summoned by a player
 * and appear on enemy player tower defense tracks.
 */
public abstract class TowerDefenseEnemy extends EntityCreature {

    protected final TowerDefenseEnemyType towerDefenseEnemyType;
    protected final TowerDefensePlayer playerWhoSpawned; //TODO: change to Team later, in case we support multiple teams

    public TowerDefenseEnemy(@NotNull TowerDefenseEnemyType towerDefenseEnemyType, @NotNull TowerDefensePlayer playerWhoSpawned) {
        super(towerDefenseEnemyType.getEntityType());
        this.towerDefenseEnemyType = towerDefenseEnemyType;
        this.playerWhoSpawned = playerWhoSpawned;

        // Initialize entity attributes
        this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(towerDefenseEnemyType.getMovementSpeed());
        this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(towerDefenseEnemyType.getHealth());
        this.setHealth(towerDefenseEnemyType.getHealth());

        //TODO: Setup entity pathfinding
        //For that, look at the arrow on the schematic, the mob could follow them.
        //This will make pathfinding a lot easier
    }

}
