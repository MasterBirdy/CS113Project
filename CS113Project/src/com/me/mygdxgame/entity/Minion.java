package com.me.mygdxgame.entity;
import java.util.Iterator;
import com.me.mygdxgame.map.Coordinate;

public abstract class Minion extends Unit 
{
	
	public Minion(int x, int y, int team, Iterator<Coordinate> p) 
	{
		super(x, y, team, p, (int)(Math.random() * 10 - 5), (int)(Math.random() * 5 - 2));
	}

	@Override
	protected void attack() 
	{
		if (target == null || !target.isAlive())
			return;
		target.takeDamage(damage);
	}

	@Override
	public void update() 
	{
		
		if (attacking && attackCooldown <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
		}
		else
		{
			attackCooldown--;
			advance();
		}
	}

}
