package me.wyndev.towerdefense;

import me.wyndev.towerdefense.player.TowerDefensePlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.registry.Registry;

public class Main {
    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.getConnectionManager().setPlayerProvider((arg1, arg2, arg3) -> new TowerDefensePlayer(arg1, arg2, arg3, (byte) 1));

        // Register Events
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        //Generate a new minestom instance
        InstanceContainer container = MinecraftServer.getInstanceManager().createInstanceContainer();
        container.setBlock(0, -5, 0, Block.STONE);

        //Handle player login in
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, e -> {
            e.setSpawningInstance(container);
        });

        // Skin handling for players that join (using Mojang auth for now)
        globalEventHandler.addListener(PlayerSkinInitEvent.class, event -> {
            PlayerSkin skin = PlayerSkin.fromUuid(event.getPlayer().getUuid().toString());
            event.setSkin(skin);
        });

        MojangAuth.init();
        minecraftServer.start("0.0.0.0", 25565);
    }
}