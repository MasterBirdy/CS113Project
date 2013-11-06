package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.entity.Entity;
import com.unknowngames.rainbowrage.parser.InstantSkillContainerStructure;
import com.unknowngames.rainbowrage.parser.SkillContainerStructure;

public class InstantSkillContainer extends SkillContainer
{

	public InstantSkillContainer(SkillSpawner skillSpawner,
			InstantSkillContainerStructure sContainer, Actor target, ArrayList<Skill> extraSkills)
	{
		super(skillSpawner, sContainer, target, extraSkills);
//		System.out.println("Created");
	}

	public InstantSkillContainer(SkillSpawner skillSpawner,
			SkillContainerStructure sContainer, float targetX, float targetY)
	{
		super(skillSpawner, sContainer, targetX, targetY);
	}

	// public InstantSkillContainer(Actor caster, Actor target,
	// InstantSkillContainerStructure sContainer)
	// {
	// super(caster, target, sContainer);
	// }
	//
	// public InstantSkillContainer(Actor caster, Actor target,
	// SkillContainerStructure sContainer, float targetX, float targetY)
	// {
	// super(caster, sContainer, targetX, targetY);
	// }

	@Override
	public void update()
	{
//		System.out.println("Updating Skill");
		if (target != null && target.isAlive())
		{
			xCoord(target.xCoord());
			yCoord(target.yCoord());
		}
		else
		{
			xCoord(targetX);
			yCoord(targetY);
		}

		// for (Skill s : skills)
		// {
		// s.xCoord(targetX);
		// s.yCoord(targetY);
		// }
//		System.out.println("About to detonate");
		detonate();
//		alive = false;
	}
}
