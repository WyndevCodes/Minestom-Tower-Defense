package me.wyndev.towerdefense.files.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.wyndev.towerdefense.files.config.pojo.TowersPojo;
import me.wyndev.towerdefense.files.config.pojo.WavesPojo;

import java.io.File;
import java.io.FileWriter;

public class Towers {
    public static TowersPojo towerData;

    public static String getPath() {
        return new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent() + "/towers.yml";
    }

    public static void read() {
        try {
            File file = new File(getPath());
            if (!file.exists()) {
                create();
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            towerData = mapper.readValue(file, TowersPojo.class);
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
            mapper.writer().writeValue(new FileWriter(file), towerData);
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
            mapper.writer().writeValue(new FileWriter(file), new TowersPojo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
