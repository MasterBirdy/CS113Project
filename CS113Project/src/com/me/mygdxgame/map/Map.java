package com.me.mygdxgame.map;

import java.util.Collection;
import java.util.LinkedList;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Map 
{
	
	private LinkedList<Coordinate> path;
	private Sprite background;
	private int width, height;
	private Coordinate start1, start2;
	
	public Map (Coordinate c, Sprite background, int width, int height, int x1, int y1, int x2, int y2)
	{
		path = new LinkedList<Coordinate>();
		path.add(c);
		this.background = background;
		this.width = width;
		this.height = height;
		start1 = new Coordinate(x1, y1);
		start2 = new Coordinate(x2, y2);
	}

	public Map (Collection<Coordinate> c, Sprite background)
	{
		path = new LinkedList<Coordinate>();
		path.addAll(c);
		this.background = background;
	}
	
	public Coordinate start1()
	{
		return start1;
	}
	
	public Coordinate start2()
	{
		return start2;
	}
	
	public int width()
	{
		return width;
	}
	
	public int height()
	{
		return height;
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
