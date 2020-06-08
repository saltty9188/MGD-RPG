package com.mygdx.game.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import javax.swing.JDialog;

public class NPC extends Character {

    private static final float WINDOW_BORDER_RATIO = 0.045f;

    private BitmapFont bmfont;
    private Texture textWindow;
    private boolean textAnimating;
    private String textBuilder;
    private int textIndex;
    private float textTime;

    private String[] dialogue;
    private String currentDialogue;
    private int currentDialogueIndex;

    private Animation nextLinePrompt;
    private Texture nextLine;

    Random rand;
    float walkDuration;
    int direction;

    public NPC() {
        this(new Texture("Characters/NPC_test.png"), 14, 21, "Hello");
    }

    public NPC(String ... dialogue) {
        this(new Texture("Characters/NPC_test.png"), 14, 21, dialogue);
    }

    public NPC(Texture spriteSheet, int width, int height, String ... dialogue) {
        super(spriteSheet, width, height);
        this.dialogue = dialogue;

        loadAssets();

        currentDialogue = dialogue[0];
        currentDialogueIndex = 0;

        stateTimer = 0.0f;
        genAnimations();
        currentAni = idleAni;
        setRegion(getFrame(0));

        rand = new Random();
        walkDuration = 0.5f;

        textAnimating = true;
        textIndex = 0;
        textBuilder = "";
        textTime = 0;
    }

    private void genAnimations() {
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 7, 14, 21),
                new TextureRegion(spriteSheet, 17, 8, 14, 21),
                new TextureRegion(spriteSheet, 33, 7, 14, 21),
                new TextureRegion(spriteSheet, 49, 8, 14, 21)};
        walkDownAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 70, 14, 21),
                new TextureRegion(spriteSheet, 17, 71, 14, 21),
                new TextureRegion(spriteSheet, 33, 70, 14, 21),
                new TextureRegion(spriteSheet, 49, 71, 14, 21)};
        walkUpAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 103, 14, 21),
                new TextureRegion(spriteSheet, 17, 104, 14, 21),
                new TextureRegion(spriteSheet, 33, 103, 14, 21),
                new TextureRegion(spriteSheet, 49, 104, 14, 21)};
        walkLeftAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 2, 39, 14, 21),
                new TextureRegion(spriteSheet, 18, 40, 14, 21),
                new TextureRegion(spriteSheet, 34, 39, 14, 21),
                new TextureRegion(spriteSheet, 50, 40, 14, 21)};
        walkRightAni = new Animation<TextureRegion>(AFS, fFrames);
        fFrames = new TextureRegion[]{new TextureRegion(spriteSheet, 1, 7, 14, 21),
            new TextureRegion(spriteSheet, 33, 7, 14, 21)};
        idleAni = new Animation<TextureRegion>(AFS, fFrames);

        TextureRegion[][] temp = TextureRegion.split(nextLine, nextLine.getWidth()/4, nextLine.getHeight());
        nextLinePrompt = new Animation<TextureRegion>(AFS, temp[0]);
    }

    private void updateNextLine(float delta) {
        stateTimer += delta;
    }

    private void loadAssets() {
        bmfont = new BitmapFont(
                Gdx.files.internal("font/good_neighbors.fnt"),
                Gdx.files.internal("font/good_neighbors.png"),
                false);

        textWindow = new Texture("window_blue_long.png");

        nextLine = new Texture("Characters/next line.png");
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = currentAni.getKeyFrame(stateTimer, true);
        stateTimer += dt;
        return region;
    }

    /**
     * Moves the NPC and adjusts their animation according to their direction of movement.
     * NPCs are only able to move within their designated "roam zone".
     * @param roamZone The Rectangle area that the NPC is allowed to move within.
     * @param player   The player.
     */
    public void update(float delta, Rectangle roamZone, Player player) {
        setRegion(getFrame(delta));
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
                currentAni = walkUpAni;
                break;
            //Move right
            case 1:
                NPCDelta.y = 0;
                currentAni = walkRightAni;
                break;
            //Move down
            case 2:
                NPCDelta.x = 0;
                NPCDelta.y *= -1;
                currentAni = walkDownAni;
                break;
            //Move left
            case 3:
                NPCDelta.y = 0;
                NPCDelta.x *= -1;
                currentAni = walkLeftAni;
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

        // Don't move if they're about to leave the "roam zone"
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

        if(NPCDelta.len2() <= 0) {
            currentAni = idleAni;
        }
        translate(NPCDelta.x, NPCDelta.y);

        walkDuration -= delta;
    }

    /**
     * Updates the cutscene NPC's position and current animation.
     * @param talking True if the cutscene NPC is still talking
     */
    public void updateCutscene(float delta, boolean talking) {
        // Face left while he's talking
        if(talking) {
            setDefaultPose(2);
        } else {
            setAnimation(4); // Walks right off of the screen
            translateX(98 * delta);
            setRegion(getFrame(delta));
        }
    }

    /**
     * Displays whatever dialogue is tied to this NPC in a window at the top of the screen.
     * @param batch The SpriteBatch used to draw the window and text.
     * @param delta The time passed since the last frame.
     */
    public void displayDialogue(SpriteBatch batch, float delta) {
        updateNextLine(delta);
        float arrowWidth = Gdx.graphics.getWidth() * 0.025f;
        float arrowHeight = arrowWidth;

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight()/6;
        float x = 0;
        float y = Gdx.graphics.getHeight() * 5/6;
        batch.draw(textWindow, x, y, width, height);

        // Adjust values for the text so it doesn't go onto the border
        width = Gdx.graphics.getWidth() - 2 * WINDOW_BORDER_RATIO * Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight()/6 - 2 * WINDOW_BORDER_RATIO * Gdx.graphics.getHeight();
        x = Gdx.graphics.getWidth() * WINDOW_BORDER_RATIO;
        y = Gdx.graphics.getHeight() * 5/6 + Gdx.graphics.getHeight() * 5/6 * WINDOW_BORDER_RATIO;

        bmfont.getData().setScale(2);
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bmfont, currentDialogue.replace("\n", ""));

        //Get the height of a single line of text
        float fontHeight = glyphLayout.height;

        glyphLayout.setText(bmfont, currentDialogue, bmfont.getColor(), width, 1, true);
        int textX = (int) ((width / 2 - glyphLayout.width / 2) + x);
        float textY =  ((height / 2 - fontHeight / 2) + y);

        // Raise the text proportionally to how many lines there are
        int numLines = (int) (glyphLayout.height / fontHeight);
        if (numLines > 1) textY += fontHeight / 2 * numLines;

        if (textAnimating) {
            textTime += delta;
            System.out.println(textTime);
            if (textTime >= 0.03f) {
                if (textIndex < currentDialogue.length()) textBuilder += currentDialogue.charAt(textIndex++);
                textTime = 0;
            }

            bmfont.draw(batch, textBuilder, textX, textY, glyphLayout.width, 1, true);
            if (textBuilder.equals(currentDialogue)) {
                textAnimating = false;
                textBuilder = "";
                textIndex = 0;
            }
        } else {
            bmfont.draw(batch, currentDialogue, textX, textY, glyphLayout.width, 1, true);
            batch.draw((TextureRegion) nextLinePrompt.getKeyFrame(stateTimer, true), x + width - arrowWidth, y - arrowHeight/2, arrowWidth, arrowHeight);
        }

        // Error catching, resets if there is an issue with the textBuilder. Only happens if the player spam clicks through dialogue
        if(!currentDialogue.contains(textBuilder)) {
            textIndex = 0;
            textBuilder = "";
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

    /**
     * Advances this NPC to the next line of dialogue.
     */
    public void nextDialogueLine() {
        if(!textAnimating) {
            currentDialogue = dialogue[++currentDialogueIndex];
            textAnimating = true;
        }
    }

    /**
     * Makes the NPC face the player (used when talking)
     * @param player The player.
     */
    public void face(Player player) {
        // NPC to the left of the player
        if(getX() + getWidth() < player.getX()) setDefaultPose(4);
        // NPC above
        else if(getY() > player.getY() + player.getHeight()) setDefaultPose(0);
        // NPC to the right
        else if(getX() > player.getX() + player.getWidth()) setDefaultPose(2);
        // NPC below
        else setDefaultPose(1);
    }

    /**
     * Returns true if this NPC has more dialogue.
     * @return True if this NPC has more dialogue, false otherwise.
     */
    public boolean hasNextDialogueLine() {
        return currentDialogueIndex < dialogue.length - 1 ;
    }

    public void resetTextValues() {
        textAnimating = true;
        currentDialogue = dialogue[0];
        currentDialogueIndex = 0;
    }

    public void dispose() {
        super.dispose();
        textWindow.dispose();
        bmfont.dispose();
        nextLine.dispose();
    }
}
