package com.awesomeincorporated.unknowndefense;

import com.badlogic.gdx.Game;

public class UnknownDefense extends Game {
	
	MainMenuScreen mainMenuScreen;
	GameScreen gameScreen;
	SettingsScreen settingsScreen;

	@Override
	public void create() 
	{
		//this.resize(800, 480);
		mainMenuScreen = new MainMenuScreen(this);
//		gameScreen = new GameScreen(this);
		settingsScreen = new SettingsScreen(this);
		setScreen(mainMenuScreen);
	}
	
}
