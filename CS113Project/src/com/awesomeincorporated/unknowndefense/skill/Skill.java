package com.awesomeincorporated.unknowndefense.skill;

import java.util.ArrayList;

import com.awesomeincorporated.unknowndefense.EverythingHolder;
import com.awesomeincorporated.unknowndefense.entity.Actor;
import com.awesomeincorporated.unknowndefense.entity.Entity;
import com.awesomeincorporated.unknowndefense.parser.SkillStructure;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Skill extends Entity 
{
	int 			aoe,
					targetTeam,
					effect,			// Type of effect (Damage, heal, slow, etc.)
					effectAmount,   // The amount of the effect (Damage, heal, slow, etc.)	
					effectTick;		// Ticks between effect applying
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
	
	public Skill(SkillStructure s, Actor c)
	{
		xCoord(c.xCoord());
		yCoord(c.yCoord());
		aoe = s.aoe;
		if (s.targetTeam == 0)
		{
			targetTeam = 0;
		}
		else if (s.targetTeam == 1)
		{
			if (c.team() == 0)
				targetTeam = 1;
			else
				targetTeam = 2;
		}
		else
		{
			if (c.team() == 0)
				targetTeam = 2;
			else
				targetTeam = 1;
		}
		targetTeam = s.targetTeam;
		effect = s.effect;
		effectAmount = s.effectAmount;
		damageSplit = s.damageSplit;
		additive = s.additive;
		continuous = s.continuous;
		detonateEffect = everything.getEffect(s.detonateEffect);
		affected = new ParticleEffect();
		affected.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
//		affected = everything.getEffect(s.affected);
		
//		detonateEffect = s.detonateEffect;
//		affected = s.affected;
		name = s.name;
		caster = c;
		alive = true;
	}
	
	// Returns list of actors in range of aoe
	public ArrayList<Actor> inRange() 
	{
		if (aoe == 0)
		{
			ArrayList<Actor> temp = new ArrayList<Actor>();
			temp.add(caster);
			return temp;
		}
		
		return everything.team(targetTeam);
	}
	
	public void detonate()
	{
		applyToTargets();
		detonateEffect.setPosition(xCoord(), yCoord());
		detonateEffect.start();
//		travelEffect.dispose();
		alive = false;
//		detonateSound.play();
	}
	
	public void draw(SpriteBatch batch)
	{
		if (detonateEffect.isComplete())
			detonateEffect.start();
		detonateEffect.draw(batch);
//		if (alive || !detonateEffect.isComplete())
//			detonateEffect.draw(batch);
	}
	
	public int totalAffected()
	{
		return inRange().size();
	}
	
	public void applyToTargets()
	{
		System.out.println("Applying to targets (Skill)");
		for (Actor a : inRange())
			if (a.isAlive())
				a.takeSkillEffect(new SkillEffect(this, a));
	}
}
