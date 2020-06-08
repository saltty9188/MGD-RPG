package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attack;
import com.mygdx.game.InfiniteAttack;

public class WanderingShroom extends Enemy {

    public WanderingShroom() {
        super(new Texture("Characters/mushroom.png"), 15, 23, new Texture("Characters/mushroom-battle.png"),
                50, 5, 1, 7, 2, "Wandering Shroom", 10);

        gold = 10;
    }

    @Override
    protected void genAnimations() {
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 447, 208, 19, 30),
                new TextureRegion(spriteSheet, 494, 210, 19, 30),
                new TextureRegion(spriteSheet, 541, 209, 19, 30)};
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        idleAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 447, 352, 19, 30),
                new TextureRegion(spriteSheet, 494, 354, 19, 30),
                new TextureRegion(spriteSheet, 541, 353, 19, 30)};
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 447, 257, 19, 30),
                new TextureRegion(spriteSheet, 494, 258, 19, 30),
                new TextureRegion(spriteSheet, 541, 257, 19, 30)};
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 446, 305, 19, 30),
                new TextureRegion(spriteSheet, 495, 306, 19, 30),
                new TextureRegion(spriteSheet, 544, 305, 19, 30)};
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
    }

    @Override
    public void setAttacks() {
        Attack attack1 = new InfiniteAttack(0, "Fall Over");
        Attack attack2 = new Attack(5, 15, "Spore Cloud");
        setAttacks(attack1, attack2);
    }
}
