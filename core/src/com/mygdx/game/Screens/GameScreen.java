package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Character.Character;
import com.mygdx.game.Character.Enemy;
import com.mygdx.game.Character.NPC;
import com.mygdx.game.Character.Player;
import com.mygdx.game.WorldAnimations.Fountain;
import com.mygdx.game.RPGGame;

public class GameScreen implements Screen {

    public static final int PLAYER_SPEED = 98;

    //Game
    RPGGame game;
    private BattleScreen battleScreen;

    private SpriteBatch spriteBatch;
    private OrthographicCamera gameCam;
    private FitViewport gamePort;
    private Rectangle tileRectangle, playerDeltaRectangle;

    private Vector2 playerDelta;

    private TiledMap map;
    private int mapWidth;
    private int mapHeight;
    private TiledMapRenderer renderer;

    private Player player;
    private Fountain fountain;
    private Enemy[] enemies;
    private NPC[] NPCs;

    //The layer that holds the enemies/NPCs roaming areas
    private MapLayer roamZones;

    public GameScreen(RPGGame game) {
        this.game = game;
        create();
    }

    private void create() {
        spriteBatch = new SpriteBatch();

        TmxMapLoader temp = new TmxMapLoader();

        map = temp.load("Town.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        MapProperties properties = map.getProperties();
        //Width and height return number of tiles so we multiply by 16
        mapWidth = properties.get("width", Integer.class) * 16;
        mapHeight = properties.get("height", Integer.class) * 16;
        roamZones = map.getLayers().get("Roaming");

        //create can used to follow Character through the game world
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, w, h);

        gameCam.position.x = Gdx.graphics.getWidth()/2;
        gameCam.position.y = Gdx.graphics.getHeight()/2;
        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(RPGGame.WIDTH,
                RPGGame.HEIGHT, gameCam);

        player = new Player();
        fountain = new Fountain();
        playerDelta = new Vector2();
        playerDeltaRectangle = new Rectangle(0, 0, player.getWidth(), player.getHeight());

        enemies = new Enemy[5];
        for(int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy();
        }

        spawn();

        gameCam.position.set(player.getX(), player.getY(), 0);
    }

    public void spawn() {
        MapLayer objectLayer = map.getLayers().get("Spawns");
        RectangleMapObject playerSpawn = (RectangleMapObject)objectLayer.getObjects().get("Player");
        player.setCenter(playerSpawn.getRectangle().x, playerSpawn.getRectangle().y);

        RectangleMapObject fountainSpawn = (RectangleMapObject)objectLayer.getObjects().get("Fountain");
        fountain.setCenter(fountainSpawn.getRectangle().x, fountainSpawn.getRectangle().y);

        RectangleMapObject enemySpawn;
        for(int i = 0; i < enemies.length; i++) {
            enemySpawn = (RectangleMapObject) objectLayer.getObjects().get("Enemy " + Integer.toString(i + 1));
            enemies[i].setCenter(enemySpawn.getRectangle().x, enemySpawn.getRectangle().y);
        }

    }

    @Override
    public void show() {

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
        gameCam.position.x = player.getX();
        gameCam.position.y = player.getY();

        cameraBounds();

        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            gameCam.zoom += 1;
            System.out.println(gameCam.zoom);
        }
    }

    /**
     * Adjusts the camera's location so that it doesn't go out of the tile map bounds.
     */
    private void cameraBounds() {
        //Get the edges of the camera by adding/subtracting half of the viewport width/height
        if(gameCam.position.x -  gameCam.viewportWidth/ 2 < 0) gameCam.position.x = 0 + gameCam.viewportWidth/2;
        else if (gameCam.position.x + gameCam.viewportWidth/ 2 > mapWidth) gameCam.position.x = mapWidth - gameCam.viewportWidth/2;
        if(gameCam.position.y -  gameCam.viewportHeight/ 2 < 0) gameCam.position.y = 0 + gameCam.viewportHeight/2;
        else if (gameCam.position.y + gameCam.viewportHeight/ 2 > mapHeight) gameCam.position.y = mapHeight - gameCam.viewportHeight/2;
    }

    public void update(float delta) {
        handleInput(delta);
        gameCam.update();
        player.update(delta);
        fountain.update(delta);
        moveEnemies(delta);
        checkEnemies();
    }

    public void moveEnemies(float delta) {
        //Add for loop for all enemies
        RectangleMapObject roamBox = (RectangleMapObject) roamZones.getObjects().get("Enemy 1");
        enemies[0].update(delta, roamBox.getRectangle());
    }

    public void checkEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && gameCam.frustum.pointInFrustum(enemy.getX(), enemy.getY(), 0) &&
                    enemy.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                battleScreen = new BattleScreen(this, enemy, player);
                game.setScreen(battleScreen);
                enemy.die();
            }
        }
    }

    public void drawEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && onScreen(enemy)) {
                enemy.draw(spriteBatch);
            }
        }
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
        drawEnemies();
        player.draw(spriteBatch);
        spriteBatch.setProjectionMatrix(gameCam.combined);
        spriteBatch.draw(fountain, fountain.getX(), fountain.getY());
        spriteBatch.end();
    }

    private boolean onScreen(Character character) {
        return (gameCam.frustum.pointInFrustum(character.getX(), character.getY(), 0) ||
                gameCam.frustum.pointInFrustum(character.getX() + character.getWidth(), character.getY(), 0) ||
                gameCam.frustum.pointInFrustum(character.getX(), character.getY() + character.getHeight(), 0) ||
                gameCam.frustum.pointInFrustum(character.getX() + character.getWidth(), character.getY() + character.getHeight(), 0));
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
        map.dispose();
        spriteBatch.dispose();
        player.dispose();
        for(Enemy enemy: enemies) {
            enemy.dispose();
        }
    }
}
