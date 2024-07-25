package me.wyndev.towerdefense.Game.CustomEntity;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class Cursor extends Entity {
    public static final HashMap<Player, Cursor> cursorHashMap = new HashMap<>();

    Player player;
    private final @Getter BlockDisplayMeta meta;
    private final @Getter Block defaultBlockTexture = Block.LODESTONE;

    public Cursor(Player player) {
        super(EntityType.BLOCK_DISPLAY);
        this.player = player;
        meta = (BlockDisplayMeta) getEntityMeta();
        meta.setBlockState(defaultBlockTexture);
        meta.setScale(new Vec(1.1, 1.1, 1.1));
        meta.setHasNoGravity(true);
        meta.setPosRotInterpolationDuration(2);
        cursorHashMap.put(player, this);
    }

    @Override
    public @NotNull CompletableFuture<Void> teleport(@NotNull Pos position) {
        position = position.add(new Pos(-0.05, -0.05, -0.05));
        return super.teleport(position);
    }
}
