package com.flickenherz.game.managers;

// Manages timed visual hit effects
public class HitEffectManager {

    private static final float HIT_DURATION = 0.15f;

    private boolean teddyHitActive = false;
    private float teddyHitTimer = 0f;

    private boolean girlHitActive = false;
    private float girlHitTimer = 0f;

    // Update timers and deactivate expired effects
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

    public void activateTeddyHit() {
        teddyHitActive = true;
        teddyHitTimer = HIT_DURATION;
    }

    public void activateGirlHit() {
        girlHitActive = true;
        girlHitTimer = HIT_DURATION;
    }

    public boolean isTeddyHitActive() {
        return teddyHitActive;
    }

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
