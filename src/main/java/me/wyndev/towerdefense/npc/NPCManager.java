package me.wyndev.towerdefense.npc;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class NPCManager {

    private final List<PlayerNPC> npcs;

    public NPCManager() {
        npcs = new ArrayList<>();

        EventNode<PlayerEvent> node = EventNode.type("player-listener", EventFilter.PLAYER);
        node.addListener(PlayerEntityInteractEvent.class, e -> {
            if (e.getTarget() instanceof PlayerNPC npc && npcs.contains(npc)) {
                npc.onClick(e.getPlayer());
            }
        });
        node.addListener(PlayerHandAnimationEvent.class, e -> {
            if (e.getHand() == Player.Hand.MAIN) {
                for (PlayerNPC npc : npcs) {
                    Entity target = e.getPlayer().getLineOfSightEntity(3, Predicate.isEqual(npc));
                    if (target != null) npc.onClick(e.getPlayer());
                }
            }
        });
        MinecraftServer.getGlobalEventHandler().addChild(node);
    }

    public void spawnNPC(PlayerNPC npc, Instance instance, Pos pos) {
        npc.setInstance(instance).handle((v, throwable) -> npc.teleport(pos));
        npcs.add(npc);
    }

}
