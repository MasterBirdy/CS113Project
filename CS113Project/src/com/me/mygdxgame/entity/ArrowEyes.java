package com.me.mygdxgame.entity;

import java.util.ListIterator;

import com.me.mygdxgame.map.Coordinate;

public class ArrowEyes extends Hero
{
	public ArrowEyes(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, true, team, p);
		maxHealth = 75;
		currentHealth = maxHealth;
		damage = 11;
		attackSpeed = 30;
		attackCooldown = 0;
		attackRange = 100;
		speed = 1.34f;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
