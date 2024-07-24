package me.wyndev.towerdefense.enemy.base;

import me.wyndev.towerdefense.enemy.TowerDefenseEnemy;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemyType;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * A chicken enemy with an enemy type of {@link TowerDefenseEnemyType#CHICKEN}.
 */
public class ChickenEnemy extends TowerDefenseEnemy {

    public ChickenEnemy(@NotNull TowerDefensePlayer playerWhoSpawned) {
        super(TowerDefenseEnemyType.CHICKEN, playerWhoSpawned);
    }

}
