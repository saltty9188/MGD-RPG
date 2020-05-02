package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.RPGGame;

public class GameScreen implements Screen {

    //Game
    RPGGame game;

    private SpriteBatch spriteBatch;

    Texture playerSheet;
    TextureRegion walkDown,walkRight,walkUp,walkLeft;
    Animation<TextureRegion> animation;
    Sprite player;

    OrthographicCamera gamCam;
    FitViewport gamePort;

    public GameScreen(RPGGame game) {this.game = game;}

    public void create(){

        gamCam = new OrthographicCamera();
        gamePort = new FitViewport(800 / RPGGame.PPM, 480 / RPGGame.PPM);

        spriteBatch = new SpriteBatch();
        playerSheet = new Texture("character.png");
        walkDown = new TextureRegion(playerSheet,0,0,16,32);
        player = new Sprite(walkDown);

        gamCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);
    }

    @Override
    public void show() {
        create();
    }

    @Override
    public void render(float delta) {
        spriteBatch.setProjectionMatrix(gamCam.combined);
        spriteBatch.begin();
        spriteBatch.draw(player,0,0);
        spriteBatch.end();
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
