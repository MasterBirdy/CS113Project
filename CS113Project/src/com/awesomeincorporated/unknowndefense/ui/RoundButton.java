package com.awesomeincorporated.unknowndefense.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.awesomeincorporated.unknowndefense.entity.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RoundButton extends Button
{
	String type;
	int diameter;
	
	public RoundButton(int x, int y, int diam, TextureRegion up)
	{
		xCoord(x);
		yCoord(y);
		diameter = diam;
		upButton = up;
	}
	
	public boolean hit(float x, float y)
	{
//		System.out.println("Button " + x + ": " + y);
		if (getDistanceSquared(x, y) > diameter * diameter)
		{
//			System.out.println("Miss");
			return false;
		}
//		System.out.println("HIT!");
		return true;
	}
	

	@Override
	public void draw(SpriteBatch batch) 
	{
		// TODO Auto-generated method stub
		batch.draw(upButton, xCoord() - diameter, yCoord() - diameter, diameter * 2, diameter * 2);
//		batch.draw(upButton, xCoord(), yCoord(), diameter, diameter, diamter * 2, diameter * 2);
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub	
	}
	
}
