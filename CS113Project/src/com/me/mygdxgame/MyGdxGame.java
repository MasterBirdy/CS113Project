package com.me.mygdxgame;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	
	MainMenuScreen mainMenuScreen;
	GameScreen gameScreen;
	SettingsScreen settingsScreen;
	GameChoiceScreen gameChoiceScreen;

	@Override
	public void create() 
	{
		//this.resize(800, 480);
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		settingsScreen = new SettingsScreen(this);
		gameChoiceScreen = new GameChoiceScreen(this);
		//setScreen(mainMenuScreen);
		setScreen(new WinnerLoserScreen(this, true));
	}
	
}
