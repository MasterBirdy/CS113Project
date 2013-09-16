package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.entity.Entity;

public abstract class Button extends Entity
{
	TextureRegion upButton;
	boolean clickable = true, visible = true;
	
	public void setClickable(boolean c)
	{
		clickable = c;
		visible = true;
	}
	
	public void setVisible(boolean v)
	{
		visible = v;
	}
	
	public abstract boolean hit(float x, float y);
	
}
