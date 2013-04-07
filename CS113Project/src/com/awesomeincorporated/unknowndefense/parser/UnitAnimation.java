package com.awesomeincorporated.unknowndefense.parser;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public class UnitAnimation 
{
	Animation[] animations   = new Animation[8];
	Vector2[] feet = new Vector2[8];
	
	public UnitAnimation(Animation[] a, Vector2[] points)
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
