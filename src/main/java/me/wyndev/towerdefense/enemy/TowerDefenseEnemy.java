package me.wyndev.towerdefense.enemy;

import lombok.Getter;
import me.wyndev.towerdefense.ChatColor;
import me.wyndev.towerdefense.Utils;
import me.wyndev.towerdefense.files.config.object.EnemieObject;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefenseTeam;
import me.wyndev.towerdefense.tower.Tower;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.network.packet.server.play.EntityVelocityPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

/**
 * Abstract framework for enemies that can be summoned by a player
 * and appear on enemy player tower defense tracks.
 */
@Getter
public class TowerDefenseEnemy extends EntityCreature {

    protected final EnemieObject enemieObject;
    private final TowerDefenseTeam spawner;
    private int tickAlive = 0;
    @Getter private Pos shift;

    //TODO: link a mob to a team if we do team support
    public TowerDefenseEnemy(@NotNull EnemieObject enemieObject, @Nullable TowerDefenseTeam spawner) {
        super(EntityType.fromNamespaceId(enemieObject.getModelName()));
        this.enemieObject = enemieObject;
        this.spawner = spawner; //team who spawned the tower defense enemy

        double maxShift = enemieObject.getMaxShift(); //Add this to the config when enemies are ported to configs
        Random random = new Random();
        shift = new Pos(random.nextDouble(-maxShift, maxShift), 0, random.nextDouble(-maxShift, maxShift));

        // Initialize entity attributes
        this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(enemieObject.getSpeed());
        this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(enemieObject.getMaxHealth());
        this.setHealth((float) enemieObject.getMaxHealth());

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
        if (this.getHealth() <= 0) {
            source.getTeamWhoSpawned().setGold(source.getTeamWhoSpawned().getGold() + (enemieObject.getCost() / 10));
            //TODO: we really need to move the money system to doubles
        }
    }

    /**
     * Actions this enemy takes when reaching the end of a tower defense track.
     * @param team The team to damage (the team who owns the track)
     */
    public void reachEnd(TowerDefenseTeam team) {
        this.remove();
        //TODO: double lifesteal for advanced
        //TODO: logic on damaged player
        if (spawner != null) {
            spawner.setHealth(spawner.getHealth() + 1);
            spawner.setIncome(spawner.getIncome() + (enemieObject.getCost() / 5));
            //TODO: user feedback? Sound/message?
        }
    }

    // I feel like this can be optimized or condensed. Any ideas?
    private Component getCustomNameText() {
        Component nameText = Utils.format("<green>" + enemieObject.getName());
        nameText = nameText.append(Component.text(Utils.formatWithCommas(getHealth()), ChatColor.RED.toColor()));
        nameText = nameText.append(Component.text("/", ChatColor.GRAY.toColor()));
        nameText = nameText.append(Component.text(Utils.formatWithCommas(enemieObject.getMaxHealth()), ChatColor.RED.toColor()));
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

            Vec walk = dir.mul(5 * enemieObject.getSpeed());
            player.sendPacket(new EntityVelocityPacket(this.getEntityId(), (short) walk.x(), (short) walk.y(), (short) walk.z()));
        }
    }
}
