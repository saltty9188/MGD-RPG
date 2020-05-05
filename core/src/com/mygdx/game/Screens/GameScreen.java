package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Character.Player;
import com.mygdx.game.RPGGame;

import java.awt.Rectangle;

public class GameScreen implements Screen {

    //Game
    private RPGGame game;

    private SpriteBatch spriteBatch;

    private Texture playerSheet;
    private TextureRegion walkDown,walkRight,walkUp,walkLeft;
    private Animation<TextureRegion> animation;
    private Player player;
    private OrthographicCamera gameCam;
    private FitViewport gamePort;

    private TiledMap loader;
    private TiledMapRenderer renderer;

    public GameScreen(RPGGame game) {
        this.game = game;
    }

    private void create() {

        spriteBatch = new SpriteBatch();

        TmxMapLoader temp = new TmxMapLoader();

        loader = temp.load("Town.tmx");
        renderer = new OrthogonalTiledMapRenderer(loader);

        //create can used to follow Character through the game world
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, w, h);

        gameCam.position.x = Gdx.graphics.getWidth()/2;
        gameCam.position.y = Gdx.graphics.getHeight()/2;
        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(RPGGame.WIDTH / RPGGame.PPM,
                RPGGame.HEIGHT / RPGGame.PPM);

        player = new Player();

        MapLayer objectLayer = loader.getLayers().get("Spawns");
        RectangleMapObject playerSpawn = (RectangleMapObject)objectLayer.getObjects().get("Player");
        player.setCenter(playerSpawn.getRectangle().x, playerSpawn.getRectangle().y);
        gameCam.position.set(player.getX(), player.getY(), 0);
    }

    @Override
    public void show() {
        create();
        if (Gdx.graphics.getHeight() != 1080){
            Gdx.graphics.setWindowedMode(1920,1080);
        }
    }

    public void handleInput(float delta){
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            player.setAnimation(1);
            player.setY(player.getY() + (98*delta));
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            player.setAnimation(2);
            player.setX(player.getX() - (98*delta));
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
            player.setAnimation(3);
            player.setY(player.getY() - (98*delta));
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)){
            player.setAnimation(4);
            player.setX(player.getX() + (98*delta));
        } else if (Gdx.input.isKeyPressed(Input.Keys.X)){
            player.setAnimation(9);
        } else {
            player.setAnimation(0);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            gameCam.zoom += 1;
        }
    }

    public void update(float delta) {
        handleInput(delta);
        gameCam.position.x = player.getX();
        gameCam.update();
        player.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameCam.update();
        renderer.setView(gameCam);
        renderer.render();

        spriteBatch.setProjectionMatrix(gameCam.combined);
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
