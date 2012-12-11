package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.entity.*;
import com.me.mygdxgame.input.MyInputProcessor;
import com.me.mygdxgame.map.Coordinate;
import com.me.mygdxgame.map.Map;

public class MyGdxGame extends Game {
	
	MainMenuScreen mainMenuScreen;
	GameScreen gameScreen;
	SettingsScreen settingsScreen;
	CreditsScreen creditsScreen;

	@Override
	public void create() 
	{
		//this.resize(800, 480);
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		settingsScreen = new SettingsScreen(this);
		creditsScreen = new CreditsScreen(this);
		setScreen(mainMenuScreen);
	}
	
}
