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
	
	public int maxHealth(int i)
	{
		return maxHealth.get(i);
	}
	
	public int damage(int i)
	{
		return damage.get(i);
	}
	
	public int attackSpeed(int i)
	{
		return attackSpeed.get(i);
	}
	
	public int attackCoolDown(int i)
	{
		return attackCoolDown.get(i);
	}
	
	public int attackRange(int i)
	{
		return attackRange.get(i);
	}
	
	public int cost(int i)
	{
		return cost.get(i);
	}
	
	public boolean ranged(int i)
	{
		return ranged.get(i);
	}
}