package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;

public class GameMenu extends BaseClass
{
	Button[] buttons = new Button[2];
	int xCoord, yCoord;
	BitmapFont font;
	float scale;
	public GameMenu(int x, int y)
	{
		scale = everything.getScreenScale();
		xCoord = x;
		yCoord = y;
		
		buttons[0] = new RoundButton(xCoord + 20 * scale, yCoord + 40 * scale, 35 * scale, EverythingHolder.getObjectTexture("backbutton"));
		buttons[1] = new RoundButton(xCoord + 100 * scale, yCoord + 40 * scale, 35 * scale, EverythingHolder.getObjectTexture("quitbutton"));
		font = everything.getFont(2);
	}
	
	public GameMenu(float x, float y)
	{
		this((int)x, (int)y);
	}
	
	public void render(SpriteBatch batch)
	{
		for (int i = 0; i < 2; i++)
			buttons[i].draw(batch, 0);
		font.draw(batch, "Settings", xCoord, yCoord + 150 * scale);
	}
	
	public int hit(float x, float y)
	{
		for (int i = 0; i < buttons.length; i++)
			if (buttons[i].hit(x, y))
				return i + 20;
		return -1;
	}
}
