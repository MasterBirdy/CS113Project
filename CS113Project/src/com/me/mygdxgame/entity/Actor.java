package com.me.mygdxgame.entity;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.me.mygdxgame.map.Coordinate;

public abstract class Actor extends Entity
{
	int currentHealth, maxHealth, damage;
	float attackSpeed, attackCooldown, attackRange;
	boolean alive, attacking;
	Actor target;
	static LinkedList<Actor> team1;
	static LinkedList<Actor> team2;

	public Actor(int x, int y, int team) //int currentHealth, int maxHealth, int damage, float attackSpeed, float attackCooldown, float attackRange, int team)
	{
		super(x, y, team);
		/*this.currentHealth = currentHealth;
		this.maxHealth = maxHealth;
		this.damage = damage;
		this.attackSpeed = attackSpeed;
		this.attackCooldown = attackCooldown;
		this.attackRange = attackRange;*/
		alive = true;
	}
	
	protected abstract void attack();

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
			alive = false;
	}
	
	public static void linkActors(LinkedList<Actor> t1, LinkedList<Actor> t2)
	{
		team1 = t1;
		team2 = t2;
	}
	
	public void targetSelector()
	{
		if (target != null && target.isAlive())
			return;		
		
		LinkedList<Actor> enemy = (team == 1 ? team2 : team1);
		Iterator<Actor> actorIter = enemy.iterator();
		Actor newTarget = null;
		float newDistance = 10000;
		float currentDistance = 0;
		while(actorIter.hasNext())
		{
			Actor e = actorIter.next();
			
			currentDistance = this.getDistanceSquared(e);
			
			if (currentDistance < newDistance && currentDistance < attackRange * attackRange)
			{
				newDistance = currentDistance;
				newTarget = e;
			}
		}
		
		target = newTarget;
		
		attacking = (target == null) ? false : true; 
	}
}
