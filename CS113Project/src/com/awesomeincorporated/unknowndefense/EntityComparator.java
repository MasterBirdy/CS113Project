package com.awesomeincorporated.unknowndefense;

import java.util.Comparator;

import com.awesomeincorporated.unknowndefense.entity.Entity;

public class EntityComparator implements Comparator
{

	@Override
	public int compare(Object arg0, Object arg1) 
	{
		if (arg0 == null)
			return -1;
		if (arg1 == null)
			return 1;
		int y1 = (int)((Entity)arg0).yCoord();
		int y2 = (int)((Entity)arg1).yCoord();
		if (y1 < y2)
			return 1;
		if (y1 > y2)
			return -1;
		if (((Entity)arg0).xCoord() < ((Entity)arg1).xCoord())
			return 1;
		if (((Entity)arg0).xCoord() > ((Entity)arg1).xCoord())
			return -1;		
		return 0;
	}

}
