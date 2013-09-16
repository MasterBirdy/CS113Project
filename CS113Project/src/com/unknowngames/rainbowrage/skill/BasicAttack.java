package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.entity.Building;
import com.unknowngames.rainbowrage.parser.InstantSkillContainerStructure;
import com.unknowngames.rainbowrage.parser.SkillContainerStructure;
import com.unknowngames.rainbowrage.parser.SkillStructure;
import com.unknowngames.rainbowrage.parser.TravelingSkillContainerStructure;

public class BasicAttack extends SkillSpawner
{
//	static SkillContainerStructure sContainerStructure;
//	static SkillStructure sStructure;
	
	ArrayList<Skill> extraSkills = new ArrayList<Skill>();
	
	public BasicAttack(Actor c)//, int damage, boolean ranged)
	{
		super(c, makeContainer(c, c.getDamage(), c.isRanged()));
		if (c instanceof Building)
			this.relativeY = 40;
		range = c.getAttackRange();
	}
	
	private static SkillContainerStructure makeContainer(Actor c, int damage, boolean ranged)
	{
		if (ranged)
		{
			TravelingSkillContainerStructure tSkill = (TravelingSkillContainerStructure)everything.getSkillContainer("ranged");
			tSkill.skillSprite = c.getProjectileSprite() + everything.teamColor(c.team());
			return new TravelingSkillContainerStructure(tSkill);
		}
		else
			return new InstantSkillContainerStructure(everything.getSkillContainer("melee"));
		
//		SkillContainerStructure scs;
//		if (ranged)
//			scs = new TravelingSkillContainerStructure(everything.getSkillContainer("ranged"));
//		else
//			scs = new InstantSkillContainerStructure(everything.getSkillContainer("melee"));
//		
//		return scs;
		
		/*SkillStructure ss = new SkillStructure();
		ss.aoe.set(0, -1);
		ss.targeting.set(0, -1);
		ss.effect.set(0, 0);
		ss.effectAmount.set(0, damage);
		ss.duration.set(0, 1);
		ss.effectTick.set(0, 0);
		ss.travelTime.set(0, (ranged ? 70 : -1));
		ss.additive.set(0, true);
		ss.speed.set(0, 3f);
		
		SkillContainerStructure scs;
		if (ranged)
		{
			scs = new TravelingSkillContainerStructure();
		}
		else
		{
			
		}
		
		scs.targetType = s.targetType;
		scs.targetRange = s.targetRange;
		scs.castTime = s.castTime;
		scs.cooldown = s.cooldown;
		scs.trigger = s.trigger;
		scs.cost = s.cost;
		scs.castEffect = s.castEffect;
		scs.castSound = s.castSound;
		scs.name = s.name;
		
		scs.skills = new ArrayList<String>();
		scs.skills.addAll(s.skills);*/
	}
	
	public void addExtraSkills(Skill s)
	{
		extraSkills.add(s);
	}
	
	@Override
	public boolean cast()
	{
//		System.out.print("Basic attack!");
		if (count < 0)
		{
			range = caster.getAttackRange(); //castEffect.caster.getAttackRange();
			Actor target = inRange();
			if (target != null)
			{
				cast(target);
//				SkillContainer sContainer;
//				if (skillContainerStructure instanceof InstantSkillContainerStructure)
//					sContainer = new InstantSkillContainer(this, (InstantSkillContainerStructure)skillContainerStructure, target);
////					sContainer = new InstantSkillContainer(caster, target, (InstantSkillContainerStructure)skillContainerStructure);
////					sContainer = new InstantSkillContainer(caster, skillContainerStructure), 
////								   						   caster.team());
//				else
//					sContainer = new TravelingSkillContainer(this, (TravelingSkillContainerStructure)skillContainerStructure, target);
////					sContainer = new TravelingSkillContainer(caster, target, (TravelingSkillContainerStructure)skillContainerStructure);
////					everything.add(new TravelingSkillContainer(caster, (TravelingSkillContainerStructure)skillContainerStructure), 
////								   caster.team());
//				sContainer.skills.get(0).effectAmount = caster.getDamage();
//				sContainer.skills.get(0).aoe = caster.getAoe();
//				count = caster.getCooldown();
//				
//				for (Skill s : extraSkills)
//					sContainer.skills.add(s);
//				
//				everything.add(sContainer); //, caster.team());
//				extraSkills.clear();
				if (trigger == 1 || trigger == 3)
					return true;
			}
		}
		return false;
	}
	
	public void cast(Actor target)
	{
		SkillContainer sContainer;
		if (skillContainerStructure instanceof InstantSkillContainerStructure)
			sContainer = new InstantSkillContainer(this, (InstantSkillContainerStructure)skillContainerStructure, target);
//			sContainer = new InstantSkillContainer(caster, target, (InstantSkillContainerStructure)skillContainerStructure);
//			sContainer = new InstantSkillContainer(caster, skillContainerStructure), 
//						   						   caster.team());
		else
			sContainer = new TravelingSkillContainer(this, (TravelingSkillContainerStructure)skillContainerStructure, target);
//			sContainer = new TravelingSkillContainer(caster, target, (TravelingSkillContainerStructure)skillContainerStructure);
//			everything.add(new TravelingSkillContainer(caster, (TravelingSkillContainerStructure)skillContainerStructure), 
//						   caster.team());
		sContainer.skills.get(0).effectAmount = caster.getDamage();
		sContainer.skills.get(0).aoe = caster.getAoe();
		count = caster.getCooldown();
		
		for (Skill s : extraSkills)
			sContainer.skills.add(s);
		
		everything.add(sContainer); //, caster.team());
		extraSkills.clear();
	}
	
//	static void load()
//	{
//		sStructure.aoe.set(0, -1);
//		sStructure.targeting.set(0, -1);
//		sStructure.effect.set(0, 0);
////		sStructure.effectAmount.set(0, 0);
//		sStructure.duration.set(0, 1);
//		sStructure.effectTick.set(0, 0);
////		sStructure.travelTime.set(0, (a.ranged(level) ? 70 : -1));
//		sStructure.additive.set(0, true);
//		sStructure.speed.set(0, 3f);
//	}
}
