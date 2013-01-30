package com.awesomeincorporated.unknowndefense.entity;

import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.map.Coordinate;

public class SwordFace extends Hero
{
	public SwordFace(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, false, team, p);
		maxHealth = 120;
		currentHealth = maxHealth;
		damage = 15;
		attackSpeed = 70;
		attackCooldown = 0;
		attackRange = 40;
		speed = 1.34f;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
