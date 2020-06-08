package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.RPGGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CreditsScreen implements Screen {

    private RPGGame game;

    //String for holding the credits text
    private ArrayList<String> credits;
    Iterator<String> iterator;
    String currentText;

    //Font
    private BitmapFont bmfont;

    private SpriteBatch batch;

    private boolean textAnimating;
    private float textTime;
    private int textIndex;
    private String textBuilder;

    public CreditsScreen(RPGGame game) {
        this.game = game;
        create();
    }

    private void create() {

        batch = new SpriteBatch();

        // Make the array larget
        credits = new ArrayList<String>(5);

        //Write all the lines from the credits text file to a String array list
        credits.addAll(Arrays.asList(Gdx.files.internal("credits.txt").readString().split("--", 0)));

        iterator = credits.iterator();
        currentText = iterator.next();

        //BMFont
        bmfont = new BitmapFont(
                Gdx.files.internal("font/good_neighbors.fnt"),
                Gdx.files.internal("font/good_neighbors.png"),
                false);

        textAnimating = true;
        textBuilder = "";
        textIndex = 0;
        textTime = 0;
    }

    @Override
    public void show() {
        // Change the song if coming from the title screen
        if(!RPGGame.currentTrack.equals(RPGGame.creditsTheme)) {
            RPGGame.currentTrack.stop();
            RPGGame.currentTrack = RPGGame.creditsTheme;
            RPGGame.currentTrack.play();
        }
    }

    @Override
    public void render(float delta) {

        //Clear the screen before drawing
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();

        batch.begin();
        //Draw the credits to the screen
        int scale = ((currentText.equals(credits.get(0)) || currentText.equals(credits.get(credits.size() - 1))) ? 2 : 1);
        float speed = ((currentText.equals(credits.get(0)) || currentText.equals(credits.get(credits.size() - 1))) ? 0.03f : 0.01f);
        drawText(batch, currentText, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, delta, scale, speed);
        batch.end();
    }

    private void update() {
        if(Gdx.input.justTouched()) {
            if(iterator.hasNext()) {
                currentText = iterator.next();
                textAnimating = true;
            } else {
                // reset credits and return to title screen
                iterator = credits.iterator();
                currentText = iterator.next();
                textAnimating = true;
                game.setScreen(RPGGame.titleScreen);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    /**
     * Draws text within a rectangle texture (e.g. text window). Wraps and centers according to the texture's width and position.
     * Animates the text character by character until the text is complete
     *
     * @param batch          The SpriteBatch used to draw the text
     * @param text           The text to be drawn
     * @param boundingWidth  The width of the bounding texture
     * @param boundingHeight The height of the bounding texture
     * @param x              The bounding texture's x position.
     * @param y              The bounding texture's y position
     * @param delta          The delta time since the last frame.
     * @param scale          The scale of the text being drawn.
     * @param speed          The time between each character being printed.
     */
    private void drawText(SpriteBatch batch, String text, float boundingWidth, float boundingHeight, float x, float y, float delta, int scale, float speed) {
        bmfont.getData().setScale(scale);
        GlyphLayout glyphLayout = new GlyphLayout();
        // Remove any newlines so we can get an accurate value for the font height
        glyphLayout.setText(bmfont, text.replace("\n", ""));

        //Get the height of a single line of text
        float fontHeight = glyphLayout.height;

        glyphLayout.setText(bmfont, text, bmfont.getColor(), boundingWidth, 1, true);
        int textX = (int) ((boundingWidth / 2 - glyphLayout.width / 2) + x);
        float textY =  ((boundingHeight / 2 - fontHeight / 2) + y);

        // Raise the text proportionally to how many lines there are
        int numLines = (int) (glyphLayout.height / fontHeight);
        if (numLines > 1) textY += fontHeight / 2 * numLines;

        if (textAnimating) {
            textTime += delta;
            if (textTime >= speed) {
                if (textIndex < text.length()) textBuilder += text.charAt(textIndex++);
                textTime = 0;
            }

            bmfont.draw(batch, textBuilder, textX, textY, glyphLayout.width, 1, true);
            if (textBuilder.equals(text)) {
                textAnimating = false;
                textBuilder = "";
                textIndex = 0;
            }
        } else {
            bmfont.draw(batch, text, textX, textY, glyphLayout.width, 1, true);
        }

        // Error catching, resets if there is an issue with the textBuilder.
        if(!text.contains(textBuilder)) {
            textIndex = 0;
            textBuilder = "";
        }
    }
}
