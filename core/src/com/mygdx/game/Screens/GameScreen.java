package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.RPGGame;

public class GameScreen implements Screen {

    //Game
    private RPGGame game;

    private SpriteBatch spriteBatch;

    private Texture playerSheet;
    private TextureRegion walkDown,walkRight,walkUp,walkLeft;
    private Animation<TextureRegion> animation;
    private Sprite player;
    private OrthographicCamera gameCam;
    private FitViewport gamePort;

    public GameScreen(RPGGame game) {

        this.game = game;

        //create can used to follow Character through the game world
        gameCam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(RPGGame.WIDTH / RPGGame.PPM,
                RPGGame.HEIGHT / RPGGame.PPM);

        spriteBatch = new SpriteBatch();

        playerSheet = new Texture("character.png");

        walkDown = new TextureRegion(playerSheet,0,0,16,32);

        player = new Sprite(walkDown); player.setPosition(0,0);

        player.setSize(64,128);

        gameCam.position.set(player.getX(), player.getY(), 0);
    }

    @Override
    public void show() {}

    public void update(float delta){
        gameCam.position.x = player.getX();
        gameCam.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //spriteBatch.setProjectionMatrix(gameCam.combined);
        spriteBatch.begin();
        player.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
