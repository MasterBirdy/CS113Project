package com.awesomeincorporated.unknowndefense.parser;

import java.util.ArrayList;

public abstract class ActorStructure 
{
	ArrayList<Integer> maxHealth;
	ArrayList<Integer> damage;
	ArrayList<Integer> attackSpeed;
	ArrayList<Integer> attackCoolDown;
	ArrayList<Integer> attackRange;
	ArrayList<Integer> cost;
	ArrayList<Boolean> ranged;
	ArrayList<Integer> animation;
	
	public int maxHealth(int level)
	{
		return maxHealth.get(level);
	}
	
	public int damage(int level)
	{
		return damage.get(level);
	}
	
	public int attackSpeed(int level)
	{
		return attackSpeed.get(level);
	}
	
//	public int attackCoolDown(int level)
//	{
//		return attackCoolDown.get(level);
//	}
	
	public int attackRange(int level)
	{
		return attackRange.get(level);
	}
	
	public int cost(int level)
	{
		return cost.get(level);
	}
	
	public boolean ranged(int level)
	{
		return ranged.get(level);
	}
	
	public int animation(int level)
	{
		return animation.get(level);
	}
}
