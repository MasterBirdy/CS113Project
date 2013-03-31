package com.awesomeincorporated.unknowndefense.skill;

import com.awesomeincorporated.unknowndefense.EverythingHolder;
import com.awesomeincorporated.unknowndefense.entity.Actor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SkillEffect 
{
	public int 			effect,			// Type of effect (Damage, heal, slow, etc.)
						effectAmount,   // The amount of the effect (Damage, heal, slow, etc.)	
//						effectTick,     // Ticks between effect applying
						ticksLeft;		// Ticks until effect is removed		
	public boolean 		additive,
						continuous, // For particle effect
						alive = false;
	public ParticleEffect 	affected = new ParticleEffect();
	public Actor 			target;
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
	
	public SkillEffect(Skill s, Actor a)
	{
		target = a;
		effect = s.effect;
		if (additive == false)
			effectAmount = s.effectAmount / s.totalAffected();
		else
			effectAmount = s.effectAmount;
		if (s instanceof PassiveSkill)
			ticksLeft = 2;
		else
			ticksLeft = ((TargetedSkill)s).duration;
		continuous = s.continuous;
//		affected.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/BloodEffectAndroid.p" : "data/BloodEffect.p")), Gdx.files.internal("images"));
//		affected.setPosition(400, 300);
//		
//		affected.start();
		affected.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
//		affected = s.affected;
		affected.setPosition(target.xCoord(), target.yCoord());
		affected.start();
		alive = true;
	}
	
	public void update()
	{
		if (!alive)
			return;
		System.out.println("Updating Skill Effect");
		if (!target.isAlive() || --ticksLeft < 0)
		{
			kill();
			return;
		}
		
		affected.setPosition(target.xCoord(), target.yCoord());
		causeEffect();
	}
	
	public void draw(SpriteBatch batch)
	{
		if (!alive)
			return;
//		if (!alive || affected.isComplete())
//			return;
		System.out.println("Drawing Skill Effect");
		if (affected.isComplete())
			affected.start();
		affected.draw(batch);
	}
	
	public void causeEffect()
	{
		switch(effect)
		{
			case 0:
				target.takeDamage(effectAmount);
			break;
			case 1:
				target.takeDamage(-effectAmount);
			break;
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
		System.out.println("KILLLING WTF");
		alive = false;
//		affected.dispose();
	}
}
