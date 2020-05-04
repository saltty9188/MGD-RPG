package com.mygdx.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Character {

    float stateTimer;
    Texture playerTextures;
    Animation<TextureRegion> walkAnimation;

    public Player(){
        playerTextures = new Texture("character.png");
        this.setSize(64,128);
        stateTimer = 0.0f;
        TextureRegion walkingDown = new TextureRegion(
                playerTextures, 0,0, 48,32);
        TextureRegion[][] temp = TextureRegion.split((walkingDown,walkingDown.getRegionWidth() / 3, walkingDown.getRegionHeight());
    }

    public TextureRegion getFrame(float delta){
        TextureRegion region;

        region = standAnimation.getKeyFrame(stateTimer, true);
        stateTimer += delta;
        return region;

    }
}
