package me.wyndev.towerdefense.Enemy.base;

import me.wyndev.towerdefense.Enemy.TowerDefenseEnemy;
import me.wyndev.towerdefense.Enemy.TowerDefenseEnemyType;
import me.wyndev.towerdefense.Player.TowerDefensePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * A chicken enemy with an enemy type of {@link TowerDefenseEnemyType#CHICKEN}.
 */
public class ChickenEnemy extends TowerDefenseEnemy {

    public ChickenEnemy(@NotNull TowerDefensePlayer playerWhoSpawned) {
        super(TowerDefenseEnemyType.CHICKEN, playerWhoSpawned);
    }

}
