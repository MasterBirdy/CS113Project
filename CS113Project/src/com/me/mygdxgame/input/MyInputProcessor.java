package com.me.mygdxgame.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.me.mygdxgame.EverythingHolder;
import com.me.mygdxgame.entity.Hero;

public class MyInputProcessor implements InputProcessor
{
	boolean down = false;
	int x, y;
	int deltaX, deltaY;
	static OrthographicCamera camera;
	static Hero hero;

	@Override
	public boolean keyDown(int keycode) 
	{
		if (keycode == Keys.TAB)
			EverythingHolder.toggleShowRange();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) 
	{
		if (!down)
		{
			deltaX = 0;
			deltaY = 0;
			down = true;
		}
		this.x = x;
		this.y = y;
		
		hero.stance(-1);
		
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		down = false;
		
		hero.stance(1);
		
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) 
	{
		deltaX = this.x - x;
		deltaY = y - this.y;
		this.x = x;
		this.y = y;
		camera.translate(deltaX, deltaY);
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void loadCamera(OrthographicCamera cameraIn)
	{
		camera = cameraIn;
	}
	
	public static void loadHero(Hero h)
	{
		hero = h;
	}
}
