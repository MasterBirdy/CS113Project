package com.me.mygdxgame.entity;

import java.util.Iterator;
import java.util.LinkedList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Actor extends Entity
{
	int currentHealth, maxHealth, damage;
	float attackSpeed, attackCooldown, attackRange;
	boolean alive, attacking;
	Actor target;
	static LinkedList<Actor> team1;
	static LinkedList<Actor> team2;
	
	static TextureRegion[] rangeIndicator;

	public Actor(int x, int y, int team)
	{
		super(x, y, team);
		alive = true;
	}
	
	static public void loadRange(Texture sheet)
	{
		rangeIndicator = new TextureRegion[2];
		rangeIndicator[0] = new TextureRegion(sheet, 472, 40, 40, 40);//new Texture(Gdx.files.internal("images/walkingrange.png"));
		rangeIndicator[1] = new TextureRegion(sheet, 472, 0, 40, 40);//new Texture(Gdx.files.internal("images/attackrange.png"));
	}
	
	protected abstract void attack();
	
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
		float newDistance = 1000000;
		float currentDistance = 0;
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
}
