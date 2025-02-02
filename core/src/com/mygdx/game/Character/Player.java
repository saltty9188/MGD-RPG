package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attack;
import com.mygdx.game.InfiniteAttack;
import com.mygdx.game.Items.Ether;
import com.mygdx.game.Items.HiEther;
import com.mygdx.game.Items.HiPotion;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.Potion;

import java.util.Random;

public class Player extends BattleCharacter {

    private int currentExp;
    private int toNextLevel;

    private Item[] items;

    private Random rand;

    private int gold;

    public Player(){
        super(new Texture("Characters/character.png"), 15, 23, new Texture("Characters/character-battle.png"),
                50, 10, 5, 5, 1, "Hero");

        currentExp = 0;
        toNextLevel = (int) (4 * Math.pow(level + 1, 2) - 1.4 * (level + 1) - 0.4);

        rand = new Random();

        stateTimer = 0.0f;
        genAnimations();
        currentAni = idleAni;

        InfiniteAttack attack1 = new InfiniteAttack(0, "Sword Slash");
        Attack attack2 = new Attack(5, 20, "Whirlwind Blade");
        Attack attack3 = new Attack(15, 10, "Mighty Stab");
        Attack attack4 = new Attack(10, 15, "Low Blow");

        Item item1 = new Potion(this);
        item1.addItems(5);
        Item item2 = new HiPotion(this);
        item2.addItems(1);
        Item item3 = new Ether();
        item3.addItems(3);
        Item item4 = new HiEther();

        setAttacks(attack1, attack2, attack3, attack4);
        setItems(item1, item2, item3, item4);

        gold = 100;
    }

    public void restoreHealth(int health) {
        HP += health;
        if(HP > maxHP) {
            HP = maxHP;
        }
    }

    public Attack getAttack(int index) {
        return attacks[index];
    }

    public Item getItem(int index) {
        return items[index];
    }

    /**
     * Gives the player exp from a fallen enemy after battle. Also returns whether or not the player will level up.
     * @param exp The amount of exp given to the player
     * @return The number of levels gained from this exp
     */
    public int receiveExp(int exp) {
        currentExp += exp;
        int numLevels = 0;
        if(currentExp >= toNextLevel) {
            while(currentExp >= toNextLevel) {
                currentExp -= toNextLevel;
                numLevels++;
                calculateNextLevelExp(level + numLevels + 1);
            }
            return numLevels;
        }
        return 0;
    }

    /**
     * Calculates the exp needed to reach the next level
     * @param nextLevel The next level to be reached.
     */
    private void calculateNextLevelExp(int nextLevel) {
        toNextLevel = (int) (4 * Math.pow(nextLevel, 2) - 1.4 * (nextLevel) - 0.4);
    }

    /**
     * Increase the player's level by the given amount.
     * Increases the player's stats by a variable amount as well.
     */
    public void levelUp(int numLevels) {
        level += numLevels;

        int HPIncrease = (rand.nextInt(10) + 5) * numLevels;

        maxHP += HPIncrease;
        HP += HPIncrease;
        strength += (rand.nextInt(5) + 1) * numLevels;
        defence += (rand.nextInt(5) + 1) * numLevels;
        speed += (rand.nextInt(3) + 1) * numLevels;
    }

    public int getLevel() {
        return level;
    }

    public void setItems(Item... items) {
        this.items = items;
    }

    public void earnGold(int amount) {
        gold += amount;
    }

    public void spendGold(int amount) {
        gold -= amount;
    }

    public int getGold() {
        return gold;
    }

    /**
     * Gives the player max HP and PP for all attacks.
     */
    public void revive() {
        alive = true;
        HP = maxHP;
        for(Attack attack : attacks) {
            attack.restorePP(attack.getMaxPP());
        }
    }

    /**
     * Returns true if the player is facing the given NPC and is mostly touching them (half of their body is in contact with half of the NPC's).
     * @param npc The NPC being checked.
     * @return True if the player is facing the NPC, false otherwise.
     */
    public boolean facing(NPC npc) {
        // To the left of the NPC
        if(getX() + getWidth() < npc.getX() && (getY() < npc.getY() + npc.getHeight() * 3/4 && getY() + getHeight() > npc.getY() + npc.getHeight()/4)
                && currentAni == walkRightAni) {
            return true;
        } else if (getY() > npc.getY() + npc.getHeight() && (getX() < npc.getX() + npc.getWidth() * 3/4 && getX() + getWidth() > npc.getX() + npc.getWidth()/4)
                    && (currentAni == walkDownAni || currentAni == idleAni)) { // Behind the NPC
            return true;
        } else if (getX() > npc.getX() + npc.getWidth() && (getY() < npc.getY() + npc.getHeight() * 3/4 && getY() + getHeight() > npc.getY() + npc.getHeight()/4)
                    && currentAni == walkLeftAni) { // To the right of the NPC
            return true;
        } else if (getY() + getHeight() < npc.getY() && (getX() < npc.getX() + npc.getWidth() * 3/4 && getX() + getWidth() > npc.getX() + npc.getWidth()/4)
                    && currentAni == walkUpAni) { // In front of the NPC
            return true;
        }
         else {
             return false;
        }
    }


    private void genAnimations(){
        //Walk Down Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 5, 15, 23),
                new TextureRegion(spriteSheet, 17, 5, 15, 23),
                new TextureRegion(spriteSheet, 33, 5, 15, 23),
                new TextureRegion(spriteSheet, 49, 5, 15, 23)};
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Right Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 2, 38, 15,23),
                new TextureRegion(spriteSheet, 18,38, 15, 23),
                new TextureRegion(spriteSheet, 34, 38, 15, 23),
                new TextureRegion(spriteSheet, 50, 38, 15, 23)};
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Left Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 102, 15,23),
                new TextureRegion(spriteSheet, 17,102, 15, 23),
                new TextureRegion(spriteSheet, 33, 102, 15, 23),
                new TextureRegion(spriteSheet, 49, 102, 15, 23)};
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        //Walk Up Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 0, 69, 15,23),
                new TextureRegion(spriteSheet, 16,69, 15, 23),
                new TextureRegion(spriteSheet, 32, 69, 15, 23),
                new TextureRegion(spriteSheet, 48, 69, 15, 23)};
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        //Idle Animation
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 5, 15,23),
                new TextureRegion(spriteSheet, 81,5, 15, 23)};
        idleAni  = new Animation<TextureRegion>(0.5f, fFrames);
    }
}
