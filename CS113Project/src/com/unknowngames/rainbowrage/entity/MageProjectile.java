package com.unknowngames.rainbowrage.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MageProjectile extends Projectile {

	public MageProjectile(float x, float y, int team, int speed, Actor target) 
	{
		super(x, y, team, speed, target);
		//speed = 1.8f;
	}

	@Override
	public void draw(SpriteBatch batch, float delta) 
	{
		System.out.println("MAGE ATTACK");
		batch.draw(everything.getObjectTexture("fireattack"), xCoord, yCoord, 12, 12);
	}

}
