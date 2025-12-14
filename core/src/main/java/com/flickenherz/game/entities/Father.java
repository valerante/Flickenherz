package com.flickenherz.game.entities;

/**
 * Final boss: Father.
 * The most powerful enemy with 250 HP and all previous enemy attacks enhanced.
 */
public class Father implements Enemy {
    /** Enemy name */
    public String name;
    
    /** Current health points */
    public int hp;
    
    /** Maximum health points */
    public int maxHp;

    /**
     * Creates a new Father enemy with 250 HP.
     */
    public Father() {
        this.name = "Father";
        this.maxHp = 250;
        this.hp = maxHp;
    }

    /**
     * Enhanced claw attack from teddy bear.
     * @return Damage dealt (12-21 HP)
     */
    public int clawAttack() {
        return 12 + (int)(Math.random() * 10);
    }

    /**
     * Enhanced fear attack from teddy bear.
     * @return Damage dealt (8-16 HP)
     */
    public int fearAttack() {
        return 8 + (int)(Math.random() * 9);
    }

    /**
     * Enhanced shard slash attack from doll.
     * @return Damage dealt (14-23 HP)
     */
    public int shardSlash() {
        return 14 + (int)(Math.random() * 10);
    }

    /**
     * Enhanced lullaby whisper attack from doll.
     * @return Damage dealt (12-21 HP)
     */
    public int lullabyWhisper() {
        return 12 + (int)(Math.random() * 10);
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
