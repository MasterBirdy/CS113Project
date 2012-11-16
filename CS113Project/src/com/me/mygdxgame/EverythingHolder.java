package com.me.mygdxgame;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.entity.Actor;
import com.me.mygdxgame.entity.Hero;
import com.me.mygdxgame.map.Map;

public class EverythingHolder 
{
	@SuppressWarnings("unchecked")
	LinkedList<Actor>[] teams = new LinkedList[3];
	static private SpriteBatch batch;
	static boolean showRange;
	static Map map;
	Hero hero1, hero2;
	
	public EverythingHolder()
	{
		teams[0] = new LinkedList<Actor>();
		teams[1] = new LinkedList<Actor>();
	}
	
	public void add(Actor a, boolean front, int team)
	{
		if (front)
			(team == 1 ? teams[0] : teams[1]).add(0, a);
		else
			(team == 1 ? teams[0] : teams[1]).add(a);		
	}
	
	public void addHero(Hero h, int team)
	{
		if (team == 1)
			hero1 = h;
		else
			hero2 = h;
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
				a.rangeIndicator(batch);
			for (Actor a : teams[1])
				a.rangeIndicator(batch);
			if (hero1 != null)
				hero1.rangeIndicator(batch);
			if (hero2 != null)
				hero2.rangeIndicator(batch);
		}
		
		Iterator<Actor> actorIter = teams[0].iterator();
		Actor a;
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a.isAlive())
				a.draw(batch);
			else
				actorIter.remove();
		}
		if (hero1 != null && hero1.isAlive())
			hero1.draw(batch);
		
		actorIter = teams[1].iterator();
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a.isAlive())
				a.draw(batch);
			else
				actorIter.remove();
		}
		if (hero2 != null && hero2.isAlive())
			hero2.draw(batch);
	}
	
	public void update()
	{
		for (Actor a : teams[0])
			a.checkAlive();
		for (Actor a : teams[1])
			a.checkAlive();
		
		if (hero1 != null)
			hero1.isAlive();
		if (hero2 != null)
			hero2.isAlive();
		
		for (Actor a : teams[0])
			a.update();
		for (Actor a : teams[1])
			a.update();
		
		if (hero1 != null)
			hero1.update();
		if (hero2 != null)
			hero2.update();
	}
	
	public Map map()
	{
		return map;
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
