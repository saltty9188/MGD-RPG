package com.mygdx.game;

/**
 * A variation of Attack that has infinite PP so that the player cannot soft-lock in battle or be forced to flee.
 * Will likely only ever have a damage value of 0 so that will only be used against weaker enemies or as a last resort.
 * However, the damage may be increased for a boss enemy so it remains alterable.
 */
public class InfiniteAttack extends Attack {

    public InfiniteAttack(int damage, String name) {
        super(damage, 100, name);
    }

    @Override
    public void decrementPP() {
        // Do nothing as PP is infinite
    }

    @Override
    public String getPPStatus() {
        return "Infinite";
    }
}
