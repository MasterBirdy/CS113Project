package com.me.mygdxgame.entity;

import java.util.Iterator;
import java.util.ListIterator;

import com.me.mygdxgame.map.Coordinate;

public class Monk extends Minion
{
	public Monk(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, false, team, p);
		maxHealth = 45;
		currentHealth = maxHealth;
		damage = 6;
		attackSpeed = 40;
		attackCooldown = 0;
		attackRange = 25;
		speed = 1.3f;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
