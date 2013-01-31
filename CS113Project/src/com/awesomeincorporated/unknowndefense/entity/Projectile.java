package com.awesomeincorporated.unknowndefense.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Projectile extends Entity {
	
	static TextureRegion[] projectileIndicator;
	float speed;
	float xSpeed, ySpeed;
	double angle = 0;
	int damage;
	Actor target;
	
	public Projectile(float x, float y, int team) 
	{
		super(x, y, team);
	}
	
	public Projectile(float x, float y, int team, float speed, Actor target) 
	{
		super(x, y, team);
//		this.xSpeed = xSpeed;
//		this.ySpeed = ySpeed;
		this.target = target;
		this.angle = getAngleToEntity(target);
		this.xSpeed =  (float) (speed * Math.sin(Math.toRadians(angle)));
		this.ySpeed = (float) (-speed * Math.cos(Math.toRadians(angle)));
	}
	
	public float getxSpeed ()
	{
		return xSpeed;
	}
	
	public float getySpeed ()
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

//	@Override
//	public abstract void update();
	
	@Override
	public void update() {
		this.xCoord(xCoord + this.xSpeed);// * speed);
		this.yCoord(yCoord + this.ySpeed);// * speed);
	}

	public Entity target() 
	{
		return target;
	}

}