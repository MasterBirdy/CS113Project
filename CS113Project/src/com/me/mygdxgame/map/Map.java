package com.me.mygdxgame.map;

import java.util.Collection;
import java.util.LinkedList;


public class Map {
	
	private LinkedList<Coordinate> path;
	
	public Map (Coordinate c)
	{
		path = new LinkedList<Coordinate>();
		path.add(c);
	}

	public Map (Collection<Coordinate> c)
	{
		path = new LinkedList<Coordinate>();
		path.addAll(c);
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
