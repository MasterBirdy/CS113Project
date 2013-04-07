package com.awesomeincorporated.unknowndefense.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ArrowProjectile extends Projectile {

	public ArrowProjectile(float x, float y, int team) {
		super(x, y, team);
		// TODO Auto-generated constructor stub
	}
	
	public ArrowProjectile(float x, float y, int team, int speed, Actor target) 
	{
		super(x, y, team, speed, target);
		//speed = 3f;
	}
	@Override
	public void draw(SpriteBatch batch, float delta) 
	{
		batch.draw(projectileIndicator[3], xCoord, yCoord, projectileIndicator[0].getRegionWidth() / 2, projectileIndicator[0].getRegionHeight() / 2, projectileIndicator[0].getRegionWidth(), projectileIndicator[0].getRegionWidth(), 1, 1, (float)angle);
//		if (this.xSpeed < 0)
//			batch.draw(projectileIndicator[0], xCoord, yCoord);
//		else if (this.xSpeed > 0)
//			batch.draw(projectileIndicator[2], xCoord, yCoord);
//		else if (this.ySpeed > 0)
//			batch.draw(projectileIndicator[1], xCoord, yCoord);
//		else
//			batch.draw(projectileIndicator[3], xCoord, yCoord);
	}

//	@Override
//	public void update() {
//		this.xCoord(xCoord + this.xSpeed * speed);
//		this.yCoord(yCoord + this.ySpeed * speed);
////		if (Math.abs(target.xCoord() - this.xCoord) > 1)
////		{
////			//System.out.println((float)Math.log10(Math.abs(target.xCoord() - this.xCoord)));
////			if (target.xCoord > this.xCoord)
////				this.xCoord(xCoord + this.xSpeed * speed * (float)Math.log10(Math.abs(target.xCoord() - this.xCoord)));
////			else
////				this.xCoord(xCoord - this.xSpeed * speed * (float)Math.log10(Math.abs(target.xCoord() - this.xCoord)));
////		}
////		if (Math.abs(target.yCoord() - this.yCoord) > 1)
////		{
////			if (target.yCoord > this.yCoord)
////				this.yCoord(yCoord + this.ySpeed * speed * (float)Math.log10(Math.abs(target.yCoord() - this.yCoord)));
////			else
////				this.yCoord(yCoord - this.ySpeed * speed * (float)Math.log10(Math.abs(target.yCoord() - this.yCoord)));
////		}
//	}

}
