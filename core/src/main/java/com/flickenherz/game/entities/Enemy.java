package com.flickenherz.game.entities;

/**
 * Interface for all enemy entities in the game.
 * Defines common enemy behaviors and properties.
 */
public interface Enemy {
    /**
     * Enum defining different enemy types for sprite selection.
     */
    enum EnemyType { TEDDY, DOLL, OTHER }

    /**
     * Gets the enemy's name.
     * @return Enemy name
     */
    String getName();
    
    /**
     * Gets the enemy's current HP.
     * @return Current health points
     */
    int getHp();
    
    /**
     * Gets the enemy's maximum HP.
     * @return Maximum health points
     */
    int getMaxHp();
    
    /**
     * Applies damage to the enemy.
     * @param dmg Damage amount
     */
    void takeDamage(int dmg);
    
    /**
     * Checks if the enemy is dead.
     * @return true if dead, false otherwise
     */
    boolean isDead();
    
    /**
     * Gets the enemy type for sprite rendering.
     * @return EnemyType enum value
     */
    EnemyType getType();
    
    /**
     * Gets the message displayed when enemy is defeated.
     * @return Defeat message string
     */
    String getDefeatMessage();
    
    /**
     * Gets the action text displayed when enemy is defeated.
     * @return Defeat action string
     */
    String getDefeatAction();
}
