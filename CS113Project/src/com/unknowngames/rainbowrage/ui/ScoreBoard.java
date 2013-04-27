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
	TextureRegion board, statBox, winloss;
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
			winloss = everything.getObjectTexture("endvictory");
		}
		else
		{
			board = new TextureRegion(new Texture(Gdx.files.internal("images/defeat.png")), 0, 0, 800, 480);
			winloss = everything.getObjectTexture("enddefeat");
		}
		
		statBox = everything.getObjectTexture("endstatsbox");
		
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
		batch.setColor(1, 1, 1, .85f);
		batch.draw(statBox, 370, 200);
		batch.draw(winloss, 400, 410);
		everything.getFont(0).draw(batch, (everything.team() == 1 ? "You" : "Enemy"), 410, 388);
		everything.getFont(0).draw(batch, "Hero Kills:   " + everything.Stats().heroKills[0], 410, 366);
		everything.getFont(0).draw(batch, "Hero Deaths:  " + everything.Stats().heroDeaths[0], 410, 350);
		everything.getFont(0).draw(batch, "Minions Sent: " + everything.Stats().minionSent[0], 410, 334);
		everything.getFont(0).draw(batch, "Minion Kills: " + everything.Stats().minionKills[0], 410, 318);
		everything.getFont(0).draw(batch, "Minion Deaths:" + everything.Stats().minionDeaths[0], 410, 302);
		
		everything.getFont(0).draw(batch, (everything.team() == 2 ? "You" : "Enemy"), 630, 388);
		everything.getFont(0).draw(batch, "Hero Kills:   " + everything.Stats().heroKills[1], 630, 366);
		everything.getFont(0).draw(batch, "Hero Deaths:  " + everything.Stats().heroDeaths[1], 630, 350);
		everything.getFont(0).draw(batch, "Minions Sent: " + everything.Stats().minionSent[1], 630, 334);
		everything.getFont(0).draw(batch, "Minion Kills: " + everything.Stats().minionKills[1], 630, 318);
		everything.getFont(0).draw(batch, "Minion Deaths:" + everything.Stats().minionDeaths[1], 630, 302);
//		batch.setColor(Color.WHITE);
		exit.draw(batch, 0);
	}
}
