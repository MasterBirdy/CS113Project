package com.unknowngames.rainbowrage;

import com.badlogic.gdx.Game;
import com.unknowngames.rainbowrage.cutscene.HeroIntros;
import com.unknowngames.rainbowrage.screens.SettingsScreen;
import com.unknowngames.rainbowrage.ui.LoadingScreen;

public class RainbowRage extends Game 
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
