package com.me.mygdxgame.entity;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.map.Coordinate;

public abstract class Building extends Actor
{
	Coordinate destination;
	static ArrayList<Sprite> sprites;
	Sprite currentSprite;
	int level = 1;
	
	public Building(int x, int y, int team)
	{
		super(x, y, team);
		currentSprite = sprites.get(0);
	}

	@Override
	public void draw(SpriteBatch batch)
	{
		TextureRegion current;
		int unitType;
		if (this.getClass() == ArrowTower.class)
			unitType = 0;
		else
			unitType = 1;
		batch.draw(currentSprite, xCoord, yCoord);
	}
	
	public void upgrade()
	{
		level++;
		currentSprite = sprites.get(0);
		currentSprite.setSize(150, 150);//(float)(currentSprite.getWidth() * (1 + level * .5)), (float)(currentSprite.getHeight() * (1 + level * .5)));
	}
	
	public static void loadSprites()
	{
		sprites = new ArrayList<Sprite>();
		//ArrayList<Sprite> unitAnimation = new ArrayList<Animation>();
		sprites.add(loadSprite(424, 0, 47, 96, false, false));
	}
	
	private static Sprite loadSprite(int x, int y, int w, int h, boolean flipX, boolean flipY)
	{
		TextureRegion region = new TextureRegion(spriteSheet, x, y, w, h);
		
		Sprite temp = new Sprite(region); 
		
		if (flipX || flipY)
			temp.flip(flipX, flipY);
		
		return temp;
	}
	
	protected abstract void attack();
}
