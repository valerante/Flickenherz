package com.flickenherz.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.flickenherz.game.entities.Enemy;
import com.flickenherz.game.bullet.Arena;

/**
 * Manager for sprite rendering and positioning in the game.
 * Handles loading, positioning, and drawing of all character sprites including
 * enemies (teddy, doll, cuddlefiend, father) and the player character (girl).
 * Pre-calculates sprite positions for optimal performance during rendering.
 */
public class SpriteManager {

    /** Texture for the teddy bear enemy */
    private final Texture teddyTexture;
    
    /** Texture for the porcelain doll enemy */
    private final Texture dollTexture;
    
    /** Texture for the cuddlefiend enemy */
    private final Texture cuddlefiendTexture;
    
    /** Texture for the father enemy (final boss) */
    private final Texture fatherTexture;
    
    /** Texture for the girl player character */
    private final Texture girlTexture;

    /** Pre-calculated X position for teddy sprite */
    public final float teddyX;
    
    /** Pre-calculated Y position for teddy sprite */
    public final float teddyY;
    
    /** Pre-calculated width for teddy sprite */
    public final float teddyWidth;
    
    /** Pre-calculated height for teddy sprite */
    public final float teddyHeight;

    /** Pre-calculated X position for doll sprite */
    public final float dollX;
    
    /** Pre-calculated Y position for doll sprite */
    public final float dollY;
    
    /** Pre-calculated width for doll sprite */
    public final float dollWidth;
    
    /** Pre-calculated height for doll sprite */
    public final float dollHeight;

    /** Pre-calculated X position for cuddlefiend sprite */
    public final float cuddlefiendX;
    
    /** Pre-calculated Y position for cuddlefiend sprite */
    public final float cuddlefiendY;
    
    /** Pre-calculated width for cuddlefiend sprite */
    public final float cuddlefiendWidth;
    
    /** Pre-calculated height for cuddlefiend sprite */
    public final float cuddlefiendHeight;

    /** Pre-calculated X position for father sprite */
    public final float fatherX;
    
    /** Pre-calculated Y position for father sprite */
    public final float fatherY;
    
    /** Pre-calculated width for father sprite */
    public final float fatherWidth;
    
    /** Pre-calculated height for father sprite */
    public final float fatherHeight;

    /** Pre-calculated X position for girl sprite */
    public final float girlX;
    
    /** Pre-calculated Y position for girl sprite */
    public final float girlY;
    
    /** Pre-calculated width for girl sprite */
    public final float girlWidth;
    
    /** Pre-calculated height for girl sprite */
    public final float girlHeight;

    /** Scale factor for teddy bear sprite */
    private static final float TEDDY_SCALE = 0.40f;
    
    /** Scale factor for porcelain doll sprite */
    private static final float DOLL_SCALE = 0.50f;
    
    /** Scale factor for cuddlefiend sprite */
    private static final float CUDDLEFIEND_SCALE = 0.50f;
    
    /** Scale factor for father sprite */
    private static final float FATHER_SCALE = 0.90f;
    
    /** Scale factor for girl sprite */
    private static final float GIRL_SCALE = 0.85f;
    
    /** Cached constant for virtual width multiplier optimization */
    private static final float VIRTUAL_WIDTH_MULTIPLIER = 0.59f;
    
    /** Cached constant for width divisor in positioning calculations */
    private static final float WIDTH_DIVISOR = 2.2f;
    
    /** Cached constant for height offset in positioning */
    private static final float HEIGHT_OFFSET = -250f;
    
    /** Cached constant for cuddlefiend-specific height offset */
    private static final float CUDDLEFIEND_HEIGHT_OFFSET = -320f;
    
    /** Cached constant for girl arena width multiplier */
    private static final float GIRL_ARENA_WIDTH_MULT = 0.25f;
    
    /** Cached constant for girl arena height multiplier */
    private static final float GIRL_ARENA_HEIGHT_MULT = 0.50f;
    
    /** Cached constant for girl width divisor */
    private static final float GIRL_WIDTH_DIVISOR = 1.4f;
    
    /** Cached constant for girl height divisor */
    private static final float GIRL_HEIGHT_DIVISOR = 2.3f;

    /**
     * Creates a new sprite manager and initializes all sprite textures and positions.
     * Pre-calculates all sprite positions for optimal rendering performance.
     * 
     * @param virtualWidth Virtual width of the game screen
     * @param virtualHeight Virtual height of the game screen
     * @param arena Arena instance for positioning calculations
     */
    public SpriteManager(int virtualWidth, int virtualHeight, Arena arena) {
        teddyTexture = new Texture(Gdx.files.internal("teddy.png"));
        girlTexture = new Texture(Gdx.files.internal("girl.png"));
        dollTexture = new Texture(Gdx.files.internal("doll.png"));
        cuddlefiendTexture = new Texture(Gdx.files.internal("cuddlefiend.png"));
        fatherTexture = new Texture(Gdx.files.internal("father.png"));

        float centerX = virtualWidth * VIRTUAL_WIDTH_MULTIPLIER;
        
        /* Calculate teddy bear sprite position and dimensions */
        teddyWidth = teddyTexture.getWidth() * TEDDY_SCALE;
        teddyHeight = teddyTexture.getHeight() * TEDDY_SCALE;
        teddyX = centerX - teddyWidth / WIDTH_DIVISOR;
        teddyY = arena.y + Arena.HEIGHT + HEIGHT_OFFSET;

        /* Calculate porcelain doll sprite position and dimensions */
        dollWidth = dollTexture.getWidth() * DOLL_SCALE;
        dollHeight = dollTexture.getHeight() * DOLL_SCALE;
        dollX = centerX - dollWidth / WIDTH_DIVISOR;
        dollY = arena.y + Arena.HEIGHT + HEIGHT_OFFSET;

        /* Calculate cuddlefiend sprite position and dimensions */
        cuddlefiendWidth = cuddlefiendTexture.getWidth() * CUDDLEFIEND_SCALE;
        cuddlefiendHeight = cuddlefiendTexture.getHeight() * CUDDLEFIEND_SCALE;
        cuddlefiendX = centerX - cuddlefiendWidth / WIDTH_DIVISOR;
        cuddlefiendY = arena.y + Arena.HEIGHT + CUDDLEFIEND_HEIGHT_OFFSET;

        /* Calculate father sprite position and dimensions */
        fatherWidth = fatherTexture.getWidth() * FATHER_SCALE;
        fatherHeight = fatherTexture.getHeight() * FATHER_SCALE;
        fatherX = centerX - fatherWidth / WIDTH_DIVISOR;
        fatherY = arena.y + Arena.HEIGHT + HEIGHT_OFFSET;

        /* Calculate girl sprite position and dimensions */
        girlWidth = girlTexture.getWidth() * GIRL_SCALE;
        girlHeight = girlTexture.getHeight() * GIRL_SCALE;
        girlX = arena.x + Arena.WIDTH * GIRL_ARENA_WIDTH_MULT - girlWidth / GIRL_WIDTH_DIVISOR;
        girlY = arena.y + Arena.HEIGHT * GIRL_ARENA_HEIGHT_MULT - girlHeight / GIRL_HEIGHT_DIVISOR;
    }

    /**
     * Draws the current enemy sprite with optional hit animation effect.
     * Automatically selects the correct texture based on enemy type.
     * 
     * @param batch SpriteBatch to draw with
     * @param enemy Enemy entity to draw
     * @param hitActive Whether to apply hit animation effect (red tint and scale)
     */
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

    /**
     * Draws the girl player sprite with optional hit animation effect.
     * Does not draw if currently in bullet hell mode.
     * 
     * @param batch SpriteBatch to draw with
     * @param hitActive Whether to apply hit animation effect (red tint and scale)
     * @param inBulletHell Whether bullet hell mode is active (skips drawing if true)
     */
    public void drawGirl(SpriteBatch batch, boolean hitActive, boolean inBulletHell) {
        if (inBulletHell) return;

        float scaleFactor = hitActive ? 1.08f : 1f;
        drawSprite(batch, girlTexture, girlX, girlY, girlWidth, girlHeight, scaleFactor, hitActive);
    }

    /**
     * Internal method to draw a sprite with scaling and color tinting.
     * Applies red tint and scaling when hit animation is active.
     * 
     * @param batch SpriteBatch to draw with
     * @param tex Texture to draw
     * @param x X position
     * @param y Y position
     * @param width Base width
     * @param height Base height
     * @param scaleFactor Scale multiplier for hit effect
     * @param hitActive Whether to apply red color tint
     */
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

    /**
     * Draws the girl sprite with attack animation and optional hit effect.
     * Does not draw if in bullet hell mode.
     * Animation includes rotation and forward movement based on progress.
     * 
     * @param batch SpriteBatch to draw with
     * @param hitActive Whether to apply hit animation effect
     * @param inBulletHell Whether bullet hell mode is active
     * @param animationProgress Animation progress from 1.0 (start) to 0.0 (end)
     */
    public void drawGirlWithAnimation(SpriteBatch batch, boolean hitActive, boolean inBulletHell, float animationProgress) {
        if (inBulletHell) return;

        float scaleFactor = hitActive ? 1.08f : 1f;
        drawSpriteWithAnimation(batch, girlTexture, girlX, girlY, girlWidth, girlHeight, scaleFactor, hitActive, animationProgress);
    }

    /**
     * Internal method to draw a sprite with animated rotation and movement.
     * Creates a swinging attack animation with rotation and forward motion.
     * 
     * @param batch SpriteBatch to draw with
     * @param tex Texture to draw
     * @param x Base X position
     * @param y Base Y position
     * @param width Base width
     * @param height Base height
     * @param scaleFactor Scale multiplier for hit effect
     * @param hitActive Whether to apply red color tint
     * @param animationProgress Progress of animation from 1.0 (start) to 0.0 (end)
     */
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

    /**
     * Disposes of all loaded textures to free memory.
     * Should be called when the sprite manager is no longer needed.
     */
    public void dispose() {
        teddyTexture.dispose();
        dollTexture.dispose();
        cuddlefiendTexture.dispose();
        fatherTexture.dispose();
        girlTexture.dispose();
    }
}
