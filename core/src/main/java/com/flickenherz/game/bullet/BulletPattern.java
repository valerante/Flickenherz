package com.flickenherz.game.bullet;

/**
 * Enum defining different bullet spawn patterns for enemy attacks.
 * Each pattern creates a unique challenge for the player to dodge.
 */
public enum BulletPattern {
    /** Horizontal bullets with wave motion */
    HORIZONTAL_WAVE,
    
    /** Vertical bullets raining down */
    VERTICAL_RAIN,
    
    /** Diagonal waves in rows */
    DIAGONAL_FAN,
    
    /** Circular burst from center */
    CIRCLE_BURST
}
