package com.awesomeincorporated.unknowndefense.ui;

import com.awesomeincorporated.unknowndefense.EverythingHolder;
import com.awesomeincorporated.unknowndefense.GameScreen;
import com.awesomeincorporated.unknowndefense.MainMenuScreen;
import com.awesomeincorporated.unknowndefense.SettingsScreen;
import com.awesomeincorporated.unknowndefense.UnknownDefense;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LoadingScreen implements Screen
{
	static EverythingHolder everything;
	static SpriteBatch batch;
	int i = 300;
	int gettingready = 0;
	float fade = 0;
	UnknownDefense game;
	boolean start = false, loaded = false;
	TextureRegion loading;
	GL10 gl = Gdx.graphics.getGL10();
	public LoadingScreen(UnknownDefense game)
	{
		loading = new TextureRegion(new Texture(Gdx.files.internal("images/teamlogo.png")), 0, 0, 800, 480);
//		GL10 gl = Gdx.graphics.getGL10();
//		gl.glClearColor(1, 1, 1, 1);
//		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
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
		gl.glClearColor(fade, fade, fade, 1);
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
		if (start && !loaded)
		{
			everything = new EverythingHolder();
//			game.mainMenuScreen = new MainMenuScreen(game);
//			GameScreen.setEverything(everything);
			
	//		gameScreen = new GameScreen(this);
			game.settingsScreen = new SettingsScreen(game);
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
			GameScreen.setEverything(everything);
			game.mainMenuScreen = new MainMenuScreen(game, everything);
			game.setScreen(game.mainMenuScreen);
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
