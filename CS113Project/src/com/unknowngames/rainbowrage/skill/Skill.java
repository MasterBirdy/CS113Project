package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.entity.Building;
import com.unknowngames.rainbowrage.entity.Entity;
import com.unknowngames.rainbowrage.parser.SkillStructure;

public abstract class Skill extends Entity 
{
	int 			aoe,
					targetTeam,
					effect,			// Type of effect (Damage, heal, slow, etc.)
					effectAmount,   // The amount of the effect (Damage, heal, slow, etc.)	
					effectTick,		// Ticks between effect applying
					tickCounter;
	boolean 		damageSplit, 	// If damage is split among enemies
					additive,		// If it stacks
					active,			// If active
					continuous;		// For particle effects
	ParticleEffect 	travelEffect,
					detonateEffect,
					affected;
	Sound			detonateSound;
	Actor 			caster;
	String 			name;
	
	ArrayList<Actor> tempActor = new ArrayList<Actor>();
	
	public Skill(SkillStructure s, Actor c)
	{
		int level = 0;
		xCoord(c.xCoord() - 8);
		yCoord(c.yCoord() + (c instanceof Building ? 50 : 0));
		aoe = s.aoe.get(0);
		if (s.targetTeam.get(0) == 0)
		{
			targetTeam = 0;
		}
		else if (s.targetTeam.get(0) == 1)
		{
			if (c.team() == 1)
				targetTeam = 2;
			else
				targetTeam = 1;
		}
		else
		{
			if (c.team() == 1)
				targetTeam = 1;
			else
				targetTeam = 2;
		}
//		targetTeam = s.targetTeam.get(0);
		effect = s.effect.get(level);
		effectAmount = s.effectAmount.get(level);
		effectTick = s.effectTick.get(level);
//		tickCounter = effectTick;
		damageSplit = s.damageSplit.get(level);
		additive = s.additive.get(level);
		continuous = s.continuous.get(level);
		travelEffect = everything.getEffect(s.travel.get(level));
		detonateEffect = everything.getEffect(s.detonateEffect.get(level));
		affected = new ParticleEffect();
		affected.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
//		affected = everything.getEffect(s.affected);
		
//		detonateEffect = s.detonateEffect;
//		affected = s.affected;
//		name = s.name.get(0);
		caster = c;
		alive = true;
	}
	
	// Returns list of actors in range of aoe
	public abstract ArrayList<Actor> inRange();
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
		applyToTargets();
		detonateEffect.setPosition(xCoord(), yCoord());
		detonateEffect.start();
		this.addParticle(detonateEffect);
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
		tempActor = inRange();
		if (tempActor == null)
			return;
//		for (Actor a : inRange())
		for (int i = 0; i < tempActor.size(); i++)
			if (tempActor.get(i).isAlive())
				tempActor.get(i).takeSkillEffect(new SkillEffect(this, tempActor.get(i), tempActor.size()));
	}
}
