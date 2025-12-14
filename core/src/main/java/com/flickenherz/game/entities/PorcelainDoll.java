package com.flickenherz.game.entities;

/**
 * Second enemy: A porcelain doll.
 * More durable than the teddy bear with 120 HP.
 */
public class PorcelainDoll implements Enemy {

    /** Enemy name */
    public String name = "Porzellanpuppe";
    
    /** Maximum health points */
    public int maxHp = 120;
    
    /** Current health points */
    public int hp = maxHp;

    @Override
    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public void takeDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getHp() { return hp; }

    @Override
    public int getMaxHp() { return maxHp; }

    @Override
    public EnemyType getType() { return EnemyType.DOLL; }

    @Override
    public String getDefeatMessage() { return "The doll shatters to pieces. The room is silent."; }

    @Override
    public String getDefeatAction() { return "Press ENTER to wake up."; }

    /**
     * Shard slash attack using porcelain fragments.
     * @return Damage dealt (10-15 HP)
     */
    public int shardSlash() {
        return 10 + (int)(Math.random() * 6);
    }

    /**
     * Lullaby whisper attack dealing psychological damage.
     * @return Damage dealt (8-13 HP)
     */
    public int lullabyWhisper() {
        return 8 + (int)(Math.random() * 6);
    }
}
