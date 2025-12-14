package com.flickenherz.game.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Arena for bullet hell combat sequences.
 * Manages the player soul's position, movement, and collision boundaries.
 */
public class Arena {
    /** Width of the arena in pixels */
    public static final float WIDTH = 600f;
    
    /** Height of the arena in pixels */
    public static final float HEIGHT = 320f;

    /** X position of the arena */
    public final float x;
    
    /** Y position of the arena */
    public final float y;

    /** Current X position of the player soul */
    public float soulX;
    
    /** Current Y position of the player soul */
    public float soulY;
    
    /** Radius of the player soul in pixels */
    public final float soulRadius = 12f;
    
    /** Pre-calculated squared radius for optimized collision detection */
    public final float soulRadiusSquared = soulRadius * soulRadius;
    
    /** Movement speed of the soul in pixels per second */
    public float soulSpeed = 350f;

    /** Whether bullet hell mode is currently active */
    public boolean inBulletHell = false;
    
    /** Timer tracking attack duration */
    public float attackTimer = 0f;
    
    /** Total duration of the current attack */
    public float attackDuration = 4f;
    
    /** Timer for bullet spawning */
    public float spawnTimer = 0f;
    
    /** Interval between bullet spawns */
    public float spawnInterval = 0.25f;
    
    /** Invincibility frames timer after taking damage */
    public float iFrameTimer = 0f;

    /**
     * Creates a new arena centered horizontally on the screen.
     * 
     * @param virtualWidth Virtual width of the game screen
     * @param y Y position of the arena
     */
    public Arena(int virtualWidth, float y) {
        this.x = (virtualWidth - WIDTH) / 2f;
        this.y = y;
        this.soulX = x + WIDTH / 2f;
        this.soulY = y + HEIGHT / 2f;
    }

    /**
     * Resets the soul to the center of the arena.
     */
    public void resetSoul() {
        soulX = x + WIDTH / 2f;
        soulY = y + HEIGHT / 2f;
    }

    /**
     * Handles player soul movement based on WASD input.
     * Normalizes diagonal movement and clamps position within arena bounds.
     * Uses optimized inverse square root for direction normalization.
     * 
     * @param delta Time elapsed since last frame
     */
    public void handleMovement(float delta) {
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= 1;

        if (dx != 0 || dy != 0) {
            float lenSq = dx * dx + dy * dy;
            if (lenSq > 0.0001f) {
                float invLen = (float)(1.0 / Math.sqrt(lenSq));
                dx *= invLen;
                dy *= invLen;

                float movement = soulSpeed * delta;
                soulX += dx * movement;
                soulY += dy * movement;
            }
        }

        float minX = x + soulRadius;
        float maxX = x + WIDTH - soulRadius;
        float minY = y + soulRadius;
        float maxY = y + HEIGHT - soulRadius;
        
        if (soulX < minX) soulX = minX;
        else if (soulX > maxX) soulX = maxX;
        
        if (soulY < minY) soulY = minY;
        else if (soulY > maxY) soulY = maxY;
    }

    /**
     * Draws the player soul using the shape renderer.
     * 
     * @param shapeRenderer ShapeRenderer to draw with
     */
    public void drawSoul(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(soulX, soulY, soulRadius + 6f);
        shapeRenderer.circle(soulX, soulY, soulRadius);
    }
}
