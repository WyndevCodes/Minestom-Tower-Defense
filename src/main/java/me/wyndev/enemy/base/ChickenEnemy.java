package me.wyndev.enemy.base;

import me.wyndev.enemy.TowerDefenseEnemy;
import me.wyndev.enemy.TowerDefenseEnemyType;
import me.wyndev.player.TowerDefensePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * A chicken enemy with an enemy type of {@link TowerDefenseEnemyType#CHICKEN}.
 */
public class ChickenEnemy extends TowerDefenseEnemy {

    public ChickenEnemy(@NotNull TowerDefensePlayer playerWhoSpawned) {
        super(TowerDefenseEnemyType.CHICKEN, playerWhoSpawned);
    }

}
