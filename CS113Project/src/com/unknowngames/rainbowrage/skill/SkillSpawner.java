package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.parser.InstantSkillContainerStructure;
import com.unknowngames.rainbowrage.parser.SkillContainerStructure;
import com.unknowngames.rainbowrage.parser.TravelingSkillContainerStructure;

public class SkillSpawner extends BaseClass
{
	SkillContainerStructure skillContainerStructure;
	int targetType,
		cooldown,
		count,
		trigger,
		cost,
		relativeX,
		relativeY;
	
	float range;
	
	String castSound,
		   castEffect;
	
	Actor caster;
	
	public SkillSpawner(Actor c, SkillContainerStructure s)
	{
		caster = c;
		targetType = s.targetType;
		cooldown = s.cooldown;
		count = 0; //s.cooldown;
		range = s.targetRange;
		trigger = s.trigger;
		cost = s.cost;
		if (s instanceof InstantSkillContainerStructure)
			skillContainerStructure = new InstantSkillContainerStructure(s);
		else
			skillContainerStructure = new TravelingSkillContainerStructure((TravelingSkillContainerStructure)s);
	}
	
	public float xCoord()
	{
		return caster.xCoord() + relativeX;
	}
	
	public float yCoord()
	{
		return caster.yCoord() + relativeY;
	}
	
	public int team()
	{
		return caster.team();
	}
	
	public Actor getCaster()
	{
		return caster;
	}
	
	public int getTrigger()
	{
		return trigger;
	}
	
	public void update()
	{		
		if (trigger == 0)
			cast();
		--count;
	}
	
	public int getCooldown()
	{
		return count;
	}
	
	public boolean cast()
	{
		if ((cooldown != -1 && count < 0) || (cooldown == -1 && count == -1))
		{
			Actor target = inRange();
			if (target != null)
			{
				if (skillContainerStructure instanceof InstantSkillContainerStructure)
					everything.add(new InstantSkillContainer(this, (InstantSkillContainerStructure)skillContainerStructure, target));
//					everything.add(new InstantSkillContainer(caster, target, (InstantSkillContainerStructure)skillContainerStructure)); //,caster.team());
				else					
					everything.add(new TravelingSkillContainer(this, (TravelingSkillContainerStructure)skillContainerStructure, target));
//					everything.add(new TravelingSkillContainer(caster, target, (TravelingSkillContainerStructure)skillContainerStructure)); //,caster.team());
				if (cooldown != -1)
					count = cooldown;
				
				if (trigger == 1 || trigger == 3)
					return true;
			}
		}
		return false;
	}
	
	public Actor inRange()
	{
		if (targetType == 1)
			return caster;
//		System.out.println("Target type: " + targetType);
		return everything.actorInRange(caster, range, targetType);
//		ArrayList<Actor> temp = everything.actorsInRange(caster, range, targetType);
//		if (temp != null && temp.isEmpty())
//			return temp.get(0);
//		return null;
	}
}
