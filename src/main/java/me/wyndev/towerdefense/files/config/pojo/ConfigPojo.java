package me.wyndev.towerdefense.files.config.pojo;

import lombok.Data;

@Data
public class ConfigPojo {
    private int port = 25565;
    private String hostname = "0.0.0.0";
    private int gameStartTime = 30;
    private int timeBetweenWaves = 10;
}
