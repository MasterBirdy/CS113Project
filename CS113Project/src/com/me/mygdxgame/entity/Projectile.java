package com.me.mygdxgame.entity;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.map.Coordinate;

public abstract class Projectile extends Entity {
	
	static TextureRegion[] projectileIndicator;
	float speed;
	int xSpeed, ySpeed;
	Actor target;
	
	public Projectile(float x, float y, int team) 
	{
		super(x, y, team);
	}
	
	public Projectile(float x, float y, int team, int xSpeed, int ySpeed, Actor target) 
	{
		super(x, y, team);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.target = target;
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
		projectileIndicator = new TextureRegion[5];
		projectileIndicator[0] = new TextureRegion(new Texture(Gdx.files.internal("images/arrowprojectile.png")), 0, 0, 16, 16);
		projectileIndicator[1] = new TextureRegion(new Texture(Gdx.files.internal("images/arrowprojectile.png")), 16, 0, 16, 16);
		projectileIndicator[2] = new TextureRegion(new Texture(Gdx.files.internal("images/arrowprojectile.png")), 0, 0, 16, 16);
		projectileIndicator[2].flip(true, false);
		projectileIndicator[3] = new TextureRegion(new Texture(Gdx.files.internal("images/arrowprojectile.png")), 16, 0, 16, 16);
		projectileIndicator[3].flip(false, true);
		projectileIndicator[4] = new TextureRegion(new Texture(Gdx.files.internal("images/cannonprojectile.png")), 0, 0, 16, 16);
	}

	@Override
	public abstract void draw(SpriteBatch batch);

	@Override
	public abstract void update();

	public Entity target() 
	{
		return target;
	}

}
