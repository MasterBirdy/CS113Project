package com.awesomeincorporated.unknowndefense.entity;

import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.map.Coordinate;

public class Swordsman extends Minion
{
	public Swordsman(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, false, team, p);
		maxHealth = 300;
		currentHealth = maxHealth;
		damage = 7;
		attackSpeed = 50;
		attackCooldown = 0;
		attackRange = 35;
		speed = 1.3f;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
