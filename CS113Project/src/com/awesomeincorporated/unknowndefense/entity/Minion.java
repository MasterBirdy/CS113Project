package com.awesomeincorporated.unknowndefense.entity;
import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.map.Coordinate;
import com.awesomeincorporated.unknowndefense.parser.MinionStructure;

public class Minion extends Unit 
{	
	public Minion(int x, int y, int team, ListIterator<Coordinate> p, MinionStructure struct, int level)
	{
		super(x, y, struct.ranged(0), team, p, struct);
		this.level = level;
	}

	@Override
	public void update() 
	{
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
