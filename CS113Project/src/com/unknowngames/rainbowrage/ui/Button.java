package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.entity.Entity;

public abstract class Button extends Entity
{
	TextureRegion upButton;
	boolean clickable = true;
	
	public void setClickable(boolean c)
	{
		clickable = c;
	}
	
	public abstract boolean hit(float x, float y);
	
}
