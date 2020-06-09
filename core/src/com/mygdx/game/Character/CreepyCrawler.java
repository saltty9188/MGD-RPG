package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attack;
import com.mygdx.game.InfiniteAttack;

public class CreepyCrawler extends Enemy {

    public CreepyCrawler() {
        super(new Texture("Characters/spider07.png"), 30, 34, new Texture("Characters/spider-battle.png"),
                80, 7, 5, 5, 7, "Creepy Crawler", 40);

        gold = 31;
    }

    @Override
    protected void genAnimations() {
        // Walk down
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 264, 141, 46, 42),
                new TextureRegion(spriteSheet, 328, 142, 46, 42),
                new TextureRegion(spriteSheet, 392, 141, 46, 42),
                new TextureRegion(spriteSheet, 457, 141, 46, 42),
                new TextureRegion(spriteSheet, 521, 142, 46, 42),
                new TextureRegion(spriteSheet, 585, 141, 46, 42)};
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        // idle
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 8, 139, 46, 42),
                new TextureRegion(spriteSheet, 72, 143, 46, 42),
                new TextureRegion(spriteSheet, 136, 145, 46, 42),
                new TextureRegion(spriteSheet, 200, 142, 46, 42)};
        idleAni = new Animation<TextureRegion>(AFS, fFrames);
        // Walk up
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 266, 13, 46, 42),
                new TextureRegion(spriteSheet, 328, 11, 46, 42),
                new TextureRegion(spriteSheet, 394, 13, 46, 42),
                new TextureRegion(spriteSheet, 456, 13, 46, 42),
                new TextureRegion(spriteSheet, 521, 11, 46, 42),
                new TextureRegion(spriteSheet, 584, 13, 46, 42)};
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        // Walk left
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 264, 76, 46, 42),
                new TextureRegion(spriteSheet, 327, 76, 46, 42),
                new TextureRegion(spriteSheet, 393, 76, 46, 42),
                new TextureRegion(spriteSheet, 457, 76, 46, 42),
                new TextureRegion(spriteSheet, 520, 76, 46, 42),
                new TextureRegion(spriteSheet, 584, 76, 46, 42)};
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        // Walk right
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 266, 204, 46, 42),
                new TextureRegion(spriteSheet, 331, 204, 46, 42),
                new TextureRegion(spriteSheet, 394, 204, 46, 42),
                new TextureRegion(spriteSheet, 458, 204, 46, 42),
                new TextureRegion(spriteSheet, 523, 204, 46, 42),
                new TextureRegion(spriteSheet, 587, 204, 46, 42)};
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
    }

    @Override
    public void setAttacks() {
        InfiniteAttack attack1 = new InfiniteAttack(1, "Bite");
        Attack attack2 = new Attack(5, 15, "Web Shot");
        Attack attack3 = new Attack(10, 5, "Pincer");
        Attack attack4 = new Attack(15, 1, "Poison Bite");

        setAttacks(attack1, attack2, attack3, attack4);
    }
}
