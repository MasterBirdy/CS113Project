package com.unknowngames.rainbowrage.parser;

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
//	ArrayList<Integer> animation;
	ArrayList<String> animation;
	ArrayList<String> passiveSkill;
	ArrayList<String> procSkill;
	ArrayList<String> soundPack;
	
	public String procSkill(int level)
	{
		if (level < procSkill.size())
			return procSkill.get(level);
		return procSkill.get(procSkill.size() - 1);
	}
	
	public String soundPack(int level)
	{
		if (level < soundPack.size())
			return soundPack.get(level);
		return soundPack.get(soundPack.size() - 1);
	}
	
	public String passiveSkill(int level)
	{
		if (level < passiveSkill.size())
			return passiveSkill.get(level);
		return passiveSkill.get(passiveSkill.size() - 1);
	}
	
	public int maxHealth(int level)
	{
		if (level < maxHealth.size())
			return maxHealth.get(level);
		return maxHealth.get(maxHealth.size() - 1);
	}
	
	public int damage(int level)
	{
		if (level < damage.size())
			return damage.get(level);
		return damage.get(damage.size() - 1);
	}
	
	public int attackSpeed(int level)
	{
		if (level < attackSpeed.size())
			return attackSpeed.get(level);
		return attackSpeed.get(attackSpeed.size() - 1);
	}
	
//	public int attackCoolDown(int level)
//	{
//		return attackCoolDown.get(level);
//	}
	
	public int attackRange(int level)
	{
		if (level < attackRange.size())
			return attackRange.get(level);
		return attackRange.get(attackRange.size() - 1);
	}
	
	public int cost(int level)
	{
		if (level < cost.size())
			return cost.get(level);
		return cost.get(cost.size() - 1);
	}
	
	public boolean ranged(int level)
	{
		if (level < ranged.size())
			return ranged.get(level);
		return ranged.get(ranged.size() - 1);
	}
	
	public String animation(int level)
	{
		if (level < animation.size())
			return animation.get(level);
		return animation.get(animation.size() - 1);
	}
}
