package com.unknowngames.rainbowrage;

import com.unknowngames.rainbowrage.AllEnums.TeamColor;


public class Player
{
	String userName, hero = "";
	TeamColor teamColor;// = TeamColor.red;
	int[][] upgrades = new int[7][];
	int funds;
	
	public Player(String name)
	{
		userName = name;
		for (int i = 0; i < 7; i++)
		{
			upgrades[i] = new int[3];
			
			for (int j = 0; j < 3; j++)
				upgrades[i][j] = -1;
		}
		
		funds = 0;
		
		/*upgrades[6] = new int[4];
		for (int i = 0; i < 4; i++)
			upgrades[6][i] = -1;*/
		
	}
	
	public int getFunds()
	{
		return funds;
	}
	
	public void setFunds(int f)
	{
		funds = f;
	}
	
	public String getHero()
	{
		return hero;
	}
	
	public void setHero(String h)
	{
		hero = h;
	}
	
	public TeamColor getColor()
	{
		return teamColor;
	}
	
	public void setColor(TeamColor c)
	{
		teamColor = c;
	}
	
	public boolean changeFunds(int change)
	{
		if (funds < -change)
			return false;
		
		funds += change;
		return true;
	}
	
	public String userName()
	{
		return userName;
	}
	
	public int unitSkillLevel(int unit, int skill)
	{
		System.out.println("Player: " + userName + " Unit: " + unit + " Skill: " + skill);
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
