package com.awesomeincorporated.unknowndefense.ui;

import com.awesomeincorporated.unknowndefense.EverythingHolder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameUI 
{
	static SpriteBatch batch;
	private Texture icons;
	TextureRegion buttonFrame;
	Button[] buttons = new Button[10];
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	int buttonRadius = (int) (width * .95f / 20);
	int stackTopX = 680; //658;
	int stackTopY = 335;//355;
	int spaceX = 75;
	int spaceY = 54;
	
//	int buttonRadius = 40;
//	int stackTopX = 275;
//	int stackTopY = 120;
//	int spaceX = 80;
//	int spaceY = 60;
//	BitmapFont font;
	static EverythingHolder everything;
	TopUI topUI;
	TextureRegion cash, time, units;
	float screenX, screenY;
	
	ParticleEffect tempPart;
	
	public GameUI()
	{		
		icons = new Texture(Gdx.files.internal("images/buttons_sheet.png"));
		
		buttons[0] = new RoundButton(stackTopX, stackTopY, buttonRadius, 				
				new TextureRegion(icons, 0, 1071, 152, 153));	// Sword //new TextureRegion(icons, 0, 1224, 152, 153));	// Pet
		buttons[1] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY, buttonRadius,				
				new TextureRegion(icons, 0, 918, 152, 153));	// Archer
		buttons[2] = new RoundButton(stackTopX, stackTopY - spaceY * 2 + 4, buttonRadius, 			
				new TextureRegion(icons, 0, 765, 152, 153));	// Ninja
		buttons[3] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 3 + 4, buttonRadius, 			
				new TextureRegion(icons, 0, 612, 152, 153));	// Mage
		buttons[4] = new RoundButton(stackTopX, stackTopY - spaceY * 4 + 9, buttonRadius, 			
				new TextureRegion(icons, 0, 459, 152, 153));	// Monk
		buttons[5] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 5 + 9, buttonRadius, 			
				new TextureRegion(icons, 0, 1224, 152, 153));	// Pet
		buttons[6] = new RoundButton(36, 137, (int) (buttonRadius * .9f), 			
				new TextureRegion(icons, 0, 0, 152, 153));		// Attack
		buttons[7] = new RoundButton(99, 99, (int) (buttonRadius * .9f), 			
				new TextureRegion(icons, 0, 153, 152, 153));	// Defend
		buttons[8] = new RoundButton(137, 35, (int) (buttonRadius * .9f), 			
				new TextureRegion(icons, 0, 306, 152, 153));	// Retreat
		buttons[9] = new RectangularButton(0, 0, (int)(buttonRadius * 2.75f), (int)(buttonRadius * 2.75f), 				
				new TextureRegion(icons, 152, 0, 215, 209)) {	// Skill
			@Override
			public boolean hit(float x, float y)
			{
				if (x < xCoord() || y < yCoord() || (getDistanceSquared(x, y) > width * height))
				{
//					System.out.println("Miss");
					return false;
				}
//				System.out.println("HIT!");
				return true;
			}
		};
		screenX = Gdx.graphics.getWidth() / 2;
		screenY = Gdx.graphics.getHeight() / 2;
		
		buttonFrame = new TextureRegion(icons, 519, 422, 361, 792);
		
		// Side Stats
		cash = new TextureRegion(icons, 152, 209, 33, 31);
		time = new TextureRegion(icons, 185, 209, 33, 31);
		units = new TextureRegion(icons, 218, 209, 33, 31);
		
		topUI = new TopUI(icons);
	}
	
	public int hit(float x, float y)
	{
		for (int i = 0; i < buttons.length; i++)
			if (buttons[i].hit(x, y))
				return i;
		return -1;
	}
	
	static public void load(SpriteBatch b, EverythingHolder things)
	{
		batch = b;
		everything = things;
		TopUI.load(batch, everything);		
	}
	
	public void render()
	{
		// Buttons
		batch.draw(buttonFrame, stackTopX - 46, stackTopY - 355, buttonFrame.getRegionWidth() * .95f * 80 / 146, buttonFrame.getRegionHeight() * .95f * 80 / 146);
		for (int i = 0; i < buttons.length; i++)
			buttons[i].draw(batch);
		
		// Side Stats
		batch.draw(cash, 1, 380);
		batch.draw(time, 1, 345);
		batch.draw(units, 1, 310);

		everything.font.draw(batch, "" + everything.funds(), 35, 404);
		everything.font.draw(batch, "" + everything.totalTime(), 35, 369);
		everything.font.draw(batch, "" + everything.teamSize(), 35, 334);
		
		// Top UI
		topUI.draw();
//		font.draw(batch, "Next wave: " + everything.timeLeft(), screenX - 150, screenY - 20);
//		font.draw(batch, "Time: " + everything.totalTime(), screenX - 150, screenY - 45);
//		font.draw(batch, "Funds: " + everything.funds(), screenX - 150, screenY - 70);
	}
}
