package me.wyndev.towerdefense.files.maps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import me.wyndev.towerdefense.files.config.Config;
import me.wyndev.towerdefense.files.config.object.MapObject;
import me.wyndev.towerdefense.files.config.pojo.MapsPojo;
import me.wyndev.towerdefense.files.config.pojo.TowersPojo;
import me.wyndev.towerdefense.game.GameType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Maps {
    private static final Logger log = LoggerFactory.getLogger(Maps.class);
    @Getter
    private static HashMap<File, MapObject> maps = new HashMap<>();
    private static File mapDir = new File(new File(Maps.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent() + "/Maps");
    private static String path = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent() + "/map-data.yml";

    public static void load() {
        try {
            maps = new HashMap<>();
            if (!mapDir.exists()) {
                mapDir.mkdir();
            }

            File file = new File(path);
            if (file.exists()) {
                file.createNewFile();
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            MapsPojo mapData = mapper.readValue(file, MapsPojo.class);

            String[] mapsPath = mapDir.list();
            for (int i = 0; i < mapsPath.length; i++) {
                String path = mapsPath[i];

                boolean matched = false;
                for (MapObject data : mapData.getMaps()) {
                    if (data.schematic().equalsIgnoreCase(path)) {
                        maps.put(new File(mapDir + "/" + path), data);
                        matched = true;
                        break;
                    }
                }

                if (!matched) {
                    log.warn("Map schematic saved as " + path + " in the maps folder has no associated map data! A default set will be used, which may cause issues.");
                    maps.put(new File(mapDir + "/" + path), new MapObject(path, ("Default-" + i), GameType.SOLO, "TowerDefense"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map.Entry<File, MapObject> getRandomMap() {
        Random random = new Random();
        List<Map.Entry<File, MapObject>> mapFiles = maps.entrySet().stream().toList();

        int i = 0;
        if (1 < mapFiles.size()) {
            i = random.nextInt(0, mapFiles.size() - 1);
        }
        return mapFiles.get(i);
    }

    private static void create() {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.writer().writeValue(new FileWriter(file), new MapsPojo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
