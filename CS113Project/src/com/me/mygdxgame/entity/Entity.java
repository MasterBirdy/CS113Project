package com.me.mygdxgame.entity;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity 
{
	int team;
	float xCoord;
	float yCoord;
	static Texture spriteSheet;
	
	public Entity()
	{
		xCoord = 0;
		yCoord = 0;
	}
	
	public Entity(float x, float y, int team)
	{
		xCoord = x;
		yCoord = y;
		this.team = team;
	}
	
	public static void loadSheet(Texture sheet)
	{
		spriteSheet = sheet;
	}
	
	public abstract void draw(SpriteBatch batch);
	
	public abstract void update();
	
	public float getDistance(Entity e)
	{
		return (float)Math.sqrt(getDistanceSquared(e));
	}
	
	public float getDistance(float x, float y)
	{
		return (float)Math.sqrt(getDistanceSquared(x, y));
	}
	
	public float getDistanceSquared(Entity e)
	{
		 return getDistanceSquared(e.xCoord, e.yCoord);
	}
	
	public float getDistanceSquared(float x, float y)
	{
		return (xCoord - x) * (xCoord - x) + (yCoord - y) * (yCoord - y);
	}
	
	public float xCoord()
	{
		return xCoord;
	}
	
	public float yCoord()
	{
		return yCoord;
	}
	
	public void xCoord(float x)
	{
		xCoord = x; 
	}
	
	public void yCoord(float y)
	{
		yCoord = y; 
	}
}
