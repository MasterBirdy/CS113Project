package com.awesomeincorporated.unknowndefense.ui;

import com.awesomeincorporated.unknowndefense.entity.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Button extends Entity
{
	TextureRegion upButton;
	
	public abstract boolean hit(float x, float y);
	
}