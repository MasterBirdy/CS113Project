package com.me.mygdxgame.entity;
import java.util.Iterator;
import java.util.ListIterator;

import com.me.mygdxgame.map.Coordinate;

public abstract class Hero extends Unit 
{
	//int stance = 1, previousStance = 1;
	boolean changedDirection = false;
	
	public Hero(int x, int y, int team, ListIterator<Coordinate> p) 
	{
		super(x, y, team, p, 0, 0);
		stance = 1;
	}
	
	public void stance(int s)
	{
		stance = s;
	}
	
	private void hold()
	{
		targetSelector();
		
		if (attacking && attackCooldown <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
		}
		xSpeed = 0;
		ySpeed = 0;
	}

	@Override
	public void update() 
	{
		attackCooldown--;
		
		if (stance == -1)
		{
			retreat();
			previousStance = -1;
		}
		else if (stance == 0)
			hold();
		else if (attacking && attackCooldown <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
			previousStance = 1;
		}
		else
		{
			if (previousStance == -1 && pathIter.hasNext())
			{
				destination = pathIter.next();
				xSpeed = 0;
				ySpeed = 0;
			}
			advance();
			previousStance = 1;
		}
		
		//previousStance = stance;
	}
	
	public void respawn(int x, int y, ListIterator<Coordinate> p)
	{
		xCoord(x);
		yCoord(y);
		alive = true;
		currentHealth = maxHealth;
		pathIter = p;
		destination = pathIter.next();
		target = null;
		xSpeed = 0;
		ySpeed = 0;
		stance = 1;
		previousStance = 1;
	}
	
	public int stance()
	{
		return stance;
	}
}
