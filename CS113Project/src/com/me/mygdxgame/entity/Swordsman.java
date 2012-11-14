package com.me.mygdxgame.entity;

import java.util.Iterator;

import com.me.mygdxgame.map.Coordinate;

public class Swordsman extends Minion
{
	public Swordsman(int x, int y, int team, Iterator<Coordinate> p)
	{
		super(x, y, team, p);
		maxHealth = 20;
		currentHealth = maxHealth;
		damage = 10;
		attackSpeed = 25;
		attackCooldown = 0;
		attackRange = 20;
		speed = 1.5f;
	}
}
