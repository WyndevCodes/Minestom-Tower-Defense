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

    /**
     * Get the price of this tower object from a level
     * @param level the level of the tower
     * */
    public float getPriceFromLevel(int level) {
        level--;
        return price[level];
    }

    /**
     * Get the attack damage of this tower object from a level
     * @param level the level of the tower
     * */
    public float getAttackDamageFromLevel(int level) {
        level--;
        return attackDamage[level];
    }
    /**
     * Get the attack speed of this tower object from a level
     * @param level the level of the tower
     * */
    public float getAttackSpeedFromLevel(int level) {
        level--;
        return attackSpeed[level];
    }
    /**
     * Get the range of this tower object from a level
     * @param level the level of the tower
     * */
    public int getAttackRangeFromLevel(int level) {
        level--;
        return attackRange[level];
    }

    /**
     * Get the armor set of this tower object from a level
     * @param level the level of the tower
     * */
    public String getArmorFromLevel(int level) {
        level--;
        if (armorSet.length <= level) {
            return "none";
        }
        return armorSet[level];
    }

    /**
     * Get the item in hand of this tower object from a level
     * @param level the level of the tower
     * */
    public String getItemInHandFromLevel(int level) {
        if (itemInHand.length <= level) {
            return "minecraft:air";
        }
        return itemInHand[level];
    }
    /**
     * Check if an armor peace should appear
     * @param id The id of the armor piece: 0 -> helmet, 1 -> chestplate ...
     * */
    public boolean getArmorPieceFromID(int id) {
        if (armorPieces.length <= id) {
            return false;
        }
        return armorPieces[id];
    }
}
