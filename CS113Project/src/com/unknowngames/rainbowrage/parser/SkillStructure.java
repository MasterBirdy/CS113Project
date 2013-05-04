package com.unknowngames.rainbowrage.parser;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class SkillStructure 
{
	////////////////////////////
	public 
		ArrayList<Integer>	aoe,
							targetTeam,
							effect,
							effectAmount, 
							state,
							castTime,
							cooldown,
							trigger,
							effectTick,
							travelTime,
							cost,
							duration;
	////////////////////////////
	public 
		ArrayList<Boolean> 	damageSplit,
							additive,
							continuous;
	////////////////////////////
	public 
		ArrayList<Float>	speed;
	////////////////////////////
	public 
		ArrayList<String>	sprite,
							cast, 
							travel, 
							detonateEffect,
							affected;
	////////////////////////////	
	public 
		ArrayList<String>	name;
	
	public SkillStructure()
	{
		
	}
	
	public SkillStructure(SkillStructure s)
	{
		aoe = new ArrayList<Integer>();
		aoe.addAll(s.aoe);
		targetTeam = new ArrayList<Integer>();
		targetTeam.addAll(s.targetTeam);
		effect = new ArrayList<Integer>();
		effect.addAll(s.effect);
		effectAmount = new ArrayList<Integer>();
		effectAmount.addAll(s.effectAmount);
//		state = new ArrayList<Integer>();
//		state.addAll(s.state);
//		castTime = new ArrayList<Integer>();
//		castTime.addAll(s.castTime);
		cooldown = new ArrayList<Integer>();
		cooldown.addAll(s.cooldown);
		trigger = new ArrayList<Integer>();
		trigger.addAll(s.trigger);
		effectTick = new ArrayList<Integer>();
		effectTick.addAll(s.effectTick);
		travelTime = new ArrayList<Integer>();
		travelTime.addAll(s.travelTime);
//		cost = new ArrayList<Integer>();
//		cost.addAll(s.cost);
		duration = new ArrayList<Integer>();
		duration.addAll(s.duration);
		damageSplit = new ArrayList<Boolean>();
		damageSplit.addAll(s.damageSplit);
		additive = new ArrayList<Boolean>();
		additive.addAll(s.additive);
		continuous = new ArrayList<Boolean>();
		continuous.addAll(s.continuous);
		speed = new ArrayList<Float>();
		speed.addAll(s.speed);
		sprite = new ArrayList<String>();
		sprite.addAll(s.sprite);
		cast = s.cast;
		travel = new ArrayList<String>();
		travel.addAll(s.travel);
		detonateEffect = new ArrayList<String>();
		detonateEffect.addAll(s.detonateEffect);
		affected = new ArrayList<String>();
		affected.addAll(s.affected);
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
