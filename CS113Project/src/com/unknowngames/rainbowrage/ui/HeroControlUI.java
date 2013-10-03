package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;

public class HeroControlUI extends BaseClass
{
	Button[] buttons = new Button[4];
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	float scale = everything.getScreenScale(); //width / 800;
	int buttonRadius = (int) (38 * scale);

	public HeroControlUI()
	{
		buttons[0] = new RoundButton((int)(35 * scale), (int)(132 * scale), (int) (buttonRadius * .9f),
				EverythingHolder.getObjectTexture("attackbutton")); // Attack
		buttons[1] = new RoundButton((int)(96 * scale), (int)(96 * scale), (int) (buttonRadius * .9f),
				EverythingHolder.getObjectTexture("defendbutton")); // Defend
		buttons[2] = new RoundButton((int)(133 * scale), (int)(35 * scale), (int) (buttonRadius * .9f),
				EverythingHolder.getObjectTexture("retreatbutton")); // Retreat
		buttons[3] = new RectangularButton(0, 0, (int) (buttonRadius * 2.75f),
				(int) (buttonRadius * 2.75f),
				EverythingHolder.getObjectTexture("skillbutton"))
		{ // Skill
			@Override
			public boolean hit(float x, float y)
			{
				if (x < xCoord() || y < yCoord()
						|| (getDistanceSquared(x, y) > width * height))
				{
					return false;
				}
				Gdx.input.vibrate(50);
				return true;
			}
		};
	}

	public int hit(float x, float y)
	{
		for (int i = 0; i < 4; i++)
			if (buttons[i].hit(x, y))
				return i + 6;
		return -1;
	}

	public void setSkillClickable(boolean c)
	{
		buttons[3].setClickable(c);
	}

	public void render(SpriteBatch batch, float delta)
	{
		for (int i = 0; i < 3; i++)
		{
			if (1 - i == everything.getHero().stance())
				batch.setColor(Color.WHITE);
			else
				batch.setColor(Color.GRAY);
			buttons[i].draw(batch, delta);
		}
		
		buttons[3].draw(batch, delta);
		everything.getFont(3).drawMultiLine(batch,
				(everything.activeCooldown() > 0 ? everything.activeCooldown() / 50 + "" : ""),
				buttons[3].xCoord() + 42 * scale, buttons[3].yCoord() + 57 * scale, 0, HAlignment.CENTER);
	}
}
