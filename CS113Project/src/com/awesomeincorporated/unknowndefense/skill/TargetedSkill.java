package com.awesomeincorporated.unknowndefense.skill;

import java.util.ArrayList;

import com.awesomeincorporated.unknowndefense.entity.Actor;
import com.awesomeincorporated.unknowndefense.entity.Hero;
import com.awesomeincorporated.unknowndefense.parser.SkillStructure;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TargetedSkill extends Skill 
{
	int 	duration,
			cooldown,
			ticksLeft = 40;
	float 	speed,
			speedX = 0,
			speedY = 0,
			targetX,
			targetY;
	Actor 	target;
	static TextureRegion spellImage;
//	ParticleEffect part;
	
	public TargetedSkill(SkillStructure s, Actor c, Actor t)
	{
		super(s, c);
		if (spellImage == null)
		{
			spellImage = new TextureRegion(new Texture(Gdx.files.internal("images/"+s.sprite.get(0))), 0, 0, 16, 16);
//			spellImage = new TextureRegion(new Texture(Gdx.files.internal("images/cannonprojectile.png")), 0, 0, 16, 16);
		}
		target = t;
		duration = s.duration.get(0);
		speed = s.speed.get(0);
		if (t != null)
		{
			targetX = t.xCoord();
			targetY = t.yCoord();
		}
		else
		{
			System.out.println("No target");
			targetX = c.xCoord();
			targetY = c.yCoord();
			switch (((Hero)c).getDirection())
			{
				case 0:
					targetY -= 1000;
					break;
				case 1:
					targetX += 1000;
					break;
				case 2:
					targetY += 1000;
					break;
				case 3:
					targetX -= 1000;
					break;
			}
		}
//		part = new ParticleEffect();
//		part.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/BloodEffectAndroid.p" : "data/BloodEffect.p")), Gdx.files.internal("images"));
//		part.setPosition(100, 100);
//		part = everything.getEffect("fireballexplosion");
//		fire.setPosition(400, 10);
//		fire.start();
//		travelEffect = everything.getEffect(s.travel);
//		part.start();
//		travelEffect = new ParticleEffect(s.affected);
	}
	
	@Override
	public ArrayList<Actor> inRange() 
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
		if (aoe == 0)
			temp.add(target);
		else
			for (Actor a : everything.team(targetTeam))
				if (this.getDistanceSquared(a) < aoe * aoe)
					temp.add(a);
		
		return temp;
//		return everything.team(targetTeam);
	}
	
	public void update()
	{
//		System.out.println("Updating TargetedSkill");
		if (!alive)
			return;
		if (--ticksLeft < 0)
			detonate();
//		travelEffect.setPosition(xCoord(), yCoord());
		if (target != null && target.isAlive())
		{
			targetX = target.xCoord();
			targetY = target.yCoord();
		}
		if (target != null && !target.isAlive())
			target = null;
		if (!closeEnough())
		{
			float distance = getDistance(targetX, targetY);
			speedX = speed * ((targetX - xCoord()) / distance);
			speedY = speed * ((targetY - yCoord()) / distance);

			this.xCoord(this.xCoord() + speedX);
			this.yCoord(this.yCoord() + speedY);
		}
		else
		{
			detonate();
		}
	}
	
	public void draw(SpriteBatch batch)
	{
		if (!alive)
			return;
//		System.out.println("Drawing Targeted SKill");
		batch.draw(spellImage, xCoord(), yCoord());
		if (travelEffect.isComplete())
			travelEffect.start();
		travelEffect.setPosition(xCoord(), yCoord());
		travelEffect.draw(batch, 0.01f);
//		if (part.isComplete())
//			part.start();
//		part.setPosition(xCoord(), yCoord());
//		part.draw(batch, 0.01f);
	}
	
	public boolean closeEnough()
	{
		if (getDistanceSquared(targetX, targetY) < speed * speed)
			return true;
		return false;
	}
	
	@Override
	public void applyToTargets()
	{
		if (aoe == 0)
		{
			if (target != null && target.isAlive())
				target.takeSkillEffect(new SkillEffect(this, target));
			return;
		}
		for (Actor a : inRange())
		{
			a.takeSkillEffect(new SkillEffect(this, a));
		}
	}
}
