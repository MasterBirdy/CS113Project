package com.unknowngames.rainbowrage.parser;

import java.util.ArrayList;

public class SkillInjectorStructure extends SkillStructure
{
	public ArrayList<String> skills = new ArrayList<String>();
	
	public SkillInjectorStructure(SkillInjectorStructure s)
	{
		super(s);
		skills = s.skills;
	}
}
