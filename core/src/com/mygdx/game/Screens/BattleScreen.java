package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Button;
import com.mygdx.game.Character.Enemy;
import com.mygdx.game.Character.Player;

public class BattleScreen implements Screen {

    private static final float BUTTON_PAD = 0.5f;

    private GameScreen gameScreen;
    private Enemy enemy;
    private Player player;

    private Texture buttonUp;
    private Texture buttonDown;

    SpriteBatch spriteBatch;

    private BitmapFont bmfont;

    private TextureRegion textWindow;

    private Button attackButton;
    private Button itemButton;
    private Button runButton;

    // Attack buttons
    private Button playerAttackButton1;
    private Button playerAttackButton2;
    private Button playerAttackButton3;
    private Button playerAttackButton4;

    private boolean inAttacks;
    private boolean inItems;

    public BattleScreen(GameScreen gameScreen, Enemy enemy, Player player) {
        this.gameScreen = gameScreen;
        this.enemy = enemy;
        this.player = player;

        spriteBatch = new SpriteBatch();

        bmfont = new BitmapFont(
                Gdx.files.internal("font/good_neighbors.fnt"),
                Gdx.files.internal("font/good_neighbors.png"),
                false);

        bmfont.getData().setScale(2);

        textWindow = new TextureRegion(new Texture("window.png"));

        buttonUp = new Texture("buttonUp.png");
        buttonDown = new Texture("buttonDown.png");

        attackButton = new Button(Gdx.graphics.getWidth()/2 + BUTTON_PAD, textWindow.getRegionHeight()/2 + BUTTON_PAD, Gdx.graphics.getWidth()/2 - 2 * BUTTON_PAD,
                textWindow.getRegionHeight()/2 - 2 * BUTTON_PAD, buttonUp, buttonDown);
        itemButton = new Button(Gdx.graphics.getWidth()/2 + BUTTON_PAD, 0, Gdx.graphics.getWidth()/4 - 2 * BUTTON_PAD,
                textWindow.getRegionHeight()/2 - BUTTON_PAD, buttonUp, buttonDown);
        runButton = new Button(Gdx.graphics.getWidth() * 3/4 + BUTTON_PAD, 0, Gdx.graphics.getWidth()/4 - 2 * BUTTON_PAD,
                textWindow.getRegionHeight()/2 - BUTTON_PAD, buttonUp, buttonDown);


        playerAttackButton1 = new Button(Gdx.graphics.getWidth()/2 + BUTTON_PAD, textWindow.getRegionHeight()/2 + BUTTON_PAD, Gdx.graphics.getWidth()/4 - 2 * BUTTON_PAD,
                textWindow.getRegionHeight()/2 - 2 * BUTTON_PAD, buttonUp, buttonDown);
        playerAttackButton2 = playerAttackButton4 = new Button(Gdx.graphics.getWidth() * 3/4 + BUTTON_PAD, textWindow.getRegionHeight()/2, Gdx.graphics.getWidth()/4 - 2 * BUTTON_PAD,
                textWindow.getRegionHeight()/2 - BUTTON_PAD, buttonUp, buttonDown);
        playerAttackButton3 = new Button(Gdx.graphics.getWidth()/2 + BUTTON_PAD, 0, Gdx.graphics.getWidth()/4 - 2 * BUTTON_PAD,
                textWindow.getRegionHeight()/2 - BUTTON_PAD, buttonUp, buttonDown);
        playerAttackButton4 = new Button(Gdx.graphics.getWidth() * 3/4 + BUTTON_PAD, 0, Gdx.graphics.getWidth()/4 - 2 * BUTTON_PAD,
                textWindow.getRegionHeight()/2 - BUTTON_PAD, buttonUp, buttonDown);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        //draw battle sprites
        spriteBatch.draw(player.getBattleSprite(), Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/8, 150, 230);
        spriteBatch.draw(enemy.getBattleSprite(), Gdx.graphics.getWidth() * 3/4, Gdx.graphics.getHeight() * 5/8, 150, 230);

        spriteBatch.draw(textWindow, 0, 0);
        bmfont.draw(spriteBatch, "How will you proceed?", Gdx.graphics.getWidth() * 0.034f, Gdx.graphics.getHeight() * 0.12f, Gdx.graphics.getWidth() * 0.43f,
                1, true);

        if(!inAttacks && !inItems) {
            attackButton.draw(spriteBatch, "Attack");
            itemButton.draw(spriteBatch, "Items");
            runButton.draw(spriteBatch, "Run Away");
        }
        else if(inAttacks) {
            playerAttackButton1.draw(spriteBatch);
            playerAttackButton2.draw(spriteBatch);
            playerAttackButton3.draw(spriteBatch);
            playerAttackButton4.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    private void update() {
        boolean checkTouch = Gdx.input.isTouched();

        int touchX = Gdx.input.getX();
        int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if(!inAttacks && !inItems) {
            attackButton.update(checkTouch, touchX, touchY);
            itemButton.update(checkTouch, touchX, touchY);
            runButton.update(checkTouch, touchX, touchY);
        }

        // Only poll for attack buttons when selecting an attack
        if(inAttacks) {
            playerAttackButton1.update(checkTouch, touchX, touchY);
            playerAttackButton2.update(checkTouch, touchX, touchY);
            playerAttackButton3.update(checkTouch, touchX, touchY);
            playerAttackButton4.update(checkTouch, touchX, touchY);
        }

        if(attackButton.justPressed()) {
            inAttacks = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) gameScreen.game.setScreen(gameScreen);
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
        bmfont.dispose();
        textWindow.getTexture().dispose();
    }
}
