package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.GameScreen;

public class ScoreBoard //implements Screen
{
	TextureRegion board;
	RoundButton exit;
	EverythingHolder everything;
	GameScreen screen;
	
	public ScoreBoard(int result, EverythingHolder e, GameScreen s)
	{
		everything = e;
		screen = s;
		if (result == 0)
		{
			board = new TextureRegion(new Texture(Gdx.files.internal("images/victory.png")), 0, 0, 800, 480);
		}
		else
		{
			board = new TextureRegion(new Texture(Gdx.files.internal("images/defeat.png")), 0, 0, 800, 480);
		}
		
		exit = new RoundButton(700, 50, 40, everything.getObjectTexture("confirmbutton"));
	}
	
	public void draw(SpriteBatch batch)
	{
		
		if (Gdx.input.justTouched())
		{
			System.out.println("Clicked");
			if (exit.hit(Gdx.input.getX(), 480 - Gdx.input.getY()))
			{
				System.out.println("Hit");
				screen.endGame();
			}
		}
		
		batch.setColor(Color.WHITE);
		batch.draw(board, 0, 0);
		exit.draw(batch, 0);
	}
}
