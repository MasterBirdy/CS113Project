package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Button {

	private float x;
	private float y;
	private Sprite image;
	private Rectangle rect;
	private int rectangleBuffer;
	private float w;
	private float h;
	private boolean visible;
	private boolean clickable;

	public Button(float x, float y, float w, float h, Sprite image)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.image = image;
		image.setPosition(x, y);
		setRectangleBuffer(6, true);
		visible = true;
		clickable = true;
	}

	public Button(float x, float y, float w, float h, Sprite image, boolean b)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.image = image;
		image.setPosition(x, y);
		setRectangleBuffer(6, b);
		visible = true;
		clickable = true;
	}

	public float getX()
	{
		return x;
	}

	public void setX(int a)
	{
		x = a;
		image.setPosition(x, y);
	}

	public void setY(int a)
	{
		y = a;
		image.setPosition(x, y);
	}

	public float getY()
	{
		return y;
	}

	public Sprite getSprite()
	{
		return image;
	}

	public Rectangle getRectangle()
	{
		return rect;
	}

	public void setRectangleBuffer(int i, boolean b)
	{
		if (b)
		{
			rectangleBuffer = i;
			rect = new Rectangle(x - rectangleBuffer - w /2, y - rectangleBuffer - h / 2, image.getWidth() + rectangleBuffer * 2, image.getWidth() + rectangleBuffer * 2);
		}
		else
		{
			rectangleBuffer = i;
			rect = new Rectangle(x - rectangleBuffer, y - rectangleBuffer, image.getWidth() + rectangleBuffer * 2, image.getWidth() + rectangleBuffer * 2);
		}
	}

	public void draw(SpriteBatch b)
	{
		if (visible)
			image.draw(b);
	}

	public void setVisibility(boolean b)
	{
		visible = b;
		clickable = b;
	}

	public boolean isClickable()
	{
		return clickable;
	}

}
