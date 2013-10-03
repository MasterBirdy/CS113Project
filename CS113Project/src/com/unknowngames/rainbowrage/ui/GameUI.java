package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.GameScreen;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.entity.Building;

public class GameUI extends BaseClass
{
	static GameScreen gameScreen;
	TextureRegion buttonFrame;
	Button[] buttons = new Button[17];
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	int buttonRadius = (int) 38;
	int stackTopX = width - 120; 
	int stackTopY = 360;
	int spaceX = 75;
	int spaceY = 54;
	TopUI topUI;
	float screenX, screenY;

	ParticleEffect tempPart;

	String message = "";
	int messageTurnsLeft = 0;
	
	GameMenu gameMenu;
	ActorSkillUpgrade skillDisplay;
	MinionUI minionUI;
	HeroControlUI heroControlUI;
	SideUI sideUI;
	boolean showingMenu = false;
	float scale;

	public GameUI()
	{
		scale = everything.getScreenScale();
		gameMenu = new GameMenu(300 * scale, 150 * scale);
		skillDisplay = new ActorSkillUpgrade((int)(175 * everything.getYRatio()), (int)(140 * everything.getYRatio()));
		skillDisplay.setActor(everything.getHeroStats(everything.getHeroName()));
		screenX = Gdx.graphics.getWidth() / 2;
		screenY = Gdx.graphics.getHeight() / 2;
		topUI = new TopUI();
		minionUI = new MinionUI();
		heroControlUI = new HeroControlUI();
		sideUI = new SideUI();
	}

	public void setup()
	{
		minionUI.setup();
	}

	public int hit(float x, float y)
	{
		if (showingMenu)
			return gameMenu.hit(x, y);
		if (skillDisplay.isShown())
		{
			if (skillDisplay.hit(x, y) == -1)
			{
				int h = minionUI.hit(x, y);
				if (h == -1)
					h = sideUI.hit(x, y);
				
				if (h != -1 && h < 6)
				{
					skillDisplay.setActor(everything.getActorStructure(h, everything.team()), h);
				}
				else if (h == 11)
					skillDisplay.setActor(everything.getHeroStats(everything.getHeroName()), 6);
				else if (h == 12)
					setUpgradeMenu(false);
			}
			return -1;
		}
		int h = minionUI.hit(x, y);
		if (h == -1)
			h = heroControlUI.hit(x, y);
		if (h == -1)
			h = minionUI.hit(x, y);
		if (h == -1)
			h = sideUI.hit(x, y);
		return h;
	}
	
	public void selectTower(Actor a)
	{
		if (a instanceof Building)
		{
			skillDisplay.setActor(everything.getBuildingStructure(0));
			skillDisplay.showDisplay();
		}
	}

	static public void load(GameScreen gs)
	{
		gameScreen = gs;
	}

	public void render(SpriteBatch batch, float delta)
	{
		update();
		everything.getFont(0).draw(batch, message, 200, 20);
		
		heroControlUI.render(batch, delta);
		
		sideUI.render(batch, delta);
		if (skillDisplay.isShown())
			skillDisplay.render(batch);
		
		topUI.draw(batch);
		minionUI.render(batch, delta);
		
		if (showingMenu)
			gameMenu.render(batch);
	}
	
	public void setUpgradeMenu(boolean menu)
	{
		skillDisplay.toggleDisplay();
	}
	
	public void setGameMenu(boolean menu)
	{
		showingMenu = !showingMenu;
	}

	public void setMessage(String m)
	{
		message = m;
		messageTurnsLeft = 500;
	}

	private void update()
	{
		heroControlUI.setSkillClickable(everything.activeCooldown() < 0);
		if (--messageTurnsLeft < 0 && !message.equals(""))
			message = "";
	}
}
