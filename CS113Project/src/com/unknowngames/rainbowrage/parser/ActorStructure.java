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
	ArrayList<Integer> physicalResist;
	ArrayList<Integer> rangedResist;
	ArrayList<Integer> magicResist;
	Integer 		   radius;
	ArrayList<Boolean> ranged;
	ArrayList<String> projectile;
//	ArrayList<Integer> animation;
	ArrayList<String> animation;
	ArrayList<String> firstSkill;
	ArrayList<String> secondSkill;
	ArrayList<String> thirdSkill;
//	ArrayList<String> passiveSkill;
//	ArrayList<String> procSkill;
	ArrayList<String> soundPack;
	
	public int radius()
	{
		return radius;
	}
	
	public String firstSkill(int level)
	{
		if (level < firstSkill.size())
			return firstSkill.get(level);
		return firstSkill.get(firstSkill.size() - 1);
	}
	
	public String secondSkill(int level)
	{
		if (level < secondSkill.size())
			return secondSkill.get(level);
		return secondSkill.get(secondSkill.size() - 1);
	}
	
	public String thirdSkill(int level)
	{
		if (level < thirdSkill.size())
			return thirdSkill.get(level);
		return thirdSkill.get(thirdSkill.size() - 1);
	}
	
//	public String procSkill(int level)
//	{
//		if (level < procSkill.size())
//			return procSkill.get(level);
//		return procSkill.get(procSkill.size() - 1);
//	}
	
	public String soundPack(int level)
	{
		if (level < soundPack.size())
			return soundPack.get(level);
		return soundPack.get(soundPack.size() - 1);
	}
	
//	public String passiveSkill(int level)
//	{
//		if (level < passiveSkill.size())
//			return passiveSkill.get(level);
//		return passiveSkill.get(passiveSkill.size() - 1);
//	}
	
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
	
	public int physicalResist(int level)
	{
		if (level < physicalResist.size())
			return physicalResist.get(level);
		return physicalResist.get(physicalResist.size() - 1);
	}
	
	public int rangedResist(int level)
	{
		if (level < rangedResist.size())
			return rangedResist.get(level);
		return rangedResist.get(rangedResist.size() - 1);
	}
	
	public int magicResist(int level)
	{
		if (level < magicResist.size())
			return magicResist.get(level);
		return magicResist.get(magicResist.size() - 1);
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
	
	public String projectile(int level)
	{
		if (level < projectile.size())
			return projectile.get(level);
		return projectile.get(projectile.size() - 1);
	}
}
