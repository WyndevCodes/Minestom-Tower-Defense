package me.wyndev.towerdefense.files.config.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.wyndev.towerdefense.files.config.object.ColorObject;
import me.wyndev.towerdefense.files.config.object.TeamObject;
import me.wyndev.towerdefense.files.config.object.TowerLevel;
import me.wyndev.towerdefense.files.config.object.TowerObject;

import java.awt.*;

@Data
@NoArgsConstructor
public class TeamsPojo {
    TeamObject[] teams = new TeamObject[] {
            new TeamObject("Red", new ColorObject(255, 0, 0), 10),
            new TeamObject("Green", new ColorObject(0, 255, 0), 11),
            new TeamObject("Blue", new ColorObject(0, 0, 255), 12),
            new TeamObject("Pink", new ColorObject(255, 0, 170), 13),
            new TeamObject("Yellow", new ColorObject(255, 255, 0), 14),
            new TeamObject("Light blue", new ColorObject(0, 255, 255), 15),
            new TeamObject("Orange", new ColorObject(255, 140, 0), 16),
    };
}
