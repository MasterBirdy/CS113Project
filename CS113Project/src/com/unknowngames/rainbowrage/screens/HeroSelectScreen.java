package com.unknowngames.rainbowrage.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.GameScreen;
import com.unknowngames.rainbowrage.RainbowRage;
import com.unknowngames.rainbowrage.ui.Button;
import com.unknowngames.rainbowrage.ui.RoundButton;

public class HeroSelectScreen implements Screen 
{
	EverythingHolder everything;
	SpriteBatch batch;// = new SpriteBatch();
	OrthographicCamera camera;
	TextureRegion background;
	Button[] buttons = new Button[6];
	int diameter = 20;
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	
	String hero[] = {"swordface", "mrwizard"};
	String color[] = {"red", "blue"};
	
	GL10 gl = Gdx.graphics.getGL10();
	Vector2 touchPoint = new Vector2();
	RainbowRage game;
	
	
	public HeroSelectScreen(EverythingHolder everything, RainbowRage game)
	{
		this.everything = everything;
		this.game = game;
		camera = new OrthographicCamera(width, height);
		batch = new SpriteBatch();
//		batch.setProjectionMatrix(camera.combined);
//		this.batch = batch;
		background = new TextureRegion(new Texture(Gdx.files.internal("images/mainmenubackground.jpg")), 0, 0, 800, 480);
		
		buttons[0] = new RoundButton(100, 350, diameter * 2 , everything.getObjectTexture("swordfacebutton"));
		buttons[1] = new RoundButton(200, 350, diameter * 2, everything.getObjectTexture("arroweyesbutton"));
		buttons[2] = new RoundButton(300, 350, diameter * 2, everything.getObjectTexture("mrwizardbutton"));
		buttons[3] = new RoundButton(500, 100, (int)(diameter * 1.5f), everything.getObjectTexture("confirmbutton"));
		buttons[4] = new RoundButton(230, 200, (int)(diameter * 1.5f), everything.getObjectTexture("redbutton"));
		buttons[5] = new RoundButton(300, 200, (int)(diameter * 1.5f), everything.getObjectTexture("bluebutton"));
		
		buttons[1].setClickable(false);
		buttons[2].setClickable(false);
		buttons[5].setClickable(false);
		
	}
	
	public void loadEverything(EverythingHolder e, SpriteBatch b)
	{
		everything = e;
		batch = b;
	}

	@Override
	public void render(float delta) 
	{
		update(delta);
		
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
//		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		batch.draw(background, 0, 0);
		for (int i = 0; i < 6; i++)
			buttons[i].draw(batch, delta);
		
		batch.end();
	}
	
	public void update(float delta)
	{
		if (Gdx.input.justTouched()) {
//			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			touchPoint.set(Gdx.input.getX(), 480 - Gdx.input.getY());
			System.out.println(touchPoint.x + " " + touchPoint.y);
			for (int i = 0; i < buttons.length; i++)
				if (buttons[i].hit(touchPoint.x, touchPoint.y))
				{
					buttonHit(i);
					return;
				}
//			if (OverlapTester.pointInRectangle(newGameRectangle, touchPoint.x, touchPoint.y)) {
//				//System.out.println(true);
////				startMusic.stop();
//				game.gameScreen = new GameScreen(game);
//				newGameStarted = true;
//				game.setScreen(game.gameScreen);
//				return;
//			}
//			else if (OverlapTester.pointInRectangle(continueRectangle, touchPoint.x, touchPoint.y)) {
//				if (newGameStarted) {
////				startMusic.stop();
//				game.setScreen(game.gameScreen);
//				return;
//				}
//			}
//			else if (OverlapTester.pointInRectangle(settingsRectangle, touchPoint.x, touchPoint.y)) {
////				startMusic.stop();
//				game.setScreen(game.settingsScreen);
//				return;
//			}
		}
	}
	
	public void buttonHit(int h)
	{
		if (h == 0) // Swordface
		{
//			startMusic.stop();
			hero[0] = "swordface";
			hero[1] = "mrwizard";
			
			buttons[0].setClickable(true);
			buttons[1].setClickable(false);
			buttons[2].setClickable(false);
//			everything.loadTeams("red", "blue", "swordface", "mrwizard");
//			game.gameScreen = new GameScreen(game, false);
//			game.setScreen(game.gameScreen);
			return;
		}
		else if (h == 1) // Arroweyes
		{
//			startMusic.stop();
			hero[0] = "arroweyes";
			hero[1] = "swordface";
			buttons[0].setClickable(false);
			buttons[1].setClickable(true);
			buttons[2].setClickable(false);
//			everything.loadTeams("blue", "red", "arroweyes", "swordface");
//			game.gameScreen = new GameScreen(game, false);
//			game.setScreen(game.gameScreen);
			return;
		}
		else if (h == 2) // Mr. Wizard
		{
			hero[0] = "mrwizard";
			hero[1] = "arroweyes";
			buttons[0].setClickable(false);
			buttons[1].setClickable(false);
			buttons[2].setClickable(true);
//			everything.loadTeams("blue", "red", "mrwizard", "arroweyes");
//			game.gameScreen = new GameScreen(game, false);
//			game.setScreen(game.gameScreen);
			return;
		}
		else if (h == 3) // Start
		{
			everything.loadTeams(color[0], color[1], hero[0], hero[1]);
			game.gameScreen = new GameScreen(game, false);
			game.setScreen(game.gameScreen);
			return;
//			selectScreen = new HeroSelectScreen(everything);
//			game.setScreen(selectScreen);
//			startMusic.stop();
//			System.exit(0);
		}
		else if (h == 4) // Cancel
		{
			color[0] = "red";
			color[1] = "blue";
			buttons[4].setClickable(true);
			buttons[5].setClickable(false);
//			selectScreen = new HeroSelectScreen(everything);
//			game.setScreen(selectScreen);
//			startMusic.stop();
//			System.exit(0);
		}
		else if(h == 5)
		{
			color[0] = "blue";
			color[1] = "red";
			buttons[4].setClickable(false);
			buttons[5].setClickable(true);
		}
			
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
