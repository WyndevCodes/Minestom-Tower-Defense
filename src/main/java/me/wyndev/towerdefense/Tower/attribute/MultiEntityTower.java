package me.wyndev.towerdefense.Tower.attribute;

import net.minestom.server.entity.Entity;

import java.util.List;

/**
 * A tower that is made up of multiple entities.
 */
public interface MultiEntityTower {

    /**
     * @return A list of all entities, besides the base
     * entity, that make up this tower
     */
    List<Entity> getTowerEntities();

}
