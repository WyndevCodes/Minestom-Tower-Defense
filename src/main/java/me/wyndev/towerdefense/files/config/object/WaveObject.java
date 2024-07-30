package me.wyndev.towerdefense.files.config.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WaveObject {
    public WaveObject() {} //For jackson

    //The waves system is inspired by Minedustry
    private String enemyType;
    private int startingWave;
    private int endingWaves; //Set to -1 for infinite
    private int timeBetweenSpawn; //if this is equal to two, the enemy will spawn every 2 waves
    private int perWavesCount;
    private double perSpawnAddition;
    private int perWavesMaxCount;
}
