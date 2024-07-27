package me.wyndev.towerdefense.files.config.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class TowerObject {
    public TowerObject() {} //For jackson

    //TODO: add custom model data support for item in hand and armor

    private String name;
    private String desc;
    private String entityType;
    private Float[] price;
    private Float[] attackDamage;
    private Float[] attackSpeed;
    private Integer[] attackRange;
    private String[] armorSet;
    private boolean[] armorPieces;
    private String[] itemInHand;
    private boolean isSplash;
    private int maxLevel;
    private int guiPos; //TODO: replace that with an array and add a button for each level, with the icon item's number increasing by one each time
    private String IconMaterials;

    public float getPriceFromLevel(int level) {
        level--;
        return price[level];
    }
    public float getAttackDamageFromLevel(int level) {
        level--;
        return attackDamage[level];
    }
    public float getAttackSpeedFromLevel(int level) {
        level--;
        return attackSpeed[level];
    }
    public int getAttackRangeFromLevel(int level) {
        level--;
        return attackRange[level];
    }

    public String getArmorFromLevel(int level) {
        level--;
        if (armorSet.length <= level) {
            return "none";
        }
        return armorSet[level];
    }

    public String getItemInHandFromLevel(int level) {
        if (itemInHand.length <= level) {
            return "minecraft:air";
        }
        return itemInHand[level];
    }
    /**
     * @param id The id of the armor piece: 0 -> helmet, 1 -> chestplate ...
     * */
    public boolean getArmorPieceFromID(int id) {
        if (armorPieces.length <= id) {
            return false;
        }
        return armorPieces[id];
    }
}
