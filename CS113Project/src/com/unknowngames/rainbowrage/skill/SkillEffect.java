package com.unknowngames.rainbowrage.skill;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.entity.Actor;

public class SkillEffect 
{
	public int 			effect,				// Type of effect (Damage, heal, slow, etc.)
						effectAmount,   	// The amount of the effect (Damage, heal, slow, etc.)	
						effectTick,     	// Ticks between effect applying
						effectTickCounter,	// Ticks since left until effect occurs
						ticksLeft,			// Ticks until effect is removed
						priority;			// Priorty of effect during each tick
	public boolean 		additive,
						continuous, 		// For particle effect
						alive = false;
	public ParticleEffect 	affected; //= new ParticleEffect();
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
		effectTickCounter = 0; //effectTick;
//		if (s instanceof PassiveSkill)
//			ticksLeft = 1;
//		else
			ticksLeft = s.duration; //((TargetedSkill)s).duration;
		continuous = s.continuous;
		priority = s.priority;
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
	
	public SkillEffect(SkillEffect s)
	{
		target = s.target;
		caster = s.caster;
		effect = s.effect;
		effectAmount = s.effectAmount;
		effectTick = s.effectTick;
		effectTickCounter = 0;
		ticksLeft = s.ticksLeft;
		continuous = s.continuous;
		priority = s.priority;
		alive = true;
	}
	
	public void update()
	{
		if (!alive)
			return;
		
		if (ticksLeft == -1)
		{
			causeEffect();
			return;
		}
//		System.out.println("Updating Skill Effect");
		
//		affected.setPosition(target.xCoord(), target.yCoord());
		if (--effectTickCounter < 0)
		{
			effectTickCounter = effectTick;
			causeEffect();
		}
		
		if (!target.isAlive() || --ticksLeft == 0)
		{
			kill();
			return;
		}
	}
	
	public void draw(SpriteBatch batch, float delta)
	{
		if (!alive)
			return;
//		if (!alive || affected.isComplete())
//			return;
//		System.out.println("Drawing Skill Effect");
//		if (affected.isComplete())
//			affected.start();
//		affected.draw(batch, delta);
	}
	
	public void causeEffect()
	{
		switch(effect)
		{
			case 0:										// Damage
//				System.out.println("Damage");
				target.takeDamage(effectAmount);
				break;
			case 1:										// Heal
//				System.out.println("Heal");
				target.heal(effectAmount);
				break;
			case 2:										// Stun
				target.stun(effectAmount);
				break;
			case 3:										// Attack speed boost
				target.attackSpeedBoost(effectAmount);
				break;
			case 4:										// Attack damage boost
				target.attackDamageBoost(effectAmount);
				break;
			case 5:										// Attack range boost
				target.attackRangeBoost(effectAmount);
				break;
			case 6:										// Lifesteal
//				System.out.println("Lifesteal");
				target.takeDamage(effectAmount);
				caster.heal(effectAmount);
				break;
			case 7:										// Armor
				target.armor(effectAmount);
				break;
			case 8:										// Dodge
				target.dodge(effectAmount);
				break;
			case 9:										// Parry
				target.parry(effectAmount);
				break;
			case 10:									// Cloak
				target.invis(effectAmount);
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
//		if (affected.isComplete())// && continuous)
//			affected.start();
	}
	
	public void kill()
	{
//		System.out.println("KILLLING WTF");
		alive = false;
		if (affected != null && !affected.isComplete())
			affected.dispose();
//		affected.dispose();
	}
	
	public static Comparator<SkillEffect> PriorityComparator = new Comparator<SkillEffect>()
	{
		public int compare(SkillEffect a1, SkillEffect a2)
		{
			if (a1 == null)
			{
				if (a2 != null)
					return 1;
				return 0;
			}
			else if (a2 == null)
				return -1;
			
			int y1 = a1.priority;
			int y2 = a2.priority;
			if (y1 < y2)
				return -1;
			if (y1 > y2)
				return 1;	
			return 0;
		}
	};
}
