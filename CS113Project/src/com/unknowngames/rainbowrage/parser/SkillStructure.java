package com.unknowngames.rainbowrage.parser;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class SkillStructure 
{
	////////////////////////////
	public 
		Integer	aoe,
				targeting,
				targetCount,
				effect,
				effectAmount,
				castTime,
				trigger,
				effectTick,
				cost,
				duration,
				buff,
				priority;
	////////////////////////////
	public 
		Boolean damageSplit,
				additive,
				continuous;
	////////////////////////////
	public 
		Float	speed;
	////////////////////////////
	public 
		String	travelEffect,
				detonateEffect,
				affectedEffect;
	////////////////////////////	
	public 
		String	name;
	
	public SkillStructure()
	{
		
	}
	
	public SkillStructure(SkillStructure s)
	{
		aoe = s.aoe;
		targeting = s.targeting;
		targetCount = s.targetCount;
		effect = s.effect;
		effectAmount = s.effectAmount;
//		castTime = s.castTime;
		trigger = s.trigger;
		effectTick = s.effectTick;
//		cost = new ArrayList<Integer>();
//		cost.addAll(s.cost);
		duration = s.duration;
		buff = s.buff;
		damageSplit = s.damageSplit;
		additive = s.additive;
		continuous = s.continuous;
		speed = s.speed;
		travelEffect = s.travelEffect;
		detonateEffect = s.detonateEffect;
		affectedEffect = s.affectedEffect;
		priority = s.priority;
//		name = new ArrayList<String>();
//		name.addAll(s.name);
	}
//	public void init()
//	{
//		aoe.add(0);
//		targetTeam.add(0);
//		effect.add(0);
//		effectAmount.add(0);
//		state.add(0);
//		castTime.add(0);
//		cooldown.add(0);
//		trigger.add(0);
//		effectTick.add(0);
//		travelTime.add(0);
//		cost.add(0);
//		duration.add(0);
//		damageSplit.add(false);
//		additive.add(false);
//		continuous.add(false);
//		speed.add(0f);
//		sprite.add("empty");
//		cast.add("empty");
//		travel.add("empty");
//		detonateEffect.add("empty");
//		affected.add("empty");
//		name.add("");
//	}
}
