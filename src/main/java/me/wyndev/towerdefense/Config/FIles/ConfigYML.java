package me.wyndev.towerdefense.Config.FIles;

import me.wyndev.towerdefense.Config.ConfigFile;

public class ConfigYML implements ConfigFile {
    @Override
    public String getFileName() {
        return "config.yml";
    }
}
