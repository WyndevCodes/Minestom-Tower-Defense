package me.wyndev.towerdefense.files.config.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.wyndev.towerdefense.files.config.object.WaveObject;

@Data
@NoArgsConstructor
public class WavesPojo {
    WaveObject[] waves = new WaveObject[] {
            new WaveObject("chicken",
                    0,
                    -1,
                    1,
                    4,
                    1,
                    20
            ),
            new WaveObject(
                    "wolf",
                    5,
                    -1,
                    2,
                    2,
                    0.5,
                    20
            ),
            new WaveObject(
                    "cow",
                    10,
                    -1,
                    1,
                    1,
                    0.5,
                    10
            ),
            new WaveObject(
                    "horse",
                    20,
                    -1,
                    1,
                    1,
                    0.25,
                    10
            ),
            new WaveObject(
                    "zombie",
                    30,
                    -1,
                    1,
                    3,
                    0.1,
                    30
            ),
            new WaveObject(
                    "mad cow",
                    50,
                    -1,
                    1,
                    1,
                    0.5,
                    5
            ),
    };
}
