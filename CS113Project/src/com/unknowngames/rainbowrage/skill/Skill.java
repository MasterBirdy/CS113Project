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
		super(sc.xCoord(), sc.yCoord(), sc.team());
		caster = sc.caster;
//		int level = 0;
//		xCoord(c.xCoord() - 8);
//		yCoord(c.yCoord() + (c instanceof Building ? 50 : 0));
		aoe = s.aoe;
		targeting = s.targeting;
		targetCount = s.targetCount;
//		targetRange = s.targetRange.get(0);
//		if (s.targeting.get(0) == 0)
//		{
//			targetTeam = 0;
//		}
//		else if (s.targeting.get(0) == 1)
//		{
//			if (c.team() == 1)
//				targetTeam = 2;
//			else
//				targetTeam = 1;
//		}
//		else
//		{
//			if (c.team() == 1)
//				targetTeam = 1;
//			else
//				targetTeam = 2;
//		}
//		targetTeam = s.targetTeam.get(0);
		effect = s.effect;
		effectAmount = s.effectAmount;
		effectTick = s.effectTick;
		duration = s.duration;
//		tickCounter = effectTick;
		damageSplit = s.damageSplit;
		additive = s.additive;
		continuous = s.continuous;
		travelEffect = EverythingHolder.getEffect(s.travelEffect);
		detonateEffect = EverythingHolder.getEffect(s.detonateEffect);
//		affectedEffect = new ParticleEffect();
//		affectedEffect.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
//		affected = everything.getEffect(s.affected);
		
//		detonateEffect = s.detonateEffect;
//		affected = s.affected;
//		name = s.name.get(0);
//		caster = c;
//		target = c.getTarget();
		target = sc.target;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update()
	{
		if (effect == 8)
			extra += effectAmount;
	}
}
