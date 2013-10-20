package com.unknowngames.rainbowrage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.unknowngames.rainbowrage.entity.Entity;

public class TextEffect
{
	float xCoord, yCoord, xVel, yVel, xAccel;
	String content;
	int type, timer;
	static BitmapFont fonts[] = new BitmapFont[4];
	static boolean first = true;
	int font;
	
	IMovement effect;
	
	public TextEffect(float x, float y, String text, int cause)
	{
		xCoord = x;
		yCoord = y + 20;
		first = !first;
		if (first)
		{
			xVel = -.6f;
			xAccel = 0.15f;
		}
		else
		{
			xVel = .6f;
			xAccel = -0.15f;			
		}
		yVel = 0.5f;
		content = text;
		type = cause;
		timer = 100;
		if (type == 0)
		{
			effect = arcMovement;
			font = 1;
		}
		else
		{
			effect = wavyMovement;
			font = 0;
		}
		//font = new BitmapFont(fonts[0].getData(), null, false);
	}
	
	public static void loadFonts()
	{
//		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Kingthings Exeter.ttf"));
		
//		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/1900805.ttf"));
//		fonts[0] = generator.generateFont(18);
//		fonts[0].getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		fonts[0] = EverythingHolder.getFont(5);
//		fonts[0] = new BitmapFont(Gdx.files.internal("fonts/myfont.fnt"), Gdx.files.internal("fonts/myfont.png"), false);
//		fonts[0].setScale(.7f);
//		fonts[1] = generator.generateFont(32);
//		fonts[2] = generator.generateFont(45);
//		fonts[2].setColor(1, 1, 1, 1);
//		fonts[3] = generator.generateFont(45);
//		generator.dispose();
	}
	
	public void draw(SpriteBatch batch, float delta) 
	{
		if (type == 0)
			fonts[0].setColor(1, 1 - timer / 100f, 0, timer / 100f);
		else if (type == 1)
			fonts[0].setColor(1 - timer / 100f, 1 - timer / 100f, 1, timer / 100f);
		else if (type == 2)
			fonts[0].setColor(1, 1, 1 - timer / 100f, timer / 100f);
		fonts[0].draw(batch, content, xCoord, yCoord);
	}

	
	public void update() 
	{
		effect.update();		
	}
	
	private interface IMovement
	{
		public void update();
	}
	
	private IMovement wavyMovement = new IMovement()
	{
		public void update()
		{
			if (timer % 20 < 10)
				xVel -= xAccel;
			else
				xVel += xAccel;
			xCoord += xVel;
			yVel += 0.005f;
			yCoord += yVel;
		}
		
	};
	
	private IMovement arcMovement = new IMovement()
	{
		public void update()
		{
			timer -= 1;
			xCoord += xVel;
			yVel -= 0.05f;
			yCoord += yVel;
		}
	};
	
	public boolean isAlive()
	{
		if (--timer < 0)
			return false;
		return true;
	}
}
