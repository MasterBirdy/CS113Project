package com.me.mygdxgame;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.entity.Actor;
import com.me.mygdxgame.map.Map;

public class EverythingHolder 
{
	@SuppressWarnings("unchecked")
	LinkedList<Actor>[] teams = new LinkedList[2];
	static private SpriteBatch batch;
	static boolean showRange;
	static Map map;
	
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
		}
		
		Iterator<Actor> actorIter = teams[0].iterator();
		Actor a;
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a.isAlive())
				a.draw(batch);
			else
			{
				a.destroy();
				actorIter.remove();
			}
		}
		actorIter = teams[1].iterator();
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a.isAlive())
				a.draw(batch);
			else
			{
				a.destroy();
				actorIter.remove();
			}
		}
	}
	
	public void update()
	{
		for (Actor a : teams[0])
			a.checkAlive();
		for (Actor a : teams[1])
			a.checkAlive();
		for (Actor a : teams[0])
			a.update();
		for (Actor a : teams[1])
			a.update();
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
