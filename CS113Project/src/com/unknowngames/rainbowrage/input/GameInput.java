package com.unknowngames.rainbowrage.input;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.entity.Building;
import com.unknowngames.rainbowrage.entity.Entity;
import com.unknowngames.rainbowrage.entity.Hero;
import com.unknowngames.rainbowrage.screens.GameScreen;
import com.unknowngames.rainbowrage.ui.GameUI;

public class GameInput extends BaseClass implements InputProcessor
{
	boolean readyToLeave = false;
	
	boolean down = false;
	int x, y;
	int deltaX, deltaY;
	static OrthographicCamera camera, uiCamera;
	static Hero hero;
	static GameScreen game;
	static GameUI gameUI;
	int numberOfFingers = 0;
	int fingerOnePointer;
	int fingerTwoPointer;
	float lastDistance = 0;
	Vector3 touchPoint = new Vector3();
	Vector3 fingerOne = new Vector3();
	Vector3 fingerTwo = new Vector3();
	float zoom;
	boolean mouse = false;
	
	int hit;

	@Override
	public boolean keyDown(int keycode) 
	{
		if (keycode > 0)
			System.out.println(keycode);
		if (keycode == Keys.TAB)
			EverythingHolder.toggleShowRange();
		if (keycode == Keys.NUM_1)
		{
			EverythingHolder.getSettings().togglePath();
//			everything
//			EverythingHolder.setMusicVolume(0f);
			//Entity.setVolume(0);
		}
		if (keycode == Keys.NUM_2)
		{
			EverythingHolder.getSettings().toggleRange();
//			game.sendMessage("Suck it, nub");
//			game.toggleIsPaused();
//			Entity.setVolume(0.1f);
		}
		if (keycode == Keys.NUM_3)
			EverythingHolder.getSettings().toggleRadius();
//			Entity.setVolume(0.2f);
		if (keycode == Keys.NUM_4)
			EverythingHolder.getSettings().toggleTextEffect();
//			Entity.setVolume(0.3f);
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
		{
//			EverythingHolder.setMusicVolume(1f);
			Entity.setVolume(1f);
		}
		if (keycode == Keys.BACKSLASH && readyToLeave == true)
		{
			System.out.println("Sending kill");
			game.sendMessage("kill");
		}
		if (keycode == Keys.HOME || keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			gameUI.setGameMenu(true);
		}
		if (keycode == Keys.L)
		{
//			System.out.println("Ready");
			readyToLeave = true;
		}
		else if (keycode > 0)
		{
//			System.out.println(keycode + " End Ready");
			readyToLeave = false;
		}
		
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
		else if (numberOfFingers >= 2)
		{
						
		}
		 
//		if (pointer == 1)
//		{
//			EverythingHolder.toggleShowRange();
//			return false;
//		}
		
//		if (x >= 600)
//			return false;
		uiCamera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		if (buttonHit(gameUI.hit(touchPoint.x, touchPoint.y)))
		{
			return false;
		}
		/*camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		Actor selectedActor = everything.atPoint(touchPoint.x, touchPoint.y);
		if (selectedActor != null && selectedActor.team() == everything.team() && selectedActor instanceof Building)
		{
			gameUI.selectTower(selectedActor);
			System.out.println("Click on a tower!");
			
		}*/
		
		if (!down)
		{
			deltaX = 0;
			deltaY = 0;
			down = true;
		}
		this.x = x;
		this.y = y;
		
		//hero.stance(-1);
		System.out.println(x + ": " + y);
		
		return false;
	}
	
	private boolean buttonHit(int h)
	{
		if (h == -1)
			return false;
		if (h < 6)
			game.buyUnit(h);
		else if (h < 9)
			game.setHeroStance(7 - h);
		else if (h == 9)
			game.castHeroActive();
		else if (h == 11)
			game.toggleFollowing();
		else if (h == 12)
			gameUI.setUpgradeMenu(true);
		else if (h == 13)
			game.clearSend();
		else if (h == 14)
			gameUI.toggleChatMenu();
		else if (h == 15)
			game.sendMessage("happy");
		else if (h == 16)
			game.sendMessage("angry");
		else if (h == 17)
			game.sendMessage("gg");
		else if (h == 20)		// Pause menu
			gameUI.setGameMenu(false);
		else if (h == 21)
			game.endGame();
//			game.quit();
//			game.centerOnHero();
		return true;
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
		if(numberOfFingers == 2)
		{
			if (fingerOnePointer == pointer)
				fingerOnePointer = fingerTwoPointer;
			fingerTwoPointer = -1;
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
			
			camera.zoom = zoom / factor; //zoom - (factor - 1f) * zoom; //* camera.zoom;// .9f;
			game.boundCamera();
			return false;
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
		camera.translate(deltaX * camera.zoom, deltaY * camera.zoom);
		game.diasbleFollowing();
		return false;
	}

//	@Override
//	public boolean touchMoved(int x, int y) {
//		
//		return false;
//	}

	@Override
	public boolean scrolled(int amount) 
	{
		camera.zoom += amount * 0.06;
		game.boundCamera();
		return false;
	}
	
	public static void loadCamera(OrthographicCamera cameraIn, OrthographicCamera uiCam)
	{
		camera = cameraIn;
		uiCamera = uiCam;
	}
	
	public static void loadHero(Hero h)
	{
		hero = h;
	}
	
	public static void loadGame(GameScreen g, GameUI ui)
	{
		game = g;
		gameUI = ui;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) 
	{
		// TODO Auto-generated method stub
		return false;
	}
}
