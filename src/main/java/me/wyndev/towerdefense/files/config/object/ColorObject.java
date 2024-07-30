package me.wyndev.towerdefense.files.config.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.format.TextColor;

@Data
@AllArgsConstructor
public class ColorObject {
    public ColorObject() {}

    int red;
    int green;
    int blue;

    public TextColor toTextColor() {
        return TextColor.color(red, green, blue);
    }
}
