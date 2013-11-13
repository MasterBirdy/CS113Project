package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;

public class PlayerMessage extends BaseClass 
{
	String text = "";
	TextureRegion face, back;	
	int turnsLeft;
	int xCoord, yCoord, width;
	float scale, resize = 0.5f; //0.54f;
	BitmapFont font;
	
	public PlayerMessage()
	{
		face = EverythingHolder.getObjectTexture("heroface");
		back = EverythingHolder.getObjectTexture("chatbox");
		scale = everything.getScreenScale();
		width = Gdx.graphics.getWidth();
		resize *= scale;
		//font = new BitmapFont(Gdx.files.internal("fonts/myfont.fnt"), Gdx.files.internal("fonts/myfont.png"), false);
		font = EverythingHolder.getFont(4);
//		font.setColor(.5f, 0, 0, 1);
	}
	
	public PlayerMessage(String output)
	{
		text = output;
	}
	
	public void setMessage(String output)
	{
		text = output;
		turnsLeft = output.length() * 12;
//		turnsLeft = 360;
	}
	
	public void update()
	{
		--turnsLeft;
	}
	
	public boolean isAlive()
	{
		return turnsLeft >= 0;
	}
	
	public void draw(SpriteBatch batch)
	{
		if (turnsLeft < 0)
			return;
		
		batch.draw(back, 193 * scale, 20 * scale, 800 * resize, 87 * resize * .9f);
		batch.draw(face, 173 * scale, 15 * scale, 96 * resize, 96 * resize);
		font.draw(batch, text, 231 * scale, 49 * scale);
		//everything.font[0].draw(batch, text, 231 * scale, 49 * scale);
	}
}
