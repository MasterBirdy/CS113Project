package com.unknowngames.rainbowrage;

public class Player
{
	String userName;
	int[][] upgrades = new int[7][];
	
	public Player(String name)
	{
		userName = name;
		for (int i = 0; i < 6; i++)
		{
			upgrades[i] = new int[3];
			
			for (int j = 0; j < 3; j++)
				upgrades[i][j] = -1;
		}
		
		upgrades[6] = new int[4];
		for (int i = 0; i < 4; i++)
			upgrades[6][i] = -1;
	}
	
	public String userName()
	{
		return userName;
	}
	
	public int unitSkillLevel(int unit, int skill)
	{
		if (unit < 0 || unit > 6 || skill < 0)
		{
			System.out.println("Out of range");
			return -1;
		}
		return upgrades[unit][skill];
	}
	
	public int heroSkillLevel(int skill)
	{
		return unitSkillLevel(6, skill);
	}
}
