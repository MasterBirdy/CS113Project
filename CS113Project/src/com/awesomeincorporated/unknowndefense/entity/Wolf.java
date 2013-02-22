package com.awesomeincorporated.unknowndefense.entity;

import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.map.Coordinate;

public class Wolf extends Minion
{
	public Wolf(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, false, team, p);
		maxHealth = 80;
		currentHealth = maxHealth;
		damage = 12;
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
