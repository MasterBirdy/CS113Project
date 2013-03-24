package com.awesomeincorporated.unknowndefense.parser;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class SkillStructure 
{
	////////////////////////////
	public int		aoe,
					targetTeam,
					effect,
					effectAmount, 
					state,
					castTime,
					cooldown, 
					cost,
					duration;
	////////////////////////////
	public boolean 	damageSplit,
					additive,
					continuous;
	////////////////////////////
	public float	speed;
	////////////////////////////
	public String		 	cast, 
							travel, 
							detonateEffect,
							affected;
	////////////////////////////	
	public String name;
}
