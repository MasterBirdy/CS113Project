package com.unknowngames.rainbowrage.entity;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.map.Coordinate;
import com.unknowngames.rainbowrage.parser.BuildingAnimation;
import com.unknowngames.rainbowrage.parser.BuildingStructure;
import com.unknowngames.rainbowrage.parser.MinionStructure;
import com.unknowngames.rainbowrage.skill.SkillEffect;

public class Building extends Actor
{
//	Coordinate destination;
	static ArrayList<Sprite> sprites;
	BuildingAnimation buildingAnimation;
	int towerNumber = 0;
//	ArrayList<Projectile> projectiles;
	float stateTime;
	ParticleEffect fire = new ParticleEffect();
	boolean debug = false;
	BuildingStructure buildingStructure;
	
	public Building(int x, int y, int team, BuildingStructure struct)
	{
		super(x, y, struct.ranged(0), team, struct);
		if (Gdx.app.getType() != ApplicationType.Android)
		{
//			fire.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
			fire = EverythingHolder.getEffect("fire");
			fire.setPosition(this.xCoord(), this.yCoord() + 20);
		}
//		this.towerNumber = towerNumber;
		buildingAnimation = EverythingHolder.getBuildingAnimation(struct.animation(0) + team);
		alive = false;
		buildingStructure = struct;
	}
	
	@Override
	public void update() 
	{
		super.update();
		if (attacking && attackCooldown <= 0)
		{
//			System.out.println("Tower attacking");
			attack();
			attackCooldown = attackSpeed;
		}
		else
		{
			attackCooldown--;
			targetSelector();
		}
//		ArrayList<Projectile> removeList = new ArrayList<Projectile>();
//		for (Projectile p : projectiles)
//		{
//				//if (p.xCoord == this.target.xCoord() || )
//			p.update();
//			if (Math.abs(p.target.xCoord() - p.xCoord) < 2)
//				removeList.add(p);
//			else if (Math.abs(p.target.yCoord() - p.yCoord) < 2)
//				removeList.add(p);
//		}
//		projectiles.removeAll(removeList);
	}
	
	@Override
	public void checkAlive()
	{
		if (level != 0 && alive && currentHealth <= 0)
		{
			currentHealth = 0;
			alive = false;
			changeToLevel(0);
		}
	}
	
	public int towerNumber()
	{
		return towerNumber;
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta)
	{
//		super.draw(batch);
		if (this.isAlive() && currentHealth < maxHealth / 2 && Gdx.app.getType() != ApplicationType.Android)
		{
			fire.draw(batch, delta * 0.5f);
			if  (fire.isComplete())
				fire.start();
		}
		
		TextureRegion current;
		stateTime += delta;//Gdx.graphics.getDeltaTime();
		current = buildingAnimation.getAnimation(0).getKeyFrame(stateTime * 0.5f, true);
		Vector2 point = buildingAnimation.getFeet(0);
		
		batch.draw(current, xCoord - point.x * .5f, yCoord - point.y * .5f, current.getRegionWidth() * .5f, current.getRegionHeight() * .5f);
//		batch.draw(current, xCoord - point.x, yCoord - point.y);
	}
	
//	private static Animation loadAnimation(int x, int y, int w, int h, int count, boolean flipX, int team)
//	{
//		TextureRegion[] frames = new TextureRegion[count];
//		
//		TextureRegion temp = new TextureRegion(spriteSheet[team], x, y, w * count, h);
//		TextureRegion[][] tmp = temp.split(w, h);
//		
//		for (int i = 0; i < count; i++)
//		{
//			frames[i] = tmp[0][i];
//			if (flipX)// || flipY)
//				frames[i].flip(flipX, false); //flipY);
//		}
//		
//		Animation tempAnimation = new Animation(.1f, frames);
//		tempAnimation.setPlayMode(Animation.LOOP_PINGPONG);
//		return tempAnimation;
//	}
	
	public void upgrade()
	{
		changeToLevel(++level);
		alive = true;
//		buildingAnimation = everything.getBuildingAnimation(buildingStructure.animation(level) + team);
//		alive = true;
//		currentHealth = maxHealth;
//		currentSprite = sprites.get(0);
//		currentSprite.setSize(150, 150);//(float)(currentSprite.getWidth() * (1 + level * .5)), (float)(currentSprite.getHeight() * (1 + level * .5)));
	}
	
	private void changeToLevel(int level)
	{
		super.changeToLevel(level, buildingStructure);
		this.level = level;
		buildingAnimation = everything.getBuildingAnimation(buildingStructure.animation(level) + team);
		currentHealth = maxHealth;
		loadProjectile(buildingStructure);
	}
	
//	public static void loadSprites()
//	{
//		sprites = new ArrayList<Sprite>();
//		//ArrayList<Sprite> unitAnimation = new ArrayList<Animation>();
//		sprites.add(loadSprite(424, 0, 47, 96, false, 0));//, false));
//	}
	
//	private static Sprite loadSprite(int x, int y, int w, int h, boolean flipX, int team) //boolean flipY)
//	{
//		TextureRegion region = new TextureRegion(spriteSheet[team], x, y, w, h);
//		
//		Sprite temp = new Sprite(region); 
//		
//		if (flipX)// || flipY)
//			temp.flip(flipX, false);//flipY);
//		
//		return temp;
//	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
//	protected abstract void attack();
}
