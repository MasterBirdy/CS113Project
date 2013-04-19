package com.unknowngames.rainbowrage.cutscene;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HeroPortrait extends Sprite
{
	boolean moving = false;
	int startX = 0, startY = 0,
		endX = 0, endY = 0,
		speedX, speedY,
		hold = 0;
	public HeroPortrait(TextureRegion t)
	{
		super(t);
	}
	
	public void slide(int startX, int startY, int endX, int endY, int speedX, int speedY)
	{
		this.setPosition(startX, startY);
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.speedX = speedX;
		this.speedY = speedY;
	}
	
	public void setHold(int h)
	{
		hold = h;
	}
	
	public boolean update()
	{
		if (hold > 0 && distanceSquaredEnter() > speedX * speedX + speedY * speedY)
			translate(speedX, speedY);
		else
			hold--;
		
		if (hold < 0 && distanceSquaredLeave() > speedX * speedX + speedY * speedY)
			translate(-speedX, -speedY);
		else if (hold < 0)
			return true;
		
		return false;
	}
	
	public float distanceSquaredEnter()
	{
		return (getX() - endX) * (getX() - endX) + (getY() - endY) * (getY() - endY);
	}
	
	public float distanceSquaredLeave()
	{
		return (getX() - startX) * (getX() - startX) + (getY() - startY) * (getY() - startY);
	}
}
