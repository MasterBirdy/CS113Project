package com.unknowngames.rainbowrage.parser;

public class TravelingSkillContainerStructure extends SkillContainerStructure
{
	////////////////////////////
	public 
		Integer	travelTime;
	////////////////////////////
	public 
		float speed;
	////////////////////////////
	public
		String	skillSprite,
				travelEffect;
	
	public TravelingSkillContainerStructure()
	{
		
	}
	
	public TravelingSkillContainerStructure(TravelingSkillContainerStructure t)
	{
		super(t);
		travelTime = ((TravelingSkillContainerStructure)t).travelTime;
		speed = ((TravelingSkillContainerStructure)t).speed;
		skillSprite = ((TravelingSkillContainerStructure)t).skillSprite;
		travelEffect = ((TravelingSkillContainerStructure)t).travelEffect;
	}
}
