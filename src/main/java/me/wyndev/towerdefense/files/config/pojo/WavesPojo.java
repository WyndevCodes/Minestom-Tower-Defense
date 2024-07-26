package me.wyndev.towerdefense.files.config.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemyType;
import me.wyndev.towerdefense.files.config.object.WaveObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class WavesPojo {
    WaveObject[] waves = new WaveObject[] {
            new WaveObject(TowerDefenseEnemyType.CHICKEN,
                    0,
                    -1,
                    1,
                    4,
                    1,
                    20
            ),
            new WaveObject(
                    TowerDefenseEnemyType.WOLF,
                    1,
                    -1,
                    2,
                    2,
                    0.5,
                    20
            )
    };
}
