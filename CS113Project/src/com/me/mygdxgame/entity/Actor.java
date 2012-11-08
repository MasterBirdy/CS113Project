package com.me.mygdxgame.entity;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.me.mygdxgame.map.Coordinate;

public abstract class Actor extends Entity{

	double movementX;
	double movementY;
	int oldX;
	int oldY;
	int differenceXPath;
	int differenceYPath;

	LinkedList<Coordinate> path;

	public Actor(int x, int y, LinkedList<Coordinate> p, Sprite s)
	{
		positionX = x;
		positionY = y;
		oldX = positionX;
		oldY = positionY;
		path = p;
		sprite = s;
		calculatePath();

	}
	public void move()
	{
		positionX += movementX;
		positionY += movementY;
	}

	public boolean timeToChangePath()
	{
		// || differenceXPath == 0)
		if ((Math.abs(differenceXPath) <= Math.abs(positionX - oldX)) && (Math.abs(differenceYPath) <= Math.abs(positionY - oldY)))
		{
			path.pop();
			oldX = positionX;
			oldY = positionY;
			calculatePath();
			return true;
		}
		else
		{
		//	System.out.println((positionX - oldX) + " DIFFERENCE: " + differenceXPath + " // " + (positionY - oldY ) + "DIFFERNECE: " + differenceYPath);
			return false;
		}
	}

	public abstract double giveSpeed();

	private void calculatePath()
	{
		if (path.peek() == null)
		{
			movementX = 0;
			movementY = 0;
		}
		else {
		//	System.out.println("HOOO");
			differenceYPath = path.peek().returnY() - positionY;
		//	System.out.println("DIFFERENCEYPATH : "  + differenceYPath);
			differenceXPath = path.peek().returnX() - positionX;
		//	System.out.println("DIFFERENCEXPATH : "  + differenceXPath);
			if (differenceXPath == 0)
			{
				movementX = 0;
				if (differenceYPath == 0)
					throw new IllegalArgumentException("No path is detected!");
				else if (differenceYPath > 0)
					movementY = 1;
				else
					movementY = -1;
			}
			else
			{
				double slope = differenceYPath / differenceXPath;
				if (differenceYPath == 0)
				{
					movementY = 0;
					if (differenceXPath == 0)
						throw new IllegalArgumentException("No path is detected!");
					else if (differenceXPath > 0)
						movementX = 1;
					else
						movementX = -1;
				}
//				else
//				{
//					if (slope > 1)
//					{
//						movementX = 1;
//						movementY = slope;
//					}
//					else if (slope < 1)
//					{
//						movementY = 1;
//						movementX = 1 / slope;
//					}
//					else
//					{
//						movementY = 1;
//						movementX = 1;
//					}
//				}
			}
		}
	}

}
