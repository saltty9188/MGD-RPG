package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Character.Enemy;
import com.mygdx.game.Character.Player;

public class BattleScreen implements Screen {

    private GameScreen gameScreen;
    private Enemy enemy;
    private Player player;

    SpriteBatch spriteBatch;

    private BitmapFont bmfont;

    private Texture textWindow;

    public BattleScreen(GameScreen gameScreen, Enemy enemy, Player player) {
        this.gameScreen = gameScreen;
        this.enemy = enemy;
        this.player = player;

        spriteBatch = new SpriteBatch();

        bmfont = new BitmapFont(
                Gdx.files.internal("font/good_neighbors.fnt"),
                Gdx.files.internal("font/good_neighbors.png"),
                false);

        textWindow = new Texture("window_blue.png");
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
        spriteBatch.draw(player.getBattleSprite(), Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/8);
        spriteBatch.draw(enemy.getBattleSprite(), Gdx.graphics.getWidth() * 3/4, Gdx.graphics.getHeight() * 5/8);

        spriteBatch.end();
    }

    private void update() {
        if(Gdx.input.isTouched()) gameScreen.game.setScreen(gameScreen);
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
        textWindow.dispose();
        bmfont.dispose();
    }
}
