package me.wyndev.towerdefense.files.maps;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maps {
    private static final Logger log = LoggerFactory.getLogger(Maps.class);
    private static List<File> maps = new ArrayList<>();
    private static File mapDir = new File(new File(Maps.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent() + "/Maps");
    public static void load() {
        maps = new ArrayList<>();
        if (!mapDir.exists()) {
            mapDir.mkdir();
        }
        String[] mapsPath = mapDir.list();
        for (int i = 0; i < mapsPath.length; i++) {
            String path = mapsPath[i];
            maps.add(new File(mapDir + "/" + path));
        }
    }

    public static Path getRandomMap() {
        Random random = new Random();
        int i = 0;
        if (1 <= maps.size()) {
            random.nextInt(0, maps.size() - 1);
        }
        return Path.of(maps.get(i).getPath());
    }

}
