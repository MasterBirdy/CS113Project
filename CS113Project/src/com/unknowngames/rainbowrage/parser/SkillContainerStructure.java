package com.unknowngames.rainbowrage.parser;

import java.util.ArrayList;

public abstract class SkillContainerStructure
{
	////////////////////////////
	public 
		Integer	targetType,
				targetRange,
				castTime,
				cooldown,
				trigger,
				cost,
				relativeX,
				relativeY;
	////////////////////////////
	public
		String	castEffect,
				castSound;
	////////////////////////////	
	public 
		String	name,
				description,
				icon;
	////////////////////////////
	public 
		ArrayList<String>	skills;
	
	public SkillContainerStructure()
	{		
	}
	
	public SkillContainerStructure(SkillContainerStructure s)
	{
		targetType = s.targetType;
		targetRange = s.targetRange;
		castTime = s.castTime;
		cooldown = s.cooldown;
		trigger = s.trigger;
		cost = s.cost;
		relativeX = s.relativeX;
		relativeY = s.relativeY;
		castEffect = s.castEffect;
		castSound = s.castSound;
		name = s.name;
		description = s.description;
		icon = s.icon;
		
		skills = new ArrayList<String>();
		skills.addAll(s.skills);
	}
}
