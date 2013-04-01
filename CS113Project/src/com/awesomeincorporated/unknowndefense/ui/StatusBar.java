package com.awesomeincorporated.unknowndefense.ui;

import com.awesomeincorporated.unknowndefense.entity.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StatusBar 
{
	TextureRegion backBar, topBar;
	
	public StatusBar(int x, int y, TextureRegion back, TextureRegion top)
	{
		backBar = back;
		topBar = top;
	}
}
