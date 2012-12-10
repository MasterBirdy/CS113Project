package com.me.mygdxgame.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.Settings;
import com.me.mygdxgame.Sound;
import com.me.mygdxgame.map.Coordinate;

public class Archer extends Minion
{

	ArrayList<Projectile> projectiles;

	public Archer(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, team, p);
		maxHealth = 10;
		currentHealth = maxHealth;
		damage = 3;
		attackSpeed =70;
		attackCooldown = 0;
		attackRange = 100;
		speed = 1.5f;
		projectiles = new ArrayList<Projectile>();
	}

	@Override
	protected void attack() 
	{
		super.attack();
		if (!(target == null || !target.isAlive()))
		{
			if (this.attackCooldown <= 0)
			{
				if (Settings.getInstance().getSound() == Sound.ON)
					sounds.get("thwp").play(volume);
				if (this.xSpeed > 0)
					projectiles.add(new ArrowProjectile(this.xCoord, this.yCoord, this.team, 1, 0, target));
				else if (this.xSpeed < 0)
					projectiles.add(new ArrowProjectile(this.xCoord, this.yCoord, this.team, -1, 0, target));
				else if (this.ySpeed > 0)
					projectiles.add(new ArrowProjectile(this.xCoord, this.yCoord, this.team, 0, 1, target));
				else
					projectiles.add(new ArrowProjectile(this.xCoord, this.yCoord, this.team, 0, -1, target));
			}
		}
		else
			if (projectiles.size() != 0)
				projectiles.removeAll(projectiles);
	}

	@Override
	public void update() 
	{
		super.update();
		ArrayList<Projectile> removeList = new ArrayList<Projectile>();
			for (Projectile p : projectiles)
			{
					//if (p.xCoord == this.target.xCoord() || )
				p.update();
				if (p.getxSpeed() > 0 && p.xCoord > p.target.xCoord)
					removeList.add(p);
				else if (p.getxSpeed() < 0 && p.xCoord < p.target.xCoord)
					removeList.add(p);
				else if (p.getySpeed() > 0 && p.yCoord > p.target.yCoord)
					removeList.add(p);
				else if (p.getySpeed() < 0 && p.yCoord < p.target.yCoord)
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

