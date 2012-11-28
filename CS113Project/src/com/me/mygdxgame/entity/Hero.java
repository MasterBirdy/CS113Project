package com.me.mygdxgame.entity;
import java.util.Iterator;
import com.me.mygdxgame.map.Coordinate;

public abstract class Hero extends Unit 
{
	int stance = 1;
	
	public Hero(int x, int y, int team, Iterator<Coordinate> p) 
	{
		super(x, y, team, p);
		stance = 1;
	}
	
	public void stance(int s)
	{
		stance = s;
	}

	@Override
	protected void attack() 
	{
		if (target == null || !target.isAlive())
			return;
		target.takeDamage(damage);
	}
	
	private void retreat()
	{
		
	}
	
	private void hold()
	{
		if (attacking && attackCooldown <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
		}
	}

	@Override
	public void update() 
	{
		attackCooldown--;
		
		if (stance == -1)
			retreat();
		else if (stance == 0)
			hold();
		else if (attacking && attackCooldown <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
		}
		else
		{
			advance();
		}
	}
	
	public void respawn(int x, int y, Iterator<Coordinate> p)
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
	}

}
