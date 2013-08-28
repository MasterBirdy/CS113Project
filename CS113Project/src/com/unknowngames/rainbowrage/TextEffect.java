package com.unknowngames.rainbowrage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.unknowngames.rainbowrage.entity.Entity;

public class TextEffect
{
	float xCoord, yCoord, xVel;
	String content;
	int type, timer;
	boolean alive;
	static BitmapFont fonts[] = new BitmapFont[4];
	
	public TextEffect(float x, float y, String text, int cause)
	{
		xCoord = x;
		yCoord = y;
		xVel = -.4f;
		content = text;
		type = cause;
		timer = 100;
		alive = true;
	}
	
	public static void loadFonts()
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Kingthings Exeter.ttf"));
		fonts[0] = generator.generateFont(18);
		fonts[1] = generator.generateFont(32);
		fonts[2] = generator.generateFont(45);
		fonts[2].setColor(1, 1, 1, 1);
		fonts[3] = generator.generateFont(45);
	}
	
	public void draw(SpriteBatch batch, float delta) 
	{
		fonts[0].draw(batch, content, xCoord, yCoord);
	}

	
	public void update() 
	{
		yCoord += 0.5;
		if (timer % 20 < 10)
			xVel -= 0.1;
		else
			xVel += 0.1;
		xCoord += xVel;
		if (--timer < 0)
			alive = false;
	}
	
	public boolean isAlive()
	{
		return alive;
	}
}
