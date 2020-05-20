package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
<<<<<<< Updated upstream
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
=======
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Character.Player;
import com.mygdx.game.WorldAnimations.Flag;
import com.mygdx.game.WorldAnimations.Fountain;
>>>>>>> Stashed changes
import com.mygdx.game.RPGGame;

import java.util.ArrayList;

public class GameScreen implements Screen {

    //Game
    private RPGGame game;

    private SpriteBatch spriteBatch;
<<<<<<< Updated upstream

    private Texture playerSheet;
    private TextureRegion walkDown,walkRight,walkUp,walkLeft;
    private Animation<TextureRegion> animation;
    private Sprite player;
=======
    private Player player;
    private Fountain fountain;
    private Flag flag1, flag2, flag3;
    private ArrayList<Flag> flags;
    private ArrayList<RectangleMapObject> objects;
>>>>>>> Stashed changes
    private OrthographicCamera gameCam;
    private FitViewport gamePort;

    public GameScreen(RPGGame game) {
<<<<<<< Updated upstream
=======
        this.game = game;
    }

    private void create() {
        spriteBatch = new SpriteBatch();

        flags = new ArrayList<Flag>();
        objects = new ArrayList<RectangleMapObject>();

        flags.add(flag1);
        flags.add(flag2);
        flags.add(flag3);

        TmxMapLoader temp = new TmxMapLoader();
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
=======
        MapLayer objectLayer = map.getLayers().get("Spawns");
        RectangleMapObject playerSpawn = (RectangleMapObject)objectLayer.getObjects().get("Player");
        RectangleMapObject fountainSpawn = (RectangleMapObject)objectLayer.getObjects().get("Fountain");
        RectangleMapObject flag1Spawn = (RectangleMapObject)objectLayer.getObjects().get("Flag1");
        RectangleMapObject flag2Spawn = (RectangleMapObject)objectLayer.getObjects().get("Flag2");
        RectangleMapObject flag3Spawn = (RectangleMapObject)objectLayer.getObjects().get("Flag3");
        player.setCenter(playerSpawn.getRectangle().x, playerSpawn.getRectangle().y);
        fountain.setCenter(fountainSpawn.getRectangle().x, fountainSpawn.getRectangle().y);
        for(int i = 0; i < flags.size(); i++) {
            RectangleMapObject obj = (RectangleMapObject)objectLayer.getObjects().get("Flag" + i);
            flags.get(i).setCenter(obj.getRectangle().x, obj.getRectangle().y);
        }
>>>>>>> Stashed changes
        gameCam.position.set(player.getX(), player.getY(), 0);
    }

    @Override
    public void show() {}

    public void update(float delta){
        gameCam.position.x = player.getX();
        gameCam.update();
<<<<<<< Updated upstream
=======
        player.update(delta);
        fountain.update(delta);
        for(int i = 0; i < flags.size(); i++) {
            flags.get(i).update(delta);
        }
>>>>>>> Stashed changes
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //spriteBatch.setProjectionMatrix(gameCam.combined);
        spriteBatch.begin();
        player.draw(spriteBatch);
<<<<<<< Updated upstream
=======
        spriteBatch.setProjectionMatrix(gameCam.combined);
        spriteBatch.draw(fountain, fountain.getX(), fountain.getY());
        for(int i = 0; i < flags.size(); i++) {
            spriteBatch.setProjectionMatrix(gameCam.combined);
            spriteBatch.draw(flags.get(i), flags.get(i).getX(), flags.get(i).getY());
        }
>>>>>>> Stashed changes
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
