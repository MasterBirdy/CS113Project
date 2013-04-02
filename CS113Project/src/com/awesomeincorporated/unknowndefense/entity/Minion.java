package com.awesomeincorporated.unknowndefense.entity;
import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.map.Coordinate;
import com.awesomeincorporated.unknowndefense.parser.MinionStructure;

public class Minion extends Unit 
{
	
//	public Minion(int x, int y, boolean ranged, int team, ListIterator<Coordinate> p) 
//	{
//		super(x, y, ranged, team, p, 0, 0);//(int)(Math.random() * 10 - 5), (int)(Math.random() * 5 - 2));
//	}
	
	public Minion(int x, int y, int team, ListIterator<Coordinate> p, MinionStructure struct, int level)
	{
		super(x, y, struct.ranged(0), team, p, struct);
		this.level = level;
		maxHealth = struct.maxHealth(level);
		currentHealth = maxHealth;
		damage = struct.damage(level);
		attackSpeed = struct.attackSpeed(level);
//		attackCooldown = struct.attackCoolDown(level);
		attackRange = struct.attackRange(level);
		speed = struct.speed(level);
		animation = struct.animation(level);
//		if (!struct.passiveSkill(level).equals("empty"))
//			this.loadPassiveSkill(everything.getSkill(struct.passiveSkill(level)));
//		if (!struct.attackSound(level).equals("empty"))
//			this.attackSound = everything.getSound(struct.attackSound(level));
//		animation = 2;
	}

	@Override
	public void update() 
	{
		super.update();
		if (attacking && attackCooldown <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
		}
		else
		{
			attackCooldown--;
			advance();
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
