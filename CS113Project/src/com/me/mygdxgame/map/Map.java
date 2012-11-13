package com.me.mygdxgame.map;

import java.util.Collection;
import java.util.LinkedList;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Map 
{
	
	private LinkedList<Coordinate> path;
	private Sprite background;
	
	public Map (Coordinate c, Sprite background)
	{
		path = new LinkedList<Coordinate>();
		path.add(c);
		this.background = background;
	}

	public Map (Collection<Coordinate> c, Sprite background)
	{
		path = new LinkedList<Coordinate>();
		path.addAll(c);
		this.background = background;
	}
	
	public Sprite background()
	{
		return background;
	}
	
	public void add (Coordinate c)
	{
		path.add(c);
	}
	
	public LinkedList<Coordinate> getPath()
	{
		return path;
	}
}
