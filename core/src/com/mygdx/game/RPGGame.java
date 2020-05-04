package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.TitleScreen;

public class RPGGame extends Game implements ApplicationListener {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 240;
	public static final float PPM = 100;
	public static GameScreen gameScreen;
	public static TitleScreen titleScreen;

	@Override
	public void create () {
		gameScreen = new GameScreen(this);
		titleScreen = new TitleScreen(this);
		setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}
	

}
