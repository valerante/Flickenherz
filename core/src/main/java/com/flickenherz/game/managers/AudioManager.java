package com.flickenherz.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

// Manages sound effects
public class AudioManager {

    private final Sound sfxMenuMove;
    private final Sound sfxMenuSelect;
    private final Sound sfxPlayerAttack;
    private final Sound sfxPlayerHit;
    private final Sound sfxEnemyAttackStart;

    public AudioManager() {
        sfxMenuMove = safeLoad("sfx_menu_move.wav");
        sfxMenuSelect = safeLoad("sfx_menu_select.wav");
        sfxPlayerAttack = safeLoad("sfx_player_attack.wav");
        sfxPlayerHit = safeLoad("sfx_player_hit.wav");
        sfxEnemyAttackStart = safeLoad("sfx_enemy_attack_start.wav");
    }

    // Safe load to prevent crashes from missing audio files
    private Sound safeLoad(String path) {
        try {
            return Gdx.audio.newSound(Gdx.files.internal(path));
        } catch (Exception e) {
            return null;
        }
    }

    public void playMenuMove() {
        if (sfxMenuMove != null) sfxMenuMove.play(0.2f);
    }

    public void playMenuSelect() {
        if (sfxMenuSelect != null) sfxMenuSelect.play(0.2f);
    }

    public void playPlayerAttack() {
        if (sfxPlayerAttack != null) sfxPlayerAttack.play(0.2f);
    }

    public void playPlayerHit() {
        if (sfxPlayerHit != null) sfxPlayerHit.play(0.2f);
    }

    /**
     * Plays the enemy attack start sound effect at 20% volume.
     */
    public void playEnemyAttackStart() {
        if (sfxEnemyAttackStart != null) sfxEnemyAttackStart.play(0.2f);
    }

    /**
     * Disposes of all loaded sound effects to free memory.
     */
    public void dispose() {
        if (sfxMenuMove != null) sfxMenuMove.dispose();
        if (sfxMenuSelect != null) sfxMenuSelect.dispose();
        if (sfxPlayerAttack != null) sfxPlayerAttack.dispose();
        if (sfxPlayerHit != null) sfxPlayerHit.dispose();
        if (sfxEnemyAttackStart != null) sfxEnemyAttackStart.dispose();
    }
}
