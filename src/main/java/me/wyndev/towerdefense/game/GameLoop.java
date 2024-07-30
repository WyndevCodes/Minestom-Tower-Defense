package me.wyndev.towerdefense.game;

import me.wyndev.towerdefense.ChatColor;
import me.wyndev.towerdefense.Main;
import me.wyndev.towerdefense.Utils;
import me.wyndev.towerdefense.enemy.TowerDefenseEnemy;
import me.wyndev.towerdefense.files.config.Config;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefenseTeam;
import me.wyndev.towerdefense.tower.Tower;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main tower defense game loop.
 */
public class GameLoop {

    private final List<Integer> nonTenCountdownNumbers = List.of(5, 4, 3, 2, 1);
    private final GameInstance gameInstance;
    private Task countdownTask;
    private Task mainLoopTask;
    private int currentTick = 0;

    /**
     * Creates a game loop associated with a game instance.
     * @param gameInstance The game instance
     */
    public GameLoop(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        gameInstance.getInstance().eventNode().addListener(PlayerChatEvent.class, e -> {
            if (e.getMessage().equals("fs")) {
                countdownTask.cancel();
                gameInstance.start();
            }
        });
    }

    /**
     * Starts the countdown phase of this game loop lasting for the default amount of seconds.
     * This does NOT update the state in this loop's associated game instance.
     */
    public void startCountdown() {
        startCountdown(Config.configData.getGameStartTime()); //default 30 seconds
    }

    /**
     * Starts the countdown phase of this game loop, lasting a specified
     * amount of seconds. This does NOT update the state in this loop's
     * associated game instance.
     * @param seconds Number of seconds that the countdown lasts for
     */
    public void startCountdown(int seconds) {
        AtomicInteger timeLeft = new AtomicInteger(seconds);
        countdownTask = Main.scheduler.scheduleTask(() -> {

            // -- Countdown Loop ---
            timeLeft.getAndDecrement();
            int time = timeLeft.get();

            // Start game if 0 seconds are left in the countdown
            if (time == 0) {
                gameInstance.start();

                // Start message
                for (TowerDefensePlayer player : gameInstance.getPlayers()) {
                    player.sendTitlePart(TitlePart.TITLE, Component.text("The game has started!").color(ChatColor.GREEN.toColor()));
                    player.sendTitlePart(TitlePart.SUBTITLE, Component.text("Good luck!").color(ChatColor.GRAY.toColor()));
                    player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ofSeconds(1)));
                    player.playPlayerSound(Key.key("block.dispenser.dispense"));
                    player.sendMessage(Component.text("The game has started! Good luck!").color(ChatColor.GREEN.toColor()));
                }

                return;
            }

            // Otherwise, visually display countdown to players, if applicable
            if (time > 0) {
                if (nonTenCountdownNumbers.contains(time) || time % 10 == 0) {
                    for (TowerDefensePlayer player : gameInstance.getPlayers()) {
                        player.sendTitlePart(TitlePart.TITLE, countdownNumber(time));
                        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofSeconds(1)));
                        player.playPlayerSound(Key.key("block.dispenser.dispense"));
                        player.sendMessage(Component.text("The game starts in " + time + " seconds!").color(ChatColor.YELLOW.toColor()));
                    }
                }
            } else {
                countdownTask.cancel();
            }

        }, TaskSchedule.nextTick(), TaskSchedule.seconds(1), ExecutionType.TICK_START);
    }

    /**
     * Cancels the countdown loop. This does not change the game
     * state of the associated game instance.
     */
    public void cancelCountdown() {
        if (countdownTask != null && countdownTask.isAlive()) {
            countdownTask.cancel();
            // Send "start cancelled" message to players
            for (TowerDefensePlayer player : gameInstance.getPlayers()) {
                player.sendTitlePart(TitlePart.TITLE, Component.text("Cancelled!").color(ChatColor.RED.toColor()));
                player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ofSeconds(1)));
                player.playPlayerSound(Key.key("block.dispenser.dispense"), 1F, 0.8F);
                player.sendMessage(Component.text("Game start cancelled! Not enough players!").color(ChatColor.RED.toColor()));
            }
        }
    }

    /**
     * Starts the main game loop.
     */
    public void startMainLoop() {
        mainLoopTask = Main.scheduler.scheduleTask(() -> {
            // --- Main game loop ---
            // Exit main loop if the game state is no longer in the RUNNING state
            if (gameInstance.getGameState() != GameState.RUNNING) {
                mainLoopTask.cancel();
                return;
            }

            for (TowerDefenseTeam team : gameInstance.getTeams()) {
                // Give players income every 10 seconds
                if (currentTick == 0) {
                    team.setGold(team.getGold() + team.getIncome());
                    team.getTowerDefensePlayers().forEach(p -> p.sendActionBar(incomeMessage(team.getIncome())));
                }

                // Tick towers
                for (Tower tower : team.getCurrentPlacedTowers()) {
                    if (tower != null) tower.tick();
                }
            }

            synchronized (gameInstance.getEnemies()) {
                //Run every tick for every enemy
                for (TowerDefenseEnemy enemy : gameInstance.getEnemies()) {
                    enemy.tick(gameInstance.getPlayers());

                    Pos rot = enemy.getPosition();

                    //Check if enemy can be moved (if it exists in an instance)
                    //There are cases where the enemy ticks before it is created, so we cannot teleport it without an error
                    if (enemy.getInstance() == null) continue;

                    //Move enemies forward
                    if (rot.yaw() == 0) {
                        enemy.teleport(rot.add(new Pos(0, 0, enemy.getEnemieObject().getSpeed())));
                    } else if (rot.yaw() == 90) {
                        enemy.teleport(rot.add(new Pos(-enemy.getEnemieObject().getSpeed(), 0, 0)));
                    } else if (rot.yaw() == -180) {
                        enemy.teleport(rot.add(new Pos(0, 0, -enemy.getEnemieObject().getSpeed())));
                    } else if (rot.yaw() == -90) {
                        enemy.teleport(rot.add(new Pos(enemy.getEnemieObject().getSpeed(), 0, 0)));
                    }

                    rot = rot.sub(enemy.getShift());

                    Block block = gameInstance.getInstance().getBlock(rot.blockX(), rot.blockY(), rot.blockZ());
                    Block block1 = gameInstance.getInstance().getBlock(rot.blockX(), rot.blockY()+1, rot.blockZ());
                    Block block2 = gameInstance.getInstance().getBlock(rot.blockX(), rot.blockY()-2, rot.blockZ());

                    checkEnemyReachedEnd(enemy, rot, block);

                    changeEnemyDirection(enemy, rot, block);
                    changeEnemyDirection(enemy, rot, block1);
                    changeEnemyDirection(enemy, rot, block2);
                }
            }

            // Increment current tick count
            currentTick += 1;
            if (currentTick > 200) currentTick = 0; //reset every 10 seconds

        }, TaskSchedule.nextTick(), TaskSchedule.nextTick(), ExecutionType.TICK_START);
    }

    /**
     * Changes an enemy's direction based on its position and the properties of a block
     * at a specified location.
     * @param enemy The enemy to change direction for
     * @param pos The position of the enemy
     * @param blockWithProperty The block that should be checked for rotational properties
     */
    private void changeEnemyDirection(TowerDefenseEnemy enemy, Pos pos, Block blockWithProperty) {
        if (blockWithProperty.name().equals("minecraft:magenta_glazed_terracotta") && pos.z() % 1 >= 0.5 && pos.x() % 1 >= 0.5) {
            String facing = blockWithProperty.getProperty("facing");
            try {
                switch (facing) {
                    case "south" -> enemy.teleport(enemy.getPosition().withYaw(-180));
                    case "east" -> enemy.teleport(enemy.getPosition().withYaw(90));
                    case "west" -> enemy.teleport(enemy.getPosition().withYaw(-90));
                    default -> enemy.teleport(enemy.getPosition().withYaw(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if an enemy reached the end of a tower defense track.
     * @param enemy The enemy to check
     * @param pos The position of the enemy
     * @param blockToCheck The block that should be checked to ensure
     *                     the enemy is at the end of the track
     */
    private void checkEnemyReachedEnd(TowerDefenseEnemy enemy, Pos pos, Block blockToCheck) {
        if (blockToCheck.name().equals("minecraft:end_gateway") && pos.z() % 1 >= 0.5 && pos.x() % 1 >= 0.5) {
            //TODO: fetch the tower defense player who got damaged by this enemy
            enemy.reachEnd();
        }
    }

    /**
     * Stops the main game loop.
     */
    public void stopMainLoop() {
        if (mainLoopTask != null) mainLoopTask.cancel();
    }

    private Component incomeMessage(double income) {
        return Component.text("+" + Utils.formatWithCommas(income) + " Gold (Income)").color(ChatColor.GOLD.toColor());
    }

    private Component countdownNumber(int current) {
        if (current < 4) {
            return Component.text(current).color(ChatColor.RED.toColor());
        } else if (current < 10) {
            return Component.text(current).color(ChatColor.YELLOW.toColor());
        } else {
            return Component.text(current).color(ChatColor.GREEN.toColor());
        }
    }

}
