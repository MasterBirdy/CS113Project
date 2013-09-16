package com.unknowngames.rainbowrage.skill;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.parser.SkillStructure;

public class PassiveSkill extends Skill
{

	public PassiveSkill(SkillStructure s, SkillContainer sc)
	{
		super(s, sc);
		// TODO Auto-generated constructor stub
	}
//	int cooldown, cooldownCounter;
//	
//	public PassiveSkill(SkillStructure s, Actor c)
//	{
//		super(s, c);
////		cooldown = s.cooldown.get(0);
//		cooldownCounter = cooldown;
//	}
//
//	@Override
//	public void draw(SpriteBatch batch, float delta) 
//	{
//		
//	}
//
//	@Override
//	public ArrayList<Actor> inRange() 
//	{
//		ArrayList<Actor> temp = new ArrayList<Actor>();
////		if (aoe == 0)
////			temp.add(caster);
////		else
////			for (Actor a : everything.team(targetTeam))
////				if (a.isAlive() && this.getDistanceSquared(a) < aoe * aoe)
////					temp.add(a);
//		return temp;
//	}
//
//	@Override
//	public void update() 
//	{
//		if (!caster.isAlive())
//			return;
//		
//		xCoord(caster.xCoord());
//		yCoord(caster.yCoord());
//		
//		if (--cooldownCounter >= 0)
//			return;
//		cooldownCounter = cooldown;
////		System.out.println("Applying Effect");
//		applyToTargets();
//	}
}
