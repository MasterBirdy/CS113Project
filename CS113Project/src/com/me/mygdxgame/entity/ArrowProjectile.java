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
//		if (Math.abs(target.xCoord() - this.xCoord) > 1)
//		{
//			//System.out.println((float)Math.log10(Math.abs(target.xCoord() - this.xCoord)));
//			if (target.xCoord > this.xCoord)
//				this.xCoord(xCoord + this.xSpeed * speed * (float)Math.log10(Math.abs(target.xCoord() - this.xCoord)));
//			else
//				this.xCoord(xCoord - this.xSpeed * speed * (float)Math.log10(Math.abs(target.xCoord() - this.xCoord)));
//		}
//		if (Math.abs(target.yCoord() - this.yCoord) > 1)
//		{
//			if (target.yCoord > this.yCoord)
//				this.yCoord(yCoord + this.ySpeed * speed * (float)Math.log10(Math.abs(target.yCoord() - this.yCoord)));
//			else
//				this.yCoord(yCoord - this.ySpeed * speed * (float)Math.log10(Math.abs(target.yCoord() - this.yCoord)));
//		}
	}

}
