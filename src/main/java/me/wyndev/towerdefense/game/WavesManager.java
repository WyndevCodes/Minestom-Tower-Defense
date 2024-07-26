package me.wyndev.towerdefense.game;

import me.wyndev.towerdefense.Utils;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemy;
import me.wyndev.towerdefense.files.config.Config;
import me.wyndev.towerdefense.files.config.Waves;
import me.wyndev.towerdefense.files.config.object.WaveObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class WavesManager {
    private static final Logger log = LoggerFactory.getLogger(WavesManager.class);

    public void startWave(GameInstance instance, Pos pos) {
        Thread thread = new Thread(new WaveTask(0, instance, pos));
        thread.start();
    }

    public class WaveTask implements Runnable {
        int waveID;
        GameInstance instance;
        Pos pos;

        public WaveTask(int waveID, GameInstance gameInstance, Pos pos) {
            this.waveID = waveID;
            this.instance = gameInstance;
            this.pos = pos;
        }

        @Override
        public void run() {
            Utils.sleep(Config.configData.getTimeBetweenWaves() * 1000);
            log.info("Wave {}", waveID);
            instance.getIngamePlayers().forEach((k, v) -> {
                v.getTowerDefensePlayer().showTitle(Title.title(Component.empty(), Component.text("Wave " + waveID).color(TextColor.color(255, 0, 0))));
            });
            List<TowerDefenseEnemy> enemies = new ArrayList<>();
            WaveObject[] waves = Waves.waveData.getWaves();
            for (WaveObject wave : waves) {
                if (waveID >= wave.getStartingWave() && (waveID <= wave.getEndingWaves() || wave.getEndingWaves() == -1) && ((float) waveID / wave.getTimeBetweenSpawn()) % 1 == 0) {
                    System.out.println((float) waveID / wave.getTimeBetweenSpawn());
                    long entityCount = wave.getPerWavesCount();
                    if (waveID - wave.getStartingWave() != 0) {
                        entityCount+= Math.round(((double) (waveID - wave.getStartingWave()) / wave.getTimeBetweenSpawn() * wave.getPerSpawnAddition()));
                    }
                    if (entityCount > wave.getPerWavesMaxCount()) entityCount = wave.getPerWavesMaxCount();
                    for (int i = 0; i < entityCount; i++) {
                        enemies.add(new TowerDefenseEnemy(wave.getEnemyType()));
                    }
                }
            }
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Random random = new Random();
                int id = 0;
                if (i != 0) {
                    random.nextInt(0, i);
                }
                enemies.get(id).setInstance(instance.getInstance(), pos);
                enemies.get(id).getEntityMeta().setHasNoGravity(true);
                synchronized (instance.getEnemies()) {
                    instance.getEnemies().add(enemies.get(id));
                }
                enemies.remove(id);
                Utils.sleep(100);
            }

            waveID++;
            Thread thread = new Thread(new WaveTask(waveID, instance, pos));
            thread.start();
        }
    }
}
