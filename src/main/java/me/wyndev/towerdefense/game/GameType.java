package me.wyndev.towerdefense.game;

import lombok.Getter;

/**
 * The type of game
 */
@Getter
public enum GameType {

    SOLO(6, 6),
    DUO(12, 6),
    TEAM(20, 2),
    ;

    private final int maxPlayers;
    private final int maxTeams;

    GameType(int maxPlayers, int maxTeams) {
        this.maxPlayers = maxPlayers;
        this.maxTeams = maxTeams;
    }
}
