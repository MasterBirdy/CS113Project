package com.me.mygdxgame.entity;

import java.util.Iterator;
import java.util.ListIterator;

import com.me.mygdxgame.map.Coordinate;

public class ArrowEyes extends Hero
{
	public ArrowEyes(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, true, team, p);
		maxHealth = 450;
		currentHealth = maxHealth;
		damage = 2;
		attackSpeed = 10;
		attackCooldown = 0;
		attackRange = 100;
		speed = 1.5f;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
