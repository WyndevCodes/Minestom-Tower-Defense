package me.wyndev.towerdefense.files.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.wyndev.towerdefense.files.config.object.ColorObject;
import me.wyndev.towerdefense.files.config.object.EnemieObject;
import me.wyndev.towerdefense.files.config.object.TeamObject;
import me.wyndev.towerdefense.files.config.pojo.EnemiesPojo;
import me.wyndev.towerdefense.files.config.pojo.TeamsPojo;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;

public class Teams {
    public static TeamsPojo teamsData;

    public static String getPath() {
        return new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent() + "/teams.yml";
    }

    public static void read() {
        try {
            File file = new File(getPath());
            if (!file.exists()) {
                create();
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            teamsData = mapper.readValue(file, TeamsPojo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            File file = new File(getPath());
            if (!file.exists()) {
                create();
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.writer().writeValue(new FileWriter(file), teamsData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void create() {
        try {
            File file = new File(getPath());
            if (!file.exists()) {
                file.createNewFile();
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.writer().writeValue(new FileWriter(file), new TeamsPojo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static TeamObject getFromUISlot(int slot) {
        for (TeamObject teamObject : teamsData.getTeams()) {
            if (teamObject.getGUIpos() == slot) {
                return teamObject;
            }
        }
        return null;
    }

    public static TeamObject getFromColor(ColorObject color) {
        for (TeamObject teamObject : teamsData.getTeams()) {
            if (teamObject.getColor().equals(color)) {
                return teamObject;
            }
        }
        return null;
    }
}
