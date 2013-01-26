package com.me.mygdxgame.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Stronghold extends Building
{
	public Stronghold(int x, int y, int team)
	{
		super(x, y, true, team);
		maxHealth = 400;
		currentHealth = maxHealth;
		damage = 12;
		attackSpeed = 25;
		attackCooldown = 0;
		attackRange = 170;
		projectiles = new ArrayList<Projectile>();
		alive = false;
	}

//	@Override
//	protected void attack() 
//	{
//		if (target == null || !target.isAlive() || level == 0)
//			return;
//		target.takeDamage(damage);
//		projectiles.add(new CannonProjectile(this.xCoord, this.yCoord, this.team, 1, 1, target));
//	}
	
	@Override
	public void checkAlive()
	{
		if (currentHealth <= 0)
		{
			level = 0;
			alive = false;
		}
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
		ArrayList<Projectile> removeList = new ArrayList<Projectile>();
		for (Projectile p : projectiles)
		{
				//if (p.xCoord == this.target.xCoord() || )
			p.update();
			if (Math.abs(p.target.xCoord() - p.xCoord) < 2)
				removeList.add(p);
			else if (Math.abs(p.target.yCoord() - p.yCoord) < 2)
				removeList.add(p);
		}
		projectiles.removeAll(removeList);
	}

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
