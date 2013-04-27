package com.unknowngames.rainbowrage.entity;
import java.util.ListIterator;

import com.unknowngames.rainbowrage.map.Coordinate;
import com.unknowngames.rainbowrage.parser.HeroStructure;
import com.unknowngames.rainbowrage.parser.MinionStructure;
import com.unknowngames.rainbowrage.parser.SkillStructure;
import com.unknowngames.rainbowrage.skill.SkillEffect;
import com.unknowngames.rainbowrage.skill.TargetedSkill;

public class Hero extends Unit 
{
	//int stance = 1, previousStance = 1;
	int respawnTime = 250, respawnCounter = 0,
		activeCooldown = 0, activeCooldownCounter = 0;
	boolean changedDirection = false;
	SkillStructure activeSkill;
	String pet;
	
	public Hero(int x, int y, int team, ListIterator<Coordinate> p, HeroStructure struct)
	{
		super(x, y, struct.ranged(0), team, p, struct);
		this.level = 0;
//		animation = struct.animation(level);
		stance = 1;
		alive = false;
		activeSkill = everything.getSkill(struct.activeSkill(level));
		if (activeSkill != null)
		{
			activeCooldown = activeSkill.cooldown.get(0);
			activeCooldownCounter = activeCooldown;
		}
		pet = struct.pet(level);		
	}
	
	public int activeCooldown()
	{
		return activeCooldownCounter;
	}
	
	public String pet()
	{
		return pet;
	}
	
	public boolean activeSkill()
	{
		if (activeSkill != null && this.isAlive() && activeCooldownCounter < 0)
		{
			activeCooldownCounter = activeCooldown;
			everything.add(new TargetedSkill(activeSkill, this, target), team);
			return true;
		}
		
		return false;
	}
	
	public void stance(int s)
	{
		stance = s;
	}
	
	private void hold()
	{
		targetSelector();
		
		if (attacking && attackCooldown - attackSpeedBoost <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
		}
		xSpeed = 0;
		ySpeed = 0;
	}
	
	public boolean canRespawn()
	{
		return (respawnCounter < 0);
	}

	@Override
	public void update() 
	{
		super.update();
		if (!isAlive())
		{
			--respawnCounter;
//			respawnCounter = respawnTime;
			return;
		}
		attackCooldown--;
		activeCooldownCounter--;
		if (stance == -1)
		{
			retreat();
			previousStance = -1;
		}
		else if (stance == 0)
		{
			hold();
		}
		else if (attacking && attackCooldown - attackSpeedBoost <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
			previousStance = 1;
		}
		else
		{
			if (previousStance == -1 && pathIter.hasNext())
			{
				destination = pathIter.next();
				xSpeed = 0;
				ySpeed = 0;
			}
//			if (previousStance == -1 && (this.team == 1 ? pathIter.hasNext() : pathIter.hasPrevious()))
//			{
//				destination = (this.team == 1 ? pathIter.next() : pathIter.previous());
//				xSpeed = 0;
//				ySpeed = 0;
//			}
			advance();
			previousStance = 1;
		}
		
		//previousStance = stance;
	}
	
	public void respawn(int x, int y, ListIterator<Coordinate> p)
	{
		xCoord(x);
		yCoord(y);
		alive = true;
		currentHealth = maxHealth;
		pathIter = p;
		destination = pathIter.next();
		target = null;
		xSpeed = 0;
		ySpeed = 0;
		stance = 1;
		previousStance = 1;
		target = null;
		attackCooldown = 0;
		respawnCounter = respawnTime;
		this.advance();
	}
	
	public int stance()
	{
		return stance;
	}
	
	
	public void attack()
	{
		super.attack();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
