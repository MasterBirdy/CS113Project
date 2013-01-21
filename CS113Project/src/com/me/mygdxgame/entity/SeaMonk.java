package com.me.mygdxgame.entity;

import java.util.ListIterator;

import com.me.mygdxgame.map.Coordinate;

public class SeaMonk extends Hero
{
	public SeaMonk(int x, int y, int team, ListIterator<Coordinate> p)
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
