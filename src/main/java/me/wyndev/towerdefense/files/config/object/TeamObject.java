package me.wyndev.towerdefense.files.config.object;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

@Data
@AllArgsConstructor
public class TeamObject {
    public TeamObject() {}

    public String displayName;
    public ColorObject color;
    public int GUIpos;
}
