package com.flickenherz.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Manager for all game sound effects.
 * Loads and plays various sound effects for menu navigation, combat, and damage.
 * Includes safe loading to handle missing audio files gracefully.
 */
public class AudioManager {

    /** Sound effect for menu navigation movement */
    private final Sound sfxMenuMove;
    
    /** Sound effect for menu item selection */
    private final Sound sfxMenuSelect;
    
    /** Sound effect for player attack actions */
    private final Sound sfxPlayerAttack;
    
    /** Sound effect for player taking damage */
    private final Sound sfxPlayerHit;
    
    /** Sound effect for enemy initiating attack */
    private final Sound sfxEnemyAttackStart;

    /**
     * Creates a new audio manager and loads all sound effects.
     * Uses safe loading to prevent crashes from missing audio files.
     */
    public AudioManager() {
        sfxMenuMove = safeLoad("sfx_menu_move.wav");
        sfxMenuSelect = safeLoad("sfx_menu_select.wav");
        sfxPlayerAttack = safeLoad("sfx_player_attack.wav");
        sfxPlayerHit = safeLoad("sfx_player_hit.wav");
        sfxEnemyAttackStart = safeLoad("sfx_enemy_attack_start.wav");
    }

    /**
     * Safely loads a sound file, returning null if loading fails.
     * Prevents application crashes from missing audio files.
     * 
     * @param path Path to the sound file
     * @return Loaded Sound object or null if loading failed
     */
    private Sound safeLoad(String path) {
        try {
            return Gdx.audio.newSound(Gdx.files.internal(path));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Plays the menu movement sound effect at 20% volume.
     */
    public void playMenuMove() {
        if (sfxMenuMove != null) sfxMenuMove.play(0.2f);
    }

    /**
     * Plays the menu selection sound effect at 20% volume.
     */
    public void playMenuSelect() {
        if (sfxMenuSelect != null) sfxMenuSelect.play(0.2f);
    }

    /**
     * Plays the player attack sound effect at 20% volume.
     */
    public void playPlayerAttack() {
        if (sfxPlayerAttack != null) sfxPlayerAttack.play(0.2f);
    }

    /**
     * Plays the player hit sound effect at 20% volume.
     */
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
