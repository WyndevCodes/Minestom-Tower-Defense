package me.wyndev.towerdefense.game;

import lombok.Getter;

/**
 * The state of a {@link GameInstance}.
 */
@Getter
public enum GameState {
    WAITING_FOR_PLAYERS(true),
    COUNTDOWN(true),
    RUNNING(false),
    ENDING(false),
    ENDED(false);

    private final boolean isJoinable;

    GameState(boolean isJoinable) {
        this.isJoinable = isJoinable;
    }
}
