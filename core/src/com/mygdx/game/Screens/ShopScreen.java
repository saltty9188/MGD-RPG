package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Button;
import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

/**
 * This screen is shown when the player talks to the shopkeeper NPC to purchase items.
 * The details on this screen include the type of item, the price, the quantity being bought,
 * and the quantity currently in the player's inventory.
 */
public class ShopScreen implements Screen {

    private static final float PADDING = 0.5f;

    private RPGGame game;
    private Player player;

    private boolean buying;
    private boolean selling;
    private boolean itemSelected;

    private SpriteBatch batch;
    
    private String shopMessage;
    private Texture textWindow;
    private BitmapFont bmfont;
    private boolean textAnimating;
    private String textBuilder;
    private int textIndex;
    private float textTime;

    private Texture buttonUp;
    private Texture buttonDown;

    private Button buyButton;
    private Button sellButton;
    private Button exitButton;

    //buttons for the items on sale
    private Button testItemButton;

    //buttons for items in player's inventory to sell

    public ShopScreen(RPGGame game) {
        this.game = game;
        create();
    }

    private void create() {
        buying = false;
        selling = false;
        itemSelected = false;

        batch = new SpriteBatch();

        shopMessage = "How can I help you?";
        textAnimating = true;
        textIndex = 0;
        textBuilder = "";
        textTime = 0;

        bmfont = new BitmapFont(
                Gdx.files.internal("font/good_neighbors.fnt"),
                Gdx.files.internal("font/good_neighbors.png"),
                false);

        textWindow = new Texture("window_blue.png");

        buttonUp = new Texture("buttonUp.png");
        buttonDown = new Texture("buttonDown.png");

        buyButton = new Button(PADDING, Gdx.graphics.getHeight() * 33/48 - PADDING, Gdx.graphics.getWidth() / 3 - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - PADDING,
                buttonUp, buttonDown);
        sellButton = new Button(3*PADDING + Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() * 33/48 - PADDING, Gdx.graphics.getWidth() / 3 - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - PADDING,
                buttonUp, buttonDown);
        exitButton = new Button(5* PADDING + Gdx.graphics.getWidth() * 2/3, Gdx.graphics.getHeight() * 33/48 - PADDING, Gdx.graphics.getWidth() / 3 - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - PADDING,
                buttonUp, buttonDown);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void show() {
        buying = false;
        selling = false;
        itemSelected = false;
        shopMessage = "How can I help you?";
        textAnimating = true;
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        displayText(batch, delta);

        buyButton.draw(batch, "Buy");
        sellButton.draw(batch, "Sell");
        exitButton.draw(batch, "Exit");

        if(!buying && !selling) {
            // prompt
        } else if(buying) {
            //draw buy buttons
        } else if(selling) {
            //draw sell buttons
        }
        batch.end();
    }

    public void displayText(SpriteBatch batch, float delta) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight()/6;
        float x = 0;
        float y = Gdx.graphics.getHeight() * 5/6;
        batch.draw(textWindow, x, y, width, height);

        bmfont.getData().setScale(2);
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bmfont, shopMessage);

        //Get the height of a single line of text
        float fontHeight = glyphLayout.height;

        glyphLayout.setText(bmfont, shopMessage, bmfont.getColor(), width, 1, true);
        int textX = (int) ((width / 2 - glyphLayout.width / 2) + x);
        float textY =  ((height / 2 - fontHeight / 2) + y);

        // Raise the text proportionally to how many lines there are
        int numLines = (int) (glyphLayout.height / fontHeight);
        if(shopMessage.contains("\n")) textY += fontHeight/2; // Assumes there will only be one newline in the text
        if (numLines > 1) textY += fontHeight / 2 * numLines;

        if (textAnimating) {
            textTime += delta;

            if (textTime >= 0.01f) {
                if (textIndex < shopMessage.length()) textBuilder += shopMessage.charAt(textIndex++);
                textTime = 0;
            }

            bmfont.draw(batch, textBuilder, textX, textY, glyphLayout.width, 1, true);
            if (textBuilder.equals(shopMessage)) {
                textAnimating = false;
                textBuilder = "";
                textIndex = 0;
            }
        } else {
            bmfont.draw(batch, shopMessage, textX, textY, glyphLayout.width, 1, true);
        }

        // Error catching, resets if there is an issue with the textBuilder.
        if(!shopMessage.contains(textBuilder)) {
            textIndex = 0;
            textBuilder = "";
        }
    }

    private void update(float delta) {
        boolean checkTouch = Gdx.input.isTouched();
        int touchX = Gdx.input.getX();
        int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if(!itemSelected) {
            buyButton.update(checkTouch, touchX, touchY);
            sellButton.update(checkTouch, touchX, touchY);
            exitButton.update(checkTouch, touchX, touchY);
        }

        if(buyButton.justPressed()) {
            buying = true;
            selling = false;

            shopMessage = "What are you looking buy?";
            textAnimating = true;
        } else if (sellButton.justPressed()) {
            selling = true;
            buying = false;

            shopMessage = "Let me see what you've got.";
            textAnimating = true;
        } else if (exitButton.justPressed()) {
            game.setScreen(RPGGame.gameScreen);
        }

        if(buying) {
            // Want button to stay down while in the buy menu
            buyButton.isDown = true;
            // update purchase buttons
        } else if(selling) {
            sellButton.isDown = true;
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
}
