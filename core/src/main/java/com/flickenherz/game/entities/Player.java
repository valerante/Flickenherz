package com.flickenherz.game.entities;

/**
 * Represents the player character in the game.
 * Tracks health, sanity, score, and provides combat actions.
 */
public class Player {
    /** Player character name */
    public String name;
    
    /** Current health points */
    public int hp;
    
    /** Maximum health points */
    public int maxHp;
    
    /** Current sanity points */
    public int sanity;
    
    /** Maximum sanity points */
    public int maxSanity;
    
    /** Current accumulated score */
    public int score;

    /**
     * Creates a new player with the specified name.
     * Initializes health and sanity to maximum values (100 each).
     * 
     * @param name Player's name
     */
    public Player(String name) {
        this.name = name;
        this.maxHp = 100;
        this.hp = maxHp;
        this.maxSanity = 100;
        this.sanity = maxSanity;
        this.score = 0;
    }

    /**
     * Performs a basic attack with random damage.
     * 
     * @return Damage dealt (10-18 HP)
     */
    public int basicAttack() {
        return 10 + (int)(Math.random() * 9);
    }

    /**
     * Applies damage to the player.
     * Health cannot go below 0.
     * 
     * @param dmg Amount of damage to take
     */
    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    /**
     * Reduces sanity by the specified amount.
     * Sanity cannot go below 0.
     * 
     * @param amount Amount of sanity to lose
     */
    public void loseSanity(int amount) {
        sanity -= amount;
        if (sanity < 0) sanity = 0;
    }

    /**
     * Checks if the player is dead (HP or sanity at 0).
     * 
     * @return true if player is dead, false otherwise
     */
    public boolean isDead() {
        return hp <= 0 || sanity <= 0;
    }

    /**
     * Adds points to the player's score.
     * 
     * @param points Points to add
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Gets the current score.
     * 
     * @return Current score value
     */
    public int getScore() {
        return score;
    }

    /**
     * Resets the score to 0.
     */
    public void resetScore() {
        score = 0;
    }
}
