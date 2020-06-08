package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attack;
import com.mygdx.game.InfiniteAttack;

public class WildEagle extends Enemy {

    public WildEagle() {
        super(new Texture("Characters/bird_2_eagle.png"), 18, 14, new Texture("Characters/eagle-battle.png"),
                50, 5, 1, 10, 2, "Wild Eagle", 10);

        gold = 10;
    }

    @Override
    protected void genAnimations() {
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 3, 68, 27, 21),
                new TextureRegion(spriteSheet, 35, 68, 27, 21),
                new TextureRegion(spriteSheet, 67, 67, 27, 21)};
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        idleAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 3, 37, 27, 21),
                new TextureRegion(spriteSheet, 35, 36, 27, 21),
                new TextureRegion(spriteSheet, 67, 35, 27, 21)};
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 9, 27, 21),
                new TextureRegion(spriteSheet, 34, 4, 27, 21),
                new TextureRegion(spriteSheet, 66, 2, 27, 21)};
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 4, 98, 27, 21),
                new TextureRegion(spriteSheet, 35, 100, 27, 21),
                new TextureRegion(spriteSheet, 68, 105, 27, 21)};
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
    }

    @Override
    public void setAttacks() {
        InfiniteAttack attack1 = new InfiniteAttack(0, "Peck");
        Attack attack2 = new Attack(5, 10, "Talon Pincer");

        setAttacks(attack1, attack2);
    }
}
