package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attack;
import com.mygdx.game.InfiniteAttack;

public class MadBat extends Enemy {

    public MadBat() {
        super(new Texture("Characters/bat-SWEN.png"), 20, 23, new Texture("Characters/bat-battle.png"),
                50, 5, 3, 7, 5, "Mad Bat", 10);

        gold = 27;
    }

    protected void genAnimations() {
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 7, 20, 31, 22),
                new TextureRegion(spriteSheet, 56, 20, 31, 22),
                new TextureRegion(spriteSheet, 104, 20, 31, 22)};
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 7, 20, 31, 22),
                new TextureRegion(spriteSheet, 56, 20, 31, 22),
                new TextureRegion(spriteSheet, 104, 20, 31, 22)};
        idleAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 7, 212, 31, 22),
                new TextureRegion(spriteSheet, 56, 212, 31, 22),
                new TextureRegion(spriteSheet, 104, 212, 31, 22)};
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 8, 85, 31, 22),
                new TextureRegion(spriteSheet, 56, 85, 31, 22),
                new TextureRegion(spriteSheet, 104, 85, 31, 22)};
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 8, 149, 31, 22),
                new TextureRegion(spriteSheet, 56, 149, 31, 22),
                new TextureRegion(spriteSheet, 104, 149, 31, 22)};
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
    }

    @Override
    public void setAttacks() {
        Attack attack1 = new InfiniteAttack(0, "Swoop");
        Attack attack2 = new Attack(10, 5, "Bite");
        Attack attack3 = new Attack(5, 20, "Blood Suck");
        setAttacks(attack1, attack2, attack3);
    }
}
