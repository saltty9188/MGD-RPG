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
import com.mygdx.game.Items.Item;
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
    private Item selectedItem;

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


    //Buttons for purchasing and selecting quantity
    private Button qtyUp;
    private Button qtyDown;
    private Button purchaseItem;
    private Button cancel;
    private int buyQty;

    public ShopScreen(RPGGame game) {
        this.game = game;
        create();
    }

    private void create() {
        buying = false;
        selling = false;
        itemSelected = false;
        selectedItem = null;

        buyQty = 1;

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

        testItemButton = new Button(PADDING, Gdx.graphics.getHeight() * 13/24 - 2*PADDING, Gdx.graphics.getWidth() - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - 2*PADDING,
                buttonUp, buttonDown);


        qtyUp = new Button(Gdx.graphics.getWidth() * 2/5 + 2 * PADDING, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/20, Gdx.graphics.getWidth()/16, buttonUp,
                buttonDown);
        // QTY NUMBER X 7/20
        qtyDown = new Button(Gdx.graphics.getWidth()* 3/10, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/20, Gdx.graphics.getWidth()/16, buttonUp,
                buttonDown);
        purchaseItem = new Button(Gdx.graphics.getWidth() * 9/20 + 3 * PADDING, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth() / 8 - 2 * PADDING,
                Gdx.graphics.getWidth()/16 , buttonUp, buttonDown);
        cancel = new Button(Gdx.graphics.getWidth() * 23/40 + 4 * PADDING, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth() / 8 - 2 * PADDING,
                Gdx.graphics.getWidth()/16, buttonUp, buttonDown);
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
        buyQty = 1;
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(textWindow, 0, Gdx.graphics.getHeight() * 5/6, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/6);
        displayText(batch, shopMessage, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/6, 0, Gdx.graphics.getHeight() * 5/6);

        buyButton.draw(batch, "Buy");
        sellButton.draw(batch, "Sell");
        exitButton.draw(batch, "Exit");

        if(buying) {
            testItemButton.draw(batch, "Test Item", "Stock: 3 \t\t Price: 50G", false);
        } else if(selling) {
            //draw sell buttons
        }

        if(itemSelected) {
            drawQtySelector(batch, delta);
        }
        batch.end();
    }

    public void displayText(SpriteBatch batch, String text, float delta, float boundingWidth, float boundingHeight, float x, float y) {

        bmfont.getData().setScale(2);
        GlyphLayout glyphLayout = new GlyphLayout();
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

            if (textTime >= 0.03f) {
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

        if(buying && !itemSelected) {
            // Want button to stay down while in the buy menu
            buyButton.isDown = true;
            // update purchase buttons
            testItemButton.update(checkTouch, touchX, touchY);

            if(testItemButton.justPressed()) {
                //Draw qty menu
                itemSelected = true;
                //selectedItem = item;
            }
        } else if(selling && !itemSelected) {
            sellButton.isDown = true;
        } else if(itemSelected) {
            qtyDown.update(checkTouch, touchX, touchY);
            qtyUp.update(checkTouch, touchX, touchY);
            purchaseItem.update(checkTouch, touchX, touchY);
            cancel.update(checkTouch, touchX, touchY);
        }

        if(qtyDown.justPressed()) {
            buyQty--;
            if(buyQty < 1) buyQty = 1;
        } else if(qtyUp.justPressed()) {
            buyQty++;
            if(buyQty > 99) buyQty = 99;
        } else if(purchaseItem.justPressed()) {
            //do buying stuff
        } else if(cancel.justPressed()) {
            itemSelected = false;
            //selectedItem = null;
            buyQty = 1;
        }

    }

    private void drawQtySelector(SpriteBatch batch, float delta) {
        textAnimating = false;
        batch.draw(textWindow, Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/8, Gdx.graphics.getWidth() * 3/4, Gdx.graphics.getHeight() * 3/4);
        displayText(batch, "Potion\nRestores 20HP", delta, Gdx.graphics.getWidth() * 3/4, Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/2);

        qtyDown.draw(batch, "-");
        displayText(batch, Integer.toString(buyQty), delta, Gdx.graphics.getWidth()/20 - 2*PADDING, Gdx.graphics.getHeight()/16,
                Gdx.graphics.getWidth() * 7/20 + PADDING, Gdx.graphics.getHeight()/3 + 6);
        qtyUp.draw(batch, "+");
        purchaseItem.draw(batch, "Buy");
        cancel.draw(batch, "Cancel");
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
