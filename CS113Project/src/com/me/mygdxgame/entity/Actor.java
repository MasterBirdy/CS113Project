package com.me.mygdxgame.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Actor extends Entity
{
	int currentHealth, maxHealth, damage;
	float attackSpeed, attackCooldown, attackRange;
	boolean alive, attacking, ranged;
	Actor target;
	static LinkedList<Actor> team1;
	static LinkedList<Actor> team2;
	static ParticleEffect fire = new ParticleEffect();
	static ArrayList<Projectile> projectiles;
	
	static TextureRegion[] rangeIndicator;

	public Actor(int x, int y, boolean ranged, int team)
	{
		super(x, y, team);
		alive = true;
		this.ranged = ranged;
	}
	
	static public void loadRange()
	{
		rangeIndicator = new TextureRegion[2];
		rangeIndicator[0] = new TextureRegion(spriteSheet, 472, 40, 40, 40);
		rangeIndicator[1] = new TextureRegion(spriteSheet, 472, 0, 40, 40);
		fire.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
		fire.setPosition(50, 50);
		fire.start();
	}
	
	static public void loadProjectiles(ArrayList<Projectile> p)
	{
		projectiles = p;
	}
	
	public ParticleEffect fire()
	{
		ParticleEffect e = new ParticleEffect();
		e.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
		e.setPosition(xCoord, yCoord);
		e.start();
		return e;
	}
	
	public ParticleEffect blood()
	{
		ParticleEffect e = new ParticleEffect();
		e.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/BloodEffectAndroid.p" : "data/BloodEffect.p")), Gdx.files.internal("images"));
		e.setPosition(xCoord + 20, yCoord + 20);
		e.start();
		return e;
	}
	
	public ParticleEffect spark()
	{
		ParticleEffect e = new ParticleEffect();
		e.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/SparkEffectAndroid.p" : "data/SparkEffect.p")), Gdx.files.internal("images"));
		e.setPosition(xCoord + 20, yCoord + 20);
		e.start();
		return e;
	}
	
	protected void attack()
	{
		if (ranged)
			rangeAttack();
		else
			meleeAttack();
	}
	
	public void rangeIndicator(SpriteBatch batch)
	{
		batch.setColor(1, 1, 1, .35f);
		batch.draw(rangeIndicator[attacking ? 1 : 0], -attackRange + 16 + xCoord, -attackRange + 20 + yCoord, 2 * attackRange, 2 * attackRange);
		batch.setColor(1, 1, 1, 1);
	}

	public void takeDamage(int damage)
	{
		currentHealth -= damage;
	}
	
	public boolean isAlive()
	{
		return alive;
	}
	
	public void checkAlive()
	{
		if (currentHealth <= 0)
		{
			if (alive)// && Gdx.app.getType() == Application.ApplicationType.Desktop)
				effects.add(this.blood());
			alive = false;
		}
	}
	
	public static void linkActors(LinkedList<Actor> t1, LinkedList<Actor> t2)
	{
		team1 = t1;
		team2 = t2;
	}
	
	public void targetSelector()
	{
		float currentDistance;
		if (target != null && target.isAlive())
		{
			currentDistance = getDistanceSquared(target);
			if (target instanceof ArrowTower)
				currentDistance -= 3600;
			if (currentDistance < attackRange * attackRange)
			{
				return;
			}
		}		
		
		LinkedList<Actor> enemy = (team == 1 ? team2 : team1);
		Iterator<Actor> actorIter = enemy.iterator();
		Actor newTarget = null;
		float newDistance = 1000000;
		currentDistance = 0;
		while(actorIter.hasNext())
		{
			Actor e = actorIter.next();
			
			if (e.isAlive())
			{
				currentDistance = this.getDistanceSquared(e);
				if (e instanceof ArrowTower)
					currentDistance -= 3600;
				if (currentDistance < newDistance && currentDistance < attackRange * attackRange)
				{
					newDistance = currentDistance;
					newTarget = e;
				}
			}
		}
		
		target = newTarget;
		
		attacking = (target == null) ? false : true; 
	}

	public abstract void destroy();

	protected void rangeAttack() 
	{
		if (!(target == null || !target.isAlive()))
		{
			if (this.attackCooldown <= 0)
			{
				sounds.get("thwp").play(volume);
				if (!(this instanceof Stronghold))
				{
					projectiles.add(new ArrowProjectile(this.xCoord + (this instanceof ArrowTower ? 10 : 0), this.yCoord + (this instanceof ArrowTower ? 40 : 0), this.team, 3, target));
				}
				else
				{
					projectiles.add(new CannonProjectile(this.xCoord, this.yCoord + 50, this.team, 3, target));
				}
				target.takeDamage(damage);
			}
		}
		else
			if (projectiles.size() != 0)
				projectiles.removeAll(projectiles);
	}
	
	protected void meleeAttack()
	{
		if (target == null || !target.isAlive())
			return;
		target.takeDamage(damage);
	}
}
