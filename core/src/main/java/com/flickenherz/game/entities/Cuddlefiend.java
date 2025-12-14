package com.flickenherz.game.entities;

public class Cuddlefiend implements Enemy {
    public String name;
    public int hp;
    public int maxHp;

    public Cuddlefiend() {
        this.name = "Cuddlefiend";
        this.maxHp = 160;  // More HP than both previous enemies combined
        this.hp = maxHp;
    }

    // Teddy's attacks
    public int clawAttack() {
        return 10 + (int)(Math.random() * 8);  // Slightly stronger: 10-17 damage
    }

    public int fearAttack() {
        return 6 + (int)(Math.random() * 7);  // Slightly stronger: 6-12 damage
    }

    // Doll's attacks
    public int shardSlash() {
        return 12 + (int)(Math.random() * 8);  // Slightly stronger: 12-19 damage
    }

    public int lullabyWhisper() {
        return 10 + (int)(Math.random() * 8);  // Slightly stronger: 10-17 damage
    }

    @Override
    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    @Override
    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getHp() { return hp; }

    @Override
    public int getMaxHp() { return maxHp; }

    @Override
    public EnemyType getType() { return EnemyType.OTHER; }

    @Override
    public String getDefeatMessage() { 
        return "The Cuddlefiend collapses. The nightmare ends."; 
    }

    @Override
    public String getDefeatAction() { 
        return "Press ENTER to wake up."; 
    }
}
