package me.wyndev.towerdefense;

import me.wyndev.towerdefense.enemy.TowerDefenseEnemyType;

import java.util.ArrayList;
import java.util.List;

public class Registry {
    public static final List<TowerDefenseEnemyType> enemyTypes = new ArrayList<>();
    static {
        enemyTypes.add(TowerDefenseEnemyType.CHICKEN);
    }
}
