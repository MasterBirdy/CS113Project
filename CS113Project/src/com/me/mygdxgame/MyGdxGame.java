package com.me.mygdxgame;

import com.badlogic.gdx.Game;

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
