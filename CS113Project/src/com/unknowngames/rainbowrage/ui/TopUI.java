package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;

public class TopUI extends BaseClass
{
	TextureRegion frame, emptyHealth, fullHealth, nextWave, cash, time, units;
	int width, height;
	float health = 0.25f, scale, scale2;
	float hpX, hpY, waveX, waveY, waveMidX, waveScale; // = 400;

	public TopUI()
	{
		frame = EverythingHolder.getObjectTexture("healthFrame");
		emptyHealth = EverythingHolder.getObjectTexture("emptyHealth");
		fullHealth = EverythingHolder.getObjectTexture("fullHealth");
		nextWave = EverythingHolder.getObjectTexture("nextWave");

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		scale = everything.getScreenScale();
		scale2 = everything.getScreenScale();
		hpY = height - (frame.getRegionHeight() - 7) * scale2;
		hpX = -60 * scale2;
		waveScale = 0.8f;
		waveX = width / 2f - nextWave.getRegionWidth() * scale * waveScale / 2f; //(400 - 83 * .8f) * scale2; //((800f - nextWave.getRegionWidth()) / 2f) * scale2; //((width - nextWave.getRegionWidth()) / 2) * scale2;
		waveY = height - nextWave.getRegionHeight() * scale2;
		
		cash = EverythingHolder.getObjectTexture("cashicon");
		time = EverythingHolder.getObjectTexture("timeicon");
		units = EverythingHolder.getObjectTexture("uniticon");
	}

	public void draw(SpriteBatch batch)
	{
		// Left Side
		batch.draw(emptyHealth, 77 + hpX, 43 + hpY, -77, -43, 
				   345, 20, scale2, scale2, 0);
		batch.draw(fullHealth, 77 + hpX, 43 + hpY, -77, -43, 
				   345 * everything.baseHealthRatio(), 20, scale2, scale2, 0);
		batch.draw(frame, 0 + hpX, 0 + hpY, 0, 0, 431, 87, scale2, scale2, 0);

		// Right Side
		batch.draw(emptyHealth, width + 77 - hpX, 43 + hpY, -77, -43, 
				   345, 20, -scale2, scale2, 0);
		batch.draw(fullHealth, width + 77 - hpX, 43 + hpY, -77, -43, 
				   345 * everything.heroHealthRatio(), 20, -scale2, scale2, 0);
		batch.draw(frame, width - hpX, 0 + hpY, 0, 0, 431, 87, -scale2, scale2, 0);

		// Wave Timer
		batch.draw(nextWave, waveX, waveY, 0, 0, 166, 74,
				scale2 * waveScale, scale2 * waveScale, 0);
		
		everything.getFont(2).drawMultiLine(batch,
				String.format("%02d", everything.timeLeft()), width / 2,
				height - 25 * scale, 0, HAlignment.CENTER);
		
//		everything.getFont(2).drawMultiLine(batch,
//				String.format("%02d", everything.timeLeft()), width * .5f,
//				height * .9604f, 0, HAlignment.CENTER);
		
		batch.draw(units, width / 2 - (60 + cash.getRegionWidth()) * scale, height - 85 * scale, 0, 0, cash.getRegionWidth(), cash.getRegionHeight(), 
				   scale2, scale2, 0);
		batch.draw(cash, width / 2 + 60 * scale, height - 85 * scale, 0, 0, units.getRegionWidth(), units.getRegionHeight(), 
				   scale2, scale2, 0);
		
		everything.getFont(0).drawMultiLine(batch, "" + everything.funds(), width / 2 + 100 * scale, //513 * scale,
				height - 61 * scale, 0, HAlignment.LEFT); 
		everything.getFont(0).drawMultiLine(batch,
				"" + String.format("%03d", everything.teamSize()), width / 2 - 100 * scale,
				height - 61 * scale, 0, HAlignment.RIGHT);
		
		// Player Display
//		everything.getFont(0).drawMultiLine(batch,
//				everything.getPlayer(0).userName(), 10 * scale,
//				height - 61 * scale, 0, HAlignment.LEFT);
	}
}
