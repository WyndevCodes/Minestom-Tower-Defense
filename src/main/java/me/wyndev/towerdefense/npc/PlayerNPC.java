package me.wyndev.towerdefense.npc;

import net.minestom.server.entity.*;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;

/**
 * A player-like entity with a player skin.
 * Interactions are handled in interact events
 * that call {@link PlayerNPC#onClick(Player)}.
 * @author Matt Worzala (mworzala)
 * @author Wyndev
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class PlayerNPC extends LivingEntity {
    private final String username;

    private final String skinTexture;
    private final String skinSignature;

    /**
     * Creates a player NPC.
     * @param username The username of the player NPC
     * @param skinTexture The skin texture of the player, may be null
     * @param skinSignature The skin signature of the player, may be null
     */
    public PlayerNPC(@NotNull String username, @Nullable String skinTexture, @Nullable String skinSignature) {
        super(EntityType.PLAYER);
        this.username = username;

        this.skinTexture = skinTexture;
        this.skinSignature = skinSignature;

        setNoGravity(true);
    }

    /**
     * Logic that happens when this NPC is clicked by a player.
     * @param player The player who clicked
     */
    public abstract void onClick(Player player);

    @Override
    public void updateNewViewer(@NotNull Player player) {
        var properties = new ArrayList<PlayerInfoUpdatePacket.Property>();
        if (skinTexture != null && skinSignature != null) {
            properties.add(new PlayerInfoUpdatePacket.Property("textures", skinTexture, skinSignature));
        }
        var entry = new PlayerInfoUpdatePacket.Entry(getUuid(), username, properties, false,
                0, GameMode.SURVIVAL, null, null);
        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));

        // Spawn the player entity
        super.updateNewViewer(player);

        // Enable skin layers
        player.sendPackets(new EntityMetaDataPacket(getEntityId(), Map.of(17, Metadata.Byte((byte) 127))));
    }

    @Override
    public void updateOldViewer(@NotNull Player player) {
        super.updateOldViewer(player);

        player.sendPacket(new PlayerInfoRemovePacket(getUuid()));
    }
}
