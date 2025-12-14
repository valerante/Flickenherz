package com.flickenherz.game.entities;

/**
 * Third enemy: Cuddlefiend.
 * A more powerful enemy combining attacks from previous enemies.
 * Has 160 HP, more than both teddy and doll combined.
 */
public class Cuddlefiend implements Enemy {
    /** Enemy name */
    public String name;
    
    /** Current health points */
    public int hp;
    
    /** Maximum health points */
    public int maxHp;

    /**
     * Creates a new cuddlefiend enemy with 160 HP.
     */
    public Cuddlefiend() {
        this.name = "Cuddlefiend";
        this.maxHp = 160;
        this.hp = maxHp;
    }

    /**
     * Claw attack similar to teddy bear but stronger.
     * @return Damage dealt (10-17 HP)
     */
    public int clawAttack() {
        return 10 + (int)(Math.random() * 8);
    }

    /**
     * Fear attack similar to teddy bear but stronger.
     * @return Damage dealt (6-12 HP)
     */
    public int fearAttack() {
        return 6 + (int)(Math.random() * 7);
    }

    /**
     * Shard slash attack similar to doll but stronger.
     * @return Damage dealt (12-19 HP)
     */
    public int shardSlash() {
        return 12 + (int)(Math.random() * 8);
    }

    /**
     * Lullaby whisper attack similar to doll but stronger.
     * @return Damage dealt (10-17 HP)
     */
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
