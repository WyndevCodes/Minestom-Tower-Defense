package me.wyndev.towerdefense.files.pojo;

import lombok.Data;

@Data
public class ConfigPojo {
    private int port = 25565;
    private String hostname = "0.0.0.0";
}
