package com.me.mygdxgame.entity;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.me.mygdxgame.map.Coordinate;

public abstract class Entity {
	
	int positionX;
	int positionY;
	
	Sprite sprite;
	
	public Sprite getSprite()
	{
		return sprite;
	}
	
	public int getPositionX()
	{
		return positionX;
	}
	
	public int getPositionY()
	{
		return positionY;
	}

}
