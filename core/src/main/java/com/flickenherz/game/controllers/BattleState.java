package com.flickenherz.game.controllers;

/**
 * Enum representing the current state of a battle encounter.
 * Controls turn-based flow and determines which actions are available.
 */
public enum BattleState {
    /** Player's turn to select and perform actions */
    PLAYER_TURN,
    
    /** Enemy's turn to attack with bullet hell patterns */
    ENEMY_TURN,
    
    /** Battle won by player, all enemies defeated */
    WIN,
    
    /** Battle lost by player, HP or sanity depleted */
    LOSE
}
