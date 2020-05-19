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

    public void update(float delta, Rectangle roamZone, Player player) {
        if(walkDuration <= 0) {
            direction = rand.nextInt(4);
            walkDuration = 0.5f;
        }

        Vector2 NPCDelta = new Vector2(98*delta, 98*delta);
        Rectangle NPCDeltaRectangle = new Rectangle();
        NPCDeltaRectangle.setSize(this.getWidth(), this.getHeight());

        switch (direction) {
            //Move up
            case 0:
                NPCDelta.x = 0;
                break;
            //Move right
            case 1:
                NPCDelta.y = 0;
                break;
            //Move down
            case 2:
                NPCDelta.x = 0;
                NPCDelta.y *= -1;
                break;
            //Move left
            case 3:
                NPCDelta.y = 0;
                NPCDelta.x *= -1;
                break;
        }

        int right = (int)Math.ceil(Math.max(
                getX() + getWidth(),
                getX() + getWidth() + NPCDelta.x
        ));
        int top = (int)Math.ceil(Math.max(
                getY() + getHeight(),
                getY() + getHeight() + NPCDelta.y
        ));
        int left = (int)Math.floor(Math.min(
                getX(),
                getX() + NPCDelta.x
        ));
        int bottom = (int)Math.floor(Math.min(
                getY(),
                getY() + NPCDelta.y
        ));

        //Make sure the NPC doesn't walk through the player
        NPCDeltaRectangle.x = getX() + NPCDelta.x;
        NPCDeltaRectangle.y = getY();
        if(NPCDeltaRectangle.overlaps(player.getBoundingRectangle())) NPCDelta.x = 0;

        NPCDeltaRectangle.x = getX();
        NPCDeltaRectangle.y = getY() + NPCDelta.y;
        if(NPCDeltaRectangle.overlaps(player.getBoundingRectangle())) NPCDelta.y = 0;

        // Don't move if their about to leave the "roam zone"
        switch (direction) {
            //Move up
            case 0:
                if(top >= roamZone.y + roamZone.getHeight()) NPCDelta.y = 0;
                break;
            //Move right
            case 1:
                if(right >= roamZone.x + roamZone.getWidth()) NPCDelta.x = 0;
                break;
            //Move down
            case 2:
                if(bottom <= roamZone.y) NPCDelta.y = 0;
                break;
            //Move left
            case 3:
                if(left <= roamZone.x) NPCDelta.x = 0;
                break;
        }

        translate(NPCDelta.x, NPCDelta.y);

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

    /**
     * Returns true if the player is close to (within a few pixels away) to this NPC.
     * @param player The player.
     * @return True if the player is close to this NPC, false otherwise.
     */
    public boolean closeTo(Player player) {
        // Rectangle that is one pixel larger than the NPC on all sides
        Rectangle closeZone = new Rectangle();
        closeZone.setPosition(getX() - 3, getY() - 3);
        closeZone.setSize(getWidth() + 6, getHeight() + 6);

        return closeZone.overlaps(player.getBoundingRectangle());
    }

    public void resetTextValues() {
        textIndex = 0;
        textAnimating = true;
    }
}
