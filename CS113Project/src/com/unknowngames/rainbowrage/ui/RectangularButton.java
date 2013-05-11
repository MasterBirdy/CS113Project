package com.unknowngames.rainbowrage.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.entity.Entity;

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
		
		Gdx.input.vibrate(50);
		
//		System.out.println("HIT!");
		return true;
	}
	

	@Override
	public void draw(SpriteBatch batch, float delta)
	{
		if (!clickable)
			batch.setColor(.5f, .5f, .5f, 1f);
		batch.draw(upButton, xCoord(), yCoord(), width, height);
		batch.setColor(Color.WHITE);
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub	
	}
	
}
