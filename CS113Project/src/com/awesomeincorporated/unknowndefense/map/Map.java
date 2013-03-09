package com.awesomeincorporated.unknowndefense.map;

import java.util.Collection;
import java.util.LinkedList;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Map 
{
	
	private LinkedList<Coordinate> path, reversePath;
	private Sprite background;
	private int width, height;
	private Coordinate start1, start2;
	private Coordinate[] buildSites1, buildSites2;
	
	
	public Map (Coordinate c, Sprite background, int width, int height, int x1, int y1, int x2, int y2)
	{
		path = new LinkedList<Coordinate>();
		reversePath = new LinkedList<Coordinate>();
		path.add(c);
		reversePath.add(c);
		this.background = background;
		this.width = width;
		this.height = height;
		start1 = new Coordinate(x1, y1);
		start2 = new Coordinate(x2, y2);
	}

	public Map (Collection<Coordinate> c, Sprite background)
	{
		path = new LinkedList<Coordinate>();
		reversePath = new LinkedList<Coordinate>();
		path.addAll(c);
		reversePath = reversePath();
		this.background = background;
	}
	
	public void buildSites(Coordinate[] sites, int team)
	{
		if (team == 1)
			buildSites1 = sites;
		else
			buildSites2 = sites;
	}
	
	public Coordinate[] buildSites(int team)
	{
		Coordinate[] temp = (team == 1 ? buildSites1 : buildSites2);
		if (temp == null)
			return new Coordinate[0];
		return temp;
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
		reversePath = reversePath();
	}
	
	public LinkedList<Coordinate> getPath()
	{
		return path;
	}
	
	public LinkedList<Coordinate> getReversePath()
	{
		return reversePath;
	}
	
	private LinkedList<Coordinate> reversePath()
	{
		LinkedList<Coordinate> temp = new LinkedList<Coordinate>();
		for (int i = path.size() - 1; i >= 0; i--)
			temp.add(path.get(i));
		return temp;
	}
}
