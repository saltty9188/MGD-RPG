package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Attack;
import com.mygdx.game.Button;
import com.mygdx.game.Character.BattleCharacter;
import com.mygdx.game.Character.Enemy;
import com.mygdx.game.Character.Player;

public class BattleScreen implements Screen {

    private static final float PADDING = 0.5f;

    private GameScreen gameScreen;
    private Enemy enemy;
    private Player player;

    private Texture buttonUp;
    private Texture buttonDown;

    SpriteBatch spriteBatch;

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

    private Attack playerAttack;

    private boolean inAttacks;
    private boolean inItems;
    private boolean choosingMove;
    private boolean textAnimating;

    private float textTime;
    private String textBuilder;
    private int textIndex;

    public BattleScreen(GameScreen gameScreen, Enemy enemy, Player player) {

        this.gameScreen = gameScreen;
        this.enemy = enemy;
        this.player = player;

        inItems = false;
        inAttacks = false;
        choosingMove = true;
        textAnimating = true;

        textTime = 0;
        textBuilder = "";
        textIndex = 0;

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

        attackButton = new Button(Gdx.graphics.getWidth()/2 + PADDING, textWindow.getRegionHeight()/2 + PADDING, Gdx.graphics.getWidth()/2 - 2 * PADDING,
                textWindow.getRegionHeight()/2 - 2 * PADDING, buttonUp, buttonDown);
        itemButton = new Button(Gdx.graphics.getWidth()/2 + PADDING, 0, Gdx.graphics.getWidth()/4 - 2 * PADDING,
                textWindow.getRegionHeight()/2 - PADDING, buttonUp, buttonDown);
        runButton = new Button(Gdx.graphics.getWidth() * 3/4 + PADDING, 0, Gdx.graphics.getWidth()/4 - 2 * PADDING,
                textWindow.getRegionHeight()/2 - PADDING, buttonUp, buttonDown);

        backButton = new Button(Gdx.graphics.getWidth() * 23/24 - PADDING, textWindow.getRegionHeight() + PADDING, Gdx.graphics.getWidth()/24,
                Gdx.graphics.getWidth()/24, buttonUp, buttonDown);

        playerAttackButton1 = new Button(Gdx.graphics.getWidth()/2 + PADDING, textWindow.getRegionHeight()/2 + PADDING, Gdx.graphics.getWidth()/4 - 2 * PADDING,
                textWindow.getRegionHeight()/2 - 2 * PADDING, buttonUp, buttonDown);
        playerAttackButton2 = playerAttackButton4 = new Button(Gdx.graphics.getWidth() * 3/4 + PADDING, textWindow.getRegionHeight()/2, Gdx.graphics.getWidth()/4 - 2 * PADDING,
                textWindow.getRegionHeight()/2 - PADDING, buttonUp, buttonDown);
        playerAttackButton3 = new Button(Gdx.graphics.getWidth()/2 + PADDING, 0, Gdx.graphics.getWidth()/4 - 2 * PADDING,
                textWindow.getRegionHeight()/2 - PADDING, buttonUp, buttonDown);
        playerAttackButton4 = new Button(Gdx.graphics.getWidth() * 3/4 + PADDING, 0, Gdx.graphics.getWidth()/4 - 2 * PADDING,
                textWindow.getRegionHeight()/2 - PADDING, buttonUp, buttonDown);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        //draw battle sprites
        spriteBatch.draw(player.getBattleSprite(), Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/8, 150, 230);
        spriteBatch.draw(enemy.getBattleSprite(), Gdx.graphics.getWidth() * 3/4, Gdx.graphics.getHeight() * 5/8, 150, 230);

        spriteBatch.draw(textWindow, 0, 0);
        bmfont.getData().setScale(2);
        if(choosingMove) {
            drawText(spriteBatch, "How will you proceed?", textWindow.getRegionWidth(), textWindow.getRegionHeight(), textWindow.getRegionX(),
                    textWindow.getRegionY(), delta);
        }

        drawStatBox(spriteBatch, player, PADDING, textWindow.getRegionHeight() + PADDING);
        drawStatBox(spriteBatch, enemy, PADDING, Gdx.graphics.getHeight() - PADDING - textWindow.getRegionHeight() * 5/12);

        if(!inAttacks && !inItems) {
            attackButton.draw(spriteBatch, "Attack");
            itemButton.draw(spriteBatch, "Items");
            runButton.draw(spriteBatch, "Run Away");
        }
        else if(inAttacks) {
            playerAttackButton1.draw(spriteBatch, player.getAttack(0).getName(), player.getAttack(0).getPPStatus());
            playerAttackButton2.draw(spriteBatch, player.getAttack(1).getName(), player.getAttack(1).getPPStatus());
            playerAttackButton3.draw(spriteBatch, player.getAttack(2).getName(), player.getAttack(2).getPPStatus());
            playerAttackButton4.draw(spriteBatch, player.getAttack(3).getName(), player.getAttack(3).getPPStatus());
        }

        if(inAttacks || inItems) backButton.draw(spriteBatch);

        spriteBatch.end();
    }

    /**
     * Draws text within a rectangle texture (e.g. text window). Wraps and centers according to the texture's width and position.
     * Animates the text character by character until the text is complete
     * @param batch The SpriteBatch used to draw the text
     * @param text The text to be drawn
     * @param boundingWidth The width of the bounding texture
     * @param boundingHeight The height of the bounding texture
     * @param x The bounding texture's x position.
     * @param y The bounding texture's y position
     */
    private void drawText(SpriteBatch batch, String text, float boundingWidth, float boundingHeight, int x, int y, float delta) {
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(bmfont, text);

        //Get the height of a single line of text
        float fontHeight = glyphLayout.height;


        glyphLayout.setText(bmfont, text, bmfont.getColor(), boundingWidth, 1, true);
        int textX = (int) ((boundingWidth/2 - glyphLayout.width/2) + x);
        int textY = (int) ((boundingHeight/2 - fontHeight/2) + y);

        // Raise the text proportionally to how many lines there are
        int numLines =  (int) (glyphLayout.height / fontHeight);
        if(numLines > 1) textY += fontHeight/2 * numLines;

        if(textAnimating) {
            textTime += delta;
            if(textTime >= 0.01f) {
                if(textIndex < text.length()) textBuilder += text.charAt(textIndex++);
                textTime = 0;
            }
            if(textBuilder.equals(text)) {
                textAnimating = false;
                textBuilder = "";
            }
            bmfont.draw(batch, textBuilder, textX, textY, glyphLayout.width, 1, true);
        }
        else {
            bmfont.draw(batch, text, textX, textY, glyphLayout.width, 1, true);
        }

    }


    public void drawStatBox(SpriteBatch batch, BattleCharacter character, float x, float y) {
        batch.draw(buttonUp, x, y, Gdx.graphics.getWidth()/4, textWindow.getRegionHeight() * 5/12);
        float HPWidth =  (Gdx.graphics.getWidth()/4 - 80 * PADDING);
        batch.draw(HPEmpty, x + 40 * PADDING, y + 20 * PADDING, HPWidth, 4);
        batch.draw(HPFull, x +  40 * PADDING, y + 20 * PADDING, HPWidth * character.getHPPercentage(), 4);
        bmfont.getData().setScale(1);
        bmfont.draw(batch, character.getHPStatus(), x + 40 * PADDING, y + 22 * PADDING + 8);
        bmfont.draw(batch, character.getName(), x + 40 * PADDING, y + 22 * PADDING + 24);
    }

    private void update() {
        if(choosingMove) {
            boolean checkTouch = Gdx.input.isTouched();

            int touchX = Gdx.input.getX();
            int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (!inAttacks && !inItems) {
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
            }
            else if (backButton.justPressed()) {
                inAttacks = false;
                inItems = false;
            }
            else if(playerAttackButton1.justPressed()){
                playerAttack = player.getAttack(0);
                choosingMove = false;
            }
            else if(playerAttackButton2.justPressed()){
                playerAttack = player.getAttack(1);
                choosingMove = false;
            }
            else if(playerAttackButton3.justPressed()){
                playerAttack = player.getAttack(2);
                choosingMove = false;
            }
            else if(playerAttackButton4.justPressed()){
                playerAttack = player.getAttack(3);
                choosingMove = false;
            }
        }
        // Attack phases started
        else {
            // Player is faster
            if(player.getSpeed() > enemy.getSpeed()) {
                enemy.takeDamage(playerAttack.getDamage() + player.getStrength()/2);
                if(!enemy.isAlive()) {
                    //Player won
                    gameScreen.game.setScreen(gameScreen);
                    System.out.println("WIN");
                }
                else {
                    player.takeDamage(enemy.attack().getDamage() + enemy.getStrength() / 2);
                    //Player lost
                    if (!player.isAlive()) {
                        gameScreen.game.setScreen(gameScreen);
                        System.out.println("LOSE");
                    }
                }
                choosingMove = true;
            }
            else {
                player.takeDamage(enemy.attack().getDamage() + enemy.getStrength()/2);
                //Player lost
                if(!player.isAlive()) {
                    gameScreen.game.setScreen(gameScreen);
                    System.out.println("LOSE");
                }
                else {
                    enemy.takeDamage(playerAttack.getDamage() + player.getStrength() / 2);
                    if (!enemy.isAlive()) {
                        //Player won
                        gameScreen.game.setScreen(gameScreen);
                        System.out.println("WIN");
                    }
                }
                choosingMove = true;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) gameScreen.game.setScreen(gameScreen);
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
