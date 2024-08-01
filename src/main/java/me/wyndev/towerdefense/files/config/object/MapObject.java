package me.wyndev.towerdefense.files.config.object;

import me.wyndev.towerdefense.game.GameType;

/**
 * Stores data about an in-game map.
 * @param schematic The map's schematic file
 * @param name The name of the map
 * @param mapGameType The map game type
 * @param creators The names of the map creators
 */
public record MapObject(String schematic, String name, GameType mapGameType, String... creators) {
}
