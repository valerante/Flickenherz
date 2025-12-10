package com.flickenherz.game;

import com.badlogic.gdx.utils.Array;

public class BulletSpawner {

    public static void spawnHorizontalWave(Array<Bullet> bullets, Arena arena, float speed) {
        boolean fromLeft = Math.random() < 0.5;
        float y = arena.y + 40 + (float) Math.random() * (Arena.HEIGHT - 80);

        Bullet b = new Bullet();
        b.radius = 10f;
        b.y = y;

        if (fromLeft) {
            b.x = arena.x - 30f;
            b.vx = speed;
        } else {
            b.x = arena.x + Arena.WIDTH + 30f;
            b.vx = -speed;
        }

        b.vy = 0f;
        bullets.add(b);
    }

    public static void spawnVerticalRain(Array<Bullet> bullets, Arena arena, float speed) {
        Bullet b = new Bullet();
        b.radius = 10f;
        b.x = arena.x + 40f + (float) Math.random() * (Arena.WIDTH - 80f);
        b.y = arena.y + Arena.HEIGHT + 30f;
        b.vx = 0f;
        b.vy = -speed;
        bullets.add(b);
    }

    public static void spawnDiagonalFan(Array<Bullet> bullets, Arena arena, float horizontalSpeed, float vyStep) {
        boolean fromLeft = Math.random() < 0.5;
        float startY = arena.y + Arena.HEIGHT / 2f;

        for (int i = -1; i <= 1; i++) {
            Bullet b = new Bullet();
            b.radius = 9f;
            b.y = startY + i * 30f;

            if (fromLeft) {
                b.x = arena.x - 30f;
                b.vx = horizontalSpeed;
            } else {
                b.x = arena.x + Arena.WIDTH + 30f;
                b.vx = -horizontalSpeed;
            }

            b.vy = i * vyStep;
            bullets.add(b);
        }
    }

    public static float spawnCircleBurst(Array<Bullet> bullets, Arena arena, float speed, float angleOffset, int count) {
        float cx = arena.x + Arena.WIDTH / 2f;
        float cy = arena.y + Arena.HEIGHT / 2f;

        for (int i = 0; i < count; i++) {
            float baseAngle = (float) (2 * Math.PI * i / count);
            float angle = baseAngle + angleOffset;

            Bullet b = new Bullet();
            b.radius = 8f;
            b.x = cx;
            b.y = cy;
            b.vx = (float) Math.cos(angle) * speed;
            b.vy = (float) Math.sin(angle) * speed;
            bullets.add(b);
        }

        angleOffset += 0.15f;
        if (angleOffset > Math.PI * 2f) angleOffset -= (float) (Math.PI * 2f);
        return angleOffset;
    }
}
