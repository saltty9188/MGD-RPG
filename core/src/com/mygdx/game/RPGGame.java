package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.BattleScreen;
import com.mygdx.game.Screens.CreditsScreen;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.ShopScreen;
import com.mygdx.game.Screens.TitleScreen;

public class RPGGame extends Game implements ApplicationListener {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 240;
	public static GameScreen gameScreen;
	public static BattleScreen battleScreen;
	public static TitleScreen titleScreen;
	public static ShopScreen shopScreen;
	public static CreditsScreen creditsScreen;

	public static Music currentTrack;
	public static Music titleTheme;
	public static Music townTheme;
	public static Music shopTheme;
	public static Music forestTheme;
	public static Music battleTheme;
	public static Music victoryTheme;
	public static Music gameOverTheme;
	public static Music caveTheme;
	public static Music bossTheme;
	public static Music creditsTheme;

	@Override
	public void create () {
		titleTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/Blippy Trance.mp3"));
		titleTheme.setLooping(true);
		townTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/Town Theme.mp3"));
		townTheme.setLooping(true);
		shopTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/Buy Something!.mp3"));
		shopTheme.setLooping(true);
		forestTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/The Outer Forest.mp3"));
		forestTheme.setLooping(true);
		battleTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/Random Battle.mp3"));
		battleTheme.setLooping(true);
		battleTheme.setVolume(0.2f);
		victoryTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/Fanfare 03.mp3"));
		victoryTheme.setVolume(0.4f);
		gameOverTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/No Hope.mp3"));
		caveTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/dungeon theme.mp3"));
		caveTheme.setLooping(true);
		bossTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/Battle Theme II v1.2.mp3"));
		bossTheme.setLooping(true);
		creditsTheme = Gdx.audio.newMusic(Gdx.files.internal("Music/Farewell_mp3.mp3"));
		creditsTheme.setLooping(true);

		currentTrack = townTheme;

		gameScreen = new GameScreen(this);
		battleScreen = new BattleScreen(this);
		titleScreen = new TitleScreen(this);
		shopScreen = new ShopScreen(this);
		creditsScreen = new CreditsScreen(this);

		setScreen(titleScreen);
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
