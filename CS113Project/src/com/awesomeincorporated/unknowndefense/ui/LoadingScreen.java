package com.awesomeincorporated.unknowndefense.ui;

import com.awesomeincorporated.unknowndefense.EverythingHolder;
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
	int i = 100;
	Game game;
	
	public LoadingScreen(Game game)
	{
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		this.game = game;
		batch = new SpriteBatch();
//		everything = new EverythingHolder();
	}
	
	public EverythingHolder getEverything()
	{
		return everything;
	}

	@Override
	public void render(float delta) 
	{
		batch.begin();
		batch.draw(new TextureRegion(new Texture(Gdx.files.internal("images/mainmenubackground.jpg")), 0, 0, 800, 480), 0, 0);
		batch.end();
		if (--i < 0)
		{
			if (everything == null)
				everything = new EverythingHolder();
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
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
