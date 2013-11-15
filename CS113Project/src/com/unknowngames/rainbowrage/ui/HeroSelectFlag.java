package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.AllEnums.TeamColor;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.entity.Entity;

public class HeroSelectFlag extends Entity
{
//	TeamColor teamColor;
	static float scale = 1;
	String selectedFlag = "";
	
	public HeroSelectFlag(int team)
	{
		super(0, 0, team);
	}
	
	static public void setScale(float s)
	{
		scale = s;
	}
	
	public void setFlag(TeamColor color)
	{
		if (color == null)
			return;
		
		switch(color)
		{
		case red:
			selectedFlag = "selectredflag";
			break;
		case blue:
			selectedFlag = "selectblueflag";
			break;
		case green:
			selectedFlag = "selectgreenflag";
			break;
		case orange:
			selectedFlag = "selectorangeflag";
			break;
		case purple:
			selectedFlag = "selectpurpleflag";
			break;
		case yellow:
			selectedFlag = "selectyellowflag";
			break;			
		}
	}
	
	public void setLocation(float x, float y)
	{
		xCoord(x);
		yCoord(y);
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta)
	{
		if (selectedFlag.equals("") || xCoord() == 0)
			return;
		batch.draw(EverythingHolder.getObjectTexture("selectflagpole"), xCoord() + (team() * 15 - 35) * scale, yCoord() + (146 - team() * 55) * .25f * scale, 555 * .25f * scale, 18 * .25f * scale);
		batch.draw(EverythingHolder.getObjectTexture(selectedFlag), xCoord() + (80 + team() * 15) * scale, yCoord() - team() * 55 * .25f * scale, 95 * .25f * scale, 146 * .25f * scale);
//		batch.draw(EverythingHolder.getObjectTexture(selectedFlag), xCoord() + (80 + team * 20) * scale, yCoord(), 
//				   0, 146, width, height, scaleX, scaleY, rotation)
	}

	@Override
	public void update()
	{
	}

}
