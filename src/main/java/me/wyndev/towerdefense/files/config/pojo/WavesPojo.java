package me.wyndev.towerdefense.files.config.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemyType;
import me.wyndev.towerdefense.files.config.object.WaveObject;

@Data
@NoArgsConstructor
public class WavesPojo {
    WaveObject[] waves = new WaveObject[] {
            new WaveObject(TowerDefenseEnemyType.CHICKEN,
                    0,
                    -1,
                    20,
                    4,
                    1,
                    20
            ),
            new WaveObject(
                    TowerDefenseEnemyType.WOLF,
                    1,
                    -1,
                    15,
                    2,
                    0.5,
                    20
            ),
            new WaveObject(
                    TowerDefenseEnemyType.COW,
                    3,
                    -1,
                    40,
                    1,
                    0.5,
                    10
            ),
            new WaveObject(
                    TowerDefenseEnemyType.HORSE,
                    5,
                    -1,
                    40,
                    1,
                    0.25,
                    10
            ),
            new WaveObject(
                    TowerDefenseEnemyType.ZOMBIE,
                    8,
                    -1,
                    10,
                    3,
                    0.1,
                    30
            ),
            new WaveObject(
                    TowerDefenseEnemyType.MAD_COW,
                    10,
                    -1,
                    60,
                    1,
                    0.5,
                    5
            ),
    };
}
