package com.awesomeincorporated.unknowndefense.parser;

import java.util.ArrayList;

public class HeroStructure extends UnitStructure
{
	ArrayList<String> activeSkill;
//	ArrayList<Integer> maxHealth;
//	ArrayList<Integer> damage;
//	ArrayList<Integer> attackSpeed;
//	ArrayList<Integer> attackCoolDown;
//	ArrayList<Integer> attackRange;
//	ArrayList<Integer> cost;
//	ArrayList<Boolean> ranged;	
//	ArrayList<Float> speed;
	
	public String activeSkill(int level)
	{
		return activeSkill.get(level);
	}
}
