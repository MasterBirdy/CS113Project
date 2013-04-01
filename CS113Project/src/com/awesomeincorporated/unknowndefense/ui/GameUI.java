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
	Button[] buttons = new Button[10];
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	int buttonRadius = width / 20;
	int stackTopX = 675;
	int stackTopY = 360;
	int spaceX = 80;
	int spaceY = 60;
	
//	int buttonRadius = 40;
//	int stackTopX = 275;
//	int stackTopY = 120;
//	int spaceX = 80;
//	int spaceY = 60;
	BitmapFont font;
	static EverythingHolder everything;
	TopUI topUI;
	
	float screenX, screenY;
	
	ParticleEffect tempPart;
	
	public GameUI()
	{		
		icons = new Texture(Gdx.files.internal("images/buttons_sheet.png"));
		
		buttons[0] = new RoundButton(stackTopX, stackTopY, buttonRadius, 				
				new TextureRegion(icons, 0, 1071, 152, 153));	// Sword //new TextureRegion(icons, 0, 1224, 152, 153));	// Pet
		buttons[1] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY, buttonRadius,				
				new TextureRegion(icons, 0, 918, 152, 153));	// Archer
		buttons[2] = new RoundButton(stackTopX, stackTopY - spaceY * 2, buttonRadius, 			
				new TextureRegion(icons, 0, 765, 152, 153));	// Ninja
		buttons[3] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 3, buttonRadius, 			
				new TextureRegion(icons, 0, 612, 152, 153));	// Mage
		buttons[4] = new RoundButton(stackTopX, stackTopY - spaceY * 4, buttonRadius, 			
				new TextureRegion(icons, 0, 459, 152, 153));	// Monk
		buttons[5] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 5, buttonRadius, 			
				new TextureRegion(icons, 0, 1224, 152, 153));	// Pet
		buttons[6] = new RoundButton(40, 143, (int) (buttonRadius * .9f), 			
				new TextureRegion(icons, 0, 0, 152, 153));		// Attack
		buttons[7] = new RoundButton(106, 102, (int) (buttonRadius * .9f), 			
				new TextureRegion(icons, 0, 153, 152, 153));	// Defend
		buttons[8] = new RoundButton(145, 38, (int) (buttonRadius * .9f), 			
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
		font = new BitmapFont();
		screenX = Gdx.graphics.getWidth() / 2;
		screenY = Gdx.graphics.getHeight() / 2;

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
		for (int i = 0; i < buttons.length; i++)
			buttons[i].draw(batch);
		topUI.draw();
//		font.draw(batch, "Next wave: " + everything.timeLeft(), screenX - 150, screenY - 20);
//		font.draw(batch, "Time: " + everything.totalTime(), screenX - 150, screenY - 45);
//		font.draw(batch, "Funds: " + everything.funds(), screenX - 150, screenY - 70);
	}
}
