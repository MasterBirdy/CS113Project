package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.screens.GameScreen;

public class ScoreBoard extends BaseClass//implements Screen
{
	TextureRegion board, statBox, winloss;
	Vector3 touchPoint;
	int result;
	RoundButton exit;
	GameScreen screen;
	OrthographicCamera zoom1;
	float scale;
	int team1X, team2X;
	int width = Gdx.graphics.getWidth(),
		height = Gdx.graphics.getHeight();
	
	public ScoreBoard(int result, GameScreen s)
	{
		scale = everything.getScreenScale();
		touchPoint = new Vector3();
		screen = s;
		if (result == 0)
		{
			board = new TextureRegion(new Texture(Gdx.files.internal("images/victory.png")), 0, 0, 800, 480);
			winloss = EverythingHolder.getObjectTexture("endvictory");
		}
		else
		{
			board = new TextureRegion(new Texture(Gdx.files.internal("images/defeat.png")), 0, 0, 800, 480);
			winloss = EverythingHolder.getObjectTexture("enddefeat");
		}
		
		statBox = EverythingHolder.getObjectTexture("endstatsbox");
		
		exit = new RoundButton(700 * scale, 50 * scale, 40 * scale, EverythingHolder.getObjectTexture("confirmbutton"));
		this.result = result;
		zoom1 = new OrthographicCamera();
		team1X = (int) (410 * scale);
		team2X = (int) (630 * scale);
	}
	
	public void draw(SpriteBatch batch)
	{
		
		if (Gdx.input.justTouched())
		{
			System.out.println("Clicked");
			touchPoint = screen.getPointCoord(Gdx.input.getX(), Gdx.input.getY());
			if (exit.hit(touchPoint.x, touchPoint.y))
			{
				System.out.println("Hit");
				screen.endGame();
			}
		}
		
		batch.setColor(Color.WHITE);
		batch.draw(board, 0, 0, width, height);
		batch.setColor(1, 1, 1, .85f);
		batch.draw(statBox, 370 * scale, 200 * scale, statBox.getRegionWidth() * scale, statBox.getRegionHeight() * scale);
		batch.draw(winloss, 400 * scale, (result == 0 ? 410 : 422) * scale, winloss.getRegionWidth() * scale, winloss.getRegionHeight() * scale);
		EverythingHolder.getFont(0).draw(batch, (everything.team() == 0 ? "You" : "Enemy"), team1X, 388 * scale);
		EverythingHolder.getFont(0).draw(batch, "Hero Kills:   " + everything.Stats().heroKills[0], team1X, 366 * scale);
		EverythingHolder.getFont(0).draw(batch, "Hero Deaths:  " + everything.Stats().heroDeaths[0], team1X, 350 * scale);
		EverythingHolder.getFont(0).draw(batch, "Minions Sent: " + everything.Stats().minionSent[0], team1X, 334 * scale);
		EverythingHolder.getFont(0).draw(batch, "Minion Kills: " + everything.Stats().minionKills[0], team1X, 318 * scale);
		EverythingHolder.getFont(0).draw(batch, "Minion Deaths:" + everything.Stats().minionDeaths[0], team1X, 302 * scale);
		
		EverythingHolder.getFont(0).draw(batch, (everything.team() == 1 ? "You" : "Enemy"), team2X, 388 * scale);
		EverythingHolder.getFont(0).draw(batch, "Hero Kills:   " + everything.Stats().heroKills[1], team2X, 366 * scale);
		EverythingHolder.getFont(0).draw(batch, "Hero Deaths:  " + everything.Stats().heroDeaths[1], team2X, 350 * scale);
		EverythingHolder.getFont(0).draw(batch, "Minions Sent: " + everything.Stats().minionSent[1], team2X, 334 * scale);
		EverythingHolder.getFont(0).draw(batch, "Minion Kills: " + everything.Stats().minionKills[1], team2X, 318 * scale);
		EverythingHolder.getFont(0).draw(batch, "Minion Deaths:" + everything.Stats().minionDeaths[1], team2X, 302 * scale);
		exit.draw(batch, 0);
	}
}
