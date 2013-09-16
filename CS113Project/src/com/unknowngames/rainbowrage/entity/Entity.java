package com.unknowngames.rainbowrage.entity;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;

public abstract class Entity extends BaseClass
{
	int team;
	float xCoord;
	float yCoord;
	protected boolean alive;
//	static Texture spriteSheet[] = new Texture[2];
//	static HashMap<String, Sound> sounds = new HashMap<String, Sound>(); 
	static float volume = 1;
//	protected static EverythingHolder everything;
//	static ArrayList<ParticleEffect> effects;
	
	boolean remove;
	
	public Entity()
	{
//		xCoord = 0;
//		yCoord = 0;
		alive = false;
	}
	
	public Entity(float x, float y, int team)
	{
		xCoord = x;
		yCoord = y;
		this.team = team;
	}
	
	public void setRemove()
	{
		remove = true;
	}
	
//	static public void linkHolder(EverythingHolder e)
//	{
//		everything = e;
//	}
	
	public int team()
	{
		return team;
	}
	
	public static void setVolume(float v)
	{
		volume = v;
		if (v < 0)
			volume = 0;
		if (v > 1)
			volume = 1;
	}
	
	public void addParticle(ParticleEffect pe)
	{
		everything.addEffect(pe);
//		effects.add(pe);
	}
	
//	public static void loadStatics(Texture sheetR, Texture sheetB)
//	{
//		spriteSheet[0] = sheetR;
//		spriteSheet[1] = sheetB;
////		Audio tempAudio = Gdx.audio;
////		sounds.put("thwp", tempAudio.newSound(Gdx.files.internal("audio/Thwp.wav")));
//	}
	
//	public static void loadStatics(ArrayList<ParticleEffect> e)
//	{
//		effects = e;
//	}
	
	public abstract void draw(SpriteBatch batch, float delta);
	
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
	
	public double getAngleToPoint(float x, float y)
	{
//		double x = x;
//		double y = target.yCoord();
		double toActor = Math.toDegrees(Math.atan2(x - this.xCoord(), this.yCoord() - y));
		if (toActor < 0)
			toActor += 360;
		return toActor;
	} 
	
	public double getAngleToEntity(Entity target)
	{
		if (target != null && target.isAlive())
			return getAngleToPoint(target.xCoord(), target.yCoord());
		return 0;
//		double x = target.xCoord();
//		double y = target.yCoord();
//		double toActor = Math.toDegrees(Math.atan2(x - this.xCoord(), this.yCoord() - y));
//		if (toActor < 0)
//			toActor += 360;
//		return toActor;
	} 
	
	public float xCoord()
	{
		return xCoord;
	}
	
	public float yCoord()
	{
		if (this instanceof Hero)
			return yCoord - 3;
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

	protected void rangeAttack() 
	{
		// TODO Auto-generated method stub
	}
	
	public boolean isAlive()
	{
		return alive;
	}
}
