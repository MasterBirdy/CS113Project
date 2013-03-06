package com.awesomeincorporated.unknowndefense.parser;

import java.util.ArrayList;

public abstract class UnitStructure extends ActorStructure
{
	ArrayList<Float> speed;
	
	public float speed(int i)
	{
		return speed.get(i);
	}
}
