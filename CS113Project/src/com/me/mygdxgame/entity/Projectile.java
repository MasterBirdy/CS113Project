package com.me.mygdxgame.entity;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.map.Coordinate;

public class Projectile extends Entity {
	
	static TextureRegion[] projectileIndicator;
	float speed;
	int xSpeed, ySpeed;
	Actor target;
	
	public Projectile(float x, float y, int team) 
	{
		super(x, y, team);
		speed = 2.5f;
	}
	
	public Projectile(float x, float y, int team, int xSpeed, int ySpeed, Actor target) 
	{
		super(x, y, team);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.target = target;
		speed = 3f;
	}
	
	public int getxSpeed ()
	{
		return xSpeed;
	}
	
	public int getySpeed ()
	{
		return ySpeed;
	}
	
	public static void loadProjectiles()
	{
		projectileIndicator = new TextureRegion[4];
		projectileIndicator[0] = new TextureRegion(new Texture(Gdx.files.internal("images/arrowprojectile.png")), 0, 0, 16, 16);
		projectileIndicator[1] = new TextureRegion(new Texture(Gdx.files.internal("images/arrowprojectile.png")), 16, 0, 16, 16);
		projectileIndicator[2] = new TextureRegion(new Texture(Gdx.files.internal("images/arrowprojectile.png")), 0, 0, 16, 16);
		projectileIndicator[2].flip(true, false);
		projectileIndicator[3] = new TextureRegion(new Texture(Gdx.files.internal("images/arrowprojectile.png")), 16, 0, 16, 16);
		projectileIndicator[3].flip(false, true);
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
