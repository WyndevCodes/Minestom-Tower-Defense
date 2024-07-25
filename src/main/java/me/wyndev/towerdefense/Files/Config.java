package me.wyndev.towerdefense.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.wyndev.towerdefense.Files.POJO.ConfigPojo;

import java.io.File;
import java.io.FileWriter;

public class Config {
    public static ConfigPojo configData;

    public static String getPath() {
        return new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent() + "/config.yml";
    }

    public static void read() {
        System.out.println(getPath());

        try {
            File file = new File(getPath());
            if (!file.exists()) {
                create();
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            configData = mapper.readValue(file, ConfigPojo.class);
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
            mapper.writer().writeValue(new FileWriter(file), configData);
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
            mapper.writer().writeValue(new FileWriter(file), new ConfigPojo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
