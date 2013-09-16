package com.unknowngames.rainbowrage.entity;
import java.util.ListIterator;

import com.unknowngames.rainbowrage.map.Coordinate;
import com.unknowngames.rainbowrage.parser.MinionStructure;

public class Minion extends Unit 
{
	public Minion(int x, int y, int team, ListIterator<Coordinate> p, MinionStructure struct, int level)
	{
		this(x, y, team, p, struct, new int[]{0, 0, 0});
	}
	public Minion(int x, int y, int team, ListIterator<Coordinate> p, MinionStructure struct, int[] skillLevels)
	{
		super(x, y, team, p, struct, skillLevels);
	}

	@Override
	public void update() 
	{
		if (!isAlive())
		{
			deathCountdown--;
			return;
		}
		super.update();
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

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
