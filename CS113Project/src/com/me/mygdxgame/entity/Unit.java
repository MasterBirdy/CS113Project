package com.me.mygdxgame.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.map.Coordinate;

public abstract class Unit extends Actor
{
	float speed;
	float xSpeed, ySpeed;
	int randX, randY;
	Coordinate destination;
	ListIterator<Coordinate> pathIter;
	static ArrayList<ArrayList<Animation>> animations;
	TextureRegion currentFrame;
	boolean walking, changedDirection;
	float stateTime;
	int stance = 1, previousStance = 1;
	int animationDir = 0;
	
	public Unit(int x, int y, boolean ranged, int team, ListIterator<Coordinate> pathIter, int rX, int rY)
	{
		super(x + rX, y + rY, ranged, team);
		//this.speed = speed;
		this.pathIter = pathIter;
		destination = (team == 1 ? pathIter.next() : pathIter.previous());
		stateTime = 0f;
		changedDirection = true;
		randX = rX;
		randY = rY;
	}

	@Override
	public void draw(SpriteBatch batch)
	{		
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion current;
		int unitType;
		if (this instanceof Swordsman || this instanceof SwordFace)
			unitType = 0;
		else if (this instanceof Archer || this instanceof ArrowEyes)
			unitType = 1;
		else if (this instanceof Monk || this instanceof SeaMonk)
			unitType = 2;
		else
			unitType = 0;
		
		if (xSpeed > 0.6)
			animationDir = (attacking ? 6 : 2);
		else if (xSpeed < -0.6)
			animationDir = (attacking ? 5 : 1);
		else if (ySpeed > 0.6)
			animationDir = (attacking ? 7 : 3);
		else if (ySpeed < -0.6)
			animationDir = (attacking ? 4 : 0);
		else if (attacking && animationDir < 4)
			animationDir += 4;
		else if (!attacking && animationDir > 3)
			animationDir -= 4;
		current = animations.get(unitType).get(animationDir).getKeyFrame(stateTime, true);
		
		//float wi = Math.abs(current.getRegionWidth() * 1.5f);
		//float he = Math.abs(current.getRegionHeight() * 1.5f);
		
		if (this instanceof Hero)
			batch.draw(current, xCoord, yCoord, Math.abs(current.getRegionWidth() / 2), Math.abs(current.getRegionHeight() / 2f), Math.abs(current.getRegionWidth()), Math.abs(current.getRegionHeight()), 1.5f, 1.5f, 0f);
			//batch.draw(current, xCoord - 8, yCoord, wi, he);
		else
			batch.draw(current, xCoord, yCoord);
	}
	
	public void takeDamage(int damage)
	{
		super.takeDamage(damage);
		//if (Gdx.app.getType() == Application.ApplicationType.Desktop)
			effects.add(this.spark());
	}
	
	public static void loadAnimations()
	{
		animations = new ArrayList<ArrayList<Animation>>();
		ArrayList<Animation> unitAnimation = new ArrayList<Animation>();
		
		unitAnimation.add(loadAnimation(0, 0, 29, 46, 5, false, false));
		unitAnimation.add(loadAnimation(0, 47, 34, 43, 5, false, false));
		unitAnimation.add(loadAnimation(0, 47, 34, 43, 5, true, false));
		unitAnimation.add(loadAnimation(0, 90, 31, 43, 5, false, false));
		unitAnimation.add(loadAnimation(0, 134, 40, 46, 3, false, false));
		unitAnimation.add(loadAnimation(0, 180, 38, 45, 3, false, false));
		unitAnimation.add(loadAnimation(0, 180, 38, 45, 3, true, false));
		unitAnimation.add(loadAnimation(0, 225, 46, 43, 3, false, false));
		animations.add(unitAnimation);
		
		
		unitAnimation = new ArrayList<Animation>();
		
		unitAnimation.add(loadAnimation(169, 0, 35, 46, 5, false, false));
		unitAnimation.add(loadAnimation(169, 46, 35, 42, 5, false, false));
		unitAnimation.add(loadAnimation(169, 46, 35, 42, 5, true, false));
		unitAnimation.add(loadAnimation(169, 88, 41, 46, 5, false, false));
		unitAnimation.add(loadAnimation(169, 134, 33, 44, 4, false, false));
		unitAnimation.add(loadAnimation(169, 178, 39, 46, 4, false, false));
		unitAnimation.add(loadAnimation(169, 178, 39, 46, 4, true, false));
		unitAnimation.add(loadAnimation(169, 224, 40, 45, 4, false, false));
		animations.add(unitAnimation);
		
		unitAnimation = new ArrayList<Animation>();
		
		unitAnimation.add(loadAnimation(0, 267, 30, 41, 5, false, false));
		unitAnimation.add(loadAnimation(0, 308, 24, 42, 5, false, false));
		unitAnimation.add(loadAnimation(0, 308, 24, 42, 5, true, false));
		unitAnimation.add(loadAnimation(0, 350, 30, 42, 5, false, false));
		unitAnimation.add(loadAnimation(0, 391, 30, 42, 4, false, false));
		unitAnimation.add(loadAnimation(0, 433, 25, 41, 4, false, false));
		unitAnimation.add(loadAnimation(0, 433, 25, 41, 4, true, false));
		unitAnimation.add(loadAnimation(0, 474, 39, 41, 4, false, false));
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
		
		Animation tempAnimation = new Animation(.05f, frames);
		tempAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		return tempAnimation;
	}
	
	public void advance()
	{
		float distance;
		
		if (previousStance == -1)
		{
			if ((team == 1 ? pathIter.hasNext() : pathIter.hasPrevious()))
				destination = (team == 1 ? pathIter.next() : pathIter.previous());
			distance = getDistanceSquared(destination.x(), destination.y());
			//setSpeed(distance);
			xSpeed = 0;
			ySpeed = 0;
		}
		
		targetSelector();
		
		if ((target == null || !target.isAlive()) && attackCooldown <= 0)
		{
			distance = getDistanceSquared(destination.x(), destination.y());
			if (distance  < speed * speed)
			{
				this.xCoord(destination.x());
				this.yCoord(destination.y());
				if ((team == 1 ? pathIter.hasNext() : pathIter.hasPrevious()))
					destination = (team == 1 ? pathIter.next() : pathIter.previous());
				else
				{
					alive = false;
					return;
				}
				distance = getDistanceSquared(destination.x(), destination.y());
				setSpeed(distance);
			}
			else
			{
				this.xCoord(this.xCoord + xSpeed);
				this.yCoord(this.yCoord + ySpeed);
			}
			if (xSpeed + ySpeed == 0)
				setSpeed(distance);
		}
	}
	
	protected void retreat()
	{

		float distance;
		target = null;
		attacking = false;
		
		if (previousStance != -1)
		{
			if ((team == 1 ? pathIter.hasPrevious() : pathIter.hasNext()))
				destination = (team == 1 ? pathIter.previous() : pathIter.next());
			if ((team == 1 ? pathIter.hasPrevious() : pathIter.hasNext()))
				destination = (team == 1 ? pathIter.previous() : pathIter.next());
			distance = getDistanceSquared(destination.x(), destination.y());
			setSpeed(distance);
		}
		else
			distance = getDistanceSquared(destination.x(), destination.y());
		
		
		/*if (previousStance != -1)
			setSpeed(distance);*/
		if (distance  < speed * speed)
		{
			this.xCoord(destination.x());
			this.yCoord(destination.y());
			if ((team == 1 ? pathIter.hasPrevious() : pathIter.hasNext()))
				destination = (team == 1 ? pathIter.previous() : pathIter.next());
			else
			{
				return;
			}
			distance = getDistanceSquared(destination.x(), destination.y());
			setSpeed(distance);
		}
		else
		{
			this.xCoord(this.xCoord + xSpeed);
			this.yCoord(this.yCoord + ySpeed);
		}
		if (xSpeed + ySpeed == 0)
			setSpeed(distance);
		
	}
	
	protected void setSpeed(float distance)
	{
		distance = (float)Math.sqrt(distance);
		xSpeed = speed * ((destination.x() - xCoord) / distance);
		ySpeed = speed * ((destination.y() - yCoord) / distance);
	}
	
	protected void sendAura()
	{
		
	}
	
	protected void takeAura(int aura)
	{
		
	}
}
