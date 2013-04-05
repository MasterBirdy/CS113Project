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
	ArrayList<String> attackSound;
	ArrayList<String> passiveSkill;
	ArrayList<String> procSkill;
	ArrayList<String> soundPack;
	
	public String procSkill(int level)
	{
		if (procSkill.size() > level)
			return procSkill.get(level);
		return procSkill.get(procSkill.size());
	}
	
	public String soundPack(int level)
	{
		if (soundPack.size() > level)
			return soundPack.get(level);
		return soundPack.get(soundPack.size());
	}
	
	public String passiveSkill(int level)
	{
		return passiveSkill.get(level);
	}
	
	public String attackSound(int level)
	{
		if (attackSound.size() > level)
			return attackSound.get(level);
		return attackSound.get(attackSound.size());
	}
	
	public int maxHealth(int level)
	{
		if (maxHealth.size() > level)
			return maxHealth.get(level);
		return maxHealth.get(maxHealth.size());
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
