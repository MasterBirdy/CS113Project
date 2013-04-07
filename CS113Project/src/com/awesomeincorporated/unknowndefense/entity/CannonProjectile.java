package com.awesomeincorporated.unknowndefense.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CannonProjectile extends Projectile {

	public CannonProjectile(float x, float y, int team, int speed, Actor target) 
	{
		super(x, y, team, speed, target);
		//speed = 1.8f;
	}

	@Override
	public void draw(SpriteBatch batch, float delta) 
	{
		// TODO Auto-generated method stub
		batch.draw(projectileIndicator[4], xCoord, yCoord);
	}

//	@Override
//	public void update() {
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
//	}

}
