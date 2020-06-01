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

    private SpriteBatch spriteBatch;
    private OrthographicCamera gameCam;
    private FitViewport gamePort;
    private Rectangle tileRectangle, playerDeltaRectangle;

    private Vector2 playerDelta;

    private TiledMap townMap;
    private TiledMap currentMap;

    private int mapWidth;
    private int mapHeight;
    private TiledMapRenderer renderer;

    private Player player;
    private Fountain fountain;
    private Enemy[] enemies;
    private NPC[] NPCs;
    private NPC cutsceneNPC;

    private float cutsceneDelta;
    private boolean inCutscene;

    //The layer that holds the enemies/NPCs roaming areas
    private MapLayer roamZones;

    SpriteBatch uiBatch;
    NPC talkingNPC;

    public GameScreen(RPGGame game) {
        this.game = game;
        create();
    }

    private void create() {
        spriteBatch = new SpriteBatch();
        uiBatch = new SpriteBatch();

        talkingNPC = null;

        TmxMapLoader temp = new TmxMapLoader();

        townMap = temp.load("Town.tmx");
        // TODO: Load other maps here

        currentMap = townMap;


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
        NPCs = new NPC[5];
        for(int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy();
            NPCs[i] = new NPC();
        }

        initialiseMap("Player");

        cutsceneNPC = new NPC(new Texture("placeholder2.png"), 15, 23,
                "Man that guy really beat the snot out of you.", "You're lucky I was able to drag you out of there!",
                "Be more careful next time, OK?");
        cutsceneDelta = 0;
        inCutscene = false;


        gameCam.position.set(player.getX(), player.getY(), 0);
    }

    /**
     * Initialise the enemy, NPC and player spawns for the current map. Also sets the current map width and height and stores
     * a reference to the map's "Roaming" layer. Enemies also respawn upon reloading a map.
     * The iterative spawning for the NPCs and Enemies assumes the spawn layer will follow the naming pattern "Enemy 1", "Enemy 2" etc. with
     * the same being true for NPCs.
     *
     * @param entrance A String corresponding to where the player will spawn on the new map. Defined in the "Spawns" layer of the map.
     */
    public void initialiseMap(String entrance) {

        renderer = new OrthogonalTiledMapRenderer(currentMap);
        MapProperties properties = currentMap.getProperties();
        //Width and height return number of tiles so we multiply by 16
        mapWidth = properties.get("width", Integer.class) * 16;
        mapHeight = properties.get("height", Integer.class) * 16;

        // Assumes all maps will have a roaming layer -- roaming rectangles are the areas in which enemies and NPCs can move around in
        roamZones = currentMap.getLayers().get("Roaming");

        // Assumes all maps will have a spawn layer
        MapLayer spawnLayer = currentMap.getLayers().get("Spawns");
        RectangleMapObject playerSpawn = (RectangleMapObject)spawnLayer.getObjects().get(entrance);
        player.setCenter(playerSpawn.getRectangle().x, playerSpawn.getRectangle().y);

        // Will very likely have similar statements for other maps
        if(currentMap == townMap) {
            RectangleMapObject fountainSpawn = (RectangleMapObject) spawnLayer.getObjects().get("Fountain");
            fountain.setCenter(fountainSpawn.getRectangle().x, fountainSpawn.getRectangle().y);

            // Assumes only the town map will have NPCs, can be changed later if needed
            RectangleMapObject NPCSpawn;
            for(int i = 0; i < NPCs.length; i++) {
                NPCSpawn = (RectangleMapObject) spawnLayer.getObjects().get("NPC " + Integer.toString(i + 1));
                NPCs[i].setCenter(NPCSpawn.getRectangle().x, NPCSpawn.getRectangle().y);
            }
        }

        // May need to change this for different maps
        // Could possibly create new enemy arrays for each map?
        RectangleMapObject enemySpawn;
        for(int i = 0; i < enemies.length; i++) {
            // respawn enemies
            enemies[i] = new Enemy();
            enemySpawn = (RectangleMapObject) spawnLayer.getObjects().get("Enemy " + Integer.toString(i + 1));
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
            checkCollision();
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

    public void checkCollision() {
        tileRectangle = new Rectangle();
        MapLayer collisionLayer = townMap.getLayers().get("Collision");
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

        // Test for collision against NPCs
        for(NPC npc : NPCs) {
            playerDeltaRectangle.x = player.getX() + playerDelta.x;
            playerDeltaRectangle.y = player.getY();

            if(npc.getBoundingRectangle().overlaps(playerDeltaRectangle)) {
                playerDelta.x = 0;
            }

            playerDeltaRectangle.x = player.getX();
            playerDeltaRectangle.y = player.getY() + playerDelta.y;
            if(npc.getBoundingRectangle().overlaps(playerDeltaRectangle)) {
                playerDelta.y = 0;
            }
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
        if(!player.isAlive()) {
            player.setCenter(fountain.getX(), fountain.getY() - 30); // player died in battle will do more later

            gameCam.position.x = player.getX();
            gameCam.position.y = player.getY();
            cameraBounds();
            player.revive();
            inCutscene = true;
        } else if (inCutscene) {
            playCutscene(delta);
        }

        boolean checkTouch = Gdx.input.justTouched();

        fountain.update(delta);

        if(talkingNPC != null) {
            if(checkTouch) {
                if(talkingNPC.hasNextDialogueLine()) {
                    talkingNPC.nextDialogueLine();
                } else {
                    talkingNPC.resetTextValues();
                    talkingNPC = null;
                }
            }
        } else {
            handleInput(delta);
            gameCam.update();
            player.update(delta);
            moveActors(delta);
            checkEnemies();
            checkNPCs();
        }
    }

    /**
     * Moves all NPCs and Enemies around the map randomly as specified in their respective update methods.
     * Acts under the assumption that the "roamboxes" follow the same naming pattern as their respective spawns
     * (e.g. "Enemy 1", "Enemy 2" etc.)
     */
    public void moveActors(float delta) {
        RectangleMapObject roamBox;
        //Add for loop for all enemies
        for(int i = 0; i < enemies.length; i++) {
            roamBox = (RectangleMapObject) roamZones.getObjects().get("Enemy " + Integer.toString(i + 1));
            enemies[i].update(delta, roamBox.getRectangle());
        }

        // Add for loop for all NPCs
        for(int i = 0; i < NPCs.length; i++) {
            roamBox = (RectangleMapObject) roamZones.getObjects().get("NPC " + Integer.toString(i + 1));
            NPCs[i].update(delta, roamBox.getRectangle(), player);
        }
    }

    public void checkEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && gameCam.frustum.pointInFrustum(enemy.getX(), enemy.getY(), 0) &&
                    enemy.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                RPGGame.battleScreen.setEnemy(enemy);
                RPGGame.battleScreen.setPlayer(player);
                game.setScreen(RPGGame.battleScreen);
            }
        }
    }

    public void checkNPCs() {
        for (NPC npc : NPCs) {
            if(gameCam.frustum.pointInFrustum(npc.getX(), npc.getY(), 0) &&
            npc.closeTo(player) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                //talkingNPC = npc;
                RPGGame.shopScreen.setPlayer(player);
                game.setScreen(RPGGame.shopScreen);
            }
        }
    }

    public void drawActors() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && onScreen(enemy)) {
                enemy.draw(spriteBatch);
            }
        }

        for(NPC npc : NPCs) {
            if(onScreen(npc)) {
                npc.draw(spriteBatch);
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
        drawActors();
        if(inCutscene) cutsceneNPC.draw(spriteBatch);
        player.draw(spriteBatch);
        spriteBatch.setProjectionMatrix(gameCam.combined);
        spriteBatch.draw(fountain, fountain.getX(), fountain.getY());

        spriteBatch.end();

        uiBatch.begin();
        if(talkingNPC != null) talkingNPC.displayDialogue(uiBatch, delta);
        uiBatch.end();
    }

    public void playCutscene(float delta) {
        cutsceneDelta += delta;

        System.out.println(delta);
        System.out.println(cutsceneDelta);

        // Only set on the first frame
        if(cutsceneDelta == delta) {
            cutsceneNPC.setPosition(player.getX() + player.getWidth() + 5, player.getY());
            talkingNPC = cutsceneNPC;
        }

        if(talkingNPC == null) {
            cutsceneNPC.translateX(98 * delta);
        }

        if(!onScreen(cutsceneNPC)) {
            inCutscene = false;
            cutsceneDelta = 0;
        }

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
        townMap.dispose();
        spriteBatch.dispose();
        player.dispose();
        for(Enemy enemy: enemies) {
            enemy.dispose();
        }
    }
}
