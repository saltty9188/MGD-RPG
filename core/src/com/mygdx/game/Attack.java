package com.mygdx.game;

public class Attack {

    // The strength of this attack
    private int damage;
    // Stat for determining how many uses the Attack has left
    private int maxPP;
    private int PP;

    private String name;

    public Attack(int damage, int maxPP, String name) {
        this.damage = damage;
        this.maxPP = maxPP;
        this.PP = maxPP;
        this.name = name;
    }

    /**
     * Decrements the PP left for this Attack by 1, does nothing if PP is already 0.
     */
    public void decrementPP() {
        PP--;
        if(PP < 0) PP = 0;
    }

    /**
     * Restores the PP of this Attack by the given amount. PP cannot exceed the maximum.
     * @param amount The amount of PP being restored.
     */
    public void restorePP(int amount) {
        PP += amount;
        if(PP > maxPP) PP = maxPP;
    }

    /**
     * A simple battle message to be displayed when this Attack is used in battle.
     * @param name The name of the character using this attack.
     * @return The battle message.
     */
    public String battleMessage(String name) {
        return name + " used " + this.name;
    }

    public String getPPStatus() {
        return "PP: " + PP + "/" + maxPP;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getPP() {
        return PP;
    }

    public int getMaxPP() {
        return maxPP;
    }
}
