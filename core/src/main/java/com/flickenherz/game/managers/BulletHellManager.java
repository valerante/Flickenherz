package com.flickenherz.game.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import com.flickenherz.game.entities.Enemy;
import com.flickenherz.game.bullet.Bullet;
import com.flickenherz.game.bullet.BulletPattern;
import com.flickenherz.game.bullet.BulletSpawner;
import com.flickenherz.game.bullet.Arena;

/**
 * Manager for bullet hell attack sequences during enemy turns.
 * Handles bullet spawning, movement, collision detection, and pattern management.
 * Uses object pooling for bullets to reduce garbage collection pressure.
 */
public class BulletHellManager {

    /** Array of all active bullets in the current attack */
    private final Array<Bullet> bullets;
    
    /** Reference to the combat arena for positioning */
    private final Arena arena;
    
    /** Audio manager for sound effects */
    private final AudioManager audio;
    
    /** Object pool for bullet reuse to minimize memory allocations */
    private final Pool<Bullet> bulletPool = new Pool<Bullet>(200, 400) {
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };

    /** Current bullet pattern being spawned */
    private BulletPattern currentPattern = BulletPattern.HORIZONTAL_WAVE;
    
    /** Rotation offset for circle burst pattern */
    private float circleBurstAngleOffset = 0f;
    
    /** Index for tracking wave row spawning position */
    private int waveRowSpawnIndex = 0;
    
    /** Current enemy executing the attack */
    private Enemy currentEnemy = null;
    
    /** Boundary margin for bullet despawn distance */
    private static final float BOUNDARY_MARGIN = 50f;
    
    /** Constant for 2*PI to avoid recalculation */
    private static final float TWO_PI = (float)(Math.PI * 2);

    /**
     * Creates a new bullet hell manager.
     * 
     * @param arena Arena instance for bullet positioning
     * @param audio AudioManager for playing attack sounds
     */
    public BulletHellManager(Arena arena, AudioManager audio) {
        this.bullets = new Array<>();
        this.arena = arena;
        this.audio = audio;
    }

    /**
     * Gets the array of all active bullets.
     * 
     * @return Array containing all active bullet instances
     */
    public Array<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Starts a new bullet hell attack sequence.
     * Initializes arena state, selects bullet pattern based on enemy type,
     * and configures attack duration and difficulty.
     * 
     * @param enemy Enemy performing the attack
     * @param messageText Message to display during attack
     * @param actionText Action text to display during attack
     */
    public void startAttack(Enemy enemy, String messageText, String actionText) {
        arena.inBulletHell = true;
        arena.attackTimer = 0f;
        arena.spawnTimer = 0f;
        arena.iFrameTimer = 0f;
        bullets.clear();
        currentEnemy = enemy;

        arena.resetSoul();

        if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            arena.attackDuration = 4f;
            arena.spawnInterval = 0.25f;
        } else if (enemy != null && enemy.getName().equals("Father")) {
            arena.attackDuration = 6.5f;
            arena.spawnInterval = 0.14f;
        } else {
            arena.attackDuration = 6f;
            arena.spawnInterval = 0.16f;
        }

        if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            int patternIndex = (int) (Math.random() * 3);
            currentPattern = BulletPattern.values()[patternIndex];
        } else {
            int patternIndex = (int) (Math.random() * 4);
            currentPattern = BulletPattern.values()[patternIndex];
        }

        if (currentPattern == BulletPattern.CIRCLE_BURST) {
            circleBurstAngleOffset = 0f;
            arena.soulX = arena.x + Arena.WIDTH / 2f;
            arena.soulY = arena.y + arena.soulRadius + 8f;
        }

        audio.playEnemyAttackStart();
    }

    /**
     * Updates the bullet hell system each frame.
     * Handles bullet spawning, movement, wave motion, and boundary checking.
     * Uses optimized collision detection with pre-calculated boundary values.
     * 
     * @param delta Time elapsed since last frame in seconds
     */
    public void update(float delta) {
        if (!arena.inBulletHell) return;

        arena.attackTimer += delta;
        arena.spawnTimer -= delta;
        if (arena.iFrameTimer > 0f) {
            arena.iFrameTimer -= delta;
        }

        if (arena.spawnTimer <= 0f) {
            arena.spawnTimer = arena.spawnInterval;
            spawnBulletPattern();
        }

        float minX = arena.x - BOUNDARY_MARGIN;
        float maxX = arena.x + Arena.WIDTH + BOUNDARY_MARGIN;
        float minY = arena.y - BOUNDARY_MARGIN;
        float maxY = arena.y + Arena.HEIGHT + BOUNDARY_MARGIN;

        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            if (!b.alive) {
                bullets.removeIndex(i);
                bulletPool.free(b);
                continue;
            }

            b.x += b.vx * delta;
            
            if (b.waveAmplitude > 0) {
                b.travelDistance += Math.abs(b.vx) * delta;
                b.y = b.baseY + (float) Math.sin(b.travelDistance * b.waveFrequency) * b.waveAmplitude;
            } else {
                b.y += b.vy * delta;
            }

            if (b.x < minX || b.x > maxX || b.y < minY || b.y > maxY) {
                bullets.removeIndex(i);
                bulletPool.free(b);
            }
        }
    }

    /**
     * Checks if the current attack sequence has finished.
     * 
     * @return true if attack timer exceeds duration, false otherwise
     */
    public boolean isAttackFinished() {
        return arena.inBulletHell && arena.attackTimer >= arena.attackDuration;
    }

    /**
     * Ends the current bullet hell attack.
     * Returns all bullets to the pool and resets state.
     */
    public void endAttack() {
        arena.inBulletHell = false;
        bulletPool.freeAll(bullets);
        bullets.clear();
    }

    /**
     * Resets the bullet hell manager to initial state.
     * Clears all bullets and resets pattern state.
     */
    public void reset() {
        arena.inBulletHell = false;
        bulletPool.freeAll(bullets);
        bullets.clear();
        circleBurstAngleOffset = 0f;
        waveRowSpawnIndex = 0;
    }
    
    /**
     * Gets the bullet object pool for use by spawner classes.
     * 
     * @return Bullet pool instance
     */
    public Pool<Bullet> getBulletPool() {
        return bulletPool;
    }

    /**
     * Spawns bullets according to the current pattern.
     * Adjusts bullet speed and count based on enemy difficulty (Father is harder).
     */
    private void spawnBulletPattern() {
        boolean isFather = currentEnemy != null && currentEnemy.getName().equals("Father");
        
        switch (currentPattern) {
            case HORIZONTAL_WAVE:
                BulletSpawner.spawnHorizontalWave(bullets, arena, isFather ? 290f : 260f, bulletPool);
                break;
            case VERTICAL_RAIN:
                BulletSpawner.spawnVerticalRain(bullets, arena, isFather ? 290f : 260f, bulletPool);
                break;
            case DIAGONAL_FAN:
                waveRowSpawnIndex = BulletSpawner.spawnWaveRows(bullets, arena, 
                    isFather ? 160f : 140f,
                    isFather ? 32f : 30f,
                    0.015f, 
                    waveRowSpawnIndex,
                    bulletPool);
                break;
            case CIRCLE_BURST:
                circleBurstAngleOffset = BulletSpawner.spawnCircleBurst(bullets, arena, 
                    isFather ? 200f : 180f,
                    circleBurstAngleOffset, 
                    isFather ? 9 : 8,
                    bulletPool);
                break;
        }
    }
}
