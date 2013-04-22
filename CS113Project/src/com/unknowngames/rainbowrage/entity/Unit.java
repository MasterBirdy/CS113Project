package com.unknowngames.rainbowrage.entity;

import java.util.ArrayList;
import java.util.ListIterator;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unknowngames.rainbowrage.map.Coordinate;
import com.unknowngames.rainbowrage.parser.UnitAnimation;
import com.unknowngames.rainbowrage.parser.UnitStructure;

public abstract class Unit extends Actor
{
	float speed;
	float xSpeed, ySpeed;
	int randX, randY;
	Coordinate destination;
	ListIterator<Coordinate> pathIter;
	UnitAnimation unitAnimation;
	TextureRegion currentFrame;
	boolean walking, changedDirection;
	float stateTime;
	int stance = 1, previousStance = 1;
	int animationDir = 0;
	ParticleEffect spark, fire, heal;
	
	boolean standing = false;
	
	int targetX, targetY;
	
	public Unit(int x, int y, boolean ranged, int team, ListIterator<Coordinate> pathIter, UnitStructure u)//int rX, int rY)
	{
		super(x, y, ranged, team, u);
		this.pathIter = pathIter;
		destination = pathIter.next();
		speed = u.speed(level);
		stateTime = 0f;
		changedDirection = true;
		spark = everything.getEffect("spark");
		fire = everything.getEffect("fireball");
		heal = everything.getEffect("heal");
		unitAnimation = everything.getUnitAnimation(u.animation(0) + team);
		int i = 0;
//		randX = rX;
//		randY = rY;
	}
	
	// 0 = Up, 1 = Left, 2 = Right, 3 = Down
	public int getDirection()
	{
		return animationDir % 4;
	}

	@Override
	public void draw(SpriteBatch batch, float delta)
	{
//		super.draw(batch);
		stateTime += delta;//Gdx.graphics.getDeltaTime();
		TextureRegion current;
		
		standing = false;
		
		if (target != null && attacking)
		{
			targetX = (int) (target.xCoord - xCoord);
			targetY = (int) (target.yCoord - yCoord);
			
			if (targetX * targetX > targetY * targetY)
			{
				if (targetX > 0)
					animationDir = 6;
				else
					animationDir = 5;
			}
			else if (targetY > 0)
				animationDir = 7;
			else
				animationDir = 4;
		}
		else
		{
			if (xSpeed > 0.6)
				animationDir = 2;
			else if (xSpeed < -0.6)
				animationDir = 1;
			else if (ySpeed > 0.6)
				animationDir = 3;
			else if (ySpeed < -0.6)
				animationDir = 0;
			else
				stateTime = .08f;
		}
		
		current = unitAnimation.getAnimation(animationDir).getKeyFrame(stateTime / (attacking ? 2 : 1), !attacking);	
		Vector2 point = unitAnimation.getFeet(animationDir);
		
		if (this.invis > 0)
		{
			if (this.team != everything.team())
				batch.setColor(.8f, .8f, .8f, 0f);
			else
				batch.setColor(.8f, .8f, .8f, .7f);
		}
		
		
		if (this instanceof Hero)
			batch.draw(current, xCoord - point.x * 1.5f - 3, yCoord - point.y * 1.5f - 3, current.getRegionWidth() * 1.5f, current.getRegionHeight() * 1.5f);
		else
			batch.draw(current, xCoord - point.x, yCoord - point.y);
		batch.setColor(Color.WHITE);
		drawParticleEffects(batch, delta);
	}
	
	protected void attack()
	{
		super.attack();
		
		if (attacking)
			stateTime = 0;
	}
	
	@Override
	public void particleOnSelf(String s)
	{
		ParticleEffect p = everything.getEffect(s);
		p.setPosition(xCoord - unitAnimation.getFeet(animationDir).x + 10, yCoord + 20);
		p.start();
		effects.add(p);
	}
	public void particleOnSelf(ParticleEffect p)
	{
		p.setPosition(xCoord - unitAnimation.getFeet(animationDir).x, xCoord);
		p.start();
		effects.add(p);
	}
	
	public void takeDamage(int damage)
	{
		super.takeDamage(damage);
		particleOnSelf("spark");
	}
	
	public void takeDamage(int damage, int type)
	{
		super.takeDamage(damage);
		if (type == 0)
			this.particleOnSelf("fire");
//			effects.add(this.fire());
		else if (type == 1)
			this.particleOnSelf("blood");
//			effects.add(this.blood());
		else if (type == 2)
			this.particleOnSelf("blood");
//			effects.add(this.blood());
	}
	
	@Override
	public void heal(int heal)
	{
		super.heal(heal);
		
		if (Gdx.app.getType() == ApplicationType.Android)
			return;
		
		particleOnSelf("heal");
	}
	
	public void advance()
	{
		float distance;
		
		if (previousStance == -1)
		{
			if (pathIter.hasNext())
				destination = pathIter.next();
//			if ((team == 1 ? pathIter.hasNext() : pathIter.hasPrevious()))
//				destination = (team == 1 ? pathIter.next() : pathIter.previous());
			distance = getDistanceSquared(destination.x(), destination.y());
			//setSpeed(distance);
			xSpeed = 0;
			ySpeed = 0;
		}
		
		if (invis < 0)
			targetSelector();
		else
		{
			attackCooldown = 0;
			target = null;
		}
		
		if ((target == null || !target.isAlive()) && attackCooldown - attackSpeedBoost <= 0)
		{
			distance = getDistanceSquared(destination.x(), destination.y());
			if (distance  < speed * speed)
			{
				this.xCoord(destination.x());
				this.yCoord(destination.y());
				if (pathIter.hasNext())
					destination = pathIter.next();
//				if ((team == 1 ? pathIter.hasNext() : pathIter.hasPrevious()))
//					destination = (team == 1 ? pathIter.next() : pathIter.previous());
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
			if (pathIter.hasPrevious())
				destination = pathIter.previous();
			if (pathIter.hasPrevious())
				destination = pathIter.previous();
//			if ((team == 1 ? pathIter.hasPrevious() : pathIter.hasNext()))
//				destination = (team == 1 ? pathIter.previous() : pathIter.next());
//			if ((team == 1 ? pathIter.hasPrevious() : pathIter.hasNext()))
//				destination = (team == 1 ? pathIter.previous() : pathIter.next());
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
			if (pathIter.hasPrevious())
				destination = pathIter.previous();
//			if ((team == 1 ? pathIter.hasPrevious() : pathIter.hasNext()))
//				destination = (team == 1 ? pathIter.previous() : pathIter.next());
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