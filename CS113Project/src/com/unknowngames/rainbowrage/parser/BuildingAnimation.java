package com.unknowngames.rainbowrage.parser;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class BuildingAnimation 
{
	Animation[] animations   = new Animation[1];
	Vector2[] feet = new Vector2[1];
	
	public BuildingAnimation(Animation[] a, Vector2[] points)
	{
		animations = a;
		feet = points;
	}
	
	public void loadAnimations(Animation[] animations)
	{
		this.animations = animations;
	}
	
	public void loadFeet(Vector2[] points)
	{
		feet = points;
	}
	
	public Animation getAnimation(int direction)
	{
		return animations[direction];
	}
	
	public Vector2 getFeet(int direction)
	{
		return feet[direction];
	}
}
