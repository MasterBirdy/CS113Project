package com.unknowngames.rainbowrage.skill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.entity.Actor;

public class SkillEffect 
{
	public int 			effect,			// Type of effect (Damage, heal, slow, etc.)
						effectAmount,   // The amount of the effect (Damage, heal, slow, etc.)	
						effectTick,     // Ticks between effect applying
						effectTickCounter,
						ticksLeft;		// Ticks until effect is removed		
	public boolean 		additive,
						continuous, // For particle effect
						alive = false;
	public ParticleEffect 	affected = new ParticleEffect();
	public Actor 			target, caster;
	public String 			name;
	static EverythingHolder everything;
	
	public void tempEffect()
	{
//		affected.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
//		affected.start();
	}
	
	public SkillEffect()
	{
	}
	
	public SkillEffect(Skill s, Actor a, int count)
	{
		target = a;
		caster = s.caster;
		effect = s.effect;
		if (s.damageSplit == true)
			effectAmount = s.effectAmount / count;
		else
			effectAmount = s.effectAmount;
		effectTick = s.effectTick;
		effectTickCounter = effectTick;
		if (s instanceof PassiveSkill)
			ticksLeft = 1;
		else
			ticksLeft = ((TargetedSkill)s).duration;
		continuous = s.continuous;
//		affected.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/BloodEffectAndroid.p" : "data/BloodEffect.p")), Gdx.files.internal("images"));
//		affected.setPosition(400, 300);
//		
//		affected.start();
//		affected.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
////		affected = s.affected;
//		affected.setPosition(target.xCoord(), target.yCoord());
//		affected.start();
		alive = true;
	}
	
	public void update()
	{
		if (!alive)
			return;
//		System.out.println("Updating Skill Effect");
		if (!target.isAlive() || --ticksLeft < 0)
		{
			kill();
			return;
		}
		
		affected.setPosition(target.xCoord(), target.yCoord());
		if (--effectTickCounter < 0)
		{
			effectTickCounter = effectTick;
			causeEffect();
		}
	}
	
	public void draw(SpriteBatch batch, float delta)
	{
		if (!alive)
			return;
//		if (!alive || affected.isComplete())
//			return;
//		System.out.println("Drawing Skill Effect");
		if (affected.isComplete())
			affected.start();
		affected.draw(batch, delta);
	}
	
	public void causeEffect()
	{
		switch(effect)
		{
			case 0:
				target.takeDamage(effectAmount);
				break;
			case 1:
				target.heal(effectAmount);
				break;
			case 2:
				target.stun(effectAmount);
				break;
			case 3:
				target.attackSpeedBoost(effectAmount);
				break;
			case 4:
				target.attackDamageBoost(effectAmount);
				break;
			case 5:
				target.attackRangeBoost(effectAmount);
				break;
			case 6:
				target.takeDamage(effectAmount);
				caster.heal(effectAmount);
				break;
//			case 4:
//				damageBoost += skill.effectAmount;
//				break;
//			case 5:
//				attackSpeedBoost += skill.effectAmount;
//				break;
//			case 6:
//				attackRangeBoost += skill.effectAmount;
//				break;
		}
		
		continueParticleEffect();
	}
	
	public void continueParticleEffect()
	{
		if (affected.isComplete())// && continuous)
			affected.start();
	}
	
	public void kill()
	{
//		System.out.println("KILLLING WTF");
		alive = false;
//		affected.dispose();
	}
}
