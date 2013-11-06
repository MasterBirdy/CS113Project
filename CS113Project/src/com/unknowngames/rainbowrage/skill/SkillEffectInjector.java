package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.unknowngames.rainbowrage.entity.Actor;

public class SkillEffectInjector extends SkillEffect
{
	private ArrayList<Skill> skills = new ArrayList<Skill>();
	
	public SkillEffectInjector(SkillInjector skill, Actor a, int count)
	{
		super(skill, a, count);
		for (Skill s : skill.skills())
		{
			skills.add(s);
		}
	}
	
	@Override
	public void causeEffect()
	{
		
	}
	
	public ArrayList<Skill> getSkills()
	{
		return skills;
	}
}
