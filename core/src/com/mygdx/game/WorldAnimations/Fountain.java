package com.mygdx.game.WorldAnimations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Fountain extends Sprite {

    private float stateTime;
    private int FRAME_ROWS = 1;
    private int FRAME_COLS = 3;
    private Texture fountainSheet;
    private TextureRegion[] fountainRegion;
    private Animation<TextureRegion> fountainAnimation;

    public Fountain() {
        fountainSheet = new Texture(Gdx.files.internal("Tile Maps/Fountain.png"));
        stateTime = 0.0f;
        this.setSize(16, 32);

        TextureRegion[][] temp = TextureRegion.split(fountainSheet,
                fountainSheet.getWidth() / FRAME_COLS,
                fountainSheet.getHeight() / FRAME_ROWS);

        fountainRegion = new TextureRegion[FRAME_ROWS * FRAME_COLS];
        int index = 0;
        for(int i = 0; i < FRAME_ROWS; i++) {
            for(int j = 0; j < FRAME_COLS; j++) {
                fountainRegion[index++] = temp[i][j];
            }
        }
        fountainAnimation = new Animation<TextureRegion>(0.1f, fountainRegion);
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = fountainAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    public void dispose() {
        fountainSheet.dispose();
    }
}
