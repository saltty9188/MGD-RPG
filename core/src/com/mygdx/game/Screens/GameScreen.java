package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.mygdx.game.Button;
import com.mygdx.game.Character.Boss;
import com.mygdx.game.Character.Character;
import com.mygdx.game.Character.Enemy;
import com.mygdx.game.Character.MadBat;
import com.mygdx.game.Character.NPC;
import com.mygdx.game.Character.Player;
import com.mygdx.game.Character.RestlessLeaves;
import com.mygdx.game.WorldAnimations.Flag;
import com.mygdx.game.WorldAnimations.Fountain;
import com.mygdx.game.RPGGame;

public class GameScreen implements Screen {

    private static final float PADDING = 0.5f;

    //Game
    RPGGame game;

    private SpriteBatch spriteBatch;
    private OrthographicCamera gameCam;
    private FitViewport gamePort;
    private Rectangle tileRectangle, playerDeltaRectangle;

    private Vector2 playerDelta;

    private TiledMap townMap;
    private TiledMap currentMap;
    private TiledMap caveMap;
    private TiledMap forestMap;

    private int mapWidth;
    private int mapHeight;
    private TiledMapRenderer renderer;

    private float playerSpeed;

    private Player player;
    private Fountain fountain;
    //private Flag flag;
    private Flag[] flags;
    private NPC[] NPCs;
    private NPC gameOverCutsceneNPC;
    private NPC gameCompleteCutsceneNPC;
    private NPC shopkeeper;
    private NPC talkingNPC;
    private boolean nearNPC;

    private Boss boss;

    private Enemy[] forestEnemies;
    private Enemy[] caveEnemies;
    private Enemy[] currentEnemies;

    private float cutsceneDelta;
    private boolean inGameOverCutscene;
    private boolean inGameCompleteCutscene;
    private float fadeOutOpacity;
    private Texture fadeOut;

    // Map exits
    private RectangleMapObject townToForest;
    private RectangleMapObject forestToTown;
    private RectangleMapObject forestToCave;
    private RectangleMapObject caveToForest;
    private float movementPause;

    // UI Buttons
    private Texture buttonUp;
    private Texture buttonDown;

    private Button walkUpButton;
    private Button walkRightButton;
    private Button walkDownButton;
    private Button walkLeftButton;

    private Button sprintButton;
    private Button talkButton;

    //The layer that holds the enemies/NPCs roaming areas
    private MapLayer roamZones;

    private SpriteBatch uiBatch;

    private Music bgm;

    public GameScreen(RPGGame game) {
        this.game = game;
        create();
    }

    private void create() {

        //Background Music
        bgm = Gdx.audio.newMusic(Gdx.files.internal("Music/Blippy Trance.mp3"));
        bgm.setLooping(true);
        bgm.setVolume(1);

        spriteBatch = new SpriteBatch();
        uiBatch = new SpriteBatch();

        playerSpeed = 98;

        talkingNPC = null;
        nearNPC = false;

        TmxMapLoader temp = new TmxMapLoader();

        townMap = temp.load("Town.tmx");
        caveMap = temp.load("Cave.tmx");
        forestMap = temp.load("Forest.tmx");

        currentMap = townMap;
        //currentMap = caveMap;
        //currentMap = forestMap;

        // Store the exit rectangles for each map
        MapLayer exitLayer = townMap.getLayers().get("Exits");
        townToForest = (RectangleMapObject)exitLayer.getObjects().get("Forest");

        exitLayer = forestMap.getLayers().get("Exits");
        forestToTown = (RectangleMapObject) exitLayer.getObjects().get("Town");
        forestToCave = (RectangleMapObject) exitLayer.getObjects().get("Cave");

        exitLayer = caveMap.getLayers().get("Exits");
        caveToForest = (RectangleMapObject) exitLayer.getObjects().get("Forest");

        movementPause = 0;

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
        flags = new Flag[4];
        for(int i = 0; i < flags.length; i++) {
            flags[i] = new Flag();
        }
        playerDelta = new Vector2();

        forestEnemies = new Enemy[10];
        caveEnemies = new Enemy[15];
        currentEnemies = forestEnemies;

        NPCs = new NPC[7];
        // NPCs that move
        NPCs[0] = new NPC("Hello there traveller.", "Welcome to Erathis's Village");
        NPCs[1] = new NPC("I've always wanted to travel far to the west...", "But the stupid mayor won't let us.");
        NPCs[2] = new NPC("It's a pretty fountain.");
        NPCs[3] = new NPC("Leave me alone.");
        NPCs[4] = new NPC("Be careful going that way, that leads to the Eastern Forest", "They say some creature lives in a cave near there.",
                "I'm gonna stay right here.");
        NPCs[5] = new NPC("Hey you're a fighter right?", "You gotta help us! Some monster stole our medical supplies!", "I'm not sure where he went though...");

        //Stationary NPCs
        NPCs[6] = new NPC("I could stay here and look at this lake forever...");
        NPCs[6].setDefaultPose(0);
        shopkeeper = new NPC();
        shopkeeper.setAnimation(3);

        gameOverCutsceneNPC = new NPC("Man that guy really beat the snot out of you.", "You're lucky I was able to drag you out of there!",
                "Looks like you've regained your strength. So I'm gonna leave you to it.", "Be more careful next time, OK?");
        gameCompleteCutsceneNPC = new NPC("You found Medical Supplies!", "Looks like you saved the day...", "Let's head back to town and tell everyone the good news.");
        cutsceneDelta = 0;
        inGameOverCutscene = false;
        inGameCompleteCutscene = false;
        fadeOutOpacity = 0;
        fadeOut = new Texture("placeholder.png");

        float buttonWidth = Gdx.graphics.getHeight() * 7/48 - 2 * PADDING;
        float buttonHeight = buttonWidth;

        buttonUp = new Texture("buttonUp.png");
        buttonDown = new Texture("buttonDown.png");

        walkUpButton = new Button(2 * PADDING + buttonWidth, 2 * buttonHeight + 3 * PADDING, buttonWidth, buttonHeight, buttonUp, buttonDown);
        walkRightButton = new Button(3 * PADDING + 2 * buttonWidth, 2 * PADDING + buttonHeight, buttonWidth, buttonHeight, buttonUp, buttonDown);
        walkDownButton = new Button(2 * PADDING + buttonWidth, PADDING, buttonWidth, buttonHeight, buttonUp, buttonDown);
        walkLeftButton = new Button(PADDING, 2 * PADDING + buttonHeight, buttonWidth, buttonHeight, buttonUp, buttonDown);

        sprintButton = new Button(Gdx.graphics.getWidth() - PADDING - 2*buttonWidth, PADDING + 0.5f*buttonHeight, 1.5f*buttonWidth, 1.5f*buttonHeight, buttonUp, buttonDown);
        // Talk button will replace sprint button when near an NPC
        talkButton = new Button(Gdx.graphics.getWidth() - PADDING - 2*buttonWidth, PADDING + 0.5f*buttonHeight, 1.5f*buttonWidth, 1.5f*buttonHeight, buttonUp, buttonDown);

    }

    /**
     * Resets values for starting a new game.
     */
    public void newGame() {

        player = new Player();
        playerDeltaRectangle = new Rectangle(0, 0, player.getWidth(), player.getHeight());

        currentMap = townMap;
        initialiseMap("Start");

        cutsceneDelta = 0;
        inGameOverCutscene = false;
        inGameCompleteCutscene = false;
        fadeOutOpacity = 0;

        bgm.play();
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

        //Make it so the player can't move for a bit so that they can get their bearings
        movementPause = 0.2f;
        // Reset the player's animation
        player.setAnimation(0);

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
                NPCs[i].setPosition(NPCSpawn.getRectangle().x, NPCSpawn.getRectangle().y);
            }
            RectangleMapObject shopkeeperSpawn = (RectangleMapObject) spawnLayer.getObjects().get("Shopkeeper");
            shopkeeper.setPosition(shopkeeperSpawn.getRectangle().x, shopkeeperSpawn.getRectangle().y);

            RectangleMapObject flagSpawn;
            for(int i = 0; i < flags.length; i++) {
                flagSpawn = (RectangleMapObject) spawnLayer.getObjects().get("Flag " + Integer.toString(i + 1));
                flags[i].setPosition(flagSpawn.getRectangle().x, flagSpawn.getRectangle().y);
            }

        } else if (currentMap == caveMap) {
            currentEnemies = caveEnemies;
            // Spawn the boss
            boss = new Boss();
            RectangleMapObject bossSpawn = (RectangleMapObject) spawnLayer.getObjects().get("Boss");
            boss.setPosition(bossSpawn.getRectangle().x, bossSpawn.getRectangle().y);
        } else if(currentMap == forestMap){
            currentEnemies = forestEnemies;
        }

        // May need to change this for different maps
        if(currentMap != townMap) {
            RectangleMapObject enemySpawn;
            for (int i = 0; i < currentEnemies.length; i++) {
                // respawn enemies
                if (currentEnemies == forestEnemies) {
                    currentEnemies[i] = new RestlessLeaves();
                } else if (currentEnemies == caveEnemies) {
                    currentEnemies[i] = new MadBat();
                }
                enemySpawn = (RectangleMapObject) spawnLayer.getObjects().get("Enemy " + Integer.toString(i + 1));
                currentEnemies[i].setCenter(enemySpawn.getRectangle().x, enemySpawn.getRectangle().y);
            }
        }

    }

    @Override
    public void show() {
        newGame();
    }

    public void handleInput(float delta){
        playerDelta.x = 0;
        playerDelta.y = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || sprintButton.isDown) {
            sprintButton.isDown = true;
            playerSpeed = 1.5f * 98;
        } else {
            playerSpeed = 98;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) || walkUpButton.isDown){
            walkUpButton.isDown = true;
            player.setAnimation(1);
            playerDelta.y = playerSpeed * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || walkLeftButton.isDown){
            walkLeftButton.isDown = true;
            player.setAnimation(2);
            playerDelta.x = -1 * playerSpeed * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || walkDownButton.isDown){
            walkDownButton.isDown = true;
            player.setAnimation(3);
            playerDelta.y = -1 * playerSpeed * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || walkRightButton.isDown){
            walkRightButton.isDown = true;
            player.setAnimation(4);
            playerDelta.x = playerSpeed * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.X)){
            player.setAnimation(9);
        } else {
            player.setAnimation(0);
        }

        if(playerDelta.len2() > 0) {
            checkCollision();
        }

        player.translate(playerDelta.x, playerDelta.y);
    }

    public void checkCollision() {
        tileRectangle = new Rectangle();
        MapLayer collisionLayer = currentMap.getLayers().get("Collision");
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

        // Test for collision against NPCs in the town
        if(currentMap == townMap) {
            for (NPC npc : NPCs) {
                playerDeltaRectangle.x = player.getX() + playerDelta.x;
                playerDeltaRectangle.y = player.getY();

                if (npc.getBoundingRectangle().overlaps(playerDeltaRectangle)) {
                    playerDelta.x = 0;
                }

                playerDeltaRectangle.x = player.getX();
                playerDeltaRectangle.y = player.getY() + playerDelta.y;
                if (npc.getBoundingRectangle().overlaps(playerDeltaRectangle)) {
                    playerDelta.y = 0;
                }
            }
            // Check against the shopkeeper
            playerDeltaRectangle.x = player.getX() + playerDelta.x;
            playerDeltaRectangle.y = player.getY();

            if (shopkeeper.getBoundingRectangle().overlaps(playerDeltaRectangle)) {
                playerDelta.x = 0;
            }

            playerDeltaRectangle.x = player.getX();
            playerDeltaRectangle.y = player.getY() + playerDelta.y;
            if (shopkeeper.getBoundingRectangle().overlaps(playerDeltaRectangle)) {
                playerDelta.y = 0;
            }
        }
    }

    /**
     * Moves the camera to the player's position and adjusts the camera's location so that it doesn't go out of the tile map bounds.
     */
    private void moveCamera() {
        // Move the camera to the player
        gameCam.position.x = player.getX();
        gameCam.position.y = player.getY();

        //Get the edges of the camera by adding/subtracting half of the viewport width/height
        if(gameCam.position.x -  gameCam.viewportWidth/ 2 < 0) gameCam.position.x = 0 + gameCam.viewportWidth/2;
        else if (gameCam.position.x + gameCam.viewportWidth/ 2 > mapWidth) gameCam.position.x = mapWidth - gameCam.viewportWidth/2;
        if(gameCam.position.y -  gameCam.viewportHeight/ 2 < 0) gameCam.position.y = 0 + gameCam.viewportHeight/2;
        else if (gameCam.position.y + gameCam.viewportHeight/ 2 > mapHeight) gameCam.position.y = mapHeight - gameCam.viewportHeight/2;
    }

    public void update(float delta) {

        checkExits();

        //Player died in battle
        if(!player.isAlive()) {
            currentMap = townMap;
            initialiseMap("Start");

            player.setCenter(fountain.getX(), fountain.getY() - 30);
            // Make the player face the NPC
            player.setAnimation(4);
            player.update(delta);

            gameOverCutsceneNPC.setPosition(player.getX() + player.getWidth() + 5, player.getY());

            gameCam.position.x = player.getX();
            gameCam.position.y = player.getY();
            moveCamera();
            player.revive();
            inGameOverCutscene = true;
        } else if (inGameOverCutscene) {
            playGameOverCutscene(delta);
        }
        // Player defeated the boss
        if(currentMap == caveMap && !boss.isAlive()) {
            inGameCompleteCutscene = true;
            playGameCompleteCutscene(delta);
        }

        if(currentMap == townMap) {
            fountain.update(delta);
            for (Flag flag : flags) {
                flag.update(delta);
            }
        }

        if(talkingNPC != null) {
            if(Gdx.input.justTouched()) {
                if(talkingNPC.hasNextDialogueLine()) {
                    talkingNPC.nextDialogueLine();
                } else {
                    talkingNPC.resetTextValues();
                    talkingNPC = null;
                }
            }
        } else if(!inGameOverCutscene && !inGameCompleteCutscene) {
            boolean checkTouch;
            int touchX;
            int touchY;
            // manually reset buttons
            walkDownButton.isDown = false;
            walkRightButton.isDown = false;
            walkLeftButton.isDown = false;
            walkUpButton.isDown = false;
            talkButton.isDown = false;
            sprintButton.isDown = false;
            // update the buttons
            for(int i = 0; i < 20; i++ ) { // Check all finger IDs (max 20) in case someone wants to sprint
                checkTouch = Gdx.input.isTouched(i);
                 if(checkTouch) {
                     touchX = Gdx.input.getX(i);
                     touchY = Gdx.graphics.getHeight() - Gdx.input.getY(i);

                     // Only update a button if it hasn't already been registered as being pressed this frame
                     if(!walkUpButton.isDown) walkUpButton.update(checkTouch, touchX, touchY);
                     if(!walkRightButton.isDown) walkRightButton.update(checkTouch, touchX, touchY);
                     if(!walkDownButton.isDown) walkDownButton.update(checkTouch, touchX, touchY);
                     if(!walkLeftButton.isDown) walkLeftButton.update(checkTouch, touchX, touchY);

                     if (nearNPC && !talkButton.isDown) talkButton.update(checkTouch, touchX, touchY);
                     else if(!sprintButton.isDown) sprintButton.update(checkTouch, touchX, touchY);
                 }
            }

            if(movementPause > 0) movementPause -= delta;
            else handleInput(delta);
            moveCamera();
            player.update(delta);
            moveActors(delta);
            if(currentMap != townMap) checkEnemies();
            else checkNPCs();
        }

        gameCam.update();
    }

    /**
     * Moves all NPCs and Enemies around the map randomly as specified in their respective update methods.
     * Acts under the assumption that the "roamboxes" follow the same naming pattern as their respective spawns
     * (e.g. "Enemy 1", "Enemy 2" etc.)
     */
    public void moveActors(float delta) {
        RectangleMapObject roamBox;
        // Only update enemies if on the forest or cave map
        if(currentMap != townMap) {
            // Update position for all enemies
            for (int i = 0; i < currentEnemies.length; i++) {
                // Only update enemies that are alive
                if (currentEnemies[i].isAlive()) {
                    roamBox = (RectangleMapObject) roamZones.getObjects().get("Enemy " + Integer.toString(i + 1));
                    currentEnemies[i].update(delta, roamBox.getRectangle());
                }
            }
        } else { // Only update NPCs if on the town map
            // Add for loop for all NPCs
            for(int i = 0; i < NPCs.length; i++) {
                roamBox = (RectangleMapObject) roamZones.getObjects().get("NPC " + Integer.toString(i + 1));
                //Stationary NPCs have no roam box
                if(roamBox != null && !NPCs[i].closeTo(player)) NPCs[i].update(delta, roamBox.getRectangle(), player);
                else NPCs[i].setDefaultPose(0);
            }
        }
    }

    /**
     * Checks all the enemies of the current map for a collision with the player and transitions to the battle screen
     * if one has occurred.
     */
    public void checkEnemies() {
        for (Enemy enemy : currentEnemies) {
            if (enemy.isAlive() && gameCam.frustum.pointInFrustum(enemy.getX(), enemy.getY(), 0) &&
                    enemy.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                RPGGame.battleScreen.setEnemy(enemy);
                RPGGame.battleScreen.setPlayer(player);
                if(currentEnemies == forestEnemies) RPGGame.battleScreen.setLocation(BattleScreen.Location.FOREST);
                else if(currentEnemies == caveEnemies) RPGGame.battleScreen.setLocation(BattleScreen.Location.CAVE);

                game.setScreen(RPGGame.battleScreen);
            }
        }

        if(currentMap == caveMap && onScreen(boss) && boss.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
            RPGGame.battleScreen.setLocation(BattleScreen.Location.CAVE);
            RPGGame.battleScreen.setEnemy(boss);
            RPGGame.battleScreen.setPlayer(player);

            game.setScreen(RPGGame.battleScreen);
        }
    }

    /**
     * Checks if the player is next to an NPC and displays their dialogue if the player interacts with them.
     */
    public void checkNPCs() {
        nearNPC = false;

        for (NPC npc : NPCs) {
            // Player is near an NPC
            if(gameCam.frustum.pointInFrustum(npc.getX(), npc.getY(), 0) &&
            npc.closeTo(player)) {
                nearNPC = true;
                // Set talking NPC is the button is pressed
                if((Gdx.input.isKeyPressed(Input.Keys.SPACE) || talkButton.isDown) && player.facing(npc)) {
                    talkButton.isDown = true;
                    talkingNPC = npc;
                    npc.face(player);
                }
            }
        }

        if(gameCam.frustum.pointInFrustum(shopkeeper.getX(), shopkeeper.getY(), 0) &&
                shopkeeper.closeTo(player)) {
            nearNPC = true;
            if((Gdx.input.isKeyPressed(Input.Keys.SPACE) || talkButton.isDown) && player.facing(shopkeeper)) {
                talkButton.isDown = true;
                RPGGame.shopScreen.setPlayer(player);
                game.setScreen(RPGGame.shopScreen);
            }

        }
    }

    /**
     * Checks to see if the player is about to exit the current map and loads the next map if they are.
     */
    public void checkExits() {

        if(currentMap == townMap && player.getBoundingRectangle().overlaps(townToForest.getRectangle())) {
            currentMap = forestMap;
            initialiseMap("Town");
        } else if(currentMap == forestMap && player.getBoundingRectangle().overlaps(forestToTown.getRectangle())) {
            currentMap = townMap;
            initialiseMap("Forest");
        } else if(currentMap == forestMap && player.getBoundingRectangle().overlaps(forestToCave.getRectangle())) {
            currentMap = caveMap;
            initialiseMap("Forest");
        } else if(currentMap == caveMap && player.getBoundingRectangle().overlaps(caveToForest.getRectangle())) {
            currentMap = forestMap;
            initialiseMap("Cave");
        }


    }

    /**
     * Draws the NPCs/Enemies to the screen if they are on camera.
     */
    public void drawActors() {
        if(currentMap != townMap) {
            for (Enemy enemy : currentEnemies) {
                if (enemy.isAlive() && onScreen(enemy)) {
                    enemy.draw(spriteBatch);
                }
            }
            if(currentMap == caveMap && onScreen(boss)) boss.draw(spriteBatch);
        } else {
            for (NPC npc : NPCs) {
                if (onScreen(npc)) {
                    npc.draw(spriteBatch);
                }
            }
            if(onScreen(shopkeeper)) {
                shopkeeper.draw(spriteBatch);
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
        if(inGameOverCutscene) gameOverCutsceneNPC.draw(spriteBatch);
        player.draw(spriteBatch);
        spriteBatch.setProjectionMatrix(gameCam.combined);
        if(currentMap == townMap) {
            spriteBatch.draw(fountain, fountain.getX(), fountain.getY());
            for(int i = 0; i < flags.length; i++) {
                spriteBatch.draw(flags[i], flags[i].getX(), flags[i].getY());
            }
        }
        spriteBatch.end();

        uiBatch.begin();
        if(talkingNPC != null) talkingNPC.displayDialogue(uiBatch, delta);

        //draw buttons
        walkUpButton.draw(uiBatch, 0.8f);
        walkRightButton.draw(uiBatch, 0.8f);
        walkDownButton.draw(uiBatch, 0.8f);
        walkLeftButton.draw(uiBatch, 0.8f);

        if(nearNPC) talkButton.draw(uiBatch, 0.8f);
        else sprintButton.draw(uiBatch, 0.8f);

        // Fade to black once the text is over
        if(inGameCompleteCutscene && talkingNPC == null) {
            uiBatch.setColor(1, 1, 1, fadeOutOpacity);
            uiBatch.draw(fadeOut, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            uiBatch.setColor(1, 1, 1, 1);
        }
        uiBatch.end();
    }

    public void playGameOverCutscene(float delta) {
        cutsceneDelta += delta;

        // Only set on the first frame
        if(cutsceneDelta == delta) {
            talkingNPC = gameOverCutsceneNPC;
        }

        gameOverCutsceneNPC.updateCutscene(delta, talkingNPC != null);

        if(!onScreen(gameOverCutsceneNPC)) {
            inGameOverCutscene = false;
            cutsceneDelta = 0;
        }
    }

    private void playGameCompleteCutscene(float delta) {
        cutsceneDelta += delta;

        if(cutsceneDelta == delta) {
            talkingNPC = gameCompleteCutsceneNPC;
        }

        if(talkingNPC == null) {
            fadeOutOpacity += 0.2f * delta;
        }

        if(fadeOutOpacity >= 1) {
            //TODO: change to credit screen
            inGameCompleteCutscene = false;
            game.setScreen(RPGGame.shopScreen);
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
        caveMap.dispose();
        spriteBatch.dispose();
        player.dispose();
        for(Enemy enemy: forestEnemies) {
            enemy.dispose();
        }
        for(Enemy enemy: caveEnemies) {
            enemy.dispose();
        }
        for (NPC npc : NPCs) {
            npc.dispose();
        }
    }
}
