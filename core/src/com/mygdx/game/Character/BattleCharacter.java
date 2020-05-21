package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Attack;

public abstract class BattleCharacter extends Character {
    // Sprite used on the BattleScreen
    Texture battleSprite;

    // stats
    protected int maxHP;
    protected int HP;
    protected int strength;
    protected int defence;
    protected int speed;

    protected String name;

    protected Attack[] attacks;

    private boolean alive;

    public BattleCharacter(Texture spriteSheet, int width, int height, Texture battleSprite) {
        super(spriteSheet, width, height);
        this.battleSprite = battleSprite;
        alive = true;
    }

    public BattleCharacter(Texture spriteSheet, int width, int height, Texture battleSprite,
                           int maxHP, int strength, int defence, int speed, String name, Attack... attacks) {
        this(spriteSheet, width, height, battleSprite);
        this.maxHP = maxHP;
        this.HP = maxHP;
        this.strength = strength;
        this.defence = defence;
        this.speed = speed;
        this.name = name;
        this.attacks = attacks;
        alive = true;
    }

    public void setAttacks(Attack... attacks) {
        this.attacks = attacks;
    }

    /**
     * Returns the percentage of HP remaining for this Character.
     * @return The percentage of HP remaining.
     */
    public float getHPPercentage() {
        return (float) HP/(float) maxHP;
    }

    public String getHPStatus() {
        if(HP > 0) return "HP: " + HP + "/" + maxHP;
        else return "HP: 0/" + maxHP;
    }

    public Texture getBattleSprite() {
        return battleSprite;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public int getStrength() {
        return strength;
    }

    public int getHP() {
        return HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    public void revive() {
        alive = true;
        HP = 1;
    }

    public void takeDamage(int damage) {
        HP -= (damage * (50 - defence))/56 + 1;
        if(HP <= 0) {
            die();
            HP = 0;
        }
    }

    public void dispose() {
        super.dispose();
        battleSprite.dispose();
    }

    /**
     * Returns the actual amount of damage taken by the given BattleCharacter after factoring in the defender's defence stat.
     * @param defender      The BattleCharacter that will receive the damage.
     * @param attackPower   The Attack Power of the incoming attack.
     * @return The damage to be taken by the defender.
     */
    public static int damageTaken(BattleCharacter defender, int attackPower) {
        return (attackPower * (50 - defender.defence))/56 + 1;
    }
}
