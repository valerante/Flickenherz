package com.flickenherz.game.entities;

public class Father implements Enemy {
    public String name;
    public int hp;
    public int maxHp;

    public Father() {
        this.name = "Father";
        this.maxHp = 250;  // Significantly more HP than Cuddlefiend (160)
        this.hp = maxHp;
    }

    // All previous attacks from Teddy, Doll, and Cuddlefiend
    public int clawAttack() {
        return 12 + (int)(Math.random() * 10);  // 12-21 damage (stronger)
    }

    public int fearAttack() {
        return 8 + (int)(Math.random() * 9);  // 8-16 damage (stronger)
    }

    public int shardSlash() {
        return 14 + (int)(Math.random() * 10);  // 14-23 damage (stronger)
    }

    public int lullabyWhisper() {
        return 12 + (int)(Math.random() * 10);  // 12-21 damage (stronger)
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
        return "The Father collapses. The nightmare is finally over."; 
    }

    @Override
    public String getDefeatAction() { 
        return "Press ENTER to wake up."; 
    }
}
