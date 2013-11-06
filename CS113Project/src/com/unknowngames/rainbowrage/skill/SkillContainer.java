package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.entity.Entity;
import com.unknowngames.rainbowrage.parser.SkillContainerStructure;

public abstract class SkillContainer extends Entity
{

	float targetX, targetY;
	Actor caster, target;
	ArrayList<Skill> skills = new ArrayList<Skill>();

	// public SkillContainer(Actor caster, Actor target,
	// SkillContainerStructure sContainer)
	public SkillContainer(SkillSpawner skillSpawner,
			SkillContainerStructure sContainer, Actor target, ArrayList<Skill> extraSkills)
	{
		super(skillSpawner.xCoord(), skillSpawner.yCoord(), skillSpawner.team());
//		System.out.println("Creating");
		this.caster = skillSpawner.caster;
		this.target = target;
		if (target != null)
		{
			targetX = target.xCoord();
			targetY = target.yCoord();
		} else
		{
			targetX = caster.xCoord();
			targetY = caster.yCoord();
		}
		alive = true;

		loadSkills(sContainer);
		loadSkills(extraSkills);
//		System.out.println("Finally Created");
	}

	public SkillContainer(SkillSpawner skillSpawner,
			SkillContainerStructure sContainer, float targetX, float targetY)
	{
		super(skillSpawner.xCoord(), skillSpawner.yCoord(), skillSpawner.team());
		this.targetX = targetX;
		this.targetY = targetY;
		alive = true;

		loadSkills(sContainer);
	}

	public Actor getCaster()
	{
		return caster;
	}

	public Actor getTarget()
	{
		return target;
	}

	public Skill getSkill(int i)
	{
		return skills.get(i);
	}

	// protected void loadSkills(Actor caster, SkillContainerStructure
	// sContainer)
	protected void loadSkills(SkillContainerStructure sContainer)
	{
		//loadSkills(sContainer.skills);
		for (String s : sContainer.skills)
		{
			if (s != null)
			{
//				System.out.println("Skill: " + s);
				skills.add(new Skill(everything.getSkill(s), this));
				// skills.add(new Skill(everything.getSkill(s), caster,
				// target));
			}
		}
	}
	
//	protected void loadSkills(ArrayList<String> skills)
//	{
//		for (String s : skills)
//			if (s != null)
//				this.skills.add(new Skill(everything.getSkill(s), this));
//	}
	
	protected void loadSkills(ArrayList<Skill> skills)
	{
		for (Skill s : skills)
			if (s != null)
				this.skills.add(s);
	}

	@Override
	public void draw(SpriteBatch batch, float delta)
	{
	}

	// @Override
	// public void update()
	// {
	// if (target != null && target.isAlive())
	// {
	// xCoord(target.xCoord());
	// yCoord(target.yCoord());
	// } else
	// {
	// xCoord(targetX);
	// yCoord(targetY);
	// }
	//
	// for (Skill s : skills)
	// {
	// s.xCoord(targetX);
	// s.yCoord(targetY);
	// }
	//
	// detonate();
	// alive = false;
	// }

	public void detonate()
	{
//		System.out.println("Detonate container! at: " + xCoord() + ", " + yCoord());
		for (Skill s : skills)
		{
			s.xCoord(xCoord());
			s.yCoord(yCoord());
			s.detonate();
		}
		alive = false;
	}
}
