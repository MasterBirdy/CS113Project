package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.EverythingHolder;

public class TopUI 
{
	static SpriteBatch batch;
	static EverythingHolder everything;
	Texture icons;
	TextureRegion frame, topBar, emptyHealth, fullHealth, nextWave;
	int width, height, adHeight, topX, topY, adTopY;
	float health = 0.25f;
	
	public TopUI(Texture icons)
	{
		this.icons = icons;
		frame = new TextureRegion(icons, 519, 0, 431, 87);
//		frame = new TextureRegion(icons, 519, 0, 971, 87);
		topBar = new TextureRegion(icons, 519, 99, 800, 83);
		emptyHealth = new TextureRegion(icons, 598, 200, 345, 20);
//		emptyHealth = new TextureRegion(icons, 519, 174, 971, 87);
		fullHealth = new TextureRegion(icons, 598, 287, 345, 20);
//		fullHealth = new TextureRegion(icons, 519, 261, 971, 87);
		nextWave = new TextureRegion(icons, 519, 348, 166, 74);
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		topX = 0;
		topY = Gdx.graphics.getHeight();
		adTopY = topY - 84;
		adHeight = width * 87 / 971;
	}
	
	public void draw()
	{
		batch.draw(topBar, topX, adTopY + 34, width, 50);
		
		// Left Side
		batch.draw(emptyHealth, topX + 66, adTopY + 50, 282, 16);
		batch.draw(fullHealth, topX + 66, adTopY + 50, 282 * everything.baseHealthRatio(), 16);
		batch.draw(frame, 0, adTopY + 18, 356, adHeight);
//		batch.draw(frame, topX, adTopY + 15, width, adHeight);
		
		// Right Side
		batch.draw(emptyHealth, 734, adTopY + 50, -282, 16);
		batch.draw(fullHealth, 734, adTopY + 50, -282 * everything.heroHealthRatio(), 16);
		batch.draw(frame, 800, adTopY + 18, -356, adHeight);
//		batch.draw(frame, 800, adTopY + 15, -width, adHeight);
		
		// Wave Timer
		batch.draw(nextWave, 320, adTopY, 166 * .95f, 74 * .95f);
		everything.getFont(1).draw(batch, String.format("%02d", everything.timeLeft()), 374, adTopY + 58);
//		everything.font2.draw(batch, String.format("%02d", everything.timeLeft()), 374, adTopY + 58);
	}
	
	static public void load(SpriteBatch b, EverythingHolder things)
	{
		batch = b;
		everything = things;
	}
}
