package me.wyndev.towerdefense.Enemy;

import me.wyndev.towerdefense.ChatColor;
import me.wyndev.towerdefense.Player.TowerDefensePlayer;
import me.wyndev.towerdefense.Tower.Tower;
import me.wyndev.towerdefense.Utils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.damage.Damage;
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

        this.setCustomNameVisible(true);
        this.setCustomName(getCustomNameText());

        //TODO: Setup entity pathfinding

        // I had planned to do a set of points as well. We'd have to do some sort of algorithm, or we could just save the turns in a config file
    }

    /**
     * Damages this TowerDefenseEnemy.
     * @param source The source of the damage
     * @param damage The damage amount
     */
    public void damage(Tower source, float damage) {
        this.damage(Damage.fromEntity(source, damage));
        this.setCustomName(getCustomNameText());
    }

    // I feel like this can be optimized or condensed. Any ideas?
    private Component getCustomNameText() {
        Component nameText = Component.text(towerDefenseEnemyType.getNameComponentText() + " ", towerDefenseEnemyType.getNameColor());
        nameText = nameText.append(Component.text(Utils.formatWithCommas(getHealth()), ChatColor.RED.toColor()));
        nameText = nameText.append(Component.text("/", ChatColor.GRAY.toColor()));
        nameText = nameText.append(Component.text(Utils.formatWithCommas(towerDefenseEnemyType.getHealth()), ChatColor.RED.toColor()));
        return nameText;
    }

}
