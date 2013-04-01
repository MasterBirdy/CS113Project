package com.awesomeincorporated.unknowndefense.ui;

import com.awesomeincorporated.unknowndefense.EverythingHolder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
		frame = new TextureRegion(icons, 519, 0, 971, 87);
		topBar = new TextureRegion(icons, 519, 87, 971, 87);
		emptyHealth = new TextureRegion(icons, 519, 174, 971, 87);
		fullHealth = new TextureRegion(icons, 598, 287, 345, 20);
//		fullHealth = new TextureRegion(icons, 519, 261, 971, 87);
		nextWave = new TextureRegion(icons, 519, 348, 166, 74);
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		topX = 0;
		topY = Gdx.graphics.getHeight();
		adTopY = topY - 70;
		adHeight = width * 87 / 971;
	}
	
	public void draw()
	{
		batch.draw(topBar, topX, adTopY, width, adHeight);
		batch.draw(emptyHealth, topX, adTopY, width, adHeight);
//		fullHealth.setRegionWidth(width);
//		fullHealth = new TextureRegion(icons, 519, 261, 400, 87);
//		batch.draw(fullHealth, topX, adTopY, 400, adHeight);
		batch.draw(fullHealth, topX + 66, adTopY + 32, 281 * everything.heroHealthRatio(), 16);
//		batch.draw(new TextureRegion(fullHealth, 0, 0, 400, 87), topX, adTopY, width, adHeight);
		batch.draw(frame, topX, adTopY, width, adHeight);
	}
	
	static public void load(SpriteBatch b, EverythingHolder things)
	{
		batch = b;
		everything = things;
	}
}
