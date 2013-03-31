package com.awesomeincorporated.unknowndefense.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.awesomeincorporated.unknowndefense.entity.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RectangularButton extends Button
{
	String type;
	int width, height;
	
	public RectangularButton(int x, int y, int w, int h, TextureRegion up)
	{
		xCoord(x);
		yCoord(y);
		width = w;
		height = h;
		upButton = up;
	}
	
	public boolean hit(float x, float y)
	{
		if (xCoord() > x || xCoord() + width < x || yCoord() > y || yCoord() + height < y)
			return false;
		
		System.out.println("HIT!");
		return true;
	}
	

	@Override
	public void draw(SpriteBatch batch)
	{
		batch.draw(upButton, xCoord(), yCoord(), width, height);
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub	
	}
	
}
