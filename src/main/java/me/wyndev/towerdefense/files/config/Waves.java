package me.wyndev.towerdefense.files.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.wyndev.towerdefense.files.config.pojo.ConfigPojo;
import me.wyndev.towerdefense.files.config.pojo.WavesPojo;

import java.io.File;
import java.io.FileWriter;

public class Waves {
    public static WavesPojo waveData;

    public static String getPath() {
        return new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent() + "/waves.yml";
    }

    public static void read() {
        try {
            File file = new File(getPath());
            if (!file.exists()) {
                create();
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            waveData = mapper.readValue(file, WavesPojo.class);
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
            mapper.writer().writeValue(new FileWriter(file), waveData);
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
            mapper.writer().writeValue(new FileWriter(file), new WavesPojo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
