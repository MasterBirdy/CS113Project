package com.me.mygdxgame;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.entity.Actor;
import com.me.mygdxgame.entity.Archer;
import com.me.mygdxgame.entity.Hero;
import com.me.mygdxgame.entity.Swordsman;
import com.me.mygdxgame.entity.ArrowTower;
import com.me.mygdxgame.map.Coordinate;
import com.me.mygdxgame.map.Map;

public class EverythingHolder 
{
	@SuppressWarnings("unchecked")
	LinkedList<Actor>[] teams = new LinkedList[2];
	LinkedList<Integer>[] pools = new LinkedList[4];
	static private SpriteBatch batch;
	static boolean showRange;
	static Map map;
	//Hero hero1, hero2;
	long waveTimer, spawnTimer1, spawnTimer2, spawnInterval1, spawnInterval2;;
	long waveTime, waveInterval;
	boolean spawning;
	int nano = 1000000000;
	int income = 100;
	int funds = 200;
		
	public EverythingHolder()
	{
		teams[0] = new LinkedList<Actor>();
		teams[1] = new LinkedList<Actor>();
		pools[0] = new LinkedList<Integer>();
		pools[1] = new LinkedList<Integer>();
		pools[2] = new LinkedList<Integer>();
		pools[3] = new LinkedList<Integer>();
		
		// Wave control
		waveTimer = System.nanoTime() / 1000000; // Timer to keep track of waves
		waveInterval = 10000;// = 5000000000; // Milliseconds between waves
		
		waveTime = 1000;//(long) (1 * nano); // Milliseconds to spawn a wave
				
		spawning = false;
	}
	
	public void add(Actor a, boolean front, int team)
	{
		if (front)
			(team == 1 ? teams[0] : teams[1]).add(0, a);
		else
			(team == 1 ? teams[0] : teams[1]).add(a);		
	}
	
	/*public void addHero(Hero h, int team)
	{
		if (team == 1)
			hero1 = h;
		else
			hero2 = h;
	}*/
	
	public void add(int unit, int team)
	{
		if (team == 1)
		{
			if (funds < 20)
				return;
			funds -= 20;
		}
		pools[team - 1].add(unit);
	}
	
	public void spawnPools()
	{
		for (int m : pools[0])
			spawnMob(m, 1);
		for (int m : pools[1])
			spawnMob(m, 2);
		
		pools[0] = new LinkedList<Integer>();
		pools[1] = new LinkedList<Integer>();
		pools[2] = new LinkedList<Integer>();
		pools[3] = new LinkedList<Integer>();
	}
	
	private void spawnPool(int team)
	{
		if (team == 1)
			spawnMob(pools[2].pop(), 1);
		else
			spawnMob(pools[3].pop(), 2);
	}
	
	private void spawnMob(int m, int team)
	{
		Coordinate start = (team == 1 ? map.start1() : map.start2());
		ListIterator<Coordinate> iter = (team == 1 ? map.getPath().listIterator() : map.getPath().listIterator(map.getPath().size() - 1));
		int randX = (int)(Math.random() * 10);
		int randY = 0; //(int)(Math.random() * 5);
		
		if (m == 1)
			add(new Swordsman(start.x() + randX, start.y() + randY, team, iter), true, team);
		else if (m == 2)
			add(new Archer(start.x() + randX, start.y() + randY, team, iter), true, team);
	}
	
	public LinkedList<Actor> team(int t)
	{
		if (t == 1)
			return teams[0];
		return teams[1];
	}
	
	public void render()
	{
		if (showRange)
		{
			for (Actor a : teams[0])
				if (a.isAlive())
					a.rangeIndicator(batch);
			for (Actor a : teams[1])
				if (a.isAlive())
					a.rangeIndicator(batch);
			/*if (hero1 != null)
				hero1.rangeIndicator(batch);
			if (hero2 != null)
				hero2.rangeIndicator(batch);*/
		}
		
		Iterator<Actor> actorIter = teams[0].iterator();
		Actor a;
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a.isAlive() || (a instanceof ArrowTower))
				a.draw(batch);
			else if (!(a instanceof Hero))
			{
				a.destroy();
				actorIter.remove();
			}
		}
		/*if (hero1 != null && hero1.isAlive())
			hero1.draw(batch);*/
		
		actorIter = teams[1].iterator();
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a.isAlive() || (a instanceof ArrowTower))
				a.draw(batch);
			else
			{
				a.destroy();
				actorIter.remove();
			}
		}
		/*if (hero2 != null && hero2.isAlive())
			hero2.draw(batch);*/
	}
	
	public void update()
	{
		for (Actor a : teams[0])
			a.checkAlive();
		for (Actor a : teams[1])
			a.checkAlive();
		
		/*if (hero1 != null)
			hero1.isAlive();
		if (hero2 != null)
			hero2.isAlive();*/
		
		for (Actor a : teams[0])
			if (a.isAlive())
				a.update();
		for (Actor a : teams[1])
			if (a.isAlive())
				a.update();
		
		/*if (hero1 != null)
			hero1.update();
		if (hero2 != null)
			hero2.update();*/
		
		spawnTimers();
	}
	
	private void spawnTimers()
	{
		long currentTime = System.nanoTime() / 1000000;
		long timeDiff = currentTime - waveTimer;
		if ((timeDiff) > waveInterval)
		{
			spawning = true;
			waveTimer = currentTime;
			spawnTimer1 = waveTimer;
			
			spawnInterval1 = (!pools[0].isEmpty() ? waveTime / pools[0].size() : 10000);
				
			spawnTimer2 = waveTimer;
			spawnInterval2 = (!pools[1].isEmpty() ? waveTime / pools[1].size() : 10000);
			
			pools[2] = pools[0];
			pools[3] = pools[1];
			pools[0] = new LinkedList<Integer>();
			pools[1] = new LinkedList<Integer>();
			if (pools[2] == null)
				pools[2] = new LinkedList<Integer>();
			if (pools[3] == null)
				pools[3] = new LinkedList<Integer>();
			
			for (Actor a : teams[0])
				if (a instanceof Hero && !a.isAlive())
					((Hero)a).respawn(map.start1().x(), map.start1().y(), map.getPath().listIterator());
			funds += income;
			Gdx.input.vibrate(100);
		}
		
		/*if (spawning && pools[2].isEmpty() && pools[3].isEmpty())
			spawning = false;
		
		if (spawning)
		{*/
			if (!pools[2].isEmpty() && currentTime - spawnTimer1 > spawnInterval1)
			{
				spawnPool(1);
				spawnTimer1 = currentTime;
			}
			
			if (!pools[3].isEmpty() && currentTime - spawnTimer2 > spawnInterval2)
			{
				spawnPool(2);
				spawnTimer2 = currentTime;
			}
		//}
	}
	
	public Map map()
	{
		return map;
	}
	
	public int funds()
	{
		return funds;
	}
	
	static public void toggleShowRange()
	{
		showRange = (showRange ? false : true);
	}
	
	static void load(SpriteBatch b, Map m)
	{
		batch = b;
		map = m;
	}
}
