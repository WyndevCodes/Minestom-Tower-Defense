package me.wyndev.towerdefense.game;

import me.wyndev.towerdefense.ChatColor;
import me.wyndev.towerdefense.Main;
import me.wyndev.towerdefense.Utils;
import me.wyndev.towerdefense.files.config.Config;
import me.wyndev.towerdefense.player.IngameTowerDefensePlayer;
import me.wyndev.towerdefense.player.TowerDefensePlayer;
import me.wyndev.towerdefense.tower.Tower;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.listener.ChatMessageListener;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameLoop {

    private final List<Integer> nonTenCountdownNumbers = List.of(5, 4, 3, 2, 1);
    private final GameInstance gameInstance;
    private Task countdownTask;
    private Task mainLoopTask;
    private int currentTick = 0;

    public GameLoop(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        gameInstance.getInstance().eventNode().addListener(PlayerChatEvent.class, e -> {
            if (e.getMessage().equals("fs")) {
                countdownTask.cancel();
                gameInstance.start();
            }
        });
    }

    public void startCountdown() {
        startCountdown(Config.configData.getGameStartTime()); //default 30 seconds
    }

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

    public void startMainLoop() {
        mainLoopTask = Main.scheduler.scheduleTask(() -> {

            // --- Main game loop ---
            // Exit main loop if the game state is no longer in the RUNNING state
            if (gameInstance.getGameState() != GameState.RUNNING) {
                mainLoopTask.cancel();
                return;
            }

            for (IngameTowerDefensePlayer player : gameInstance.getIngamePlayers().values()) {
                // Give players income every 10 seconds
                if (currentTick == 0) {
                    player.setGold(player.getGold() + player.getIncome());
                    player.getTowerDefensePlayer().sendActionBar(incomeMessage(player.getIncome()));
                }

                // Tick towers
                for (EntityCreature tower : player.getCurrentPlacedTowers()) {
                    if (tower instanceof Tower) {
                        ((Tower) tower).tick();
                    }
                }
            }

            // Increment current tick count
            currentTick += 1;
            if (currentTick > 200) currentTick = 0; //reset every 10 seconds

        }, TaskSchedule.nextTick(), TaskSchedule.nextTick(), ExecutionType.TICK_START);
    }

    public void stopMainLoop() {
        if (mainLoopTask != null) mainLoopTask.cancel();
    }

    private Component incomeMessage(long income) {
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
