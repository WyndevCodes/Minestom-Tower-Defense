package me.wyndev.towerdefense.files.config.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnemieObject {
    public EnemieObject() {} //For jackson

    private String name;
    private boolean isWSEE;
    private String modelName;
    private String iconMaterial; //TODO: add custom model support
    private String baseEffect;
    private double maxShift;
    private double maxHealth;
    private double speed;
    private double cost;
    private double killReward;
    private int GUIpos;
    private int minWave;
}
