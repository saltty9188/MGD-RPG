package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class NPC extends Character {

    private String dialogue;

    Random rand;
    float walkDuration;
    int direction;

    public NPC(Texture spriteSheet, int width, int height, String dialogue) {
        super(spriteSheet, width, height);
        this.dialogue = dialogue;

        rand = new Random();
        walkDuration = 0.5f;
    }

    public void update(float delta, Rectangle roamZone) {
        if(walkDuration <= 0) {
            direction = rand.nextInt(4);
            walkDuration = 0.5f;
        }

        Vector2 enemyDelta = new Vector2(98*delta, 98*delta);

        int right = (int)Math.ceil(Math.max(
                getX() + getWidth(),
                getX() + getWidth() + enemyDelta.x
        ));
        int top = (int)Math.ceil(Math.max(
                getY() + getHeight(),
                getY() + getHeight() + enemyDelta.y
        ));
        int left = (int)Math.floor(Math.min(
                getX(),
                getX() + enemyDelta.x
        ));
        int bottom = (int)Math.floor(Math.min(
                getY(),
                getY() + enemyDelta.y
        ));

        switch (direction) {
            //Move up
            case 0:
                if(top < roamZone.y + roamZone.getHeight()) translateY(enemyDelta.y);
                break;
            //Move right
            case 1:
                if(right < roamZone.x + roamZone.getWidth()) translateX(enemyDelta.x);
                break;
            //Move down
            case 2:
                if(bottom > roamZone.y) translateY(-(enemyDelta.y));
                break;
            //Move left
            case 3:
                if(left > roamZone.x) translateX(-(enemyDelta.x));
                break;
        }
        walkDuration -= delta;
    }
}
