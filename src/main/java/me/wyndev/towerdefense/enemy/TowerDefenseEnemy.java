package me.wyndev.towerdefense.enemy;

import lombok.Getter;
import me.wyndev.towerdefense.ChatColor;
import me.wyndev.towerdefense.player.IngameTowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.tower.Tower;
import me.wyndev.towerdefense.Utils;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.network.packet.server.play.EntityVelocityPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Abstract framework for enemies that can be summoned by a player
 * and appear on enemy player tower defense tracks.
 */
@Getter
public class TowerDefenseEnemy extends EntityCreature {

    protected final TowerDefenseEnemyType towerDefenseEnemyType;
    private final IngameTowerDefensePlayer spawner;
    private int tickAlive = 0;

    //TODO: link a mob to a team if we do team support
    public TowerDefenseEnemy(@NotNull TowerDefenseEnemyType towerDefenseEnemyType, @Nullable IngameTowerDefensePlayer spawner) {
        super(towerDefenseEnemyType.getEntityType());
        this.towerDefenseEnemyType = towerDefenseEnemyType;
        this.spawner = spawner; //player who spawned the tower defense enemy

        // Initialize entity attributes
        this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(towerDefenseEnemyType.getMovementSpeed());
        this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(towerDefenseEnemyType.getHealth());
        this.setHealth(towerDefenseEnemyType.getHealth());

        this.setCustomNameVisible(true);
        this.setCustomName(getCustomNameText());
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

    /**
     * Actions this enemy takes when reaching the end of a tower defense track.
     * @param player The player to damage (the player who owns the track)
     */
    public void reachEnd(IngameTowerDefensePlayer player) {
        this.remove();
        //TODO: double lifesteal for advanced
        //TODO: logic on damaged player
        spawner.setHealth(spawner.getHealth() + 1);
        spawner.setIncome(spawner.getIncome() + (towerDefenseEnemyType.getCost() / 5));
        //TODO: user feedback? Sound/message?
    }

    // I feel like this can be optimized or condensed. Any ideas?
    private Component getCustomNameText() {
        Component nameText = Component.text(towerDefenseEnemyType.getNameComponentText() + " ", towerDefenseEnemyType.getNameColor());
        nameText = nameText.append(Component.text(Utils.formatWithCommas(getHealth()), ChatColor.RED.toColor()));
        nameText = nameText.append(Component.text("/", ChatColor.GRAY.toColor()));
        nameText = nameText.append(Component.text(Utils.formatWithCommas(towerDefenseEnemyType.getHealth()), ChatColor.RED.toColor()));
        return nameText;
    }

    /**
     * Ticks this enemy, incrementing its ticks alive
     * and sending animation packets to all players
     * in the tower defense game.
     * @param towerDefensePlayers The list of players
     *                            whom the entity animation
     *                            should be displayed to
     */
    public void tick(List<TowerDefensePlayer> towerDefensePlayers) {
        tickAlive++;
        for (TowerDefensePlayer player : towerDefensePlayers) {
            Vec dir = position.direction();
            if (!dir.isZero()) dir = dir.normalize();

            Vec walk = dir.mul(5 * towerDefenseEnemyType.getMovementSpeed());
            player.sendPacket(new EntityVelocityPacket(this.getEntityId(), (short) walk.x(), (short) walk.y(), (short) walk.z()));
        }
    }
}
