package com.awesomeincorporated.unknowndefense.parser;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class SkillStructure 
{
	////////////////////////////
	public 
		ArrayList<Integer>	aoe,
							targetTeam,
							effect,
							effectAmount, 
							state,
							castTime,
							cooldown, 
							cost,
							duration;
	////////////////////////////
	public 
		ArrayList<Boolean> 	damageSplit,
							additive,
							continuous;
	////////////////////////////
	public 
		ArrayList<Float>	speed;
	////////////////////////////
	public 
		ArrayList<String>	sprite,
							cast, 
							travel, 
							detonateEffect,
							affected;
	////////////////////////////	
	public 
		ArrayList<String>	name;
}
