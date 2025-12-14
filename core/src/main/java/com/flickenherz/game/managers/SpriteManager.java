package com.flickenherz.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.flickenherz.game.entities.Enemy;
import com.flickenherz.game.bullet.Arena;

public class SpriteManager {

    private final Texture teddyTexture;
    private final Texture dollTexture;
    private final Texture cuddlefiendTexture;
    private final Texture fatherTexture;
    private final Texture girlTexture;

    public final float teddyX;
    public final float teddyY;
    public final float teddyWidth;
    public final float teddyHeight;

    public final float dollX;
    public final float dollY;
    public final float dollWidth;
    public final float dollHeight;

    public final float cuddlefiendX;
    public final float cuddlefiendY;
    public final float cuddlefiendWidth;
    public final float cuddlefiendHeight;

    public final float fatherX;
    public final float fatherY;
    public final float fatherWidth;
    public final float fatherHeight;

    public final float girlX;
    public final float girlY;
    public final float girlWidth;
    public final float girlHeight;

    private static final float TEDDY_SCALE = 0.50f;
    private static final float DOLL_SCALE = 0.50f;
    private static final float CUDDLEFIEND_SCALE = 0.50f;
    private static final float FATHER_SCALE = 0.90f;
    private static final float GIRL_SCALE = 0.85f;

    public SpriteManager(int virtualWidth, int virtualHeight, Arena arena) {
        teddyTexture = new Texture(Gdx.files.internal("teddy.png"));
        girlTexture = new Texture(Gdx.files.internal("girl.png"));
        dollTexture = new Texture(Gdx.files.internal("doll.png"));
        cuddlefiendTexture = new Texture(Gdx.files.internal("cuddlefiend.png"));
        fatherTexture = new Texture(Gdx.files.internal("father.png"));

        // Teddy
        teddyWidth = teddyTexture.getWidth() * TEDDY_SCALE;
        teddyHeight = teddyTexture.getHeight() * TEDDY_SCALE;
        teddyX = virtualWidth * 0.59f - teddyWidth / 2.2f;
        teddyY = arena.y + Arena.HEIGHT + -250f;

        // Doll
        dollWidth = dollTexture.getWidth() * DOLL_SCALE;
        dollHeight = dollTexture.getHeight() * DOLL_SCALE;
        dollX = virtualWidth * 0.59f - dollWidth / 2.2f;
        dollY = arena.y + Arena.HEIGHT + -250f;

        // Cuddlefiend
        cuddlefiendWidth = cuddlefiendTexture.getWidth() * CUDDLEFIEND_SCALE;
        cuddlefiendHeight = cuddlefiendTexture.getHeight() * CUDDLEFIEND_SCALE;
        cuddlefiendX = virtualWidth * 0.59f - cuddlefiendWidth / 2.2f;
        cuddlefiendY = arena.y + Arena.HEIGHT + -320f;

        // Father
        fatherWidth = fatherTexture.getWidth() * FATHER_SCALE;
        fatherHeight = fatherTexture.getHeight() * FATHER_SCALE;
        fatherX = virtualWidth * 0.59f - fatherWidth / 2.2f;
        fatherY = arena.y + Arena.HEIGHT + -250f;

        // Girl
        girlWidth = girlTexture.getWidth() * GIRL_SCALE;
        girlHeight = girlTexture.getHeight() * GIRL_SCALE;
        girlX = arena.x + Arena.WIDTH * 0.25f - girlWidth / 1.4f;
        girlY = arena.y + Arena.HEIGHT * 0.50f - girlHeight / 2.3f;
    }

    public void drawEnemy(SpriteBatch batch, Enemy enemy, boolean hitActive) {
        float scaleFactor = hitActive ? 1.08f : 1f;

        if (enemy.getType() == Enemy.EnemyType.TEDDY) {
            drawSprite(batch, teddyTexture, teddyX, teddyY, teddyWidth, teddyHeight, scaleFactor, hitActive);
        } else if (enemy.getType() == Enemy.EnemyType.DOLL) {
            drawSprite(batch, dollTexture, dollX, dollY, dollWidth, dollHeight, scaleFactor, hitActive);
        } else {
            // For OTHER type, distinguish by name
            if (enemy.getName().equals("Father")) {
                drawSprite(batch, fatherTexture, fatherX, fatherY, fatherWidth, fatherHeight, scaleFactor, hitActive);
            } else {
                drawSprite(batch, cuddlefiendTexture, cuddlefiendX, cuddlefiendY, cuddlefiendWidth, cuddlefiendHeight, scaleFactor, hitActive);
            }
        }
    }

    public void drawGirl(SpriteBatch batch, boolean hitActive, boolean inBulletHell) {
        if (inBulletHell) return;

        float scaleFactor = hitActive ? 1.08f : 1f;
        drawSprite(batch, girlTexture, girlX, girlY, girlWidth, girlHeight, scaleFactor, hitActive);
    }

    private void drawSprite(SpriteBatch batch, Texture tex, float x, float y, float width, float height,
                            float scaleFactor, boolean hitActive) {
        float drawW = width * scaleFactor;
        float drawH = height * scaleFactor;
        float offX = (drawW - width) / 2f;
        float offY = (drawH - height) / 2f;

        if (hitActive) {
            batch.setColor(1f, 0.6f, 0.6f, 1f);
        } else {
            batch.setColor(1f, 1f, 1f, 1f);
        }

        batch.draw(tex, x - offX, y - offY, drawW, drawH);
    }

    public void drawGirlWithAnimation(SpriteBatch batch, boolean hitActive, boolean inBulletHell, float animationProgress) {
        if (inBulletHell) return;

        float scaleFactor = hitActive ? 1.08f : 1f;
        drawSpriteWithAnimation(batch, girlTexture, girlX, girlY, girlWidth, girlHeight, scaleFactor, hitActive, animationProgress);
    }

    private void drawSpriteWithAnimation(SpriteBatch batch, Texture tex, float x, float y, float width, float height,
                                        float scaleFactor, boolean hitActive, float animationProgress) {
        float drawW = width * scaleFactor;
        float drawH = height * scaleFactor;
        float offX = (drawW - width) / 2f;
        float offY = (drawH - height) / 2f;

        if (hitActive) {
            batch.setColor(1f, 0.6f, 0.6f, 1f);
        } else {
            batch.setColor(1f, 1f, 1f, 1f);
        }

        // Apply swing animation: rotation based on progress
        // animationProgress goes from 1.0 (start) to 0.0 (end)
        float maxRotation = 15f; // max rotation in degrees
        float rotation = maxRotation * (1f - animationProgress); // rotate forward during attack
        
        // Move slightly forward during attack
        float moveAmount = 20f * (1f - animationProgress);
        float animatedX = x + moveAmount;
        float animatedY = y;

        // Draw with rotation (using origin at center of sprite for proper rotation)
        float originX = drawW / 2f;
        float originY = drawH / 2f;
        batch.draw(tex, animatedX - offX, animatedY - offY, originX, originY, drawW, drawH, 1f, 1f, rotation, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
    }

    public void dispose() {
        teddyTexture.dispose();
        dollTexture.dispose();
        cuddlefiendTexture.dispose();
        fatherTexture.dispose();
        girlTexture.dispose();
    }
}
