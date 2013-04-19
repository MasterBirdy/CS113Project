package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.entity.Entity;

public class StatusBar 
{
	TextureRegion backBar, topBar;
	
	public StatusBar(int x, int y, TextureRegion back, TextureRegion top)
	{
		backBar = back;
		topBar = top;
	}
}
