package com.awesomeincorporated.unknowndefense.entity;

import java.util.ArrayList;
import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.map.Coordinate;
import com.awesomeincorporated.unknowndefense.parser.UnitStructure;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Unit extends Actor
{
	float speed;
	float xSpeed, ySpeed;
	int randX, randY;
	Coordinate destination;
	ListIterator<Coordinate> pathIter;
	static ArrayList<ArrayList<Animation>> animationsR;
	static ArrayList<ArrayList<Animation>> animationsB;
	TextureRegion currentFrame;
	boolean walking, changedDirection;
	float stateTime;
	int stance = 1, previousStance = 1;
	int animationDir = 0;
	ParticleEffect spark, fire, heal;
	
	public Unit(int x, int y, boolean ranged, int team, ListIterator<Coordinate> pathIter, UnitStructure u)//int rX, int rY)
	{
		super(x, y, ranged, team, u);
		//this.speed = speed;
		this.pathIter = pathIter;
		destination = pathIter.next();
//		destination = (team == 1 ? pathIter.next() : pathIter.previous());
		stateTime = 0f;
		changedDirection = true;
		spark = this.spark();
		fire = everything.getEffect("fireball");
		heal = everything.getEffect("heal");
//		randX = rX;
//		randY = rY;
	}
	
	// 0 = Up, 1 = Left, 2 = Right, 3 = Down
	public int getDirection()
	{
		return animationDir % 4;
	}

	@Override
	public void draw(SpriteBatch batch)
	{
//		super.draw(batch);
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion current;
		
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
		current = (team == 1 ? animationsR : animationsB).get(animation).get(animationDir).getKeyFrame(stateTime, true);
//		current = animations.get(animation).get(animationDir).getKeyFrame(stateTime, true);
//		current = animations.get(unitType).get(animationDir).getKeyFrame(stateTime, true);
		
		//float wi = Math.abs(current.getRegionWidth() * 1.5f);
		//float he = Math.abs(current.getRegionHeight() * 1.5f);
		if (this.invis > 0)
			batch.setColor(Color.GRAY);
		
		if (this instanceof Hero)
			batch.draw(current, xCoord, yCoord, Math.abs(current.getRegionWidth() / 2), Math.abs(current.getRegionHeight() / 2f), Math.abs(current.getRegionWidth()), Math.abs(current.getRegionHeight()), 1.5f, 1.5f, 0f);
			//batch.draw(current, xCoord - 8, yCoord, wi, he);
		else
			batch.draw(current, xCoord, yCoord);
		batch.setColor(Color.WHITE);
		drawParticleEffects(batch);
	}
	
	public void takeDamage(int damage)
	{
		super.takeDamage(damage);
		//if (Gdx.app.getType() == Application.ApplicationType.Desktop)
//		effects.add(everything.getEffect("spark"));//this.spark());
		particleOnSelf("spark");
	}
	
	public void takeDamage(int damage, int type)
	{
		super.takeDamage(damage);
		if (type == 0)
			effects.add(this.fire());
		else if (type == 1)
			effects.add(this.blood());
		else if (type == 2)
			effects.add(this.blood());
	}
	
	@Override
	public void heal(int heal)
	{
		super.heal(heal);
		
		if (Gdx.app.getType() == ApplicationType.Android)
			return;
		
		particleOnSelf("heal");
//		temp = new ParticleEffect(everything.getEffect("heal"));
//		temp.setPosition(xCoord, yCoord + 20);
//		temp.start();
//		effects.add(temp);
	}
	
	public static void loadAnimations()
	{
		animationsR = new ArrayList<ArrayList<Animation>>();
		animationsB = new ArrayList<ArrayList<Animation>>();
		
		// Swordsman
		ArrayList<Animation> unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(0, 0, 29, 46, 5, false, 0));	// Down walk
		unitAnimation.add(loadAnimation(0, 47, 34, 43, 5, false, 0));	// Left walk
		unitAnimation.add(loadAnimation(0, 47, 34, 43, 5, true, 0));	// Right walk
		unitAnimation.add(loadAnimation(0, 90, 31, 43, 5, false, 0));	// Up walk
		unitAnimation.add(loadAnimation(0, 134, 40, 46, 3, false, 0));	// Down attack
		unitAnimation.add(loadAnimation(0, 180, 38, 45, 3, false, 0));	// Left attack
		unitAnimation.add(loadAnimation(0, 180, 38, 45, 3, true, 0));	// Right attack
		unitAnimation.add(loadAnimation(0, 225, 46, 43, 3, false, 0));	// Up attack
		animationsR.add(unitAnimation);
		
		// Archer
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(169, 0, 35, 46, 5, false, 0));
		unitAnimation.add(loadAnimation(169, 46, 35, 42, 5, false, 0));
		unitAnimation.add(loadAnimation(169, 46, 35, 42, 5, true, 0));
		unitAnimation.add(loadAnimation(169, 88, 41, 46, 5, false, 0));
		unitAnimation.add(loadAnimation(169, 134, 33, 44, 4, false, 0));
		unitAnimation.add(loadAnimation(169, 178, 39, 46, 4, false, 0));
		unitAnimation.add(loadAnimation(169, 178, 39, 46, 4, true, 0));
		unitAnimation.add(loadAnimation(169, 224, 40, 45, 4, false, 0));
		animationsR.add(unitAnimation);
		
		// Monk
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(0, 267, 30, 41, 5, false, 0));
		unitAnimation.add(loadAnimation(0, 308, 27, 42, 5, false, 0));
		unitAnimation.add(loadAnimation(0, 308, 27, 42, 5, true, 0));
		unitAnimation.add(loadAnimation(0, 350, 30, 42, 5, false, 0));
		unitAnimation.add(loadAnimation(0, 391, 30, 42, 4, false, 0));
		unitAnimation.add(loadAnimation(0, 433, 27, 41, 4, false, 0));
		unitAnimation.add(loadAnimation(0, 433, 27, 41, 4, true, 0));
		unitAnimation.add(loadAnimation(0, 474, 39, 41, 4, false, 0));
		animationsR.add(unitAnimation);
		
		// Mage
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(169, 269, 33, 48, 5, false, 0));
		unitAnimation.add(loadAnimation(169, 317, 30, 43, 5, false, 0));
		unitAnimation.add(loadAnimation(169, 317, 30, 43, 5, true, 0));
		unitAnimation.add(loadAnimation(169, 360, 33, 45, 5, false, 0));
		unitAnimation.add(loadAnimation(169, 405, 45, 46, 4, false, 0));
		unitAnimation.add(loadAnimation(169, 451, 36, 43, 4, false, 0));
		unitAnimation.add(loadAnimation(169, 451, 36, 43, 4, true, 0));
		unitAnimation.add(loadAnimation(169, 494, 40, 45, 4, false, 0));
		animationsR.add(unitAnimation);
		
		// Ninja
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(374, 0, 31, 42, 5, false, 0));
		unitAnimation.add(loadAnimation(374, 42, 29, 41, 5, false, 0));
		unitAnimation.add(loadAnimation(374, 42, 29, 41, 5, true, 0));
		unitAnimation.add(loadAnimation(374, 83, 31, 41, 5, false, 0));
		unitAnimation.add(loadAnimation(374, 124, 45, 41, 4, false, 0));
		unitAnimation.add(loadAnimation(374, 165, 30, 41, 4, false, 0));
		unitAnimation.add(loadAnimation(374, 165, 30, 41, 4, true, 0));
		unitAnimation.add(loadAnimation(374, 206, 42, 40, 4, false, 0));
		animationsR.add(unitAnimation);
		
		// Eagle
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(374, 246, 44, 84, 4, false, 0));
		unitAnimation.add(loadAnimation(374, 330, 45, 84, 4, false, 0));
		unitAnimation.add(loadAnimation(374, 330, 45, 84, 4, true, 0));
		unitAnimation.add(loadAnimation(374, 414, 43, 78, 4, false, 0));
		unitAnimation.add(loadAnimation(374, 492, 44, 59, 4, false, 0));
		unitAnimation.add(loadAnimation(374, 551, 44, 59, 4, false, 0));
		unitAnimation.add(loadAnimation(374, 551, 44, 59, 4, true, 0));
		unitAnimation.add(loadAnimation(374, 610, 43, 57, 4, false, 0));
		animationsR.add(unitAnimation);
		
		// Wolf
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(554, 0, 18, 53, 4, false, 0));
		unitAnimation.add(loadAnimation(554, 53, 56, 31, 4, false, 0));
		unitAnimation.add(loadAnimation(554, 53, 56, 31, 4, true, 0));
		unitAnimation.add(loadAnimation(554, 84, 20, 50, 4, false, 0));
		unitAnimation.add(loadAnimation(554, 134, 24, 55, 4, false, 0));
		unitAnimation.add(loadAnimation(554, 189, 56, 35, 4, false, 0));
		unitAnimation.add(loadAnimation(554, 189, 56, 35, 4, true, 0));
		unitAnimation.add(loadAnimation(554, 224, 22, 48, 4, false, 0));
		animationsR.add(unitAnimation);

		// Elemental
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(554, 272, 51, 38, 5, false, 0));
		unitAnimation.add(loadAnimation(554, 310, 32, 38, 5, false, 0));
		unitAnimation.add(loadAnimation(554, 310, 32, 38, 5, true, 0));
		unitAnimation.add(loadAnimation(554, 348, 50, 39, 5, false, 0));
		unitAnimation.add(loadAnimation(554, 387, 51, 38, 4, false, 0));
		unitAnimation.add(loadAnimation(554, 425, 32, 37, 4, false, 0));
		unitAnimation.add(loadAnimation(554, 425, 32, 37, 4, true, 0));
		unitAnimation.add(loadAnimation(554, 462, 56, 39, 4, false, 0));
		animationsR.add(unitAnimation);
		
		
		
		/// BLUE TEAM
		
		// Swordsman
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(0, 0, 29, 46, 5, false, 1));	// Down walk
		unitAnimation.add(loadAnimation(0, 47, 34, 43, 5, false, 1));	// Left walk
		unitAnimation.add(loadAnimation(0, 47, 34, 43, 5, true, 1));	// Right walk
		unitAnimation.add(loadAnimation(0, 90, 31, 43, 5, false, 1));	// Up walk
		unitAnimation.add(loadAnimation(0, 134, 40, 46, 3, false, 1));	// Down attack
		unitAnimation.add(loadAnimation(0, 180, 38, 45, 3, false, 1));	// Left attack
		unitAnimation.add(loadAnimation(0, 180, 38, 45, 3, true, 1));	// Right attack
		unitAnimation.add(loadAnimation(0, 225, 46, 43, 3, false, 1));	// Up attack
		animationsB.add(unitAnimation);
		
		// Archer
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(169, 0, 35, 46, 5, false, 1));
		unitAnimation.add(loadAnimation(169, 46, 35, 42, 5, false, 1));
		unitAnimation.add(loadAnimation(169, 46, 35, 42, 5, true, 1));
		unitAnimation.add(loadAnimation(169, 88, 41, 46, 5, false, 1));
		unitAnimation.add(loadAnimation(169, 134, 33, 44, 4, false, 1));
		unitAnimation.add(loadAnimation(169, 178, 39, 46, 4, false, 1));
		unitAnimation.add(loadAnimation(169, 178, 39, 46, 4, true, 1));
		unitAnimation.add(loadAnimation(169, 224, 40, 45, 4, false, 1));
		animationsB.add(unitAnimation);
		
		// Monk
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(0, 267, 30, 41, 5, false, 1));
		unitAnimation.add(loadAnimation(0, 308, 27, 42, 5, false, 1));
		unitAnimation.add(loadAnimation(0, 308, 27, 42, 5, true, 1));
		unitAnimation.add(loadAnimation(0, 350, 30, 42, 5, false, 1));
		unitAnimation.add(loadAnimation(0, 391, 30, 42, 4, false, 1));
		unitAnimation.add(loadAnimation(0, 433, 27, 41, 4, false, 1));
		unitAnimation.add(loadAnimation(0, 433, 27, 41, 4, true, 1));
		unitAnimation.add(loadAnimation(0, 474, 39, 41, 4, false, 1));
		animationsB.add(unitAnimation);
		
		// Mage
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(169, 269, 33, 48, 5, false, 1));
		unitAnimation.add(loadAnimation(169, 317, 30, 43, 5, false, 1));
		unitAnimation.add(loadAnimation(169, 317, 30, 43, 5, true, 1));
		unitAnimation.add(loadAnimation(169, 360, 33, 45, 5, false, 1));
		unitAnimation.add(loadAnimation(169, 405, 45, 46, 4, false, 1));
		unitAnimation.add(loadAnimation(169, 451, 36, 43, 4, false, 1));
		unitAnimation.add(loadAnimation(169, 451, 36, 43, 4, true, 1));
		unitAnimation.add(loadAnimation(169, 494, 40, 45, 4, false, 1));
		animationsB.add(unitAnimation);
		
		// Ninja
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(374, 0, 31, 42, 5, false, 1));
		unitAnimation.add(loadAnimation(374, 42, 29, 41, 5, false, 1));
		unitAnimation.add(loadAnimation(374, 42, 29, 41, 5, true, 1));
		unitAnimation.add(loadAnimation(374, 83, 31, 41, 5, false, 1));
		unitAnimation.add(loadAnimation(374, 124, 45, 41, 4, false, 1));
		unitAnimation.add(loadAnimation(374, 165, 30, 41, 4, false, 1));
		unitAnimation.add(loadAnimation(374, 165, 30, 41, 4, true, 1));
		unitAnimation.add(loadAnimation(374, 206, 42, 40, 4, false, 1));
		animationsB.add(unitAnimation);
		
		// Eagle
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(374, 246, 44, 84, 4, false, 1));
		unitAnimation.add(loadAnimation(374, 330, 45, 84, 4, false, 1));
		unitAnimation.add(loadAnimation(374, 330, 45, 84, 4, true, 1));
		unitAnimation.add(loadAnimation(374, 414, 43, 78, 4, false, 1));
		unitAnimation.add(loadAnimation(374, 492, 44, 59, 4, false, 1));
		unitAnimation.add(loadAnimation(374, 551, 44, 59, 4, false, 1));
		unitAnimation.add(loadAnimation(374, 551, 44, 59, 4, true, 1));
		unitAnimation.add(loadAnimation(374, 610, 43, 57, 4, false, 1));
		animationsB.add(unitAnimation);
		
		// Wolf
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(554, 0, 18, 53, 4, false, 1));
		unitAnimation.add(loadAnimation(554, 53, 56, 31, 4, false, 1));
		unitAnimation.add(loadAnimation(554, 53, 56, 31, 4, true, 1));
		unitAnimation.add(loadAnimation(554, 84, 20, 50, 4, false, 1));
		unitAnimation.add(loadAnimation(554, 134, 24, 55, 4, false, 1));
		unitAnimation.add(loadAnimation(554, 189, 56, 35, 4, false, 1));
		unitAnimation.add(loadAnimation(554, 189, 56, 35, 4, true, 1));
		unitAnimation.add(loadAnimation(554, 224, 22, 48, 4, false, 1));
		animationsB.add(unitAnimation);

		// Elemental
		unitAnimation = new ArrayList<Animation>();
		unitAnimation.add(loadAnimation(554, 272, 51, 38, 5, false, 1));
		unitAnimation.add(loadAnimation(554, 310, 32, 38, 5, false, 1));
		unitAnimation.add(loadAnimation(554, 310, 32, 38, 5, true, 1));
		unitAnimation.add(loadAnimation(554, 348, 50, 39, 5, false, 1));
		unitAnimation.add(loadAnimation(554, 387, 51, 38, 4, false, 1));
		unitAnimation.add(loadAnimation(554, 425, 32, 37, 4, false, 1));
		unitAnimation.add(loadAnimation(554, 425, 32, 37, 4, true, 1));
		unitAnimation.add(loadAnimation(554, 462, 56, 39, 4, false, 1));
		animationsB.add(unitAnimation);
	}
	
	private static Animation loadAnimation(int x, int y, int w, int h, int count, boolean flipX, int team) // team0 = red, team1 = blue
	{
		TextureRegion[] frames = new TextureRegion[count];
		
		TextureRegion temp = new TextureRegion(spriteSheet[team], x, y, w * count, h);
		TextureRegion[][] tmp = temp.split(w, h);
		
		for (int i = 0; i < count; i++)
		{
			frames[i] = tmp[0][i];
			if (flipX)// || flipY)
				frames[i].flip(flipX, false);//flipY);
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
