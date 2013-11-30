package com.unknowngames.rainbowrage.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.kryonet.Client;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.RainbowRage;

public class ProfileScreen extends BaseClass implements Screen
{
	private Stage stage;
	SpriteBatch batch;
	Image profilePic;
	RainbowRage game;
	Client client;
	Skin skin;
	
	public ProfileScreen(RainbowRage game)
	{
		this.game = game;
		stage = new Stage();
		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(stage);
		
		addWidgets();
	}
	
	private void addWidgets()
	{
		skin = everything.getSkin();
		
		final TextButton backButton = new TextButton("Back", skin);
		final TextButton profileButton = new TextButton("Profile", skin);
	}
	
	@Override
	public void render(float delta)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

}
