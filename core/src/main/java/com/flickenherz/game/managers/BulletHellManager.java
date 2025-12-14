package com.flickenherz.game.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import com.flickenherz.game.entities.Enemy;
import com.flickenherz.game.bullet.Bullet;
import com.flickenherz.game.bullet.BulletPattern;
import com.flickenherz.game.bullet.BulletSpawner;
import com.flickenherz.game.bullet.Arena;

// Verwaltet Bullet-Hell-Angriffssequenzen
public class BulletHellManager {

    private final Array<Bullet> bullets;
    private final Arena arena;
    private final AudioManager audio;
    
    // Objektpool für Kugelwiederverwendung
    private final Pool<Bullet> bulletPool = new Pool<Bullet>(200, 400) {
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };

    private BulletPattern currentPattern = BulletPattern.HORIZONTAL_WAVE;
    private float circleBurstAngleOffset = 0f;
    private int waveRowSpawnIndex = 0;
    private Enemy currentEnemy = null;
    private static final float BOUNDARY_MARGIN = 50f;
    private static final float TWO_PI = (float)(Math.PI * 2);

    public BulletHellManager(Arena arena, AudioManager audio) {
        this.bullets = new Array<>();
        this.arena = arena;
        this.audio = audio;
    }

    public Array<Bullet> getBullets() {
        return bullets;
    }

    // Starte Bullet-Hell-Angriff mit Muster basierend auf Gegnertyp
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

    // Aktualisiere Kugeln und verarbeite Spawning
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

        // Grenzwerte für Despawn-Prüfungen cachen
        float minX = arena.x - BOUNDARY_MARGIN;
        float maxX = arena.x + Arena.WIDTH + BOUNDARY_MARGIN;
        float minY = arena.y - BOUNDARY_MARGIN;
        float maxY = arena.y + Arena.HEIGHT + BOUNDARY_MARGIN;

        // Rückwärts iterieren für sicheres Entfernen
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

    public boolean isAttackFinished() {
        return arena.inBulletHell && arena.attackTimer >= arena.attackDuration;
    }

    // Beende Angriff und gib Kugeln an Pool zurück
    public void endAttack() {
        arena.inBulletHell = false;
        bulletPool.freeAll(bullets);
        bullets.clear();
    }

    // Manager auf Anfangszustand zurücksetzen
    public void reset() {
        arena.inBulletHell = false;
        bulletPool.freeAll(bullets);
        bullets.clear();
        circleBurstAngleOffset = 0f;
        waveRowSpawnIndex = 0;
    }
    
    public Pool<Bullet> getBulletPool() {
        return bulletPool;
    }

    // Erzeuge Kugeln basierend auf aktuellem Muster (Vater-Gegner hat höhere Schwierigkeit)
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
