package me.wyndev.towerdefense.files.config.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemyType;
import me.wyndev.towerdefense.files.config.object.TowerObject;
import me.wyndev.towerdefense.files.config.object.WaveObject;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class TowersPojo {
    TowerObject[] towers = new TowerObject[] {
            new TowerObject("Skeleton",
                    "Its a skeleton, what do you want me to say ?",
                    "minecraft:skeleton",
                    new Float[]{10f, 50f, 100f, 200f},
                    new Float[]{1f, 2f, 4f, 8f},
                    new Float[]{1000f, 900f, 800f, 700f},
                    false,
                    4,
                    10,
                    "minecraft:bow"
                    )
    };
}