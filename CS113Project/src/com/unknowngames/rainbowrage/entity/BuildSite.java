package com.unknowngames.rainbowrage.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BuildSite extends Entity
{
	int capCount;
	float stateTime;
//	static Animation[] animations;
	
	public BuildSite(int x, int y, int team, int capCount)
	{
		super(x, y, team);
		this.capCount = capCount; 
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) 
	{
		TextureRegion current;
		stateTime += delta;//Gdx.graphics.getDeltaTime();
//		current = buildingAnimation.getAnimation(0).getKeyFrame(stateTime * 0.5f, true);
//		Vector2 point = buildingAnimation.getFeet(0);
		
//		batch.draw(current, xCoord - point.x * .5f, yCoord - point.y * .5f, current.getRegionWidth() * .5f, current.getRegionHeight() * .5f);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
