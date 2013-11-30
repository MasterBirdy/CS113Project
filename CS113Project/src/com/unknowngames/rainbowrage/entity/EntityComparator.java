package com.unknowngames.rainbowrage.entity;

import java.util.Comparator;


public class EntityComparator implements Comparator<Entity>
{
	@Override
	public int compare(Entity arg0, Entity arg1) 
	{
		if (arg0 == null)
		{
			if (arg1 != null)
				return 1;
			return 0;
		}
		else if (arg1 == null)
			return -1;
		
		int y1 = (int)arg0.yCoord();
		int y2 = (int)arg1.yCoord();
		if (y1 < y2)
			return 1;
		if (y1 > y2)
			return -1;
		if (arg0.xCoord() < arg1.xCoord())
			return 1;
		if (arg0.xCoord() > arg1.xCoord())
			return -1;		
		return 0;
	}
}