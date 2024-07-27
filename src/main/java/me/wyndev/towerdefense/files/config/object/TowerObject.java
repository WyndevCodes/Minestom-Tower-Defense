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
    private TowerLevel[] towerLevels;
    private boolean[] armorPieces;
    private boolean isSplash;
    private int maxLevel;
    private String IconMaterials;

    /**
     * Get the price of this tower object from a level
     * @param level the level of the tower
     * */
    public float getPriceFromLevel(int level) {
        level--;
        return towerLevels[level].price;
    }

    /**
     * Get the attack damage of this tower object from a level
     * @param level the level of the tower
     * */
    public float getAttackDamageFromLevel(int level) {
        level--;
        return towerLevels[level].attackDamage;
    }
    /**
     * Get the attack speed of this tower object from a level
     * @param level the level of the tower
     * */
    public float getAttackSpeedFromLevel(int level) {
        level--;
        return towerLevels[level].attackSpeed;
    }
    /**
     * Get the range of this tower object from a level
     * @param level the level of the tower
     * */
    public int getAttackRangeFromLevel(int level) {
        level--;
        return towerLevels[level].attackRange;
    }

    /**
     * Get the GUI position of this tower object from a level
     * @param level the level of the tower
     * */
    public int getGUIPosLevel(int level) {
        level--;
        return towerLevels[level].guiPos;
    }

    /**
     * Get the armor set of this tower object from a level
     * @param level the level of the tower
     * */
    public String getArmorFromLevel(int level) {
        level--;
        if (towerLevels.length <= level) {
            return "none";
        }
        return towerLevels[level].armorSet;
    }

    /**
     * Get the item in hand of this tower object from a level
     * @param level the level of the tower
     * */
    public String getItemInHandFromLevel(int level) {
        if (towerLevels.length <= level) {
            return "minecraft:air";
        }
        return towerLevels[level].itemInHand;
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
