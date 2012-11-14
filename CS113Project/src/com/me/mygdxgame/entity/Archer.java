package com.me.mygdxgame.entity;

import java.util.Iterator;

import com.me.mygdxgame.map.Coordinate;

public class Archer extends Minion
{
	public Archer(int x, int y, int team, Iterator<Coordinate> p)
	{
		super(x, y, team, p);
		maxHealth = 10;
		currentHealth = maxHealth;
		damage = 3;
		attackSpeed = 30;
		attackCooldown = 0;
		attackRange = 100;
		speed = 1.5f;
	}
}
