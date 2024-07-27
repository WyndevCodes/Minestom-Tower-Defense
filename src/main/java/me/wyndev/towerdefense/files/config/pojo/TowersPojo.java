package me.wyndev.towerdefense.files.config.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemyType;
import me.wyndev.towerdefense.files.config.object.TowerLevel;
import me.wyndev.towerdefense.files.config.object.TowerObject;
import me.wyndev.towerdefense.files.config.object.WaveObject;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class TowersPojo {
    TowerObject[] towers = new TowerObject[] {
            new TowerObject("<green>Skeleton",
                    "<white>Its a skeleton, what do you want me to say ?",
                    "minecraft:skeleton",
                    new TowerLevel[]{
                            new TowerLevel(10f, 1f, 1000f, 3, "leather", "minecraft:bow"),
                            new TowerLevel(50f, 2f, 900f, 4, "iron", "minecraft:bow"),
                            new TowerLevel(100f, 4f, 800f, 5, "gold", "minecraft:bow"),
                            new TowerLevel(200f, 8f, 700f, 6, "diamond", "minecraft:bow")
                    },
                    new boolean[]{false, true, true, true},
                    false,
                    4,
                    10,
                    "minecraft:bow")
    };
}
