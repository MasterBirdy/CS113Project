package com.me.mygdxgame.entity;

import java.util.ListIterator;

import com.me.mygdxgame.map.Coordinate;

public class Swordsman extends Minion
{
	public Swordsman(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, false, team, p);
		maxHealth = 45;
		currentHealth = maxHealth;
		damage = 11;
		attackSpeed = 75;
		attackCooldown = 0;
		attackRange = 25;
		speed = 1.3f;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
