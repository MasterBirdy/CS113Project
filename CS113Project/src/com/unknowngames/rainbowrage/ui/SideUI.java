package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;

public class SideUI extends BaseClass
{
	Button[] buttons = new Button[6];
	
	int width = Gdx.graphics.getWidth();
	float scale = everything.getScreenScale();
	boolean showChatOptions, showChat;
	
	public SideUI()
	{
		buttons[0] = new RectangularButton(0, 290 * scale, 96 * .55f * scale,
				95 * .55f * scale,
				EverythingHolder.getObjectTexture("upgradebutton")); // Upgrades
		buttons[1] = new RectangularButton(0, 350 * scale, 96 * .55f * scale,
				95 * .55f * scale,
				EverythingHolder.getObjectTexture("storebutton")); // Item Shop
		buttons[2] = new RectangularButton(0, 230 * scale, 96 * .55f * scale,
				95 * .55f * scale,
				EverythingHolder.getObjectTexture("chatbutton")); // Chat
		buttons[3] = new RectangularButton(60 * scale, 350 * scale, 96 * .55f * scale,
				95 * .55f * scale,
				EverythingHolder.getObjectTexture("ecstaticface")); // Happy
		buttons[4] = new RectangularButton(60 * scale, 290 * scale, 96 * .55f * scale,
				95 * .55f * scale,
				EverythingHolder.getObjectTexture("rageface")); // Angry
		buttons[5] = new RectangularButton(60 * scale, 230 * scale, 96 * .55f * scale,
				95 * .55f * scale,
				EverythingHolder.getObjectTexture("happyface")); // GG
		
		buttons[1].setClickable(false);
		
		showChat = everything.getMultiplayerGame();
		
		buttons[2].setClickable(showChat);
		buttons[2].setVisible(showChat);
		
		buttons[3].setVisible(false);
		buttons[4].setVisible(false);
		buttons[5].setVisible(false);
	}
	
	public void toggleChatOptions()
	{
		showChatOptions = !showChatOptions;
		
		buttons[3].setVisible(showChatOptions);
		buttons[4].setVisible(showChatOptions);
		buttons[5].setVisible(showChatOptions);
	}
	
	public int hit(float x, float y)
	{
		for (int i = 0; i < 2; i++)
			if (buttons[i].hit(x, y))
				return i + 12;
		if (showChat)
			if (buttons[2].hit(x, y))
				return 14;
		if (showChatOptions)
		{
			for (int i = 3; i < 6; i++)
				if (buttons[i].hit(x, y))
					return i + 12;
		}
//		if (buttons[5].hit(x, y))
//			return 10;
		return -1;
	}
	
	public void render(SpriteBatch batch, float delta)
	{
		for (int i = 0; i < 6; i++)
		{
			buttons[i].draw(batch, delta);
		}
	}
}
