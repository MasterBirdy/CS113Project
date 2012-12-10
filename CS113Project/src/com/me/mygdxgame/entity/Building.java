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
	static ArrayList<ArrayList<Animation>> animations;
	Sprite currentSprite;
	int level = 0;
	ArrayList<Projectile> projectiles;
	float stateTime;
	
	public Building(int x, int y, boolean ranged, int team)
	{
		super(x, y, ranged, team);
		currentSprite = sprites.get(0);
	}

	@Override
	public void draw(SpriteBatch batch)
	{
		TextureRegion current;
		stateTime += Gdx.graphics.getDeltaTime();
		int unitType;
		if (this instanceof ArrowTower)
		{
			if (level == 0)
				unitType = 0;
			else
				unitType = 1;
		}
		else if (this instanceof Stronghold)
		{
			unitType = 2;
		}
		else
		{
			unitType = 0;
		}
		current = animations.get(unitType).get(0).getKeyFrame(stateTime, true);
		batch.draw(current, xCoord + (team == 1 ? 20 : -20), yCoord - (this instanceof Stronghold ? Math.abs(current.getRegionHeight() / 3f) : 0), Math.abs(current.getRegionWidth() / 4), Math.abs(current.getRegionHeight() / 2f), Math.abs(current.getRegionWidth()), Math.abs(current.getRegionHeight()), (team == 1 ? -1f : 1f), 1f, 0f);
		//batch.draw(currentSprite, xCoord, yCoord);
	}
	
	public static void loadAnimations()
	{
		animations = new ArrayList<ArrayList<Animation>>();
		ArrayList<Animation> unitAnimation = new ArrayList<Animation>();
		
		unitAnimation.add(loadAnimation(371, 447, 47, 65, 3, false, false));
		animations.add(unitAnimation);
		
		
		unitAnimation = new ArrayList<Animation>();
		
		unitAnimation.add(loadAnimation(424, 0, 46, 96, 1, false, false));
		animations.add(unitAnimation);
		
		unitAnimation = new ArrayList<Animation>();
		
		unitAnimation.add(loadAnimation(210, 333, 161, 179, 1, false, false));
		animations.add(unitAnimation);
	}
	
	private static Animation loadAnimation(int x, int y, int w, int h, int count, boolean flipX, boolean flipY)
	{
		TextureRegion[] frames = new TextureRegion[count];
		
		TextureRegion temp = new TextureRegion(spriteSheet, x, y, w * count, h);
		TextureRegion[][] tmp = temp.split(w, h);
		
		for (int i = 0; i < count; i++)
		{
			frames[i] = tmp[0][i];
			if (flipX || flipY)
				frames[i].flip(flipX, flipY);
		}
		
		Animation tempAnimation = new Animation(.08f, frames);
		tempAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		return tempAnimation;
	}
	
	public void upgrade()
	{
		level++;
		alive = true;
		currentHealth = maxHealth;
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
