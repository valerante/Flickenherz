package com.flickenherz.game.managers;

/**
 * Manager for visual hit effects when characters take damage.
 * Handles timed visual feedback effects for both enemy and player hits.
 * Effects are automatically deactivated after a short duration.
 */
public class HitEffectManager {

    /** Duration in seconds for hit effect display */
    private static final float HIT_DURATION = 0.15f;

    /** Whether enemy (teddy) hit effect is currently active */
    private boolean teddyHitActive = false;
    
    /** Remaining time for enemy hit effect */
    private float teddyHitTimer = 0f;

    /** Whether player (girl) hit effect is currently active */
    private boolean girlHitActive = false;
    
    /** Remaining time for player hit effect */
    private float girlHitTimer = 0f;

    /**
     * Updates hit effect timers and deactivates effects when time expires.
     * Should be called every frame with delta time.
     * 
     * @param delta Time elapsed since last frame in seconds
     */
    public void update(float delta) {
        if (teddyHitActive) {
            teddyHitTimer -= delta;
            if (teddyHitTimer <= 0f) {
                teddyHitActive = false;
            }
        }

        if (girlHitActive) {
            girlHitTimer -= delta;
            if (girlHitTimer <= 0f) {
                girlHitActive = false;
            }
        }
    }

    /**
     * Activates the enemy hit effect for the standard duration.
     */
    public void activateTeddyHit() {
        teddyHitActive = true;
        teddyHitTimer = HIT_DURATION;
    }

    /**
     * Activates the player hit effect for the standard duration.
     */
    public void activateGirlHit() {
        girlHitActive = true;
        girlHitTimer = HIT_DURATION;
    }

    /**
     * Checks if enemy hit effect is currently active.
     * 
     * @return true if enemy hit effect is showing, false otherwise
     */
    public boolean isTeddyHitActive() {
        return teddyHitActive;
    }

    /**
     * Checks if player hit effect is currently active.
     * 
     * @return true if player hit effect is showing, false otherwise
     */
    public boolean isGirlHitActive() {
        return girlHitActive;
    }

    /**
     * Resets all hit effects to inactive state.
     */
    public void reset() {
        teddyHitActive = false;
        girlHitActive = false;
        teddyHitTimer = 0f;
        girlHitTimer = 0f;
    }
}
