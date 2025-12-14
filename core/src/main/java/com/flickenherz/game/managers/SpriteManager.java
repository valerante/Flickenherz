package com.flickenherz.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.flickenherz.game.entities.Enemy;
import com.flickenherz.game.bullet.Arena;

// Verwaltet Sprite-Rendering und -Positionierung
public class SpriteManager {

    private final Texture teddyTexture;
    private final Texture dollTexture;
    private final Texture cuddlefiendTexture;
    private final Texture fatherTexture;
    private final Texture girlTexture;

    // Vorberechnete Sprite-Positionen
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

    // Sprite-Skalierungsfaktoren
    private static final float TEDDY_SCALE = 0.40f;
    private static final float DOLL_SCALE = 0.50f;
    private static final float CUDDLEFIEND_SCALE = 0.50f;
    private static final float FATHER_SCALE = 0.90f;
    private static final float GIRL_SCALE = 0.85f;
    
    // Gecachte Positionierungskonstanten
    private static final float VIRTUAL_WIDTH_MULTIPLIER = 0.59f;
    private static final float WIDTH_DIVISOR = 2.2f;
    private static final float HEIGHT_OFFSET = -250f;
    private static final float CUDDLEFIEND_HEIGHT_OFFSET = -320f;
    private static final float GIRL_ARENA_WIDTH_MULT = 0.25f;
    private static final float GIRL_ARENA_HEIGHT_MULT = 0.50f;
    private static final float GIRL_WIDTH_DIVISOR = 1.4f;
    private static final float GIRL_HEIGHT_DIVISOR = 2.3f;

    // Lade Texturen und berechne Sprite-Positionen
    public SpriteManager(int virtualWidth, int virtualHeight, Arena arena) {
        teddyTexture = new Texture(Gdx.files.internal("teddy.png"));
        girlTexture = new Texture(Gdx.files.internal("girl.png"));
        dollTexture = new Texture(Gdx.files.internal("doll.png"));
        cuddlefiendTexture = new Texture(Gdx.files.internal("cuddlefiend.png"));
        fatherTexture = new Texture(Gdx.files.internal("father.png"));

        float centerX = virtualWidth * VIRTUAL_WIDTH_MULTIPLIER;
        
        teddyWidth = teddyTexture.getWidth() * TEDDY_SCALE;
        teddyHeight = teddyTexture.getHeight() * TEDDY_SCALE;
        teddyX = centerX - teddyWidth / WIDTH_DIVISOR;
        teddyY = arena.y + Arena.HEIGHT + HEIGHT_OFFSET;

        dollWidth = dollTexture.getWidth() * DOLL_SCALE;
        dollHeight = dollTexture.getHeight() * DOLL_SCALE;
        dollX = centerX - dollWidth / WIDTH_DIVISOR;
        dollY = arena.y + Arena.HEIGHT + HEIGHT_OFFSET;

        cuddlefiendWidth = cuddlefiendTexture.getWidth() * CUDDLEFIEND_SCALE;
        cuddlefiendHeight = cuddlefiendTexture.getHeight() * CUDDLEFIEND_SCALE;
        cuddlefiendX = centerX - cuddlefiendWidth / WIDTH_DIVISOR;
        cuddlefiendY = arena.y + Arena.HEIGHT + CUDDLEFIEND_HEIGHT_OFFSET;

        fatherWidth = fatherTexture.getWidth() * FATHER_SCALE;
        fatherHeight = fatherTexture.getHeight() * FATHER_SCALE;
        fatherX = centerX - fatherWidth / WIDTH_DIVISOR;
        fatherY = arena.y + Arena.HEIGHT + HEIGHT_OFFSET;

        girlWidth = girlTexture.getWidth() * GIRL_SCALE;
        girlHeight = girlTexture.getHeight() * GIRL_SCALE;
        girlX = arena.x + Arena.WIDTH * GIRL_ARENA_WIDTH_MULT - girlWidth / GIRL_WIDTH_DIVISOR;
        girlY = arena.y + Arena.HEIGHT * GIRL_ARENA_HEIGHT_MULT - girlHeight / GIRL_HEIGHT_DIVISOR;
    }

    // Zeichne Gegner-Sprite mit Treffereffekt
    public void drawEnemy(SpriteBatch batch, Enemy enemy, boolean hitActive) {
        float scaleFactor = hitActive ? 1.08f : 1f;

        if (enemy.getType() == Enemy.EnemyType.TEDDY) {
            drawSprite(batch, teddyTexture, teddyX, teddyY, teddyWidth, teddyHeight, scaleFactor, hitActive);
        } else if (enemy.getType() == Enemy.EnemyType.DOLL) {
            drawSprite(batch, dollTexture, dollX, dollY, dollWidth, dollHeight, scaleFactor, hitActive);
        } else {
            if (enemy.getName().equals("Father")) {
                drawSprite(batch, fatherTexture, fatherX, fatherY, fatherWidth, fatherHeight, scaleFactor, hitActive);
            } else {
                drawSprite(batch, cuddlefiendTexture, cuddlefiendX, cuddlefiendY, cuddlefiendWidth, cuddlefiendHeight, scaleFactor, hitActive);
            }
        }
    }

    // Zeichne Mädchen-Sprite mit Treffereffekt
    public void drawGirl(SpriteBatch batch, boolean hitActive, boolean inBulletHell) {
        if (inBulletHell) return;

        float scaleFactor = hitActive ? 1.08f : 1f;
        drawSprite(batch, girlTexture, girlX, girlY, girlWidth, girlHeight, scaleFactor, hitActive);
    }

    // Zeichne Sprite mit Skalierung und Farbton
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

    // Zeichne Mädchen mit Angriffsanimation
    public void drawGirlWithAnimation(SpriteBatch batch, boolean hitActive, boolean inBulletHell, float animationProgress) {
        if (inBulletHell) return;

        float scaleFactor = hitActive ? 1.08f : 1f;
        drawSpriteWithAnimation(batch, girlTexture, girlX, girlY, girlWidth, girlHeight, scaleFactor, hitActive, animationProgress);
    }

    // Zeichne Sprite mit Rotations- und Bewegungsanimation
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

        float maxRotation = 15f;
        float rotation = maxRotation * (1f - animationProgress);
        
        float moveAmount = 20f * (1f - animationProgress);
        float animatedX = x + moveAmount;
        float animatedY = y;

        float originX = drawW / 2f;
        float originY = drawH / 2f;
        batch.draw(tex, animatedX - offX, animatedY - offY, originX, originY, drawW, drawH, 1f, 1f, rotation, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
    }

    // Texturen freigeben
    public void dispose() {
        teddyTexture.dispose();
        dollTexture.dispose();
        cuddlefiendTexture.dispose();
        fatherTexture.dispose();
        girlTexture.dispose();
    }
}
