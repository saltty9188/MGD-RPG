package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Character {

    float stateTimer;
    Texture playerTextures;
    TextureRegion temp0, temp1, temp2, temp3;
    TextureRegion[] fFrames;
    Animation<TextureRegion> walkAnimation;

    public Player(){
        playerTextures = new Texture("character.png");
        stateTimer = 0.0f;
        //Walk Down Animation
        temp0 = new TextureRegion(playerTextures, 0, 0, 16,32);
        temp1 = new TextureRegion(playerTextures, 16,0, 16, 32);
        temp2 = new TextureRegion(playerTextures, 32, 0, 16, 32);
        temp3 = new TextureRegion(playerTextures, 48, 0, 16, 32);
        fFrames = new TextureRegion[4*1];
        fFrames[0] = temp0;fFrames[1] = temp1;fFrames[2] = temp2;fFrames[3] = temp3;
        walkAnimation = new Animation<TextureRegion>(0.1f, fFrames);
    }

    public TextureRegion getFrame(float delta){
        TextureRegion region;
        region = walkAnimation.getKeyFrame(stateTimer, true);
        stateTimer += delta;
        return region;
    }

    public void update(float delta){

        setRegion(getFrame(delta));
    }
}
