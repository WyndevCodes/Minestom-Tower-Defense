package me.wyndev.towerdefense;

import me.wyndev.towerdefense.enemy.TowerDefenseEnemyType;
import me.wyndev.towerdefense.tower.TowerType;

import java.util.ArrayList;
import java.util.List;

public class Registry {
    //This may seam stupid but I need it

    public static final List<TowerType> towersTypes = new ArrayList<>();
    static {
        towersTypes.add(TowerType.SKELETON);
    }

    public static final List<TowerDefenseEnemyType> enemyTypes = new ArrayList<>();
    static {
        enemyTypes.add(TowerDefenseEnemyType.CHICKEN);
    }
}
