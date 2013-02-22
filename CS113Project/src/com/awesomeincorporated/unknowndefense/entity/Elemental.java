package com.awesomeincorporated.unknowndefense.entity;

import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.map.Coordinate;

public class Elemental extends Minion
{
	public Elemental(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, false, team, p);
		maxHealth = 150;
		currentHealth = maxHealth;
		damage = 9;
		attackSpeed = 40;
		attackCooldown = 0;
		attackRange = 35;
		speed = 1.3f;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
