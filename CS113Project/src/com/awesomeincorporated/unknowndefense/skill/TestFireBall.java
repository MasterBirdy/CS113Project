package com.awesomeincorporated.unknowndefense.skill;

import com.awesomeincorporated.unknowndefense.parser.SkillStructure;

public class TestFireBall extends SkillStructure
{
	public TestFireBall()
	{
		aoe = 0;
		targetTeam = 1;
		effect = 0;
		effectAmount = 500;
		duration = 1;
		damageSplit = false;
		additive = true;
		continuous = true;
		speed = 2;
		cast = "fire";
		travel = "fire";
		detonateEffect = "fire";
		affected = "fire";
	}
}
