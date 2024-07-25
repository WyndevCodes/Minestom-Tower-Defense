package me.wyndev.towerdefense.Config;


import me.wyndev.towerdefense.Main;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

public interface ConfigFile {
    Logger log = LoggerFactory.getLogger(ConfigFile.class);

    default String getFileName() {
        return "deafultPath.yml";
    }

    default String getPath() {
        try {
            String root = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            return root + "/" + getFileName();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    default String readFile() {
        File file = new File(getPath());
        if (exist()) {
            try {
                InputStream stream = new FileInputStream(file);
                String string = new String(IOUtils.readFully(stream, stream.available()));
                return string;
            } catch (Exception e) {
                log.error("Error while reading file:", e);
                return null;
            }
        } else {

        }
        return null;
    }

    default Object readValue(String key) throws IOException {
        String confData = readFile();
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(confData);
        return data.get(key);
    }

    default String readString(String s) throws IOException {
        return (String) readValue(s);
    }

    default Integer readInt(String s) throws IOException {
        return (Integer) readValue(s);
    }

    //TODO: add add that
    //default boolean writeValue() {

    //}

    default boolean exist() {
        File file = new File(getPath());
        return file.exists() && file.isFile();
    };

    default boolean create() {
        try {
            File file = new File(getPath());
            return file.createNewFile();
        } catch (IOException e) {
            log.error("Error while creating config file: ", e);
            return false;
        }
    }
}
