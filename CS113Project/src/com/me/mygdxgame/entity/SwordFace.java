package com.me.mygdxgame.entity;

import java.util.Iterator;
import java.util.ListIterator;

import com.me.mygdxgame.map.Coordinate;

public class SwordFace extends Hero
{
	public SwordFace(int x, int y, int team, ListIterator<Coordinate> p)
	{
		super(x, y, team, p);
		maxHealth = 600;
		currentHealth = maxHealth;
		damage = 1;
		attackSpeed = 25;
		attackCooldown = 0;
		attackRange = 40;
		speed = 1.5f;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
