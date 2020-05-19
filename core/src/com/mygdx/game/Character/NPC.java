package com.mygdx.game.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class NPC extends Character {

    private BitmapFont bmfont;
    private Texture textWindow;
    private boolean textAnimating;
    private String textBuilder;
    private int textIndex;
    private float textTime;

    private String dialogue;

    Random rand;
    float walkDuration;
    int direction;

    public NPC() {
        this(new Texture("placeholder2.png"), 15, 23, "Hello");
    }

    public NPC(Texture spriteSheet, int width, int height, String dialogue) {
        super(spriteSheet, width, height);
        this.dialogue = dialogue;
        setRegion(spriteSheet);

        rand = new Random();
        walkDuration = 0.5f;

        textAnimating = true;
        textIndex = 0;
        textBuilder = "";
        textTime = 0;

        loadAssets();
    }

    private void loadAssets() {
        bmfont = new BitmapFont(
                Gdx.files.internal("font/good_neighbors.fnt"),
                Gdx.files.internal("font/good_neighbors.png"),
                false);

        textWindow = new Texture("window.png");
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

    /**
     * Displays whatever dialogue is tied to this NPC in a window at the top of the screen.
     * @param batch The SpriteBatch used to draw the window and text.
     * @param delta The time passed since the last frame.
     */
    public void displayDialogue(SpriteBatch batch, float delta) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight()/6;
        float x = 0;
        float y = Gdx.graphics.getHeight() * 5/6;
        batch.draw(textWindow, x, y, width, height);

        bmfont.getData().setScale(2);
        System.out.println(dialogue);
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bmfont, dialogue);

        //Get the height of a single line of text
        float fontHeight = glyphLayout.height;

        glyphLayout.setText(bmfont, dialogue, bmfont.getColor(), width, 1, true);
        int textX = (int) ((width / 2 - glyphLayout.width / 2) + x);
        float textY =  ((height / 2 - fontHeight / 2) + y);

        // Raise the text proportionally to how many lines there are
        int numLines = (int) (glyphLayout.height / fontHeight);
        if(dialogue.contains("\n")) textY += fontHeight/2; // Assumes there will only be one newline in the text
        if (numLines > 1) textY += fontHeight / 2 * numLines;

        if (textAnimating) {
            textTime += delta;
            if (textTime >= 0.01f) {
                if (textIndex < dialogue.length()) textBuilder += dialogue.charAt(textIndex++);
                textTime = 0;
            }

            bmfont.draw(batch, textBuilder, textX, textY, glyphLayout.width, 1, true);
            if (textBuilder.equals(dialogue)) {
                textAnimating = false;
                textBuilder = "";
            }
        } else {
            bmfont.draw(batch, dialogue, textX, textY, glyphLayout.width, 1, true);
        }
    }

    public void resetTextValues() {
        textIndex = 0;
        textAnimating = true;
    }
}
