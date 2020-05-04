package com.mygdx.game;

public class Attack {

    // The strength of this attack
    private int attack;
    // Stat for determining how many uses the Attack has left
    private int maxPP;
    private int PP;

    private String name;

    public Attack(int attack, int maxPP, String name) {
        this.attack = attack;
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
}
