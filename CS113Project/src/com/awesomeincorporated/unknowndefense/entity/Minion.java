package com.awesomeincorporated.unknowndefense.entity;
import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.map.Coordinate;
import com.awesomeincorporated.unknowndefense.parser.MinionStructure;

public class Minion extends Unit 
{
	
	public Minion(int x, int y, boolean ranged, int team, ListIterator<Coordinate> p) 
	{
		super(x, y, ranged, team, p, 0, 0);//(int)(Math.random() * 10 - 5), (int)(Math.random() * 5 - 2));
	}
	
	public Minion(int x, int y, int team, ListIterator<Coordinate> p, MinionStructure struct)
	{
		super(x, y, struct.ranged(0), team, p, 0, 0);
		
		maxHealth = struct.maxHealth(0);
		currentHealth = maxHealth;
		damage = struct.damage(0);
		attackSpeed = struct.attackSpeed(0);
		attackCooldown = struct.attackCoolDown(0);
		attackRange = struct.attackRange(0);
		speed = struct.speed(0);
	}

	@Override
	public void update() 
	{
		
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
