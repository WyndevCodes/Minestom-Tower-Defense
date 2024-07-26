package me.wyndev.towerdefense.files.config.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemyType;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class TowerObject {
    public TowerObject() {} //For jackson

    private String name;
    private String desc;
    private String entityType;
    private Float[] price;
    private Float[] attackDamage;
    private Float[] attackSpeed;
    private boolean isSplash;
    private int maxLevel;
    private int guiPos;
    private String IconMaterials;

}
