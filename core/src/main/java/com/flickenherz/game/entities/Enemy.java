package com.flickenherz.game.entities;

// Enemy interface
public interface Enemy {
    enum EnemyType { TEDDY, DOLL, OTHER }

    String getName();
    int getHp();
    int getMaxHp();
    void takeDamage(int dmg);
    boolean isDead();
    EnemyType getType();
    String getDefeatMessage();
    String getDefeatAction();
}
