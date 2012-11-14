package com.me.mygdxgame.entity;
import java.util.Iterator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.map.Coordinate;

public abstract class Minion extends Unit 
{
	
	public Minion(int x, int y, int team, Iterator<Coordinate> p) 
	{
		super(x, y, team, p);
	}

	@Override
	protected void attack() 
	{
		if (target == null || !target.isAlive())
			return;
		target.takeDamage(damage);
	}

	@Override
	public void draw(SpriteBatch batch)
	{		
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion current;
		int unitType;
		if (this.getClass() == Swordsman.class)
			unitType = 0;
		else if (this.getClass() == Archer.class)
			unitType = 1;
		else
			unitType = 2;
			
		if (this.xSpeed > 0.6)
			current = animations.get(unitType).get(2).getKeyFrame(stateTime, true);
		else if (this.xSpeed < -0.6)
			current = animations.get(unitType).get(1).getKeyFrame(stateTime, true);
		else if (this.ySpeed < -0.6)
			current = animations.get(unitType).get(0).getKeyFrame(stateTime, true);
		else
			current = animations.get(unitType).get(3).getKeyFrame(stateTime, true);
		
		batch.draw(current, xCoord, yCoord);
	}

	@Override
	public void update() 
	{
		
		if (attacking && attackCooldown <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
		}
		else
		{
			attackCooldown--;
			advance();
		}
	}

}
