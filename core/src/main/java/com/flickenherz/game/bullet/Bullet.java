package com.flickenherz.game.bullet;

/**
 * Represents a single bullet in the bullet hell system.
 * Supports wave motion patterns and object pooling for performance.
 */
public class Bullet {
    /** Current X position */
    public float x, y;
    
    /** Velocity in X and Y directions */
    public float vx, vy;
    
    /** Bullet radius for collision detection */
    public float radius;
    
    /** Whether the bullet is currently active */
    public boolean alive = true;
    
    /** Base Y position for wave motion calculations */
    public float baseY;
    
    /** Amplitude of wave motion (0 = no wave) */
    public float waveAmplitude = 0f;
    
    /** Frequency of wave oscillation */
    public float waveFrequency = 0f;
    
    /** Distance traveled horizontally for wave calculation */
    public float travelDistance = 0f;
    
    /** Pre-calculated squared radius for faster collision checks */
    public float radiusSquared;

    /**
     * Default constructor for object pooling.
     */
    public Bullet() {}

    /**
     * Creates a new bullet with specified parameters.
     * 
     * @param x Initial X position
     * @param y Initial Y position
     * @param vx X velocity
     * @param vy Y velocity
     * @param radius Bullet radius
     */
    public Bullet(float x, float y, float vx, float vy, float radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.alive = true;
        this.baseY = y;
    }
    
    /**
     * Resets the bullet with new parameters for object pool reuse.
     * 
     * @param x New X position
     * @param y New Y position
     * @param vx New X velocity
     * @param vy New Y velocity
     * @param radius New radius
     */
    public void reset(float x, float y, float vx, float vy, float radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.alive = true;
        this.baseY = y;
        this.waveAmplitude = 0f;
        this.waveFrequency = 0f;
        this.travelDistance = 0f;
    }
}
