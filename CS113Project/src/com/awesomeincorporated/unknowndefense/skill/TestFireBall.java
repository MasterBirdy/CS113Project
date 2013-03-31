package com.awesomeincorporated.unknowndefense.skill;

import com.awesomeincorporated.unknowndefense.parser.SkillStructure;

public class TestFireBall extends SkillStructure
{
	public TestFireBall()
	{
		aoe = 20;
		targetTeam = -1;
		effect = 0;
		effectAmount = 1000;
		duration = 1;
		damageSplit = false;
		additive = true;
		continuous = true;
		speed = 0;
		cast = "fire";
		travel = "fire";
		detonateEffect = "fire";
		affected = "fire";
	}
}
