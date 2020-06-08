package com.mygdx.game.WorldAnimations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Flag extends Sprite {

    private float stateTime;
    private int FRAME_ROWS = 1;
    private int FRAME_COLS = 5;
    private Texture flagSheet;
    private TextureRegion[] flagRegion;
    private Animation<TextureRegion> flagAnimation;

    public Flag() {
        flagSheet = new Texture(Gdx.files.internal("Tile Maps/Flag.png"));
        stateTime = 0.0f;
        this.setSize(16, 32);

        TextureRegion[][] temp = TextureRegion.split(flagSheet,
                flagSheet.getWidth() / FRAME_COLS,
                flagSheet.getHeight() / FRAME_ROWS);

        flagRegion = new TextureRegion[FRAME_ROWS * FRAME_COLS];
        int index = 0;
        for(int i = 0; i < FRAME_ROWS; i++) {
            for(int j = 0; j < FRAME_COLS; j++) {
                flagRegion[index++] = temp[i][j];
            }
        }
        flagAnimation = new Animation<TextureRegion>(0.1f, flagRegion);
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = flagAnimation.getKeyFrame(stateTime, true);
        stateTime += dt;
        return region;
    }

    public void dispose() {
        flagSheet.dispose();
    }

}
