package com.awesomeincorporated.unknowndefense;

import com.awesomeincorporated.unknowndefense.ui.LoadingScreen;
import com.badlogic.gdx.Game;

public class UnknownDefense extends Game 
{
	LoadingScreen loadingScreen;
	MainMenuScreen mainMenuScreen;
	GameScreen gameScreen;
	SettingsScreen settingsScreen;

	@Override
	public void create() 
	{
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
		//this.resize(800, 480);
		mainMenuScreen = new MainMenuScreen(this);
		GameScreen.setEverything(loadingScreen.getEverything());
		
//		gameScreen = new GameScreen(this);
		settingsScreen = new SettingsScreen(this);
		setScreen(mainMenuScreen);
	}
	
}
