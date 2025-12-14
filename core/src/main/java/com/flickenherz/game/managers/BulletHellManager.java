package com.flickenherz.game.managers;

import com.badlogic.gdx.utils.Array;

import com.flickenherz.game.entities.Enemy;
import com.flickenherz.game.bullet.Bullet;
import com.flickenherz.game.bullet.BulletPattern;
import com.flickenherz.game.bullet.BulletSpawner;
import com.flickenherz.game.bullet.Arena;

public class BulletHellManager {

    private final Array<Bullet> bullets;
    private final Arena arena;
    private final AudioManager audio;

    private BulletPattern currentPattern = BulletPattern.HORIZONTAL_WAVE;
    private float circleBurstAngleOffset = 0f;
    private int waveRowSpawnIndex = 0;
    private Enemy currentEnemy = null;

    public BulletHellManager(Arena arena, AudioManager audio) {
        this.bullets = new Array<>();
        this.arena = arena;
        this.audio = audio;
    }

    public Array<Bullet> getBullets() {
        return bullets;
    }

    public void startAttack(Enemy enemy, String messageText, String actionText) {
        arena.inBulletHell = true;
        arena.attackTimer = 0f;
        arena.spawnTimer = 0f;
        arena.iFrameTimer = 0f;
        bullets.clear();
        currentEnemy = enemy;

        arena.resetSoul();

        // Difficulty based on enemy type
        if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            arena.attackDuration = 4f;
            arena.spawnInterval = 0.25f;
        } else if (enemy != null && enemy.getName().equals("Father")) {
            // Father: Slightly harder settings
            arena.attackDuration = 6.5f;
            arena.spawnInterval = 0.14f; // Slightly faster spawning
        } else {
            arena.attackDuration = 6f;
            arena.spawnInterval = 0.16f;
        }

        // Pattern selection: Teddy uses first 3, Father uses all 4, others use all
        if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            // Teddy: only first 3 patterns
            int patternIndex = (int) (Math.random() * 3);
            currentPattern = BulletPattern.values()[patternIndex];
        } else {
            // Father and other enemies: use all 4 basic patterns
            int patternIndex = (int) (Math.random() * 4);
            currentPattern = BulletPattern.values()[patternIndex];
        }

        // Circle burst positioning
        if (currentPattern == BulletPattern.CIRCLE_BURST) {
            circleBurstAngleOffset = 0f;
            arena.soulX = arena.x + Arena.WIDTH / 2f;
            arena.soulY = arena.y + arena.soulRadius + 8f;
        }

        audio.playEnemyAttackStart();
    }

    public void update(float delta) {
        if (!arena.inBulletHell) return;

        arena.attackTimer += delta;
        arena.spawnTimer -= delta;
        if (arena.iFrameTimer > 0f) {
            arena.iFrameTimer -= delta;
        }

        // Spawn new bullets
        if (arena.spawnTimer <= 0f) {
            arena.spawnTimer = arena.spawnInterval;
            spawnBulletPattern();
        }

        // Move bullets (collision with the player's soul is handled by the screen)
        for (Bullet b : bullets) {
            if (!b.alive) continue;

            b.x += b.vx * delta;
            b.travelDistance += Math.abs(b.vx) * delta;
            
            // Apply wave motion if waveAmplitude is set
            if (b.waveAmplitude > 0) {
                b.y = b.baseY + (float) Math.sin(b.travelDistance * b.waveFrequency) * b.waveAmplitude;
            } else {
                b.y += b.vy * delta;
            }

            if (b.x < arena.x - 50 || b.x > arena.x + Arena.WIDTH + 50 ||
                b.y < arena.y - 50 || b.y > arena.y + Arena.HEIGHT + 50) {
                b.alive = false;
            }
        }
    }

    public boolean isAttackFinished() {
        return arena.inBulletHell && arena.attackTimer >= arena.attackDuration;
    }

    public void endAttack() {
        arena.inBulletHell = false;
        bullets.clear();
    }

    public void reset() {
        arena.inBulletHell = false;
        bullets.clear();
        circleBurstAngleOffset = 0f;
        waveRowSpawnIndex = 0;
    }

    private void spawnBulletPattern() {
        // Determine if this is Father for slightly harder patterns
        boolean isFather = currentEnemy != null && currentEnemy.getName().equals("Father");
        
        switch (currentPattern) {
            case HORIZONTAL_WAVE:
                // Father: Slightly faster bullets
                BulletSpawner.spawnHorizontalWave(bullets, arena, isFather ? 290f : 260f);
                break;
            case VERTICAL_RAIN:
                // Father: Slightly faster bullets
                BulletSpawner.spawnVerticalRain(bullets, arena, isFather ? 290f : 260f);
                break;
            case DIAGONAL_FAN:
                // Father: Slightly faster bullets
                waveRowSpawnIndex = BulletSpawner.spawnWaveRows(bullets, arena, 
                    isFather ? 160f : 140f, // Slightly faster
                    isFather ? 32f : 30f,   // Slightly larger amplitude
                    0.015f, 
                    waveRowSpawnIndex);
                break;
            case CIRCLE_BURST:
                // Father: Slightly more bullets and faster
                circleBurstAngleOffset = BulletSpawner.spawnCircleBurst(bullets, arena, 
                    isFather ? 200f : 180f,  // Slightly faster bullets
                    circleBurstAngleOffset, 
                    isFather ? 9 : 8);       // One more bullet
                break;
        }
    }
}
