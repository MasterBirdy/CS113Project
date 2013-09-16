package com.unknowngames.rainbowrage.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.entity.Entity;

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
		Gdx.input.vibrate(50);
//		System.out.println("HIT!");
		return true;
	}
	

	@Override
	public void draw(SpriteBatch batch, float delta) 
	{
		if (!visible)
			return;
		if (!clickable)
			batch.setColor(.5f, .5f, .5f, 1f);
		batch.draw(upButton, xCoord() - diameter, yCoord() - diameter, diameter * 2, diameter * 2);
		batch.setColor(Color.WHITE);
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub	
	}
	
}
