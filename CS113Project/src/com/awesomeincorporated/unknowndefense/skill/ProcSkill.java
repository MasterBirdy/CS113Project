package com.awesomeincorporated.unknowndefense.skill;

import java.util.ArrayList;

import com.awesomeincorporated.unknowndefense.entity.Actor;
import com.awesomeincorporated.unknowndefense.parser.SkillStructure;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ProcSkill extends Skill
{
	int cooldown, cooldownCounter;
	
	public ProcSkill(SkillStructure s, Actor c)
	{
		super(s, c);
		cooldown = s.cooldown.get(0);
		cooldownCounter = 0;
	}

	@Override
	public void draw(SpriteBatch batch) 
	{
		
	}

	@Override
	public ArrayList<Actor> inRange() 
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
		if (aoe == 0)
			temp.add(caster);
		else
			for (Actor a : everything.team(targetTeam))
				if (a.isAlive() && this.getDistanceSquared(a) < aoe * aoe)
					temp.add(a);
		return temp;
	}
	
	public void trip(int hit)
	{
		System.out.println("Tripped");
		if (!caster.isAlive() || cooldownCounter >=0)
			return;
		cooldownCounter = cooldown;
		System.out.println("INVISIBLE!");
		if (effect == 0)
			caster.invis(effectAmount);
	}

	@Override
	public void update() 
	{
		if (!caster.isAlive())
			return;
		
		xCoord(caster.xCoord());
		yCoord(caster.yCoord());
		
		if (--cooldownCounter >= 0)
			return;
//		cooldownCounter = cooldown;
//		System.out.println("Applying Effect");
//		applyToTargets();
	}
}
