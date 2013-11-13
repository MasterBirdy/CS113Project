package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.parser.SkillInjectorStructure;
import com.unknowngames.rainbowrage.parser.SkillStructure;

public class SkillInjector extends Skill
{
	SkillStructure skill;
	
	public SkillInjector(SkillInjectorStructure s, SkillContainer sc)
	{
		super(s, sc);
		System.out.println(s.skill);
		skill = everything.getSkill(s.skill);
	}
	
	public SkillStructure getSkill()
	{
		return skill;
	}
	
	@Override
	public void applyToTargets()
	{
		ArrayList<Actor> targetActors = inRange();
		if (targetActors == null)
			return;
		
		for (Actor a : targetActors)
			if (a.isAlive())
				a.takeSkillEffect(new SkillEffectInjected(this, a, targetActors.size()));
		
//		for (int i = 0; i < targetActors.size(); i++)
//			if (targetActors.get(i).isAlive())
//				targetActors.get(i).takeSkillEffect(new SkillEffect(this, targetActors.get(i), targetActors.size()));
	}
}
