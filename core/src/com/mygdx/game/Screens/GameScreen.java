package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Character.Player;
import com.mygdx.game.WorldAnimations.Fountain;
import com.mygdx.game.RPGGame;

public class GameScreen implements Screen {

    public static final int PLAYER_SPEED = 98;

    //Game
    private RPGGame game;
    private SpriteBatch spriteBatch;
    private Player player;
    private Fountain fountain;
    private OrthographicCamera gameCam;
    private FitViewport gamePort;
    private Rectangle tileRectangle, playerDeltaRectangle;

    private Vector2 playerDelta;

    private TiledMap map;
    private TiledMapRenderer renderer;

    public GameScreen(RPGGame game) {
        this.game = game;
    }

    private void create() {
        spriteBatch = new SpriteBatch();

        TmxMapLoader temp = new TmxMapLoader();

        map = temp.load("Town.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

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
        fountain = new Fountain();
        playerDelta = new Vector2();
        playerDeltaRectangle = new Rectangle(0, 0, player.getWidth(), player.getHeight());

        MapLayer objectLayer = map.getLayers().get("Spawns");
        RectangleMapObject playerSpawn = (RectangleMapObject)objectLayer.getObjects().get("Player");
        RectangleMapObject fountainSpawn = (RectangleMapObject)objectLayer.getObjects().get("Fountain");
        player.setCenter(playerSpawn.getRectangle().x, playerSpawn.getRectangle().y);
        fountain.setCenter(fountainSpawn.getRectangle().x, fountainSpawn.getRectangle().y);
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
        playerDelta.x = 0;
        playerDelta.y = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            player.setAnimation(1);
            playerDelta.y = PLAYER_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)){
            player.setAnimation(2);
            playerDelta.x = -1 * PLAYER_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
            player.setAnimation(3);
            playerDelta.y = -1 * PLAYER_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)){
            player.setAnimation(4);
            playerDelta.x = PLAYER_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.X)){
            player.setAnimation(9);
        } else {
            player.setAnimation(0);
        }

        if(playerDelta.len2() > 0) {
            tileRectangle = new Rectangle();
            MapLayer collisionLayer = map.getLayers().get("Collision");
            TiledMapTileLayer tileLayer = (TiledMapTileLayer) collisionLayer;
            tileRectangle.width = tileLayer.getTileWidth();
            tileRectangle.height = tileLayer.getTileHeight();

            int right = (int)Math.ceil(Math.max(
                    player.getX() + player.getWidth(),
                    player.getX() + player.getWidth() + playerDelta.x
            ));
            int top = (int)Math.ceil(Math.max(
                    player.getY() + player.getHeight(),
                    player.getY() + player.getHeight() + playerDelta.y
            ));

            //Makes it so the Player's "body" is stopped by collision and not their head when checking the top
            top -= player.getHeight() * 2 / 3;

            int left = (int)Math.floor(Math.min(
                    player.getX(),
                    player.getX() + playerDelta.x
            ));
            int bottom = (int)Math.floor(Math.min(
                    player.getY(),
                    player.getY() + playerDelta.y
            ));

            right /= tileLayer.getTileWidth();
            top /= tileLayer.getTileHeight();
            left /= tileLayer.getTileWidth();
            bottom /= tileLayer.getTileHeight();

            for(int y = bottom; y <= top; y++) {
                for(int x = left; x <= right; x++) {
                    TiledMapTileLayer.Cell targetCell = tileLayer.getCell(x, y);
                    if(targetCell == null) {
                        continue;
                    }
                    tileRectangle.x = x * tileLayer.getTileWidth();
                    tileRectangle.y = y * tileLayer.getTileHeight();

                    playerDeltaRectangle.x = player.getX() + playerDelta.x;
                    playerDeltaRectangle.y = player.getY();

                    if(tileRectangle.overlaps(playerDeltaRectangle)) {
                        playerDelta.x = 0;
                    }

                    playerDeltaRectangle.x = player.getX();
                    playerDeltaRectangle.y = player.getY() + playerDelta.y;
                    if(tileRectangle.overlaps(playerDeltaRectangle)) {
                        playerDelta.y = 0;
                    }
                }
            }
        }

        player.translate(playerDelta.x, playerDelta.y);
        gameCam.translate(playerDelta);

        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            gameCam.zoom += 1;
        }
    }

    public void update(float delta) {
        handleInput(delta);
        gameCam.update();
        player.update(delta);
        fountain.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(gameCam);
        renderer.render();
        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(gameCam.combined);
        player.draw(spriteBatch);
        spriteBatch.setProjectionMatrix(gameCam.combined);
        spriteBatch.draw(fountain, fountain.getX(), fountain.getY());
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
