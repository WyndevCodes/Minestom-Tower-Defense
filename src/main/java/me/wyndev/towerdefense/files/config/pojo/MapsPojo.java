package me.wyndev.towerdefense.files.config.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.wyndev.towerdefense.files.config.object.MapObject;
import me.wyndev.towerdefense.game.GameType;

@Data
@NoArgsConstructor
public class MapsPojo {
    MapObject[] maps = new MapObject[] {
            new MapObject("map.schem", "Basic Map", GameType.SOLO, "tbvns")
    };
}
