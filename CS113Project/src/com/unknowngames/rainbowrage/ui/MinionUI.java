package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;

public class MinionUI extends BaseClass
{
	TextureRegion buttonFrame;
	Button[] buttons = new Button[7];
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	float scale = everything.getScreenScale();
	int buttonRadius = (int) (39 * scale);
	int stackTopX = (int)(width - 120 * scale);
	int stackTopY = (int)(360 * scale);
	int spaceX = (int)(75 * scale);
	int spaceY = (int)(54 * scale);

	int xCoord = 800, yCoord = 0;


	public MinionUI()
	{
		buttonFrame = EverythingHolder.getObjectTexture("gamebuttonframe");
		buttons[0] = new RoundButton(stackTopX, stackTopY, buttonRadius,
				EverythingHolder.getObjectTexture("swordbutton")); // Sword
		buttons[1] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY,
				buttonRadius, EverythingHolder.getObjectTexture("archerbutton")); // Archer
		buttons[2] = new RoundButton(stackTopX, (int)(stackTopY - spaceY * 2 + 4 * scale),
				buttonRadius, EverythingHolder.getObjectTexture("ninjabutton")); // Ninja
		buttons[3] = new RoundButton(stackTopX + spaceX, (int)(stackTopY - spaceY * 3
				+ 4 * scale), buttonRadius,
				EverythingHolder.getObjectTexture("magebutton")); // Mage
		buttons[4] = new RoundButton(stackTopX, (int)(stackTopY - spaceY * 4 + 9 * scale),
				buttonRadius, EverythingHolder.getObjectTexture("monkbutton")); // Monk
		buttons[5] = new RoundButton(stackTopX + spaceX, (int)(stackTopY - spaceY * 5
				+ 9 * scale), buttonRadius,
				EverythingHolder.getObjectTexture("petbutton")); // Pet
		setup();
	}
	
	public void setup()
	{
		buttons[6] = new RectangularButton((int)(stackTopX - 80 * scale), 0,
				(int) (158 * .55f * scale), (int) (155 * .55f * scale),
				EverythingHolder.getObjectTexture(everything.getHeroName()
						+ "button"));
	}

	public int hit(float x, float y)
	{
		for (int i = 0; i < 6; i++)
			if (buttons[i].hit(x, y))
				return i;
		if (buttons[6].hit(x, y))
			return 11;
		return -1;
	}

	public void render(SpriteBatch batch, float delta)
	{
		batch.draw(buttonFrame, stackTopX - 46 * scale, stackTopY - 355 * scale,
				buttonFrame.getRegionWidth() * .95f * 80 * scale / 146,
				buttonFrame.getRegionHeight() * .95f * 80 * scale / 146);
		for (int i = 0; i < 6; i++)
		{
			buttons[i].draw(batch, delta);
			everything.getFont(3).drawMultiLine(batch,
					(everything.getSentUnit(i) > 0 ? everything.getSentUnit(i)
							+ "" : ""), buttons[i].xCoord(),
					buttons[i].yCoord() + 20, 0, HAlignment.CENTER);
		}
		buttons[6].draw(batch, delta);
	}
}
