package me.wyndev.towerdefense.player;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A player that has logged into the tower defense server.
 */
public class TowerDefensePlayer extends Player {
    private final byte profileNumber;
    /**
     * This object stores every stat about a player
     * */
    public stats stats;

    @Data
    public static class stats {
        // ---------- Player stats (on this profile) ----------
        // Game Stats
        private int wins = 0;
        private int losses = 0;
        private int totalPlayersEliminated = 0;
        private long totalGoldSpent = 0;
        private int totalTowersPlaced = 0;
        private int totalTowersSold = 0;
        private int totalEnemiesSpawned = 0;
        private int totalEnemiesDefeated = 0;
        // Player Info
        private long playtimeInMillis = 0;
        private long firstJoinTimeInMillis = -1;
    }

    //TODO: add more useful metrics

    /**
     * Initializes a TowerDefensePlayer. Intended to be called when the player logs in.
     * @param uuid The UUID of the player
     * @param username The username of the player
     * @param playerConnection The connection from which the player joined
     * @param profileNumber The profile number of the player, set by an external source
     *                      before the player joins, or 1 by default
     */
    public TowerDefensePlayer(@NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection, byte profileNumber) {
        super(uuid, username, playerConnection);
        this.profileNumber = profileNumber;
        //Create a new stats class
        this.stats = new stats();
    }

    /**
     * @return A byte representing the profile number of this player.
     * A profile number is just an extra piece of data that specifies
     * how the player data is saved. For example, a player with a profile
     * number of 1 will have different data as the same player with a
     * profile number of 2.
     */
    public byte getProfileNumber() {
        return profileNumber;
    }

    /**
     * Gets the String object under which this player's data should be
     * saved to.
     * @return The player's data save key, including the player's current
     * profile number
     */
    public String getSaveKey() {
        return getUuid() + "-" + profileNumber;
    }
}
