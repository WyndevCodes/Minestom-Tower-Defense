package me.wyndev.towerdefense.files.config.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColorObject {
    public ColorObject() {}

    int red;
    int green;
    int blue;
}
