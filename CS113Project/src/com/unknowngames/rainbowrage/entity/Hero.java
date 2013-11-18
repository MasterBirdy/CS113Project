package com.unknowngames.rainbowrage.entity;
import java.util.HashMap;
import java.util.ListIterator;

import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.map.Coordinate;
import com.unknowngames.rainbowrage.parser.HeroStructure;
import com.unknowngames.rainbowrage.parser.MinionStructure;
import com.unknowngames.rainbowrage.parser.SkillContainerStructure;
import com.unknowngames.rainbowrage.parser.SkillStructure;
import com.unknowngames.rainbowrage.skill.SkillEffect;
import com.unknowngames.rainbowrage.skill.SkillSpawner;
import com.unknowngames.rainbowrage.skill.TargetedSkill;

public class Hero extends Unit 
{
	//int stance = 1, previousStance = 1;
	int respawnTime = 250, respawnCounter = 0;//,
//		activeCooldown = 0, activeCooldownCounter = 0;
	boolean changedDirection = false;
//	SkillStructure activeSkill;
//	SkillSpawner activeSkill;
	String pet;
	HashMap<String, String> phrases;
	
	public Hero(int x, int y, int team, ListIterator<Coordinate> p, HeroStructure struct)
	{
		this(x, y, team, p, struct, new int[]{0, -1, -1});
	}
	public Hero(int x, int y, int team, ListIterator<Coordinate> p, HeroStructure struct, int[] skillLevels)
	{
		super(x, y, team, p, struct, skillLevels);
		this.level = 0;
//		animation = struct.animation(level);
		stance = 1;
		alive = false;
		
		
//		SkillContainerStructure sContainer = everything.getSkillContainer(struct.activeSkill(level));
//		if (sContainer != null)
//			activeSkill = new SkillSpawner(this, sContainer);
//		else
//			activeSkill = null;
//		activeSkill = new SkillSpawner(this, everything.getSkillContainer(struct.activeSkill(level)));
		
		
		
//		if (activeSkill != null)
//		{
//			activeCooldown = activeSkill.cooldown.get(0);
//			activeCooldownCounter = activeCooldown;
//		}
		pet = struct.pet(level);
		
		phrases = new HashMap<String, String>(struct.getPhrases());
	}
	
	public int getRespawnTime()
	{
		return respawnCounter;
	}
	
	public String getPhrase(String type)
	{
		String phrase = phrases.get(type);
		if (phrase == null)
			phrase = "I've got nothing to say";
		return phrase;
	}
	
	public int activeCooldown()
	{
		if (skillSpawners[0] != null)
			return skillSpawners[0].getCooldown();
//		if (activeSkill != null)
//			return activeSkill.getCooldown();
		return 0;
//		return activeCooldownCounter;
	}
	
	public String pet()
	{
		return pet;
	}
	
	public boolean activeSkill()
	{
		for (int i = 0; i < 3; i++)
		{
			if (skillSpawners[i] != null && isAlive() && skillSpawners[i].getTrigger() == 3)
				skillSpawners[i].cast();
		}
		
		/*if (activeSkill != null && this.isAlive()) // && activeSkill.getCooldown() < 0) //activeCooldownCounter < 0)
		{
			//activeCooldownCounter = activeCooldown;
			return activeSkill.cast();
//			everything.add(new TargetedSkill(activeSkill, this, findTarget(this, activeSkill))); //, team);
//			everything.add(new TargetedSkill(activeSkill, this, target), team);
//			return true;
		}*/
		
		return false;
	}
	
	public void stance(int s)
	{
//		if (s == 0)
//			EverythingHolder.getUnitSounds(sounds).playDefend(everything.getSoundLevel());
		
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
			--deathCountdown;
			--respawnCounter;
//			respawnCounter = respawnTime;
			return;
		}
//		if (activeSkill != null)
//			activeSkill.update();
		attackCooldown--;
//		activeCooldownCounter--;
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
