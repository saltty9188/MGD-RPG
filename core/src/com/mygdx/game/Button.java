package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button {

    float x;
    float y;
    float width;
    float height;
    public boolean isDown;
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

    public void draw (SpriteBatch batch, float opacity) {
        batch.setColor(1,1,1, opacity);
        if(isDown) {
            batch.draw(downTexture, x, y, width, height);
        }
        else {
            batch.draw(upTexture, x, y, width, height);
        }
        batch.setColor(1,1,1,1);
    }
    public void draw(SpriteBatch batch, String text) {
        GlyphLayout glyphLayout = new GlyphLayout();
        bmfont.getData().setScale(2f);
        glyphLayout.setText(bmfont, text);

        glyphLayout.setText(bmfont, text);
        //Get the height of a single line of text
        float fontHeight = glyphLayout.height;

        glyphLayout.setText(bmfont, text, Color.BLACK, width, 1, true);
        int textX = (int) ((width/2 - glyphLayout.width/2) + x);
        int textY = (int) ((height/2 - fontHeight/2) + y);

        // Raise the text proportionally to how many lines there are
        int numLines =  (int) (glyphLayout.height / fontHeight);
        if(numLines > 1) textY += fontHeight/2 * numLines;

        if(isDown) {
            batch.draw(downTexture, x, y, width, height);
            bmfont.draw(batch, text, textX, textY-1, glyphLayout.width,
                    1, true);
        } else {
            batch.draw(upTexture, x, y, width, height);
            bmfont.draw(batch, text, textX, textY, glyphLayout.width,
                    1, true);
        }
    }

    public void draw(SpriteBatch batch, String text, String subText, boolean isRed) {
        GlyphLayout textLayout = new GlyphLayout();
        bmfont.getData().setScale(2f);
        textLayout.setText(bmfont, text);

        // Get the GlyphLayout for the subtext - text is scaled down
        GlyphLayout subTextLayout = new GlyphLayout();
        bmfont.getData().setScale(1);
        subTextLayout.setText(bmfont, subText);

        // Scale text back up
        bmfont.getData().setScale(2f);

        //Get the height of a single line of text
        float fontHeight = textLayout.height;

        textLayout.setText(bmfont, text, Color.BLACK, width, 1, true);
        int textX = (int) ((width/2 - textLayout.width/2) + x);
        int textY = (int) ((height/2 - fontHeight/2) + y) + 3;

        // Raise the text proportionally to how many lines there are
        int numLines =  (int) (textLayout.height / fontHeight);
        if(numLines > 1) textY += fontHeight/2 * numLines;

        int subTextX = (int) ((width/2 - subTextLayout.width/2) + x);
        int subTextY = textY - (int) subTextLayout.height - 1;

        if(isRed) bmfont.setColor(Color.RED);
        else bmfont.setColor(Color.WHITE);
        if(isDown) {
            batch.draw(downTexture, x, y, width, height);
            bmfont.draw(batch, text, textX, textY-1, textLayout.width,
                    1, true);
            bmfont.getData().setScale(1);
            bmfont.draw(batch, subText, subTextX, subTextY-1, subTextLayout.width, 1, true);
        }
        else {
            batch.draw(upTexture, x, y, width, height);
            bmfont.draw(batch, text, textX, textY, textLayout.width,
                    1, true);
            bmfont.getData().setScale(1);
            bmfont.draw(batch, subText, subTextX, subTextY, subTextLayout.width, 1, true);
        }
    }

    public void draw(SpriteBatch batch, Texture graphic) {

        float graphicX = x + 5;
        float graphicY = y + 5;
        float graphicWidth = width - 10;
        float grapicHeight = height - 10;
        // Want the graphic to be square
        if(width != height) {
            graphicWidth = Math.min(graphicWidth, grapicHeight);
            grapicHeight = graphicWidth;
        }

        if(isDown) {
            batch.draw(downTexture, x, y, width, height);
            batch.draw(graphic, graphicX, graphicY - 1, graphicWidth, grapicHeight);
        }
        else {
            batch.draw(upTexture, x, y, width, height);
            batch.draw(graphic, graphicX, graphicY, graphicWidth, grapicHeight);
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