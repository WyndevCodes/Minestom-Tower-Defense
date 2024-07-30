package me.wyndev.towerdefense.files.config.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.wyndev.towerdefense.files.config.object.EnemieObject;

@Data
@NoArgsConstructor
public class EnemiesPojo {
    EnemieObject[] enemies = new EnemieObject[] {
            new EnemieObject("Chicken",
                    false,
                    "minecraft:chicken",
                    "minecraft:chicken_spawn_egg",
                    "none",
                    1,
                    3,
                    0.1,
                    5,
                    2,
                    10,
                    0
                    ),
            new EnemieObject("Wolf",
                    false,
                    "minecraft:wolf",
                    "minecraft:wolf_spawn_egg",
                    "none",
                    1,
                    10,
                    0.2,
                    20,
                    6,
                    11,
                    3
            ),
            new EnemieObject("Cow",
                    false,
                    "minecraft:cow",
                    "minecraft:cow_spawn_egg",
                    "none",
                    1,
                    20,
                    0.4,
                    100,
                    0.5,
                    12,
                    5
            ),
            new EnemieObject("Horse",
                    false,
                    "minecraft:horse",
                    "minecraft:horse_spawn_egg",
                    "none",
                    0.5,
                    50,
                    1,
                    50,
                    25,
                    13,
                    20
            ),
            new EnemieObject("Zombie",
                    false,
                    "minecraft:zombie",
                    "minecraft:zombie_spawn_egg",
                    "none",
                    0.8,
                    400,
                    0.2,
                    200,
                    100,
                    14,
                    30
            ),
            new EnemieObject("Mad cow",
                    false,
                    "minecraft:mooshroom",
                    "minecraft:mooshroom_spawn_egg",
                    "none",
                    1,
                    5,
                    0.2,
                    1000,
                    500,
                    15,
                    60
            )
    };
}
