package com.unknowngames.rainbowrage.parser;

import java.util.ArrayList;

public class SkillInjectorStructure extends SkillStructure
{
	public String skill;
	
	public SkillInjectorStructure()
	{
		
	}
	
	public SkillInjectorStructure(SkillInjectorStructure s)
	{
		super(s);
		skill = s.skill;
	}
}
