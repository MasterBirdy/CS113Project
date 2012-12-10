package com.me.mygdxgame.input;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.EverythingHolder;
import com.me.mygdxgame.GameScreen;
import com.me.mygdxgame.entity.Entity;
import com.me.mygdxgame.entity.Hero;

public class MyInputProcessor implements InputProcessor
{
	boolean down = false;
	int x, y;
	int deltaX, deltaY;
	static OrthographicCamera camera;
	static Hero hero;
	static GameScreen game;
	int numberOfFingers = 0;
	int fingerOnePointer;
	int fingerTwoPointer;
	float lastDistance = 0;
	Vector3 fingerOne = new Vector3();
	Vector3 fingerTwo = new Vector3();
	float zoom;
	boolean mouse = false;

	@Override
	public boolean keyDown(int keycode) 
	{
		if (keycode == Keys.TAB)
			EverythingHolder.toggleShowRange();
		if (keycode == Keys.NUM_1)
			Entity.setVolume(0);
		if (keycode == Keys.NUM_2)
			Entity.setVolume(0.1f);
		if (keycode == Keys.NUM_3)
			Entity.setVolume(0.2f);
		if (keycode == Keys.NUM_4)
			Entity.setVolume(0.3f);
		if (keycode == Keys.NUM_5)
			Entity.setVolume(0.4f);
		if (keycode == Keys.NUM_6)
			Entity.setVolume(0.5f);
		if (keycode == Keys.NUM_7)
			Entity.setVolume(0.6f);
		if (keycode == Keys.NUM_8)
			Entity.setVolume(0.7f);
		if (keycode == Keys.NUM_9)
			Entity.setVolume(0.8f);
		if (keycode == Keys.NUM_0)
			Entity.setVolume(1f);
		if (keycode == 84)
		{
			int temp = 5;
			temp += 6;
		}
			//Gdx.input.vibrate(20000);
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
		// for pinch-to-zoom
	    numberOfFingers++;
	    if (button == Buttons.RIGHT)
	    	mouse = true;
	    /*if (button == Buttons.LEFT)
	    	System.out.println("Left");
	    if (button == Buttons.RIGHT)
	    	System.out.println("Right");
	    if (button == Buttons.MIDDLE)
	    	System.out.println("Middle");
	    if (button != Buttons.RIGHT || button != Buttons.LEFT)
	    	mouse = true;
	    else
	    	mouse = false;*/
	    	
		if(numberOfFingers == 1)
		{
			fingerOnePointer = pointer;
			fingerOne.set(x, y, 0);
		}
		else if(numberOfFingers == 2)
		{
			fingerTwoPointer = pointer;
			fingerTwo.set(x, y, 0);
			
			float distance = fingerOne.dst(fingerTwo);
			lastDistance = distance;
			zoom = camera.zoom;
		}
		 
		if (pointer == 1)
		{
			EverythingHolder.toggleShowRange();
			return false;
		}
		
		if (x >= 600)
			return false;
		
		if (!down)
		{
			deltaX = 0;
			deltaY = 0;
			down = true;
		}
		this.x = x;
		this.y = y;
		
		//hero.stance(-1);
		
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) 
	{
		if (button == Buttons.RIGHT)
			mouse = false;
		down = false;
		// for pinch-to-zoom           
		if(numberOfFingers == 1)
		{
			Vector3 touchPoint = new Vector3(x, y, 0);
			camera.unproject(touchPoint);
		}
		numberOfFingers--;
		 
		// just some error prevention... clamping number of fingers (ouch! :-)
		if(numberOfFingers<0)
		{
			numberOfFingers = 0;
		}
		 

		lastDistance = 0;
		//hero.stance(1);
		
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) 
	{
		// for pinch-to-zoom
		if (pointer == fingerOnePointer) 
		{
			fingerOne.set(x, y, 0);
		}
		if (pointer == fingerTwoPointer) 
		{
			fingerTwo.set(x, y, 0);
		}
		 
		float distance = fingerOne.dst(fingerTwo);
		float factor = distance / lastDistance;
		
		if (numberOfFingers == 2 && (Gdx.app.getType() == ApplicationType.Android))
		{
			/*if (factor > 2)
				factor = 2;
			else if (factor < .5)
				factor = 0.5f;*/
			
			camera.zoom = zoom - (factor - 1) * .9f;
			game.boundCamera();
		}
		

		/*if (lastDistance > distance) 
		{
			camera.zoom = factor;
		} else if (lastDistance < distance) 
		{
			camera.zoom = factor;
		}*/
		
		
		if (pointer == 1 || !down)
			return false;
		
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
		camera.zoom += amount * 0.06;
		game.boundCamera();
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
	
	public static void loadGame(GameScreen g)
	{
		game = g;
	}
}
