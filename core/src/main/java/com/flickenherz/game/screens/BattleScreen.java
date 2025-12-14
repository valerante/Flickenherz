package com.flickenherz.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Array;

import com.flickenherz.game.entities.Player;
import com.flickenherz.game.entities.Enemy;
import com.flickenherz.game.controllers.BattleState;
import com.flickenherz.game.controllers.BattleController;
import com.flickenherz.game.managers.AudioManager;
import com.flickenherz.game.managers.BattleUI;
import com.flickenherz.game.managers.SpriteManager;
import com.flickenherz.game.managers.HitEffectManager;
import com.flickenherz.game.managers.BulletHellManager;
import com.flickenherz.game.managers.MenuController;
import com.flickenherz.game.bullet.Arena;
import com.flickenherz.game.bullet.Bullet;

public class BattleScreen implements Screen {

    // Auflösung
    public static final int VIRTUAL_WIDTH = 1920;
    public static final int VIRTUAL_HEIGHT = 1080;

    private final com.badlogic.gdx.Game game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final BitmapFont fontBig;

    private Player girl;
    private BattleController battleController;
    private BattleState battleState;

    private String messageText = "It's so quiet here...";
    private String actionText = "";

    private final Arena arena;
    private final Music battleMusic;

    // ===== MANAGERS =====
    private final AudioManager audio;
    private final BattleUI ui;
    private final SpriteManager spriteManager;
    private final HitEffectManager hitEffects;
    private final BulletHellManager bulletHell;
    private final MenuController menuController;
    // Background texture used inside the main white frame
    private final Texture hallwayTexture;

    // Attack animation
    private float attackAnimationTimer = 0f;
    private static final float ATTACK_ANIMATION_DURATION = 0.4f;
    private float postAttackDelay = 0f;
    private static final float POST_ATTACK_DELAY = 0.6f;
    private String pendingAttackMsg = "";
    private String pendingActionMsg = "";

    // Score tracking
    private long fightStartTime = 0;
    private int damageTakenThisFight = 0;
    private int perfectDodges = 0;
    
    // StringBuilder for text concatenation optimization
    private final StringBuilder stringBuilder = new StringBuilder(100);

    public BattleScreen() {
        this(null);
    }

    public BattleScreen(com.badlogic.gdx.Game game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        fontBig = new BitmapFont();

        girl = new Player("Mädchen");
        battleController = new BattleController();

        battleState = BattleState.PLAYER_TURN;

        arena = new Arena(VIRTUAL_WIDTH, 360f);
        arena.resetSoul();

        battleMusic = Gdx.audio.newMusic(Gdx.files.internal("battle_theme.ogg"));
        battleMusic.setLooping(true);
        battleMusic.setVolume(0.05f);
        battleMusic.play();

        audio = new AudioManager();
        ui = new BattleUI(font, fontBig);
        spriteManager = new SpriteManager(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, arena);
        hitEffects = new HitEffectManager();
        bulletHell = new BulletHellManager(arena, audio);
        menuController = new MenuController(audio);
        // Load hallway background
        hallwayTexture = new Texture(Gdx.files.internal("Hallway_background.png"));
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        updateBulletHell(delta);
        hitEffects.update(delta);
        
        // Update attack animation
        if (attackAnimationTimer > 0) {
            attackAnimationTimer -= delta;
        }
        
        // Update post-attack delay before enemy attacks
        if (postAttackDelay > 0) {
            postAttackDelay -= delta;
            if (postAttackDelay <= 0) {
                // Attack animation finished, now start enemy attack
                startEnemyAttack(pendingAttackMsg, pendingActionMsg);
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // Draw hallway background inside the main white box (unless in bullet hell)
        if (!arena.inBulletHell) {
            float outerMargin = 60f;
            float bgX = outerMargin;
            float bgY = outerMargin + 260f;
            float bgW = VIRTUAL_WIDTH - outerMargin * 2f;
            float bgH = VIRTUAL_HEIGHT - (outerMargin * 2f + 260f);

            if (hallwayTexture != null) {
                float texW = hallwayTexture.getWidth();
                float texH = hallwayTexture.getHeight();

                // preserve aspect ratio (letterbox) and center inside the bg rect
                float scale = Math.min(bgW / texW, bgH / texH);
                float drawW = texW * scale;
                float drawH = texH * scale;
                float drawX = bgX + (bgW - drawW) / 2f;
                float drawY = bgY + (bgH - drawH) / 2f;

                batch.setProjectionMatrix(camera.combined);
                batch.begin();
                batch.draw(hallwayTexture, drawX, drawY, drawW, drawH);
                batch.end();
            }
        }

        drawBoxes();

        if (arena.inBulletHell) {
            drawBulletHell();
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawEnemyArea();
        // Only draw characters if NOT in bullet hell
        if (!arena.inBulletHell) {
            drawCharacters();
        }
        ui.drawMessageArea(batch, messageText, actionText, VIRTUAL_HEIGHT);
        ui.drawScore(batch, girl.getScore(), VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        boolean highlightActive = (menuController.isInMainMenu() || menuController.isInAttackSubmenu()) && battleState == BattleState.PLAYER_TURN && !arena.inBulletHell;
        ui.drawBottomMenu(batch, menuController.getMenuItems(), menuController.getSelectedIndex(), highlightActive, VIRTUAL_WIDTH);

        batch.end();
    }

    // ==== Rahmen ====
    private void drawBoxes() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);

        float outerMargin = 60f;

        // Großer Rahmen
        shapeRenderer.rect(
            outerMargin,
            outerMargin + 260,
            VIRTUAL_WIDTH - outerMargin * 2,
            VIRTUAL_HEIGHT - (outerMargin * 2 + 260)
        );

        // Unteres Menü-Feld
        float menuBoxHeight = 260f;
        shapeRenderer.rect(
            outerMargin,
            outerMargin,
            VIRTUAL_WIDTH - outerMargin * 2,
            menuBoxHeight
        );

        // Bullet-Hell-Box NUR wenn Angriff läuft
        if (arena.inBulletHell) {
            shapeRenderer.rect(
                arena.x,
                arena.y,
                Arena.WIDTH,
                Arena.HEIGHT
            );
        }

        shapeRenderer.end();
    }

    // ==== Bullet-Hell zeichnen ====
    private void drawBulletHell() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Treffer-Blitz hinter der Seele
        if (hitEffects.isGirlHitActive()) {
            shapeRenderer.setColor(1, 0.6f, 0.6f, 1);
            shapeRenderer.circle(arena.soulX, arena.soulY, arena.soulRadius + 6f);
        }

        // Spieler-Seele
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.circle(arena.soulX, arena.soulY, arena.soulRadius);

        // Bullets
        shapeRenderer.setColor(1, 1, 1, 1);
        for (Bullet b : bulletHell.getBullets()) {
            if (b.alive) {
                shapeRenderer.circle(b.x, b.y, b.radius);
            }
        }

        shapeRenderer.end();
    }

    // ==== Enemy-Area & Status ====
    private void drawEnemyArea() {
        fontBig.getData().setScale(2f);

        Enemy enemy = battleController.getCurrent();
        // Optimize string concatenation with StringBuilder
        stringBuilder.setLength(0);
        stringBuilder.append(enemy != null ? enemy.getName() : "Unknown");
        stringBuilder.append("   HP: ");
        stringBuilder.append(enemy != null ? enemy.getHp() : 0);
        stringBuilder.append("/");
        stringBuilder.append(enemy != null ? enemy.getMaxHp() : 0);
        fontBig.draw(batch, stringBuilder, 120, VIRTUAL_HEIGHT - 80);

        font.getData().setScale(1.5f);
        stringBuilder.setLength(0);
        stringBuilder.append(girl.name);
        stringBuilder.append("   HP: ");
        stringBuilder.append(girl.hp);
        stringBuilder.append("/");
        stringBuilder.append(girl.maxHp);
        stringBuilder.append("   SAN: ");
        stringBuilder.append(girl.sanity);
        stringBuilder.append("/");
        stringBuilder.append(girl.maxSanity);
        font.draw(batch, stringBuilder, 120, 360);
    }

    // ==== Sprites zeichnen (mit Hit-Animation) ====
    private void drawCharacters() {
        Enemy enemy = battleController.getCurrent();
        spriteManager.drawEnemy(batch, enemy != null ? enemy : new com.flickenherz.game.entities.TeddyBear(), hitEffects.isTeddyHitActive());
        
        // Draw girl with attack animation if active
        float animationProgress = attackAnimationTimer / ATTACK_ANIMATION_DURATION;
        if (animationProgress > 0) {
            spriteManager.drawGirlWithAnimation(batch, hitEffects.isGirlHitActive(), arena.inBulletHell, animationProgress);
        } else {
            spriteManager.drawGirl(batch, hitEffects.isGirlHitActive(), arena.inBulletHell);
        }
        
        batch.setColor(1f, 1f, 1f, 1f);
    }

    // ==== Input ====
    private void handleInput(float delta) {
        // DEBUG: Skip to next enemy (press K key) - REMOVE THIS LATER
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            Enemy currentEnemy = battleController.getCurrent();
            if (currentEnemy != null) {
                currentEnemy.takeDamage(currentEnemy.getHp()); // Kill current enemy instantly
                String defeatMsg = currentEnemy.getDefeatMessage();
                String defeatAct = currentEnemy.getDefeatAction();
                
                if (battleController.hasNext()) {
                    messageText = defeatMsg;
                    actionText = defeatAct;
                    battleController.advanceToNext();
                    battleState = BattleState.PLAYER_TURN;
                    arena.inBulletHell = false;
                    bulletHell.getBullets().clear();
                    menuController.reset();
                    // Reset score tracking for next fight
                    fightStartTime = System.currentTimeMillis();
                    damageTakenThisFight = 0;
                    perfectDodges = 0;
                } else {
                    // Player defeated final boss - show victory screen
                    if (game != null) {
                        if (battleMusic != null && battleMusic.isPlaying()) {
                            battleMusic.stop();
                        }
                        game.setScreen(new VictoryScreen(game, girl.getScore()));
                    } else {
                        battleState = BattleState.WIN;
                        messageText = defeatMsg;
                        actionText = defeatAct;
                    }
                }
            }
        }
        
        // Während Bullet-Hell: nur Movement
        if (arena.inBulletHell) {
            arena.handleMovement(delta);
            return;
        }

        if (battleState == BattleState.PLAYER_TURN && menuController.isInMainMenu()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                menuController.navigateLeft();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                menuController.navigateRight();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                menuController.selectCurrentMenu();
                handleMenuSelection();
            }
        } else if (battleState == BattleState.PLAYER_TURN && menuController.isInAttackSubmenu()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                menuController.navigateLeft();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                menuController.navigateRight();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                menuController.selectCurrentMenu();
                handleAttackSelection();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.X) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                menuController.exitSubmenu();
            }
        } else if (menuController.isGameOver() && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            resetBattle();
        }
    }

    // ==== Menüaktionen ====
    private void handleMenuSelection() {
        String choice = menuController.getSelectedChoice();

        if ("FIGHT".equals(choice)) {
            menuController.enterAttackSubmenu();
            messageText = "Choose your attack!";
            actionText = "";
        } else if ("ITEM".equals(choice)) {
            performItem();
        }
    }

    private void handleAttackSelection() {
        String attackType = menuController.getSelectedChoice();
        menuController.exitSubmenu();
        performAttack(attackType);
    }

    private void performAttack(String attackType) {
        Enemy enemy = battleController.getCurrent();
        if (enemy == null) return;

        // Start attack animation
        attackAnimationTimer = ATTACK_ANIMATION_DURATION;

        int dmg;
        String attackName;
        boolean skipEnemyAttack = false;
        
        switch (attackType) {
            case "SLASH":
                dmg = 15;
                attackName = "slash";
                break;
            case "STRIKE":
                dmg = 20;
                attackName = "strike";
                break;
            case "HEAVY":
                dmg = 30;
                attackName = "heavy blow";
                // Heavy attack damages player
                girl.hp = Math.max(0, girl.hp - 15);
                hitEffects.activateGirlHit();
                break;
            case "QUICK":
                dmg = 10;
                attackName = "quick jab";
                // 33.33% chance to skip enemy attack
                if (Math.random() < 0.3333f) {
                    skipEnemyAttack = true;
                }
                break;
            default:
                dmg = girl.basicAttack();
                attackName = "attack";
        }

        // Add score for damage dealt (10 points per HP)
        girl.addScore(dmg * 10);

        // Flavor text depending on enemy type
        if (enemy.getType() == Enemy.EnemyType.TEDDY) {
            actionText = "You " + attackName + " the torn teddy. -" + dmg + " HP";
            messageText = "The air feels heavier.";
        } else if (enemy.getType() == Enemy.EnemyType.DOLL) {
            actionText = "You " + attackName + " the porcelain doll. -" + dmg + " HP";
            messageText = "New cracks spread across the porcelain.";
        } else if (enemy.getName().equals("Cuddlefiend")) {
            actionText = "You " + attackName + " the cuddlefiend. -" + dmg + " HP";
            messageText = "The creature's stitching unravels slightly.";
        } else if (enemy.getName().equals("Father")) {
            actionText = "You " + attackName + " Father. -" + dmg + " HP";
            messageText = "His expression doesn't change.";
        } else {
            actionText = "You " + attackName + ". -" + dmg + " HP";
            messageText = "The air feels heavier.";
        }
        
        // Add self-damage message for heavy attack
        if ("HEAVY".equals(attackType)) {
            actionText += " (-15 HP recoil)";
        }
        
        // Add dodge message and bonus for quick attack
        if (skipEnemyAttack && "QUICK".equals(attackType)) {
            messageText = "You dodge before they can react!";
            perfectDodges++;
            girl.addScore(100); // Bonus for perfect dodge
        }

        enemy.takeDamage(dmg);
        hitEffects.activateTeddyHit();
        audio.playPlayerAttack();

        if (enemy.isDead()) {
            // Calculate bonuses
            int defeatBonus = 500;
            girl.addScore(defeatBonus);

            // Time bonus (max 1000 points if under 30 seconds)
            long fightDuration = System.currentTimeMillis() - fightStartTime;
            int timeBonus = Math.max(0, 1000 - (int)(fightDuration / 30));
            girl.addScore(timeBonus);

            // No-hit bonus (2000 points if no damage taken)
            if (damageTakenThisFight == 0) {
                girl.addScore(2000);
            }

            String defeatMsg = enemy.getDefeatMessage();
            String defeatAct = enemy.getDefeatAction();
            if (battleController.hasNext()) {
                messageText = defeatMsg;
                actionText = defeatAct;
                battleController.advanceToNext();
                // Reset score tracking for next fight
                fightStartTime = System.currentTimeMillis();
                damageTakenThisFight = 0;
                perfectDodges = 0;
            } else {
                // Player defeated final boss - show victory screen
                if (game != null) {
                    if (battleMusic != null && battleMusic.isPlaying()) {
                        battleMusic.stop();
                    }
                    game.setScreen(new VictoryScreen(game, girl.getScore()));
                } else {
                    battleState = BattleState.WIN;
                    menuController.setMenuState(MenuController.MenuState.WIN);
                    messageText = defeatMsg;
                    actionText = defeatAct;
                }
            }
        } else if (!skipEnemyAttack) {
            String attackMsg;
            String actionMsg;
            
            if (enemy.getName().equals("Cuddlefiend")) {
                attackMsg = "The cuddlefiend lunges forward.";
                actionMsg = "Its claws gleam in the dim light.";
            } else if (enemy.getName().equals("Father")) {
                attackMsg = "Father's presence grows heavier.";
                actionMsg = "The walls seem to close in.";
            } else if (enemy.getType() == Enemy.EnemyType.TEDDY) {
                attackMsg = "The teddy prepares an attack...";
                actionMsg = "Something moves in the dark.";
            } else if (enemy.getType() == Enemy.EnemyType.DOLL) {
                attackMsg = "The doll's glass eyes flare.";
                actionMsg = "Shards of porcelain whirl around you.";
            } else {
                attackMsg = "The enemy prepares an attack...";
                actionMsg = "Something moves in the dark.";
            }
            
            // Delay enemy attack to show animation
            postAttackDelay = POST_ATTACK_DELAY;
            pendingAttackMsg = attackMsg;
            pendingActionMsg = actionMsg;
        } else {
            // Quick attack succeeded - skip enemy turn, return to player turn
            battleState = BattleState.PLAYER_TURN;
            menuController.setMenuState(MenuController.MenuState.MAIN_MENU);
        }
    }

    private void performItem() {
        Enemy enemy = battleController.getCurrent();
        if (girl.hp < girl.maxHp) {
            girl.hp = Math.min(girl.maxHp, girl.hp + 10);
            actionText = "You use a bandage. +10 HP";
            messageText = "The bandage smells faintly of dust.";
        } else {
            actionText = "Your pockets are empty.";
            messageText = "You feel around, but find nothing.";
        }

        String attackMsg;
        String actionMsg;
        
        if (enemy != null && enemy.getName().equals("Cuddlefiend")) {
            attackMsg = "The cuddlefiend's eyes gleam.";
            actionMsg = "Something shifts in the darkness.";
        } else if (enemy != null && enemy.getName().equals("Father")) {
            attackMsg = "Father raises his hand slowly.";
            actionMsg = "Reality bends around his gesture.";
        } else if (enemy != null && enemy.getType() == Enemy.EnemyType.TEDDY) {
            attackMsg = "The teddy doesn't move.";
            actionMsg = "But the room starts to close in.";
        } else if (enemy != null && enemy.getType() == Enemy.EnemyType.DOLL) {
            attackMsg = "The doll stares through you.";
            actionMsg = "The shadows swell and close around you.";
        } else {
            attackMsg = "The enemy watches silently.";
            actionMsg = "The shadows swell and close around you.";
        }
        
        startEnemyAttack(attackMsg, actionMsg);
    }

    // ==== Enemy Bullet-Hell ====
    private void startEnemyAttack(String msg, String act) {
        battleState = BattleState.ENEMY_TURN;
        menuController.setMenuState(MenuController.MenuState.ENEMY_TURN);
        messageText = msg;
        actionText = act;
        fightStartTime = System.currentTimeMillis();
        damageTakenThisFight = 0;
        bulletHell.startAttack(battleController.getCurrent(), msg, act);
    }

    private void updateBulletHell(float delta) {
        bulletHell.update(delta);

        if (!arena.inBulletHell) return;

        // Check collisions with player soul (bullets) - optimized with squared distances
        if (arena.iFrameTimer <= 0f) {
            Array<Bullet> bullets = bulletHell.getBullets();
            for (int i = 0, n = bullets.size; i < n; i++) {
                Bullet b = bullets.get(i);
                if (!b.alive) continue;

                float dx = b.x - arena.soulX;
                float dy = b.y - arena.soulY;
                float distSq = dx * dx + dy * dy;
                float r = b.radius + arena.soulRadius;
                float rSq = r * r;
                
                if (distSq <= rSq) {
                    girl.takeDamage(5);
                    damageTakenThisFight += 5;
                    arena.iFrameTimer = 0.5f;
                    b.alive = false;
                    actionText = "You are hit! -5 HP";
                    hitEffects.activateGirlHit();
                    audio.playPlayerHit();

                    if (girl.isDead()) {
                        endEnemyAttack(true);
                        return;
                    }
                    break; // Only process one collision per frame
                }
            }
        }

        if (bulletHell.isAttackFinished()) {
            endEnemyAttack(false);
        }
    }

    private void endEnemyAttack(boolean playerDied) {
        bulletHell.endAttack();

        if (playerDied || girl.isDead()) {
            if (game != null) {
                // Stop music before transitioning
                if (battleMusic != null && battleMusic.isPlaying()) {
                    battleMusic.stop();
                }
                game.setScreen(new DeathScreen(game, girl.getScore()));
                // Don't call dispose here - let LibGDX handle it
            } else {
                battleState = BattleState.LOSE;
                menuController.setMenuState(MenuController.MenuState.LOSE);
                messageText = "You can't tell what's real anymore.";
                actionText = "";
            }
        } else {
            battleState = BattleState.PLAYER_TURN;
            menuController.setMenuState(MenuController.MenuState.MAIN_MENU);
            messageText = "The room stops moving for a moment.";
            actionText = "";
        }
    }

    private void resetBattle() {
        girl = new Player("Mädchen");
        battleController.clearAndAddDefaults();
        battleState = BattleState.PLAYER_TURN;
        menuController.reset();
        messageText = "You wake up in the same room again.";
        actionText = "";
        arena.inBulletHell = false;
        hitEffects.reset();
        bulletHell.reset();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        fontBig.dispose();
        spriteManager.dispose();
        if (hallwayTexture != null) hallwayTexture.dispose();
        if (battleMusic != null) {
            battleMusic.dispose();
        }
        if (audio != null) audio.dispose();
    }
}
