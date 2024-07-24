package me.wyndev.towerdefense.game.CustomEntity;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.entity.metadata.other.FallingBlockMeta;
import net.minestom.server.instance.block.Block;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Cursor extends Entity {
    public static final HashMap<Player, Cursor> cursorHashMap = new HashMap<>();

    Player player;
    public Cursor(Player player) {
        super(EntityType.BLOCK_DISPLAY);
        this.player = player;
        BlockDisplayMeta meta = (BlockDisplayMeta) getEntityMeta();
        meta.setBlockState(Block.GRASS_BLOCK);
        meta.setScale(new Vec(0.98, 0.98, 0.98));
        meta.setHasNoGravity(true);
        meta.setHasGlowingEffect(true);
        cursorHashMap.put(player, this);
    }

    @Override
    public @NotNull CompletableFuture<Void> teleport(@NotNull Pos position) {
        position = position.add(new Pos(0.01, 0.01, 0.01));
        return super.teleport(position);
    }
}