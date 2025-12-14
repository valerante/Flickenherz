package com.flickenherz.game.bullet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

// Erzeugt verschiedene Kugelangriffsmuster
public class BulletSpawner {

    // Erzeuge horizontale Kugel mit Wellenbewegung
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

    // Erzeuge vertikale Kugel von oben
    public static void spawnVerticalRain(Array<Bullet> bullets, Arena arena, float speed, Pool<Bullet> pool) {
        float x = arena.x + 40f + (float) Math.random() * (Arena.WIDTH - 80f);
        float y = arena.y + Arena.HEIGHT + 30f;
        
        Bullet b = pool.obtain();
        b.reset(x, y, 0f, -speed, 10f);
        bullets.add(b);
    }

    // Erzeuge horizontale Welle in gleichmäßig verteilten Reihen
    public static int spawnWaveRows(Array<Bullet> bullets, Arena arena, float horizontalSpeed, float waveAmplitude, float waveFrequency, int spawnIndex, Pool<Bullet> pool) {
        boolean fromLeft = Math.random() < 0.5;
        
        int totalRows = 7;
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

        b.reset(x, yPosition, vx, 0f, 9f);
        b.waveAmplitude = waveAmplitude;
        b.waveFrequency = waveFrequency;
        bullets.add(b);
        
        return spawnIndex + 1;
    }

    // Erzeuge kreisförmigen Kugelschwall aus der Mitte
    public static float spawnCircleBurst(Array<Bullet> bullets, Arena arena, float speed, float angleOffset, int count, Pool<Bullet> pool) {
        float cx = arena.x + Arena.WIDTH / 2f;
        float cy = arena.y + Arena.HEIGHT / 2f;
        
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

    // Erzeuge vertikalen Kugelstrahl
    public static void spawnTrackingBeam(Array<Bullet> bullets, Arena arena, float beamX, float speed, Pool<Bullet> pool) {
        float beamWidth = 40f;
        int bulletsPerRow = 3;
        
        for (int i = 0; i < bulletsPerRow; i++) {
            float offsetX = (i - 1) * (beamWidth / 2f);
            
            Bullet b = pool.obtain();
            b.reset(beamX + offsetX, arena.y + Arena.HEIGHT, 0f, -speed, 10f);
            bullets.add(b);
        }
    }

    // Erzeuge konvergierende Kugeln von allen Rändern zur Mitte
    public static void spawnConvergingCross(Array<Bullet> bullets, Arena arena, float speed, Pool<Bullet> pool) {
        float cx = arena.x + Arena.WIDTH / 2f;
        float cy = arena.y + Arena.HEIGHT / 2f;

        float[][] corners = {
            {arena.x, arena.y},
            {arena.x + Arena.WIDTH, arena.y},
            {arena.x, arena.y + Arena.HEIGHT},
            {arena.x + Arena.WIDTH, arena.y + Arena.HEIGHT}
        };

        for (float[] corner : corners) {
            Bullet b = pool.obtain();
            float x = corner[0];
            float y = corner[1];

            float dx = cx - x;
            float dy = cy - y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            
            b.reset(x, y, (dx / len) * speed, (dy / len) * speed, 8f);
            bullets.add(b);
        }

        float[][] edges = {
            {cx, arena.y},
            {cx, arena.y + Arena.HEIGHT},
            {arena.x, cy},
            {arena.x + Arena.WIDTH, cy}
        };

        for (float[] edge : edges) {
            float x = edge[0];
            float y = edge[1];

            float dx = cx - x;
            float dy = cy - y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            
            if (len > 0) {
                Bullet b = pool.obtain();
                b.reset(x, y, (dx / len) * speed, (dy / len) * speed, 8f);
                bullets.add(b);
            }
        }

        float[][] offsets = {
            {arena.x + Arena.WIDTH * 0.25f, arena.y},
            {arena.x + Arena.WIDTH * 0.75f, arena.y},
            {arena.x + Arena.WIDTH * 0.25f, arena.y + Arena.HEIGHT},
            {arena.x + Arena.WIDTH * 0.75f, arena.y + Arena.HEIGHT},
            {arena.x, arena.y + Arena.HEIGHT * 0.25f},
            {arena.x, arena.y + Arena.HEIGHT * 0.75f},
            {arena.x + Arena.WIDTH, arena.y + Arena.HEIGHT * 0.25f},
            {arena.x + Arena.WIDTH, arena.y + Arena.HEIGHT * 0.75f}
        };

        for (float[] offset : offsets) {
            float x = offset[0];
            float y = offset[1];

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