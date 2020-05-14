package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button {

    float x;
    float y;
    float width;
    float height;
    boolean isDown;
    boolean justPressed;

    Texture upTexture;
    Texture downTexture;

    private BitmapFont bmfont;

    public Button(float x, float y, float width, float height, Texture up, Texture down) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.upTexture = up;
        this.downTexture = down;
        isDown = false;
        justPressed = false;

        bmfont = new BitmapFont(
                Gdx.files.internal("font/good_neighbors.fnt"),
                Gdx.files.internal("font/good_neighbors.png"),
                false);
        bmfont.getData().setScale(2f);
    }

    public void update(boolean checkTouch, int touchX, int touchY) {
        isDown = false;

        if(checkTouch) {
            if(touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height ) {
                isDown = true;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if(isDown) {
            batch.draw(downTexture, x, y, width, height);
        }
        else {
            batch.draw(upTexture, x, y, width, height);
        }
    }

    public void draw(SpriteBatch batch, String text) {
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bmfont, text);

        int textX = (int) ((width/2 - glyphLayout.width/2) + x);
        int textY = (int) ((height/2 - glyphLayout.height/2) + y);

        if(isDown) {
            batch.draw(downTexture, x, y, width, height);
            bmfont.draw(batch, text, textX, textY-1, glyphLayout.width,
                    1, true);
        }
        else {
            batch.draw(upTexture, x, y, width, height);
            bmfont.draw(batch, text, textX, textY, glyphLayout.width,
                    1, true);
        }
    }

    /**
     * Returns if the button has just been released.
     * @return True if the button was just released, false otherwise.
     */
    public boolean justPressed() {
        if(isDown) {
            justPressed = true;
            return false;
        }
        else if(justPressed) {
            justPressed = false;
            return true;
        }
        return false;
    }
}