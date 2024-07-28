package me.wyndev.towerdefense.npc;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.*;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

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
    private final Component nametag;

    private final String skinTexture;
    private final String skinSignature;

    private Team npcTeam;

    private Entity target;

    /**
     * Creates a player NPC.
     * @param username The username of the player NPC
     * @param skinTexture The skin texture of the player, may be null
     * @param skinSignature The skin signature of the player, may be null
     */
    public PlayerNPC(@NotNull String username, @Nullable String skinTexture, @Nullable String skinSignature) {
        super(EntityType.PLAYER);
        this.username = "$" + username;
        this.nametag = null;

        this.skinTexture = skinTexture;
        this.skinSignature = skinSignature;

        setNoGravity(true);
        createNPCTeam();
        createNametag();
    }

    /**
     * Creates a player NPC.
     * @param username The username of the player NPC
     * @param nametag The NPC nametag, in component format
     * @param skinTexture The skin texture of the player, may be null
     * @param skinSignature The skin signature of the player, may be null
     */
    public PlayerNPC(@NotNull String username, @Nullable Component nametag, @Nullable String skinTexture, @Nullable String skinSignature) {
        super(EntityType.PLAYER);
        this.username = "$" + username;
        this.nametag = nametag;

        this.skinTexture = skinTexture;
        this.skinSignature = skinSignature;

        setNoGravity(true);
        createNPCTeam();
        createNametag();
    }

    private void createNPCTeam() {
        npcTeam = MinecraftServer.getTeamManager().createBuilder("npc_team_" + username)
                .collisionRule(TeamsPacket.CollisionRule.NEVER).nameTagVisibility(TeamsPacket.NameTagVisibility.NEVER).build();
        npcTeam.addMember(username);
    }

    private void createNametag() {
        //TODO: visual nameplate display, maybe multiline?
    }

    /**
     * Logic that happens when this NPC is clicked by a player.
     * @param player The player who clicked
     */
    public abstract void onClick(Player player);

    @Override
    public void update(long time) {
        super.update(time);

        if (time % 20 == 0) {
            Optional<Entity> nearest = instance.getNearbyEntities(position, 7).stream()
                    .filter(e -> e instanceof Player)
                    .min((a, b) -> (int) Math.round(a.getDistanceSquared(b)));
            nearest.ifPresent(entity -> target = entity);
        }
        if (target != null && target.getInstance().equals(instance)) lookAt(target);
    }

    @Override
    public void remove() {
        MinecraftServer.getTeamManager().deleteTeam(npcTeam);
        super.remove();
    }

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
