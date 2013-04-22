package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.GameScreen;
import com.unknowngames.rainbowrage.RainbowRage;
import com.unknowngames.rainbowrage.screens.MainMenuScreen;

public class LoadingScreen implements Screen
{
	static EverythingHolder everything;
	static SpriteBatch batch;
	int i = 300;
	int gettingready = 0;
	float fade = 0, fade2 = 1.1f;
	RainbowRage game;
	boolean start = false, loaded = false;
	TextureRegion loading;
	GL10 gl = Gdx.graphics.getGL10();
	
	public LoadingScreen(RainbowRage game)
	{
		loading = new TextureRegion(new Texture(Gdx.files.internal("images/teamlogo.png")), 0, 0, 800, 480);
		this.game = game;
		batch = new SpriteBatch();
	}
	
	public EverythingHolder getEverything()
	{
		return everything;
	}

	@Override
	public void render(float delta) 
	{
		if (--gettingready > 0)
			return;
		
		if (fade2 > 1)
			gl.glClearColor(fade, fade, fade, 1);
		else
			gl.glClearColor(fade2, fade2, fade2, 1);
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		fade += 0.01;
		
		batch.begin();
		batch.draw(loading, 0, 0);
		batch.end();
		
//		if (!start)
//		{
//			game.mainMenuScreen = new MainMenuScreen(game);
////			GameScreen.setEverything(everything);
//			
//	//		gameScreen = new GameScreen(this);
//			game.settingsScreen = new SettingsScreen(game);
//		}
		if (start && !loaded && i < 0)
		{
			everything = new EverythingHolder();
//			game.mainMenuScreen = new MainMenuScreen(game);
//			GameScreen.setEverything(everything);
			
	//		gameScreen = new GameScreen(this);
//			game.settingsScreen = new SettingsScreen(game);
			loaded = true;
//			game.mainMenuScreen = new MainMenuScreen(game);
//			GameScreen.setEverything(everything);
//			
////			gameScreen = new GameScreen(this);
//			game.settingsScreen = new SettingsScreen(game);
		}
		
		else
			start = true;
		
		if (--i < 0 && loaded && everything != null && everything.finished())
		{
			fade2 -= 0.05f;
			if (fade2 < -.1)
			{
				GameScreen.setEverything(everything);
				game.mainMenuScreen = new MainMenuScreen(game, everything);
				game.setScreen(game.mainMenuScreen);
			}
		}
		
	}

	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub
		start = false;
		loaded = false;
		everything = null;
		i = 200;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
