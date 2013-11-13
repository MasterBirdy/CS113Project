package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.parser.SkillStructure;

public class SkillEffectInjected extends SkillEffect
{
	private Skill skill;
	
	public SkillEffectInjected(SkillInjector skill, Actor a, int count)
	{
		super(skill, a, count);
		System.out.println("Starting injection");
		if (skill.getSkill() != null)
		{
			System.out.println("Creating injected skill now");
			this.skill = new Skill(skill.skill, skill.target, skill.caster, skill.target);
//			SkillStructure skillStruct = everything.getSkill(skill.gets)getSkill(skill.getSkill());
			
//			System.out.println("Skill: " + s);
//			skill = new Skill(everything.getSkill(skill.getSkill()), skill);
			// skills.add(new Skill(everything.getSkill(s), caster,
			// target));
		}
//		this.skill = new Skill(skill.getSkill());
//		this.skill = skill.getSkill();
//		for (Skill s : skill.skills())
//		{
//			skills.add(s);
//		}
	}
	
	@Override
	public void causeEffect()
	{		
		if (skill != null)
		{
			System.out.println("No skill =(");
			skill.xCoord(target.xCoord());
			skill.yCoord(target.yCoord());
			skill.detonate();
		}
	}
	
	@Override
	public void update()
	{
		if (!alive)
			return;
		
		if (this.effectTickCounter == -2)
		{
			if (!target.isAlive())
			{
				causeEffect();
				kill();
			}
			return;
		}
		super.update();
	}
	
	public Skill getSkill()
	{
		return skill;
	}
}
