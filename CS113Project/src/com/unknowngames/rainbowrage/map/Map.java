package com.unknowngames.rainbowrage.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;


public class Map 
{
	
//	private LinkedList<Coordinate> path, reversePath;
	private ArrayList<Coordinate>[] path, reversePath;
	private Sprite background;
	private int width, height;
	private Coordinate start1, start2;
	private Coordinate[] buildSites1, buildSites2;
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	
//	public Map (Coordinate c, Sprite background, int width, int height, int x1, int y1, int x2, int y2)
	public Map (Sprite background, int width, int height, int x1, int y1, int x2, int y2)
	{
		path = (ArrayList<Coordinate>[]) new ArrayList[2];
		reversePath = (ArrayList<Coordinate>[]) new ArrayList[2];
		for (int i = 0; i < 2; i++)
		{
			path[i] = new ArrayList<Coordinate>();
			reversePath[i] = new ArrayList<Coordinate>();
		}
//		path.add(c);
//		reversePath.add(c);
		this.background = background;
		this.width = width;
		this.height = height;
		start1 = new Coordinate(x1, y1);
		start2 = new Coordinate(x2, y2);
	}

//	public Map (Collection<Coordinate> c, Sprite background)
//	{
//		path = new ArrayList<Coordinate>();
//		reversePath = new ArrayList<Coordinate>();
//		path.addAll(c);
////		reversePath = reversePath();
//		this.background = background;
//	}
	
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
	
	public void add (Coordinate c, int route)
	{
		path[route - 1].add(c);
		reversePath[route - 1].add(0, c);
//		reversePath = reversePath();
	}
	
	public ArrayList<Coordinate> getPath(int route)
	{
		return path[route - 1];
	}
	
	public ArrayList<Coordinate> getReversePath(int route)
	{
		return reversePath[route - 1];
	}
	
	public void drawPaths(SpriteBatch batch, Camera camera)
	{
		int x;// = path[0].get(0).x();
		int y;// = path[0].get(0).y();
		int x1, y1;
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		
		shapeRenderer.setColor(1, 1, 0, 1);
		for (int i = 0; i < path.length; i++)
		{
			x = path[i].get(0).x();
			y = path[i].get(0).y();
			
			for (int j = 1; j < path[i].size(); j++)
			{
				x1 = path[i].get(j).x();
				y1 = path[i].get(j).y();
				shapeRenderer.line(x, y, x1, y1);
				x = x1;
				y = y1;
			}
			shapeRenderer.setColor(0, 1, 1, 1);
		}
		shapeRenderer.end();
	}
	
//	private LinkedList<Coordinate> reversePath()
//	{
//		LinkedList<Coordinate> temp = new LinkedList<Coordinate>();
//		for (int i = path.size() - 1; i >= 0; i--)
//			temp.add(path.get(i));
//		return temp;
//	}
}
