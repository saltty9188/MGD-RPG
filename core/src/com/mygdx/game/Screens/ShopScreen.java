package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

        buttonUp = new Texture("buttonUp.png");
        buttonDown = new Texture("buttonDown.png");

        buyButton = new Button(PADDING, Gdx.graphics.getHeight() - PADDING - Gdx.graphics.getHeight() * 7/24, Gdx.graphics.getWidth() / 3 - 2*PADDING, Gdx.graphics.getHeight() * 7/24 - PADDING,
                buttonUp, buttonDown);
        sellButton = new Button(3*PADDING + Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() - PADDING - Gdx.graphics.getHeight() * 7/24, Gdx.graphics.getWidth() / 3 - 2*PADDING, Gdx.graphics.getHeight() * 7/24 - PADDING,
                buttonUp, buttonDown);
        exitButton = new Button(5* PADDING + Gdx.graphics.getWidth() * 2/3, Gdx.graphics.getHeight() - PADDING - Gdx.graphics.getHeight() * 7/24, Gdx.graphics.getWidth() / 3 - 2*PADDING, Gdx.graphics.getHeight() * 7/24 - PADDING,
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
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
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
        } else if (sellButton.justPressed()) {
            selling = true;
            buying = false;
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
