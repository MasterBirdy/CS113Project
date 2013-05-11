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
import com.unknowngames.rainbowrage.EverythingHolder;

public class GameUI 
{
	static SpriteBatch batch;
	private Texture icons;
	TextureRegion buttonFrame;
	Button[] buttons = new Button[13];
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	int buttonRadius = (int)38;// (width * .95f / 20);
	int stackTopX = width - 120; //680; //658;
	int stackTopY = 345;//355;
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
	
	String message = "";
	int messageTurnsLeft = 0;
	
	public GameUI()
	{
		buttons[0] = new RoundButton(stackTopX, stackTopY, buttonRadius, 				
				EverythingHolder.getObjectTexture("swordbutton"));	// Sword //new TextureRegion(icons, 0, 1224, 152, 153));	// Pet
		buttons[1] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY, buttonRadius,				
				EverythingHolder.getObjectTexture("archerbutton"));	// Archer
		buttons[2] = new RoundButton(stackTopX, stackTopY - spaceY * 2 + 4, buttonRadius, 			
				EverythingHolder.getObjectTexture("ninjabutton"));	// Ninja
		buttons[3] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 3 + 4, buttonRadius, 			
				EverythingHolder.getObjectTexture("magebutton"));	// Mage
		buttons[4] = new RoundButton(stackTopX, stackTopY - spaceY * 4 + 9, buttonRadius, 			
				EverythingHolder.getObjectTexture("monkbutton"));	// Monk
		buttons[5] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 5 + 9, buttonRadius, 			
				EverythingHolder.getObjectTexture("petbutton"));	// Pet
		buttons[6] = new RoundButton(35, 132, (int) (buttonRadius * .9f), 			
				EverythingHolder.getObjectTexture("attackbutton"));		// Attack
		buttons[7] = new RoundButton(96, 96, (int) (buttonRadius * .9f), 			
				EverythingHolder.getObjectTexture("defendbutton"));	// Defend
		buttons[8] = new RoundButton(133, 35, (int) (buttonRadius * .9f), 			
				EverythingHolder.getObjectTexture("retreatbutton"));	// Retreat
		buttons[9] = new RectangularButton(0, 0, (int)(buttonRadius * 2.75f), (int)(buttonRadius * 2.75f), 				
				EverythingHolder.getObjectTexture("skillbutton")) {	// Skill
			@Override
			public boolean hit(float x, float y)
			{
				if (x < xCoord() || y < yCoord() || (getDistanceSquared(x, y) > width * height))
				{
//					System.out.println("Miss");
					return false;
				}
//				System.out.println("HIT!");
				Gdx.input.vibrate(50);
				return true;
			}
		};
		
//		icons = new Texture(Gdx.files.internal("images/buttons_sheet.png"));
//		
//		buttons[0] = new RoundButton(stackTopX, stackTopY, buttonRadius, 				
//				new TextureRegion(icons, 0, 1071, 152, 153));	// Sword //new TextureRegion(icons, 0, 1224, 152, 153));	// Pet
//		buttons[1] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY, buttonRadius,				
//				new TextureRegion(icons, 0, 918, 152, 153));	// Archer
//		buttons[2] = new RoundButton(stackTopX, stackTopY - spaceY * 2 + 4, buttonRadius, 			
//				new TextureRegion(icons, 0, 765, 152, 153));	// Ninja
//		buttons[3] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 3 + 4, buttonRadius, 			
//				new TextureRegion(icons, 0, 612, 152, 153));	// Mage
//		buttons[4] = new RoundButton(stackTopX, stackTopY - spaceY * 4 + 9, buttonRadius, 			
//				new TextureRegion(icons, 0, 459, 152, 153));	// Monk
//		buttons[5] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 5 + 9, buttonRadius, 			
//				new TextureRegion(icons, 0, 1224, 152, 153));	// Pet
//		buttons[6] = new RoundButton(35, 132, (int) (buttonRadius * .9f), 			
//				new TextureRegion(icons, 0, 0, 152, 153));		// Attack
//		buttons[7] = new RoundButton(96, 96, (int) (buttonRadius * .9f), 			
//				new TextureRegion(icons, 0, 153, 152, 153));	// Defend
//		buttons[8] = new RoundButton(133, 35, (int) (buttonRadius * .9f), 			
//				new TextureRegion(icons, 0, 306, 152, 153));	// Retreat
//		buttons[9] = new RectangularButton(0, 0, (int)(buttonRadius * 2.75f), (int)(buttonRadius * 2.75f), 				
//				new TextureRegion(icons, 152, 0, 215, 209)) {	// Skill
//			@Override
//			public boolean hit(float x, float y)
//			{
//				if (x < xCoord() || y < yCoord() || (getDistanceSquared(x, y) > width * height))
//				{
////					System.out.println("Miss");
//					return false;
//				}
////				System.out.println("HIT!");
//				return true;
//			}
//		};
//		buttons[10] = new RectangularButton(0, 255, (int)(143 * .55f), (int)(109 * .55f),
//				new TextureRegion(icons, 1905, 0, 143, 109));								// Item Shop
		
		buttons[10] = new RectangularButton(0, 255, (int)(143 * .55f), (int)(109 * .55f),
				EverythingHolder.getObjectTexture("storebutton"));								// Item Shop
		buttons[11] = new RectangularButton(stackTopX - 100, 0, (int)(192 * .55f), (int)(137 * .55f),
				EverythingHolder.getObjectTexture(everything.getHeroName() + "button"));			// Hero button
		buttons[12] = new RectangularButton(0, 190, (int)(143 * .55f), (int)(109 * .55f),
				EverythingHolder.getObjectTexture("upgradebutton"));								// Upgrades
		
		buttons[10].setClickable(false);
		buttons[12].setClickable(false);
		screenX = Gdx.graphics.getWidth() / 2;
		screenY = Gdx.graphics.getHeight() / 2;
		
//		buttonFrame = new TextureRegion(icons, 519, 422, 361, 792);
		buttonFrame = EverythingHolder.getObjectTexture("gamebuttonframe");
		
		// Side Stats
		cash = EverythingHolder.getObjectTexture("cashicon");
		time = EverythingHolder.getObjectTexture("timeicon");
		units = EverythingHolder.getObjectTexture("uniticon");
//		cash = new TextureRegion(icons, 152, 209, 33, 31);
//		time = new TextureRegion(icons, 185, 209, 33, 31);
//		units = new TextureRegion(icons, 218, 209, 33, 31);
		
		topUI = new TopUI(new Texture(Gdx.files.internal("images/buttons_sheet.png")));
//		topUI = new TopUI(icons);
	}
	
	public void setup()
	{
		buttons[11] = new RectangularButton(stackTopX - 100, 0, (int)(192 * .55f), (int)(137 * .55f),
				everything.getObjectTexture(everything.getHeroName() + "button"));			// Hero button
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
	
	public void render(float delta)
	{
		update();
		// Buttons
		batch.draw(buttonFrame, stackTopX - 46, stackTopY - 355, buttonFrame.getRegionWidth() * .95f * 80 / 146, buttonFrame.getRegionHeight() * .95f * 80 / 146);
		for (int i = 0; i < 6; i++)
		{
			buttons[i].draw(batch, delta);
			everything.getFont(3).draw(batch, (everything.getSentUnit(i) > 0 ? everything.getSentUnit(i) + "" : ""), buttons[i].xCoord() - 12, buttons[i].yCoord() + 20);
		}
		
		for (int i = 6; i < 9; i++)
		{
			if (7 - i == everything.getHero().stance())
				batch.setColor(Color.WHITE);
			else
				batch.setColor(Color.GRAY);
			
			buttons[i].draw(batch, delta);
		}
		
		for (int i = 9; i < 13; i++)
		{
			buttons[i].draw(batch, delta);
		}
		
		// Side Stats
		batch.draw(cash, 478, height - 85); //395);
//		batch.draw(time, 1, height - 120); //360);
		batch.draw(units, 285, height - 85); //325);
		
//		batch.draw(cash, 1, height - 85); //395);
//		batch.draw(time, 1, height - 120); //360);
//		batch.draw(units, 1, height - 155); //325);

		
//		everything.getFont(0).draw(batch, "" + everything.funds(), 35, height - 61); // 419);
//		everything.getFont(0).draw(batch, "" + everything.totalTime(), 35, height - 96); //384);
//		everything.getFont(0).draw(batch, "" + everything.teamSize(), 35, height - 131); //349);
//		everything.font.draw(batch, "" + everything.totalTime(), 35, 369);
//		everything.font.draw(batch, "" + everything.teamSize(), 35, 334);
		
		everything.getFont(0).draw(batch, message, 200, 20);
		// Top UI
		topUI.draw();
		
		everything.getFont(0).draw(batch, "" + everything.funds(), 513, height - 61); // 419);
		everything.getFont(0).draw(batch, "" + everything.totalTime(), 379, height - 10 ); //384);
		everything.getFont(0).draw(batch, "" + String.format("%03d", everything.teamSize()), 255, height - 61); //349);
		
		everything.getFont(3).draw(batch, (everything.activeCooldown() > 0 ? everything.activeCooldown() / 50 + "" : ""), buttons[9].xCoord() + 25, buttons[9].yCoord() + 57);
		
//		font.draw(batch, "Next wave: " + everything.timeLeft(), screenX - 150, screenY - 20);
//		font.draw(batch, "Time: " + everything.totalTime(), screenX - 150, screenY - 45);
//		font.draw(batch, "Funds: " + everything.funds(), screenX - 150, screenY - 70);
	}
	
	public void setMessage(String m)
	{
		message = m;
		messageTurnsLeft = 500;
	}
	
	private void update()
	{
		buttons[9].setClickable(everything.activeCooldown() < 0);
		if (--messageTurnsLeft < 0 && !message.equals(""))
			message = "";
	}
}
