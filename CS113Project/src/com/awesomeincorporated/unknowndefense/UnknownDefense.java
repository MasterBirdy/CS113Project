package com.awesomeincorporated.unknowndefense;

import com.awesomeincorporated.unknowndefense.cutscene.HeroIntros;
import com.awesomeincorporated.unknowndefense.ui.LoadingScreen;
import com.badlogic.gdx.Game;

public class UnknownDefense extends Game 
{
	LoadingScreen loadingScreen;
	public MainMenuScreen mainMenuScreen;
	public GameScreen gameScreen;
	public SettingsScreen settingsScreen;
	public HeroIntros heroIntros;
	
	@Override
	public void create() 
	{
//		heroIntros = new HeroIntros(this);
//		setScreen(heroIntros);
		
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
		
		
		
		//this.resize(800, 480);
//		mainMenuScreen = new MainMenuScreen(this);
//		GameScreen.setEverything(loadingScreen.getEverything());
//		
////		gameScreen = new GameScreen(this);
//		settingsScreen = new SettingsScreen(this);
//		setScreen(mainMenuScreen);
	}
	
}
