package com.unknowngames.rainbowrage.ui;

public class PlayerMessage 
{
	String text;
	int turnsLeft;
	
	public PlayerMessage()
	{
	}
	
	public void update()
	{
		--turnsLeft;
	}
}
