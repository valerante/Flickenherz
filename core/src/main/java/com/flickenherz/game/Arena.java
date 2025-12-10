package com.flickenherz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Arena {
    public static final float WIDTH = 600f;
    public static final float HEIGHT = 320f;

    public final float x;
    public final float y;

    public float soulX;
    public float soulY;
    public final float soulRadius = 12f;
    public float soulSpeed = 450f;

    public boolean inBulletHell = false;
    public float attackTimer = 0f;
    public float attackDuration = 4f;
    public float spawnTimer = 0f;
    public float spawnInterval = 0.25f;
    public float iFrameTimer = 0f;

    public Arena(int virtualWidth, float y) {
        this.x = (virtualWidth - WIDTH) / 2f;
        this.y = y;
        this.soulX = x + WIDTH / 2f;
        this.soulY = y + HEIGHT / 2f;
    }

    public void resetSoul() {
        soulX = x + WIDTH / 2f;
        soulY = y + HEIGHT / 2f;
    }

    public void handleMovement(float delta) {
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;

        if (dx != 0 || dy != 0) {
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            dx /= len;
            dy /= len;

            soulX += dx * soulSpeed * delta;
            soulY += dy * soulSpeed * delta;
        }

        if (soulX - soulRadius < x) soulX = x + soulRadius;
        if (soulX + soulRadius > x + WIDTH) soulX = x + WIDTH - soulRadius;
        if (soulY - soulRadius < y) soulY = y + soulRadius;
        if (soulY + soulRadius > y + HEIGHT) soulY = y + HEIGHT - soulRadius;
    }

    public void drawSoul(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(soulX, soulY, soulRadius + 6f);
        shapeRenderer.circle(soulX, soulY, soulRadius);
    }
}
