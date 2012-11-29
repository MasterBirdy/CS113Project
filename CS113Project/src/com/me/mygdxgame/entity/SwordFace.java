package com.me.mygdxgame.entity;

import java.util.Iterator;

import com.me.mygdxgame.map.Coordinate;

public class SwordFace extends Hero
{
	public SwordFace(int x, int y, int team, Iterator<Coordinate> p)
	{
		super(x, y, team, p);
		maxHealth = 60;
		currentHealth = maxHealth;
		damage = 15;
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
