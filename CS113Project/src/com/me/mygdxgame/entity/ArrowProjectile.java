package com.me.mygdxgame.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ArrowProjectile extends Projectile {

	public ArrowProjectile(float x, float y, int team) {
		super(x, y, team);
		// TODO Auto-generated constructor stub
	}
	
	public ArrowProjectile(float x, float y, int team, int xSpeed, int ySpeed, Actor target) 
	{
		super(x, y, team, xSpeed, ySpeed, target);
		speed = 3f;
	}
	@Override
	public void draw(SpriteBatch batch) {
		if (this.xSpeed < 0)
			batch.draw(projectileIndicator[0], xCoord, yCoord);
		else if (this.xSpeed > 0)
			batch.draw(projectileIndicator[2], xCoord, yCoord);
		else if (this.ySpeed > 0)
			batch.draw(projectileIndicator[1], xCoord, yCoord);
		else
			batch.draw(projectileIndicator[3], xCoord, yCoord);
	}

	@Override
	public void update() {
		this.xCoord(xCoord + this.xSpeed * speed);
		this.yCoord(yCoord + this.ySpeed * speed);
		
	}

}
