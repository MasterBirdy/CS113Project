package com.awesomeincorporated.unknowndefense.parser;

import java.util.ArrayList;

public abstract class UnitStructure extends ActorStructure
{
	ArrayList<Float> speed;
	
	public float speed(int level)
	{
		if (level < speed.size())
			return speed.get(level);
		return speed.get(speed.size() - 1);
	}
}
