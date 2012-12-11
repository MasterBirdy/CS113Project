package com.me.mygdxgame.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.map.Coordinate;

public class Archer extends Minion
{
	public Archer(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, true, team, p);
		maxHealth = 35;
		currentHealth = maxHealth;
		damage = 10;
		attackSpeed = 100;
		attackCooldown = 0;
		attackRange = 100;
		speed = 1.3f;
	}

//	@Override
//	protected void attack() 
//	{
//		rangedAttack();
//	}

//	@Override
//	public void update() 
//	{
//		super.update();
//	}

	@Override
	public void draw(SpriteBatch batch)
	{
		super.draw(batch);
		for (Projectile p : projectiles)
		{
			p.draw(batch);
		}
		if (this.isAlive() == false)
		{
			projectiles.removeAll(projectiles);
		}
	}

	@Override
	public void destroy() {
		projectiles.removeAll(projectiles);
	}

}

