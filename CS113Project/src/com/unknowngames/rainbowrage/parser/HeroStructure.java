package com.unknowngames.rainbowrage.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class HeroStructure extends UnitStructure
{
	ArrayList<String> activeSkill;
	ArrayList<String> pet;
	HashMap<String, String> phrases = new HashMap<String, String>();
	
	public String activeSkill(int level)
	{
		if (level < activeSkill.size())
			return activeSkill.get(level);
		return activeSkill.get(activeSkill.size() - 1);
	}
	
	public String pet(int level)
	{
		if (level < pet.size())
			return pet.get(level);
		return pet.get(pet.size() - 1);
	}
	
	public HashMap<String, String> getPhrases()
	{
		return phrases;
	}
}
