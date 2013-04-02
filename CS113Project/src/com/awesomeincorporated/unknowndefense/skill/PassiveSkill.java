package com.awesomeincorporated.unknowndefense.skill;

import java.util.ArrayList;

import com.awesomeincorporated.unknowndefense.entity.Actor;
import com.awesomeincorporated.unknowndefense.parser.SkillStructure;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PassiveSkill extends Skill
{
	public PassiveSkill(SkillStructure s, Actor c)
	{
		super(s, c);
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

	@Override
	public void update() 
	{
		if (!caster.isAlive())
			return;
		xCoord(caster.xCoord());
		yCoord(caster.yCoord());
		
		if (--tickCounter >= 0)
			return;
		tickCounter = effectTick;
		System.out.println("Applying Effect");
		applyToTargets();
	}
}
