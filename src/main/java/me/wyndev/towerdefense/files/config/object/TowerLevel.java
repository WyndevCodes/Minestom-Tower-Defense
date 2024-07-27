package me.wyndev.towerdefense.files.config.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TowerLevel {
    public TowerLevel() {} //For jackson

    float price;
    float attackDamage;
    float attackSpeed;
    int attackRange;
    String armorSet;
    String itemInHand;
    int guiPos;
}
