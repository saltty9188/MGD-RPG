package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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

    private static final float PADDING = 1f;

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
    private Button potionButton;
    private Button hiPotionButton;
    private Button etherButton;
    private Button hiEtherButton;

    //Buttons for purchasing and selecting quantity
    private Button qtyUp;
    private Button qtyDown;
    private Button purchaseItem;
    private Button sellItem;
    private Button cancel;
    private int buyQty;

    private Sound buySound;
    private Sound sellSound;

    private Texture background;

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

        textWindow = new Texture("window_blue_long.png");
        background = new Texture("Backgrounds/shop-background.png");

        buttonUp = new Texture("Buttons/buttonUp.png");
        buttonDown = new Texture("Buttons/buttonDown.png");

        buyButton = new Button(PADDING, Gdx.graphics.getHeight() * 33/48 - PADDING, Gdx.graphics.getWidth() / 3 - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - PADDING,
                buttonUp, buttonDown);
        sellButton = new Button(3*PADDING + Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() * 33/48 - PADDING, Gdx.graphics.getWidth() / 3 - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - PADDING,
                buttonUp, buttonDown);
        exitButton = new Button(5* PADDING + Gdx.graphics.getWidth() * 2/3, Gdx.graphics.getHeight() * 33/48 - PADDING, Gdx.graphics.getWidth() / 3 - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - PADDING,
                buttonUp, buttonDown);

        potionButton = new Button(PADDING, Gdx.graphics.getHeight() * 13/24 - 4*PADDING, Gdx.graphics.getWidth() - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - 2*PADDING,
                buttonUp, buttonDown);
        hiPotionButton = new Button(PADDING, Gdx.graphics.getHeight() * 19/48 - 6*PADDING, Gdx.graphics.getWidth() - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - 2*PADDING,
                buttonUp, buttonDown);
        etherButton = new Button(PADDING, Gdx.graphics.getHeight() * 12/48 - 8*PADDING, Gdx.graphics.getWidth() - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - 2*PADDING,
                buttonUp, buttonDown);
        hiEtherButton = new Button(PADDING, Gdx.graphics.getHeight() * 5/48 - 10*PADDING, Gdx.graphics.getWidth() - 2*PADDING, Gdx.graphics.getHeight() * 7/48 - 2*PADDING,
                buttonUp, buttonDown);


        qtyUp = new Button(Gdx.graphics.getWidth() * 2/5 + 2 * PADDING, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/20, Gdx.graphics.getWidth()/16, buttonUp,
                buttonDown);
        // QTY NUMBER X 7/20
        qtyDown = new Button(Gdx.graphics.getWidth()* 3/10, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth()/20, Gdx.graphics.getWidth()/16, buttonUp,
                buttonDown);
        purchaseItem = new Button(Gdx.graphics.getWidth() * 9/20 + 3 * PADDING, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth() / 8 - 2 * PADDING,
                Gdx.graphics.getWidth()/16 , buttonUp, buttonDown);
        sellItem = new Button(Gdx.graphics.getWidth() * 9/20 + 3 * PADDING, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth() / 8 - 2 * PADDING,
                Gdx.graphics.getWidth()/16 , buttonUp, buttonDown);
        cancel = new Button(Gdx.graphics.getWidth() * 23/40 + 4 * PADDING, Gdx.graphics.getHeight()/3, Gdx.graphics.getWidth() / 8 - 2 * PADDING,
                Gdx.graphics.getWidth()/16, buttonUp, buttonDown);

        buySound = Gdx.audio.newSound(Gdx.files.internal("Music/ka_ching.wav"));
        sellSound = Gdx.audio.newSound(Gdx.files.internal("Music/leather_inventory.wav"));
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

        RPGGame.currentTrack.stop();
        RPGGame.currentTrack = RPGGame.shopTheme;
        RPGGame.currentTrack.play();
    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(textWindow, 0, Gdx.graphics.getHeight() * 5/6, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/6);

        displayText(batch, shopMessage, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/6, 0, Gdx.graphics.getHeight() * 5/6);

        buyButton.draw(batch, "Buy");
        sellButton.draw(batch, "Sell");
        exitButton.draw(batch, "Exit");

        if(buying) {
            potionButton.draw(batch, "Potion", "Stock: " + player.getItem(0).getQty() + " \t\t Price " + player.getItem(0).getValue(), false);
            hiPotionButton.draw(batch, "Hi-Potion", "Stock: " + player.getItem(1).getQty() + " \t\t Price " + player.getItem(1).getValue(), false);
            etherButton.draw(batch, "Ether", "Stock: " + player.getItem(2).getQty() + " \t\t Price " + player.getItem(2).getValue(), false);
            hiEtherButton.draw(batch, "Hi-Ether", "Stock: " + player.getItem(3).getQty() + " \t\t Price " + player.getItem(3).getValue(), false);
        } else if(selling) {
            // Items are sold at half of their cost
            potionButton.draw(batch, "Potion", "Stock: " + player.getItem(0).getQty() + " \t\t Value " + player.getItem(0).getValue()/2, false);
            hiPotionButton.draw(batch, "Hi-Potion", "Stock: " + player.getItem(1).getQty() + " \t\t Value " + player.getItem(1).getValue()/2, false);
            etherButton.draw(batch, "Ether", "Stock: " + player.getItem(2).getQty() + " \t\t Value " + player.getItem(2).getValue()/2, false);
            hiEtherButton.draw(batch, "Hi-Ether", "Stock: " + player.getItem(3).getQty() + " \t\t Value " + player.getItem(3).getValue()/2, false);
        }

        if(itemSelected) {
            drawQtySelector(batch, delta, buying);
        }
        batch.end();
    }

    private void displayText(SpriteBatch batch, String text, float delta, float boundingWidth, float boundingHeight, float x, float y) {
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

    private void update() {
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

        if((buying || selling) && !itemSelected) {
            // Want button to stay down while in the correct menu
            buyButton.isDown = buying;
            sellButton.isDown = selling;
            // update purchase buttons
            potionButton.update(checkTouch, touchX, touchY);
            hiPotionButton.update(checkTouch, touchX, touchY);
            etherButton.update(checkTouch, touchX, touchY);
            hiEtherButton.update(checkTouch, touchX, touchY);

            // handle item button presses
            if(potionButton.justPressed()) {
                itemSelected = true;
                selectedItem = player.getItem(0);
            } else if(hiPotionButton.justPressed()) {
                itemSelected = true;
                selectedItem = player.getItem(1);
            } else if(etherButton.justPressed()) {
                itemSelected = true;
                selectedItem = player.getItem(2);
            } else if(hiEtherButton.justPressed()) {
                itemSelected = true;
                selectedItem = player.getItem(3);
            }
        }  else if(itemSelected) {
            qtyDown.update(checkTouch, touchX, touchY);
            qtyUp.update(checkTouch, touchX, touchY);
            if(buying) purchaseItem.update(checkTouch, touchX, touchY);
            else if (selling) sellItem.update(checkTouch, touchX, touchY);
            cancel.update(checkTouch, touchX, touchY);
        }

        if(buying && itemSelected) {
            if (qtyDown.justPressed()) {
                buyQty--;
                if (buyQty < 1) buyQty = 1;
            } else if (qtyUp.justPressed()) {
                buyQty++;
                if (buyQty > 99) buyQty = 99;
            } else if (purchaseItem.justPressed() && player.getGold() >= buyQty * selectedItem.getValue()) {
                player.spendGold(buyQty*selectedItem.getValue());
                buySound.play();
                selectedItem.addItems(buyQty);
                itemSelected = false;
                selectedItem = null;
                buyQty = 1;
            } else if (cancel.justPressed()) {
                itemSelected = false;
                selectedItem = null;
                buyQty = 1;
            }
        } else if(selling && itemSelected) {
            if (qtyDown.justPressed()) {
                buyQty--;
                if (buyQty < 1) buyQty = 1;
            } else if (qtyUp.justPressed()) {
                buyQty++;
                if (buyQty > 99) buyQty = 99;
            } else if (sellItem.justPressed() && selectedItem.getQty() >= buyQty) {
                player.earnGold(buyQty * selectedItem.getValue()/2);
                sellSound.play();
                selectedItem.removeItems(buyQty);
                itemSelected = false;
                selectedItem = null;
                buyQty = 1;
            } else if (cancel.justPressed()) {
                itemSelected = false;
                selectedItem = null;
                buyQty = 1;
            }
        }

    }

    /**
     * Draws a window where the player specifies the quantity of the selected item they wish to buy/sell.
     * @param batch  The sprite batch used to draw.
     * @param buying True if the player wishes to buy or false if they wish to sell
     */
    private void drawQtySelector(SpriteBatch batch, float delta, boolean buying) {
        textAnimating = false;
        batch.draw(textWindow, Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/8, Gdx.graphics.getWidth() * 3/4, Gdx.graphics.getHeight() * 3/4);
        displayText(batch, selectedItem.getName() + "\n" + selectedItem.getDescription(), delta, Gdx.graphics.getWidth() * 3/4,
                Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/2);

        int total = (buying ? buyQty * selectedItem.getValue() : buyQty * selectedItem.getValue() / 2);
        displayText(batch, "Total: " + total + "G", delta, Gdx.graphics.getWidth() * 3/4,
                Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight() * 3/8);


        qtyDown.draw(batch, "-");
        displayText(batch, Integer.toString(buyQty), delta, Gdx.graphics.getWidth()/20 - 2*PADDING, Gdx.graphics.getHeight()/16,
                Gdx.graphics.getWidth() * 7/20 + PADDING, Gdx.graphics.getHeight()/3 + 10);
        qtyUp.draw(batch, "+");
        if(buying) purchaseItem.draw(batch, "Buy", "", player.getGold() < buyQty * selectedItem.getValue());
        else sellItem.draw(batch, "Sell", "", selectedItem.getQty() < buyQty);
        cancel.draw(batch, "Cancel");

        displayText(batch, "Gold: " + player.getGold() + "G", delta,Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/16,
                Gdx.graphics.getWidth() * 12/20 + PADDING, Gdx.graphics.getHeight()/4);
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
        buySound.dispose();
        sellSound.dispose();
        buttonUp.dispose();
        buttonDown.dispose();
        textWindow.dispose();

        buyButton.dispose();
        sellButton.dispose();
        exitButton.dispose();
        potionButton.dispose();
        hiPotionButton.dispose();
        etherButton.dispose();
        hiEtherButton.dispose();
        qtyUp.dispose();
        qtyDown.dispose();
        purchaseItem.dispose();
        sellItem.dispose();
        cancel.dispose();
        bmfont.dispose();
        background.dispose();
        batch.dispose();
    }
}
