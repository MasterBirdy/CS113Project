package com.awesomeincorporated.unknowndefense.skill;

import com.awesomeincorporated.unknowndefense.entity.Actor;
import com.awesomeincorporated.unknowndefense.parser.SkillStructure;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TargetedSkill extends Skill 
{
	int 	duration,
			cooldown;
	float 	speed,
			speedX = 0,
			speedY = 0,
			targetX,
			targetY;
	Actor 	target;
	
	public TargetedSkill(SkillStructure s, Actor c, Actor t)
	{
		super(s, c);
		target = t;
		duration = s.duration;
		speed = s.speed;
		targetX = t.xCoord();
		targetY = t.yCoord();
		travelEffect = everything.getEffect(s.travel);
		travelEffect.start();
//		travelEffect = new ParticleEffect(s.affected);
	}
	
	public void update()
	{
		System.out.println("Updating");
		if (!alive)
			return;
		if (target.isAlive())
		{
			targetX = target.xCoord();
			targetY = target.yCoord();
		}
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
		System.out.println("Updating");
		if (alive)
			travelEffect.draw(batch);
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
			target.takeSkillEffect(new SkillEffect(this, target));
			return;
		}
		for (Actor a : inRange())
			a.takeSkillEffect(new SkillEffect(this, a));
	}
}
