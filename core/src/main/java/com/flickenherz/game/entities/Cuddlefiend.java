package com.flickenherz.game.entities;

// Third enemy: Cuddlefiend
public class Cuddlefiend implements Enemy {
    public String name;
    public int hp;
    public int maxHp;

    public Cuddlefiend() {
        this.name = "Cuddlefiend";
        this.maxHp = 160;
        this.hp = maxHp;
    }

    public int clawAttack() {
        return 10 + (int)(Math.random() * 8);
    }

    public int fearAttack() {
        return 6 + (int)(Math.random() * 7);
    }

    public int shardSlash() {
        return 12 + (int)(Math.random() * 8);
    }

    public int lullabyWhisper() {
        return 10 + (int)(Math.random() * 8);
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
