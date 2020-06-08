package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Button;
import com.mygdx.game.RPGGame;

public class TitleScreen  implements Screen {

    //Game
    RPGGame game;

    private SpriteBatch batch;

    private Texture background;

    private Texture beginButtonUp;
    private Texture beginButtonDown;
    private Texture creditsButtonUp;
    private Texture creditsButtonDown;
    private Texture exitButtonUp;
    private Texture exitButtonDown;

    private Button beginButton;
    private Button creditsButton;
    private Button exitButton;

    public TitleScreen(RPGGame game) {

        this.game = game;
        create();
    }

    private void create() {
        batch = new SpriteBatch();

        background = new Texture("Backgrounds/Title_Page.png");

        beginButtonUp = new Texture("Buttons/begin-button-up.png");
        beginButtonDown = new Texture("Buttons/begin-button-down.png");
        creditsButtonUp = new Texture("Buttons/credits-button-up.png");
        creditsButtonDown = new Texture("Buttons/credits-button-down.png");
        exitButtonUp = new Texture("Buttons/exit-button-up.png");
        exitButtonDown = new Texture("Buttons/exit-button-down.png");

        beginButton = new Button(Gdx.graphics.getWidth() * 143/240, Gdx.graphics.getHeight() * 26/45, Gdx.graphics.getWidth() * 77/240, Gdx.graphics.getHeight() * 32/135,
                beginButtonUp, beginButtonDown);
        creditsButton = new Button(Gdx.graphics.getWidth() * 19/30, Gdx.graphics.getHeight() * 53/135, Gdx.graphics.getWidth() * 59/240, Gdx.graphics.getHeight() * 19/135,
                creditsButtonUp, creditsButtonDown);
        exitButton = new Button(Gdx.graphics.getWidth() * 19/30, Gdx.graphics.getHeight() * 26/135, Gdx.graphics.getWidth() * 59/240, Gdx.graphics.getHeight() * 19/135,
                exitButtonUp, exitButtonDown);
    }

    @Override
    public void show() {
        RPGGame.currentTrack.stop();
        RPGGame.currentTrack = RPGGame.titleTheme;
        RPGGame.currentTrack.play();
    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        beginButton.draw(batch);
        creditsButton.draw(batch);
        exitButton.draw(batch);
        batch.end();
    }

    private void update() {
        boolean checkTouch = Gdx.input.isTouched();
        int touchX = Gdx.input.getX();
        int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        beginButton.update(checkTouch, touchX, touchY);
        creditsButton.update(checkTouch, touchX, touchY);
        exitButton.update(checkTouch, touchX, touchY);

        // handle button presses
        if(beginButton.justPressed()) {
            RPGGame.gameScreen.newGame();
            game.setScreen(RPGGame.gameScreen);
        } else if(creditsButton.justPressed()) {
            game.setScreen(RPGGame.creditsScreen);
        } else if(exitButton.justPressed()) {
            Gdx.app.exit();
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
