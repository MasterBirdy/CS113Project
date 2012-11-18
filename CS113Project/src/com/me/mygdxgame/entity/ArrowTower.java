package com.me.mygdxgame.entity;

import java.util.Iterator;

import com.me.mygdxgame.map.Coordinate;

public class ArrowTower extends Building
{
	public ArrowTower(int x, int y, int team)
	{
		super(x, y, team);
		maxHealth = 20;
		currentHealth = maxHealth;
		damage = 10;
		attackSpeed = 25;
		attackCooldown = 0;
		attackRange = 200;
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
			targetSelector();
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
