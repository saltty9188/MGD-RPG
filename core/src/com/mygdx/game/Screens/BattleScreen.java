package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.mygdx.game.Attack;
import com.mygdx.game.Button;
import com.mygdx.game.Character.BattleCharacter;
import com.mygdx.game.Character.Enemy;
import com.mygdx.game.Character.Player;

import java.util.Random;

public class BattleScreen implements Screen {

    private enum PlayerChoice {ATTACK, ITEM, RUN, CHOOSING}

    private PlayerChoice playerChoice;

    private Sprite enemyBattleSprite;
    private Sprite playerBattleSprite;
    // The length of time a sprite will flash for
    private float flashTimer;

    private static final float PADDING = 0.5f;

    private GameScreen gameScreen;
    private Enemy enemy;
    private Player player;

    private Texture buttonUp;
    private Texture buttonDown;

    private SpriteBatch spriteBatch;

    private BitmapFont bmfont;

    private TextureRegion textWindow;
    private Texture HPFull;
    private Texture HPEmpty;

    private Button attackButton;
    private Button itemButton;
    private Button runButton;
    private Button backButton;

    // Attack buttons
    private Button playerAttackButton1;
    private Button playerAttackButton2;
    private Button playerAttackButton3;
    private Button playerAttackButton4;

    // Store the attacks for the turn
    private Attack playerAttack;
    private Attack enemyAttack;


    private boolean inAttacks;
    private boolean inItems;

    //Used to determine if the text being displayed is static or scrolling
    private boolean textAnimating;

    // Used for building the output text while scrolling
    private float textTime;
    private String textBuilder;
    private int textIndex;


    private float HPTime;
    private float HPTransition;

    // Store the damage taken from an attack factoring in the receiving character's defence
    private int enemyDamage;
    private int playerDamage;

    private boolean animatingPlayerHP;
    private boolean animatingEnemyHP;

    private boolean playerTurnComplete;
    private boolean enemyTurnComplete;

    private Random rand;
    private String fleeMessage;
    private boolean escaped;

    private boolean battleStart;
    private boolean battleFinished;
    private String generalMessage;
    private String battleMessage;

    private float pauseTime;

    public BattleScreen(GameScreen gameScreen, Enemy enemy, Player player) {

        this.gameScreen = gameScreen;
        this.enemy = enemy;
        this.player = player;

        enemyBattleSprite = new Sprite(enemy.getBattleSprite(),  150, 230);
        enemyBattleSprite.setPosition(Gdx.graphics.getWidth() * 3 / 4, Gdx.graphics.getHeight() * 5 / 8);
        playerBattleSprite = new Sprite(player.getBattleSprite(), 150, 230);
        playerBattleSprite.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 8);

        inItems = false;
        inAttacks = false;
        playerChoice = PlayerChoice.CHOOSING;
        textAnimating = true;
        animatingEnemyHP = false;
        animatingPlayerHP = false;
        playerTurnComplete = false;
        enemyTurnComplete = false;

        rand = new Random();
        fleeMessage = "";
        battleMessage = "";

        battleStart = true;
        battleFinished = false;
        generalMessage = enemy.getName() + " ambushes " + player.getName() + "!";

        textTime = 0;
        textBuilder = "";
        textIndex = 0;

        HPTime = 0;
        pauseTime = 0;

        spriteBatch = new SpriteBatch();


        bmfont = new BitmapFont(
                Gdx.files.internal("font/good_neighbors.fnt"),
                Gdx.files.internal("font/good_neighbors.png"),
                false);

        bmfont.getData().setScale(2);

        textWindow = new TextureRegion(new Texture("window.png"));

        HPFull = new Texture("HP-full.png");
        HPEmpty = new Texture("HP-empty.png");

        buttonUp = new Texture("buttonUp.png");
        buttonDown = new Texture("buttonDown.png");

        attackButton = new Button(Gdx.graphics.getWidth() / 2 + PADDING, textWindow.getRegionHeight() / 2 + PADDING, Gdx.graphics.getWidth() / 2 - 2 * PADDING,
                textWindow.getRegionHeight() / 2 - 2 * PADDING, buttonUp, buttonDown);
        itemButton = new Button(Gdx.graphics.getWidth() / 2 + PADDING, 0, Gdx.graphics.getWidth() / 4 - 2 * PADDING,
                textWindow.getRegionHeight() / 2 - PADDING, buttonUp, buttonDown);
        runButton = new Button(Gdx.graphics.getWidth() * 3 / 4 + PADDING, 0, Gdx.graphics.getWidth() / 4 - 2 * PADDING,
                textWindow.getRegionHeight() / 2 - PADDING, buttonUp, buttonDown);

        backButton = new Button(Gdx.graphics.getWidth() * 23 / 24 - PADDING, textWindow.getRegionHeight() + PADDING, Gdx.graphics.getWidth() / 24,
                Gdx.graphics.getWidth() / 24, buttonUp, buttonDown);

        playerAttackButton1 = new Button(Gdx.graphics.getWidth() / 2 + PADDING, textWindow.getRegionHeight() / 2 + PADDING, Gdx.graphics.getWidth() / 4 - 2 * PADDING,
                textWindow.getRegionHeight() / 2 - 2 * PADDING, buttonUp, buttonDown);
        playerAttackButton2 = playerAttackButton4 = new Button(Gdx.graphics.getWidth() * 3 / 4 + PADDING, textWindow.getRegionHeight() / 2, Gdx.graphics.getWidth() / 4 - 2 * PADDING,
                textWindow.getRegionHeight() / 2 - PADDING, buttonUp, buttonDown);
        playerAttackButton3 = new Button(Gdx.graphics.getWidth() / 2 + PADDING, 0, Gdx.graphics.getWidth() / 4 - 2 * PADDING,
                textWindow.getRegionHeight() / 2 - PADDING, buttonUp, buttonDown);
        playerAttackButton4 = new Button(Gdx.graphics.getWidth() * 3 / 4 + PADDING, 0, Gdx.graphics.getWidth() / 4 - 2 * PADDING,
                textWindow.getRegionHeight() / 2 - PADDING, buttonUp, buttonDown);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(pauseTime <= 0 && !battleFinished) update();
        else if(pauseTime > 0) pauseTime -= delta;

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        //draw battle sprites
        //spriteBatch.draw(player.getBattleSprite(), Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 8, 150, 230);
        if(animatingPlayerHP && flashTimer > 0) {
            flashingSprite(playerBattleSprite, delta);
        } else {
            playerBattleSprite.draw(spriteBatch);
        }

        if(animatingEnemyHP && flashTimer > 0) {
            //TODO: Do sprite flashing for both.
            flashingSprite(enemyBattleSprite, delta);
        } else {
           // spriteBatch.draw(enemy.getBattleSprite(), Gdx.graphics.getWidth() * 3 / 4, Gdx.graphics.getHeight() * 5 / 8, 150, 230);
            enemyBattleSprite.setAlpha(1);
            enemyBattleSprite.draw(spriteBatch);
        }

        if(battleFinished) {
            victoryMessage(delta);
            if(Gdx.input.isTouched()) {
                gameScreen.game.setScreen(gameScreen);
            }
        } else {
            spriteBatch.draw(textWindow, 0, 0);
            bmfont.getData().setScale(2);

            switch (playerChoice) {
                case CHOOSING:
                    drawText(spriteBatch, generalMessage, textWindow.getRegionWidth(), textWindow.getRegionHeight(), textWindow.getRegionX(),
                            textWindow.getRegionY(), delta, battleStart, 2);

                    // Only displays intro message once
                    if (!textAnimating) battleStart = false;

                    // Draw the appropriate buttons while the player selects a move
                    if (!inAttacks && !inItems) {
                        attackButton.draw(spriteBatch, "Attack");
                        itemButton.draw(spriteBatch, "Items");
                        runButton.draw(spriteBatch, "Run Away");
                    } else if (inAttacks) {
                        playerAttackButton1.draw(spriteBatch, player.getAttack(0).getName(), player.getAttack(0).getPPStatus());
                        playerAttackButton2.draw(spriteBatch, player.getAttack(1).getName(), player.getAttack(1).getPPStatus());
                        playerAttackButton3.draw(spriteBatch, player.getAttack(2).getName(), player.getAttack(2).getPPStatus());
                        playerAttackButton4.draw(spriteBatch, player.getAttack(3).getName(), player.getAttack(3).getPPStatus());
                    }

                    if (inAttacks || inItems) backButton.draw(spriteBatch);

                    drawStatBox(spriteBatch, enemy, PADDING, Gdx.graphics.getHeight() - PADDING - textWindow.getRegionHeight() * 5 / 12);
                    drawStatBox(spriteBatch, player, PADDING, textWindow.getRegionHeight() + PADDING);

                    break;

                case ATTACK:
                    // Enemy attack message
                    if (textAnimating && animatingPlayerHP) {
                        drawText(spriteBatch, battleMessage, textWindow.getRegionWidth(), textWindow.getRegionHeight(), textWindow.getRegionX(),
                                textWindow.getRegionY(), delta, false, 2);
                    }

                    // Player attack message
                    if (textAnimating && animatingEnemyHP) {
                        drawText(spriteBatch, battleMessage, textWindow.getRegionWidth(), textWindow.getRegionHeight(), textWindow.getRegionX(),
                                textWindow.getRegionY(), delta, false, 2);
                    }

                    // Animate the player's HP bar and display the enemy's battle message
                    if (!textAnimating && animatingPlayerHP) {
                        animateStatBox(spriteBatch, player, PADDING, textWindow.getRegionHeight() + PADDING, delta, playerDamage);
                        drawText(spriteBatch, battleMessage, textWindow.getRegionWidth(), textWindow.getRegionHeight(), textWindow.getRegionX(),
                                textWindow.getRegionY(), delta, false, 2);
                    } else {
                        drawStatBox(spriteBatch, player, PADDING, textWindow.getRegionHeight() + PADDING);
                    }

                    // Animate the enemy's HP bar and display the player's battle message
                    if (!textAnimating && animatingEnemyHP) {
                        animateStatBox(spriteBatch, enemy, PADDING, Gdx.graphics.getHeight() - PADDING - textWindow.getRegionHeight() * 5 / 12, delta, enemyDamage);
                        drawText(spriteBatch, battleMessage, textWindow.getRegionWidth(), textWindow.getRegionHeight(), textWindow.getRegionX(),
                                textWindow.getRegionY(), delta, false,2);
                    } else {
                        drawStatBox(spriteBatch, enemy, PADDING, Gdx.graphics.getHeight() - PADDING - textWindow.getRegionHeight() * 5 / 12);
                    }
                    break;

                case RUN:
                    drawStatBox(spriteBatch, enemy, PADDING, Gdx.graphics.getHeight() - PADDING - textWindow.getRegionHeight() * 5 / 12);

                    // Draw the static player HP if it's not being animated
                    if (!animatingPlayerHP) {
                        drawStatBox(spriteBatch, player, PADDING, textWindow.getRegionHeight() + PADDING);
                    }
                    // Display the relevant flee message if the enemy's turn is not complete (ie it hasn't gone yet or fleeing was successful)
                    if (!enemyTurnComplete) {
                        drawText(spriteBatch, fleeMessage, textWindow.getRegionWidth(), textWindow.getRegionHeight(), textWindow.getRegionX(),
                                textWindow.getRegionY(), delta, true, 2);
                    } else {
                        // Enemy attack message
                        if (textAnimating && animatingPlayerHP) {
                            drawText(spriteBatch, battleMessage, textWindow.getRegionWidth(), textWindow.getRegionHeight(), textWindow.getRegionX(),
                                    textWindow.getRegionY(), delta, false, 2);
                            drawStatBox(spriteBatch, player, PADDING, textWindow.getRegionHeight() + PADDING);
                        }
                        // Animate the player's HP bar and display the enemy's battle message
                        else if (!textAnimating && animatingPlayerHP) {
                            animateStatBox(spriteBatch, player, PADDING, textWindow.getRegionHeight() + PADDING, delta, playerDamage);
                            drawText(spriteBatch, battleMessage, textWindow.getRegionWidth(), textWindow.getRegionHeight(), textWindow.getRegionX(),
                                    textWindow.getRegionY(), delta, false, 2);
                        }
                    }
            }
        }

        spriteBatch.end();
    }

    /**
     * Draws text within a rectangle texture (e.g. text window). Wraps and centers according to the texture's width and position.
     * Animates the text character by character until the text is complete
     *
     * @param batch          The SpriteBatch used to draw the text
     * @param text           The text to be drawn
     * @param boundingWidth  The width of the bounding texture
     * @param boundingHeight The height of the bounding texture
     * @param x              The bounding texture's x position.
     * @param y              The bounding texture's y position
     * @param delta          The delta time since the last frame.
     * @param pause          If set to true the game will not update for 1 second so the text is displayed for a short while once it has been fully constructed.
     * @param scale          The scale of the text being drawn.
     */
    private void drawText(SpriteBatch batch, String text, float boundingWidth, float boundingHeight, float x, float y, float delta, boolean pause, int scale) {
        bmfont.getData().setScale(scale);
        System.out.println(text);
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bmfont, text);

        //Get the height of a single line of text
        float fontHeight = glyphLayout.height;

        glyphLayout.setText(bmfont, text, bmfont.getColor(), boundingWidth, 1, true);
        int textX = (int) ((boundingWidth / 2 - glyphLayout.width / 2) + x);
        float textY =  ((boundingHeight / 2 - fontHeight / 2) + y);

        // Raise the text proportionally to how many lines there are
        int numLines = (int) (glyphLayout.height / fontHeight);
        if(text.contains("\n")) textY += fontHeight/2; // Assumes there will only be one newline in the text
        if (numLines > 1) textY += fontHeight / 2 * numLines;

        if (textAnimating) {
            textTime += delta;
            if (textTime >= 0.01f) {
                if (textIndex < text.length()) textBuilder += text.charAt(textIndex++);
                textTime = 0;
            }

            bmfont.draw(batch, textBuilder, textX, textY, glyphLayout.width, 1, true);
            if (textBuilder.equals(text)) {
                textAnimating = false;
                textBuilder = "";
                if(pause) pauseTime = 1;
            }
        } else {
            bmfont.draw(batch, text, textX, textY, glyphLayout.width, 1, true);
        }

    }

    /**
     * Draws the given character's HP status to the screen and displays their name.
     * @param batch         The SpriteBatch used to draw the sections of the box.
     * @param character     The BattleCharacter whose information will be displayed in the box.
     * @param x             The lower left x co-ordinate for the box to be drawn from.
     * @param y             The lower left y co-ordinate for the box to be drawn from.
     */
    private void drawStatBox(SpriteBatch batch, BattleCharacter character, float x, float y) {
        batch.draw(buttonUp, x, y, Gdx.graphics.getWidth() / 4, textWindow.getRegionHeight() * 5 / 12);
        float HPWidth = (Gdx.graphics.getWidth() / 4 - 80 * PADDING);
        batch.draw(HPEmpty, x + 40 * PADDING, y + 20 * PADDING, HPWidth, 4);
        batch.draw(HPFull, x + 40 * PADDING, y + 20 * PADDING, HPWidth * character.getHPPercentage(), 4);
        bmfont.getData().setScale(1);
        bmfont.draw(batch, character.getHPStatus(), x + 40 * PADDING, y + 22 * PADDING + 8);
        bmfont.draw(batch, character.getName(), x + 40 * PADDING, y + 22 * PADDING + 24);
    }

    /**
     * Animates the given character's HP status to its new value after taking damage to the screen and displays their name.
     * @param batch         The SpriteBatch used to draw the sections of the box.
     * @param character     The BattleCharacter whose information will be displayed in the box.
     * @param x             The lower left x co-ordinate for the box to be drawn from.
     * @param y             The lower left y co-ordinate for the box to be drawn from.
     * @param delta         The delta time since the last frame.
     * @param damage        The damage to be taken the BattleCharacter.
     */
    private void animateStatBox(SpriteBatch batch, BattleCharacter character, float x, float y, float delta, int damage) {
        HPTime += delta;

        batch.draw(buttonUp, x, y, Gdx.graphics.getWidth() / 4, textWindow.getRegionHeight() * 5 / 12);
        float HPWidth = (Gdx.graphics.getWidth() / 4 - 80 * PADDING);
        batch.draw(HPEmpty, x + 40 * PADDING, y + 20 * PADDING, HPWidth, 4);
        if (HPTime >= 0.1) {
            HPTransition -= 1;
            HPTime = 0;
            if (HPTransition <= character.getHP() - BattleCharacter.damageTaken(character, damage) || HPTransition <= 0) {
                animatingPlayerHP = false;
                animatingEnemyHP = false;
                character.takeDamage(damage);
            }
        }
        batch.draw(HPFull, x + 40 * PADDING, y + 20 * PADDING, HPWidth * (HPTransition / (float) character.getMaxHP()), 4);
        bmfont.getData().setScale(1);
        bmfont.draw(batch, character.getHPStatus(), x + 40 * PADDING, y + 22 * PADDING + 8);
        bmfont.draw(batch, character.getName(), x + 40 * PADDING, y + 22 * PADDING + 24);
    }

    private void update() {
        // Swaps general message to the player prompt after the battle intro message has finished.
        if(!battleStart && !generalMessage.equals("How will you proceed?")) {
            generalMessage = "How will you proceed?";
            textAnimating = true;
            textIndex = 0;
        }
        switch (playerChoice) {
            case CHOOSING:
                boolean checkTouch = Gdx.input.isTouched();

                int touchX = Gdx.input.getX();
                int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

                if (!inAttacks && !inItems && !textAnimating) {
                    attackButton.update(checkTouch, touchX, touchY);
                    itemButton.update(checkTouch, touchX, touchY);
                    runButton.update(checkTouch, touchX, touchY);
                }
                // Only poll for attack buttons when selecting an attack
                else if (inAttacks) {
                    playerAttackButton1.update(checkTouch, touchX, touchY);
                    playerAttackButton2.update(checkTouch, touchX, touchY);
                    playerAttackButton3.update(checkTouch, touchX, touchY);
                    playerAttackButton4.update(checkTouch, touchX, touchY);
                }

                if (inAttacks || inItems) backButton.update(checkTouch, touchX, touchY);

                // Handle button presses
                if (attackButton.justPressed()) {
                    inAttacks = true;
                } else if (backButton.justPressed()) {
                    inAttacks = false;
                    inItems = false;
                } else if (playerAttackButton1.justPressed()) {
                    playerAttack = player.getAttack(0);
                    playerAttack.decrementPP();
                    playerChoice = PlayerChoice.ATTACK;
                } else if (playerAttackButton2.justPressed()) {
                    playerAttack = player.getAttack(1);
                    playerAttack.decrementPP();
                    playerChoice = PlayerChoice.ATTACK;
                } else if (playerAttackButton3.justPressed()) {
                    playerAttack = player.getAttack(2);
                    playerAttack.decrementPP();
                    playerChoice = PlayerChoice.ATTACK;
                } else if (playerAttackButton4.justPressed()) {
                    playerAttack = player.getAttack(3);
                    playerAttack.decrementPP();
                    playerChoice = PlayerChoice.ATTACK;
                } else if (runButton.justPressed()) {
                    playerChoice = PlayerChoice.RUN;
                }

                break;

            case ATTACK:
                // Player is faster
                if (player.getSpeed() > enemy.getSpeed()) {
                    // Player attacks first
                    if (!playerTurnComplete) {
                        // Damage before defence defence considered
                        enemyDamage = playerAttack.getDamage() + player.getStrength() / 2;
                        HPTransition = enemy.getHP();
                        playerTurnComplete = true;
                        animatingEnemyHP = true;
                        textAnimating = true;
                        flashTimer = 1;
                        textIndex = 0;
                        battleMessage = playerAttack.battleMessage(player.getName()) + ".\n It did " + BattleCharacter.damageTaken(enemy, enemyDamage) + " damage!";
                    }

                    if (!enemy.isAlive() && playerTurnComplete) {
                        //Player won
                        battleFinished = true;
                        textAnimating = true;
                        textIndex = 0;
                        System.out.println("WIN");
                    } else {
                        // Enemy attacks second
                        if (!animatingEnemyHP && !enemyTurnComplete) {
                            enemyAttack = enemy.attack();
                            // Damage before defence defence considered
                            playerDamage = enemyAttack.getDamage() + enemy.getStrength() / 2;
                            HPTransition = player.getHP();
                            animatingPlayerHP = true;
                            enemyTurnComplete = true;
                            textAnimating = true;
                            textIndex = 0;
                            flashTimer = 1;
                            battleMessage = enemyAttack.battleMessage(enemy.getName()) + ".\n It did " + BattleCharacter.damageTaken(player, playerDamage) + " damage!";
                        }
                        //Player lost
                        if (!player.isAlive() && enemyTurnComplete) {
                            gameScreen.game.setScreen(gameScreen);
                            System.out.println("LOSE");
                        }
                    }
                } else {
                    // Enemy attacks first
                    if (!enemyTurnComplete) {
                        enemyAttack = enemy.attack();
                        // Damage before defence defence considered
                        playerDamage = enemyAttack.getDamage() + enemy.getStrength() / 2;
                        HPTransition = player.getHP();
                        textAnimating = true;
                        textIndex = 0;
                        battleMessage = enemyAttack.battleMessage(enemy.getName()) + ".\n It did " + BattleCharacter.damageTaken(player, playerDamage) + " damage!";
                        animatingPlayerHP = true;
                        flashTimer = 0;
                        enemyTurnComplete = true;
                    }
                    //Player lost
                    if (!player.isAlive()) {
                        gameScreen.game.setScreen(gameScreen);
                        System.out.println("LOSE");
                    } else {
                        //Player attacks second
                        if (!animatingPlayerHP && !playerTurnComplete) {
                            // Damage before defence defence considered
                            enemyDamage = playerAttack.getDamage() + player.getStrength() / 2;
                            HPTransition = enemy.getHP();
                            playerTurnComplete = true;
                            animatingEnemyHP = true;
                            textAnimating = true;
                            flashTimer = 1;
                            textIndex = 0;
                            battleMessage = playerAttack.battleMessage(player.getName()) + ".\n It did " + BattleCharacter.damageTaken(enemy, enemyDamage) + " damage!";
                        }

                        if (!enemy.isAlive()) {
                            //Player won
                            battleFinished = true;
                            textAnimating = true;
                            textIndex = 0;
                            System.out.println("WIN");
                        }
                    }
                }
                break;
            case ITEM:
                //TODO: Item stuff later
                break;

            case RUN:
                // Display the initial escape message and end the player's turn
                if (!playerTurnComplete) {
                    fleeMessage = player.getName() + " is attempting to escape!";
                    textAnimating = true;
                    textIndex = 0;
                    escaped = escaped();
                    playerTurnComplete = true;
                } else if (escaped) {
                    // If the first message has finished animating play the success message
                    if(fleeMessage.equals(player.getName() + " is attempting to escape!") && !textAnimating) {
                        fleeMessage = "It worked!";
                        textAnimating = true;
                        textIndex = 0;
                    } else if(!textAnimating) { // Then leave the battle and remove the enemy from the field
                        enemy.die();
                        gameScreen.game.setScreen(gameScreen);
                        System.out.println("FLEE");
                    }
                } else {
                    // If the first message has finished animating play the failure message
                    if(fleeMessage.equals(player.getName() + " is attempting to escape!") && !textAnimating) {
                        fleeMessage = "It failed";
                        textAnimating = true;
                        textIndex = 0;
                    } else if (!enemyTurnComplete && !textAnimating) { // Enemy gets to attack the player
                        enemyAttack = enemy.attack();
                        // Damage before defence defence considered
                        playerDamage =  enemyAttack.getDamage() + enemy.getStrength() / 2;
                        HPTransition = player.getHP();
                        textAnimating = true;
                        textIndex = 0;
                        battleMessage = enemyAttack.battleMessage(enemy.getName()) + ".\n It did " + BattleCharacter.damageTaken(player, playerDamage) + " damage!";
                        animatingPlayerHP = true;
                        flashTimer = 1;
                        enemyTurnComplete = true;
                    }
                }
                break;
        }
        // Battle phase is over, reset all values
        if (enemyTurnComplete && playerTurnComplete && !animatingPlayerHP && !animatingEnemyHP) {
            playerChoice = PlayerChoice.CHOOSING;
            playerTurnComplete = false;
            enemyTurnComplete = false;
            animatingEnemyHP = false;
            animatingPlayerHP = false;
            inAttacks = false;
            inItems = false;
            // Reset textAnimating to true so the prompt animates again
            textAnimating = true;
            textIndex = 0;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) gameScreen.game.setScreen(gameScreen);
    }

    /**
     * Determines if the player can flee the enemy they're fighting.
     * @return True if the player escaped, false otherwise.
     */
    private boolean escaped() {
        int chance = rand.nextInt(4);

        // 25% chance of escape
        if(player.getSpeed() < enemy.getSpeed()) return (chance == 0);
        // 50% chance of escape
        else if(player.getSpeed() == enemy.getSpeed()) return (chance <= 1);
        // 75% chance of escape
        else return (chance <= 2);
    }

    /**
     * Draws a large text window with a victory message to the top of the screen.
     */
    private void victoryMessage(float delta) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight()/6;
        float x = 0;
        float y = Gdx.graphics.getHeight() * 5/6;
        spriteBatch.draw(textWindow, x, y, width, height);
        drawText(spriteBatch, "You Won!", width, height, x, y, delta, false, 3);
    }

    /**
     * Causes the given sprite to flash after being hit.
     * @param battleSprite The Sprite being made to flash.
     */
    private void flashingSprite(Sprite battleSprite, float delta) {
        flashTimer -= delta;
        System.out.println("IN");
        battleSprite.draw(spriteBatch, rand.nextFloat());
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
        bmfont.dispose();
        textWindow.getTexture().dispose();
        buttonDown.dispose();
        buttonUp.dispose();
        HPEmpty.dispose();
        HPFull.dispose();
    }
}
