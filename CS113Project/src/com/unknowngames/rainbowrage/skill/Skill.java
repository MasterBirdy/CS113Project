package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.entity.Building;
import com.unknowngames.rainbowrage.entity.Entity;
import com.unknowngames.rainbowrage.parser.SkillStructure;

public class Skill extends Entity 
{
	int				effect,			// Type of effect (Damage, heal, slow, etc.)
					effectAmount,   // The amount of the effect (Damage, heal, slow, etc.)	
					effectTick,		// Ticks between effect applying
					duration,
					tickCounter,
	 				aoe,
					targeting,
					targetCount,
					priority,
					extra;
//					targetRange,
	boolean 		damageSplit, 	// If damage is split among enemies
					additive,		// If it stacks
					active,			// If active
					continuous;		// For particle effects
	ParticleEffect 	travelEffect,
					detonateEffect;
//					affectedEffect;
	Sound			detonateSound;
	Actor 			caster, target;
	String 			name;
	
	public Skill(SkillStructure s, SkillContainer sc) //Actor c)
	{
		this(s, sc, sc.caster, sc.target);
		/*super(sc.xCoord(), sc.yCoord(), sc.team());
		caster = sc.caster;
		aoe = s.aoe;
		targeting = s.targeting;
		targetCount = s.targetCount;
		effect = s.effect;
		effectAmount = s.effectAmount;
		effectTick = s.effectTick;
		duration = s.duration;
		damageSplit = s.damageSplit;
		additive = s.additive;
		continuous = s.continuous;
		travelEffect = EverythingHolder.getEffect(s.travelEffect);
		detonateEffect = EverythingHolder.getEffect(s.detonateEffect);
		target = sc.target;
		alive = true;
		priority = s.priority;
		extra = 0;*/
	}
	
	public Skill(SkillStructure s, Entity e, Actor caster, Actor target)
	{
		super(e.xCoord(), e.yCoord(), e.team());
		this.caster = caster;
		aoe = s.aoe;
		targeting = s.targeting;
		targetCount = s.targetCount;
		effect = s.effect;
		effectAmount = s.effectAmount;
		effectTick = s.effectTick;
		duration = s.duration;
		damageSplit = s.damageSplit;
		additive = s.additive;
		continuous = s.continuous;
		travelEffect = EverythingHolder.getEffect(s.travelEffect);
		detonateEffect = EverythingHolder.getEffect(s.detonateEffect);
		this.target = target;
		alive = true;
		priority = s.priority;
		extra = 0;
	}
	
	// Returns list of actors in range of aoe
	public ArrayList<Actor> inRange()
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
		if (aoe == -1)							// Single target skill
		{
			if (target != null && target.isAlive() &&
			   getDistanceSquared(target) < 100)	// Projectile cast on target
					temp.add(target);
			return temp;
		}
		else									// Aoe skill
		{
			temp = everything.actorsInRange(this, aoe, targeting);

			if (temp.size() > 1 && targetCount > 0)
			{
				switch(Math.abs(targeting))
				{
				case 4:
				case 5:
					Collections.sort(temp, Actor.HealthComparator);
					break;
				}
				return new ArrayList<Actor>(temp.subList(0, (temp.size() > targetCount - 1 ? targetCount - 1 : temp.size() - 1)));
			}
		}
		
		return temp;
	}
//	public ArrayList<Actor> inRange() 
//	{
//		if (aoe == 0)
//		{
//			ArrayList<Actor> temp = new ArrayList<Actor>();
//			temp.add(caster);
//			return temp;
//		}
//		else
//		{
//			
//		}
//		
//		return everything.team(targetTeam);
//	}
	
	public void detonate()
	{
//		System.out.println("Detonate Skill!");
		applyToTargets();
		
		if (detonateEffect != null)
		{
			detonateEffect.setPosition(xCoord(), yCoord());
			detonateEffect.start();
			this.addParticle(detonateEffect);
		}
//		travelEffect.dispose();
		alive = false;
//		detonateSound.play();
	}
	
	public void draw(SpriteBatch batch)
	{
//		if (detonateEffect.isComplete())
//			detonateEffect.start();
//		detonateEffect.draw(batch);
//		if (alive || !detonateEffect.isComplete())
//			detonateEffect.draw(batch);
	}
	
	public int totalAffected()
	{
		return inRange().size();
	}
	
	public void applyToTargets()
	{
//		for (Actor a : inRange())
//			if (a.isAlive())
//				a.takeSkillEffect(new SkillEffect(this, a, targetActors.size()));
		ArrayList<Actor> targetActors = inRange();
		if (targetActors == null)
			return;
		
		for (Actor a : targetActors)
			if (a.isAlive())
				a.takeSkillEffect(new SkillEffect(this, a, targetActors.size()));
		
//		for (int i = 0; i < targetActors.size(); i++)
//			if (targetActors.get(i).isAlive())
//				targetActors.get(i).takeSkillEffect(new SkillEffect(this, targetActors.get(i), targetActors.size()));
	}

	@Override
	public void draw(SpriteBatch batch, float delta)
	{
		if (travelEffect != null)
		{
			travelEffect.draw(batch, delta);
		}
	}

	@Override
	public void update()
	{
		if (travelEffect != null)
		{
			if (travelEffect.isComplete())
				travelEffect.start();
			travelEffect.setPosition(xCoord(), yCoord());
		}
		if (effect == 8)
			extra += effectAmount;
	}
}
