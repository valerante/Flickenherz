package com.flickenherz.game.bullet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// Bullet hell combat arena
public class Arena {
    public static final float WIDTH = 600f;
    public static final float HEIGHT = 320f;

    public final float x;
    public final float y;

    public float soulX;
    public float soulY;
    public final float soulRadius = 12f;
    public final float soulRadiusSquared = soulRadius * soulRadius;
    public float soulSpeed = 350f;

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

    // Handle WASD movement with normalized diagonal speed
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
