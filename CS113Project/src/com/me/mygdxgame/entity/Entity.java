package com.me.mygdxgame.entity;

import java.util.HashMap;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity 
{
	int team;
	float xCoord;
	float yCoord;
	static Texture spriteSheet;
	static HashMap<String, Sound> sounds = new HashMap<String, Sound>(); 
	static float volume = 1;
	
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
	
	public static void setVolume(float v)
	{
		volume = v;
		if (v < 0)
			volume = 0;
		if (v > 1)
			volume = 1;
	}
	
	public static void loadStatics(Texture sheet)
	{
		spriteSheet = sheet;
		Audio tempAudio = Gdx.audio;
		sounds.put("thwp", tempAudio.newSound(Gdx.files.internal("audio/Thwp.wav")));
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
