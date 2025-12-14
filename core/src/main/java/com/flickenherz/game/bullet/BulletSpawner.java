package com.flickenherz.game.bullet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class BulletSpawner {

    public static void spawnHorizontalWave(Array<Bullet> bullets, Arena arena, float speed, Pool<Bullet> pool) {
        boolean fromLeft = Math.random() < 0.5;
        float y = arena.y + 40 + (float) Math.random() * (Arena.HEIGHT - 80);

        Bullet b = pool.obtain();
        float x, vx;
        if (fromLeft) {
            x = arena.x - 30f;
            vx = speed;
        } else {
            x = arena.x + Arena.WIDTH + 30f;
            vx = -speed;
        }
        
        b.reset(x, y, vx, 0f, 10f);
        bullets.add(b);
    }

    public static void spawnVerticalRain(Array<Bullet> bullets, Arena arena, float speed, Pool<Bullet> pool) {
        float x = arena.x + 40f + (float) Math.random() * (Arena.WIDTH - 80f);
        float y = arena.y + Arena.HEIGHT + 30f;
        
        Bullet b = pool.obtain();
        b.reset(x, y, 0f, -speed, 10f);
        bullets.add(b);
    }

    public static int spawnWaveRows(Array<Bullet> bullets, Arena arena, float horizontalSpeed, float waveAmplitude, float waveFrequency, int spawnIndex, Pool<Bullet> pool) {
        boolean fromLeft = Math.random() < 0.5;
        
        // Calculate Y position - cycle through the arena height
        int totalRows = 7; // Number of evenly spaced rows covering the arena
        float rowSpacing = Arena.HEIGHT / (float) (totalRows - 1);
        float yPosition = arena.y + (spawnIndex % totalRows) * rowSpacing;
        
        Bullet b = pool.obtain();
        float x, vx;
        if (fromLeft) {
            x = arena.x - 30f;
            vx = horizontalSpeed;
        } else {
            x = arena.x + Arena.WIDTH + 30f;
            vx = -horizontalSpeed;
        }

        // Reset bullet with wave parameters
        b.reset(x, yPosition, vx, 0f, 9f);
        b.waveAmplitude = waveAmplitude;
        b.waveFrequency = waveFrequency;
        bullets.add(b);
        
        return spawnIndex + 1;
    }

    public static float spawnCircleBurst(Array<Bullet> bullets, Arena arena, float speed, float angleOffset, int count, Pool<Bullet> pool) {
        float cx = arena.x + Arena.WIDTH / 2f;
        float cy = arena.y + Arena.HEIGHT / 2f;
        
        // Pre-calculate angle increment
        float angleIncrement = (float) (2 * Math.PI / count);

        for (int i = 0; i < count; i++) {
            float angle = angleIncrement * i + angleOffset;
            float cosAngle = (float) Math.cos(angle);
            float sinAngle = (float) Math.sin(angle);

            Bullet b = pool.obtain();
            b.reset(cx, cy, cosAngle * speed, sinAngle * speed, 6f);
            bullets.add(b);
        }

        angleOffset += 0.15f;
        if (angleOffset > Math.PI * 2f) angleOffset -= (float) (Math.PI * 2f);
        return angleOffset;
    }

    // New attack pattern: Vertical beam that tracks player position then locks and shoots
    public static void spawnTrackingBeam(Array<Bullet> bullets, Arena arena, float beamX, float speed, Pool<Bullet> pool) {
        // Spawn bullets along the vertical line at beamX position
        // Create a dense vertical line of bullets
        float beamWidth = 40f; // Width of the beam
        int bulletsPerRow = 3; // Bullets across the beam width
        
        // Spawn bullets from top of arena moving down
        for (int i = 0; i < bulletsPerRow; i++) {
            float offsetX = (i - 1) * (beamWidth / 2f); // -1, 0, 1 positions
            
            Bullet b = pool.obtain();
            b.reset(beamX + offsetX, arena.y + Arena.HEIGHT, 0f, -speed, 10f);
            bullets.add(b);
        }
    }

    // New attack pattern: Bullets converge from all four corners and edges
    public static void spawnConvergingCross(Array<Bullet> bullets, Arena arena, float speed, Pool<Bullet> pool) {
        float cx = arena.x + Arena.WIDTH / 2f;
        float cy = arena.y + Arena.HEIGHT / 2f;

        // Four corners of the arena - these create diagonal coverage
        float[][] corners = {
            {arena.x, arena.y},                           // Bottom-left
            {arena.x + Arena.WIDTH, arena.y},             // Bottom-right
            {arena.x, arena.y + Arena.HEIGHT},            // Top-left
            {arena.x + Arena.WIDTH, arena.y + Arena.HEIGHT} // Top-right
        };

        for (float[] corner : corners) {
            Bullet b = pool.obtain();
            float x = corner[0];
            float y = corner[1];

            // Calculate direction towards center
            float dx = cx - x;
            float dy = cy - y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            
            b.reset(x, y, (dx / len) * speed, (dy / len) * speed, 8f);
            bullets.add(b);
        }

        // Add bullets from edge midpoints to cover the cross
        float[][] edges = {
            {cx, arena.y},                    // Bottom center
            {cx, arena.y + Arena.HEIGHT},     // Top center
            {arena.x, cy},                    // Left center
            {arena.x + Arena.WIDTH, cy}       // Right center
        };

        for (float[] edge : edges) {
            float x = edge[0];
            float y = edge[1];

            // Calculate direction towards center
            float dx = cx - x;
            float dy = cy - y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            
            if (len > 0) {
                Bullet b = pool.obtain();
                b.reset(x, y, (dx / len) * speed, (dy / len) * speed, 8f);
                bullets.add(b);
            }
        }

        // Add offset bullets between corners and edges to fill diagonal gaps
        float[][] offsets = {
            {arena.x + Arena.WIDTH * 0.25f, arena.y},                      // Bottom left-quarter
            {arena.x + Arena.WIDTH * 0.75f, arena.y},                      // Bottom right-quarter
            {arena.x + Arena.WIDTH * 0.25f, arena.y + Arena.HEIGHT},       // Top left-quarter
            {arena.x + Arena.WIDTH * 0.75f, arena.y + Arena.HEIGHT},       // Top right-quarter
            {arena.x, arena.y + Arena.HEIGHT * 0.25f},                     // Left bottom-quarter
            {arena.x, arena.y + Arena.HEIGHT * 0.75f},                     // Left top-quarter
            {arena.x + Arena.WIDTH, arena.y + Arena.HEIGHT * 0.25f},       // Right bottom-quarter
            {arena.x + Arena.WIDTH, arena.y + Arena.HEIGHT * 0.75f}        // Right top-quarter
        };

        for (float[] offset : offsets) {
            float x = offset[0];
            float y = offset[1];

            // Calculate direction towards center
            float dx = cx - x;
            float dy = cy - y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            
            if (len > 0) {
                Bullet b = pool.obtain();
                b.reset(x, y, (dx / len) * speed, (dy / len) * speed, 7f);
                bullets.add(b);
            }
        }
    }
}