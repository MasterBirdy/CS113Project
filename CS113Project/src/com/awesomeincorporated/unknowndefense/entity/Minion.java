package com.awesomeincorporated.unknowndefense.entity;
import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.map.Coordinate;

public abstract class Minion extends Unit 
{
	
	public Minion(int x, int y, boolean ranged, int team, ListIterator<Coordinate> p) 
	{
		super(x, y, ranged, team, p, (int)(Math.random() * 10 - 5), (int)(Math.random() * 5 - 2));
	}

	@Override
	public void update() 
	{
		
		if (attacking && attackCooldown <= 0)
		{
			attack();
			attackCooldown = attackSpeed;
		}
		else
		{
			attackCooldown--;
			advance();
		}
	}

}
