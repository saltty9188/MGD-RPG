package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Character.Enemy;
import com.mygdx.game.Character.Player;

public class BattleScreen implements Screen {

    private GameScreen gameScreen;
    private Enemy enemy;
    private Player player;

    public BattleScreen(GameScreen gameScreen, Enemy enemy, Player player) {
        this.gameScreen = gameScreen;
        this.enemy = enemy;
        this.player = player;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

    }
}
