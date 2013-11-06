package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.parser.TravelingSkillContainerStructure;

public class TravelingSkillContainer extends SkillContainer
{
	float speedX, speedY, speed, angle;
	int travelTime, ticksLeft;
	TextureRegion skillSprite;
	ParticleEffect travelEffect;

	public TravelingSkillContainer(SkillSpawner skillSpawner,
			TravelingSkillContainerStructure t, Actor target, ArrayList<Skill> extraSkills)
	{
		super(skillSpawner, t, target, extraSkills);
		speed = t.speed;
		// System.out.println("TravelTime: " + t.travelTime);
		travelTime = t.travelTime;
		ticksLeft = t.travelTime;
		// skillSprite = EverythingHolder.getObjectTexture("arrow");
		skillSprite = EverythingHolder.getObjectTexture(t.skillSprite);
		angle = (float) getAngleToPoint(targetX, targetY);
		travelEffect = EverythingHolder.getEffect(t.travelEffect);
		// if (travelEffect != null)
		// travelEffect.start();
	}

	public TravelingSkillContainer(SkillSpawner skillSpawner,
			TravelingSkillContainerStructure t, float targetX, float targetY, 
			ArrayList<Skill> extraSkills)
	{
		super(skillSpawner, t, targetX, targetY);
		speed = t.speed;
		travelTime = t.travelTime;
		ticksLeft = t.travelTime;
		skillSprite = EverythingHolder.getObjectTexture(t.skillSprite);
		angle = (float) getAngleToPoint(targetX, targetY);
		travelEffect = EverythingHolder.getEffect(t.travelEffect);
		// if (travelEffect != null)
		// travelEffect.start();
	}

	// public TravelingSkillContainer(Actor caster, Actor target,
	// TravelingSkillContainerStructure t)
	// {
	// super(caster, target, t);
	// speed = t.speed;
	// System.out.println("TravelTime: " + t.travelTime);
	// travelTime = t.travelTime;
	// ticksLeft = t.travelTime;
	// // skillSprite = EverythingHolder.getObjectTexture("arrow");
	// skillSprite = EverythingHolder.getObjectTexture(t.skillSprite);
	// angle = (float) getAngleToPoint(targetX, targetY);
	// }
	//
	// public TravelingSkillContainer(Actor caster,
	// TravelingSkillContainerStructure t, float targetX, float targetY)
	// {
	// super(caster, t, targetX, targetY);
	// speed = t.speed;
	// travelTime = t.travelTime;
	// ticksLeft = t.travelTime;
	// skillSprite = EverythingHolder.getObjectTexture(t.skillSprite);
	// angle = (float) getAngleToPoint(targetX, targetY);
	// }

	@Override
	public void update()
	{
		// Don't update if null
		// System.out.println("Hmmm" + xCoord() + ":" + yCoord());
		if (!alive)
			return;

		// Detonate at end of its life (such as when chasing an enemy or fired
		// blindly)
		if (--ticksLeft < 0 && travelTime > 0)
		{
			detonate();
			return;
		}
		for (Skill s : skills)
		{
			s.update();
			s.xCoord(xCoord());
			s.yCoord(yCoord());
		}

		// For tracking skills, try to set target location to target's location
		if (target != null)
		{
			if (target.isAlive())
			{
				targetX = target.xCoord();
				targetY = target.yCoord();
			}
			else
				target = null;
		}

		// System.out.println(alive + ":" + speed + ":" + ticksLeft + ":" +
		// travelTime + ":" + targetX + ":" + targetY);

		// Detonate when close enough to target, otherwise move towards it
		if (!closeEnough())
		{
			// System.out.println("!CloseEnough");
			float distance = getDistance(targetX, targetY);
			speedX = speed * ((targetX - xCoord()) / distance);
			speedY = speed * ((targetY - yCoord()) / distance);

			xCoord(this.xCoord() + speedX);
			yCoord(this.yCoord() + speedY);
		}
		else
		{
			// System.out.println("CloseEnough!");
			xCoord(targetX);
			yCoord(targetY);
			detonate();
		}
	}

	public boolean closeEnough()
	{
		if (getDistanceSquared(targetX, targetY) < speed * speed)
			return true;
		return false;
	}

	@Override
	public void draw(SpriteBatch batch, float delta)
	{
		// System.out.println("Hmmm =(" + xCoord() + ":" + yCoord() + ":" +
		// skillSprite.getRegionX());
		if (!alive)
			return;
		// System.out.println("Drawing Targeted SKill");
		if (travelEffect != null)
		{
			if (travelEffect.isComplete())
				travelEffect.start();
			travelEffect.setPosition(xCoord() + 10, yCoord() + 10);
			travelEffect.draw(batch, delta * 0.5f);
		}

		if (skillSprite != null)
		{
			// System.out.println("SkillSprite isn't empty: " + xCoord() + ":" +
			// yCoord());
			batch.draw(skillSprite, xCoord(), yCoord(), 8, 8, 16, 16, 1, 1,
					angle);
		}
		for (Skill s : skills)
			s.draw(batch, delta);

	}
}
