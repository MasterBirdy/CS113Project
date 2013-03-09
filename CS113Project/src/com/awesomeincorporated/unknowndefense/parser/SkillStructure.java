package com.awesomeincorporated.unknowndefense.parser;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class SkillStructure 
{
	////////////////////////////
	int 			state,
					castTime,
					cooldown, 
					damage, 
					cost,
					aoe,
					duration,
					targetTeam;
	////////////////////////////
	boolean 		damageSplit;
	////////////////////////////
	float			speed;
	////////////////////////////
	ParticleEffect 	cast, 
					travel, 
					trigger;
	////////////////////////////	
}
