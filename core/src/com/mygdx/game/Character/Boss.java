package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attack;
import com.mygdx.game.InfiniteAttack;

public class Boss extends Enemy {

    public Boss() {
        super(new Texture("Characters/pumpkin_monster.png"), 27, 47, new Texture("Characters/pumpkin-boss-battle.png"),
                300, 15, 10, 7, 10, "Pumpkin Menace", 10000);

        // Boss doesn't move and therefore does not need animations so we set the region here
        setRegion(new TextureRegion(spriteSheet, 35, 15, 27, 47));

        gold = 1000;
    }

    @Override
    protected void genAnimations() {

    }

    @Override
    public void setAttacks() {
        Attack attack1 = new InfiniteAttack(5, "Vine Sling");
        Attack attack2 = new Attack(5, 20, "Seed Shot");
        Attack attack3 = new Attack(10, 10, "Bowl Over");
        Attack attack4 = new Attack(17, 2, "Pumpkin Bomb");

        setAttacks(attack1, attack2, attack3, attack4);
    }
}
