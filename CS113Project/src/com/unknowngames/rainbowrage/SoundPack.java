package com.unknowngames.rainbowrage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.audio.Sound;

public class SoundPack 
{
//	static float volume = 1f;
	Sound attack, 
		  die,
		  advance,
		  retreat,
		  defend;
	
	public SoundPack(String name)
	{
		attack 	= getSound(name + "attack.mp3");
		die 	= getSound(name + "die.mp3");
		advance = getSound(name + "defend.mp3");
		retreat = getSound(name + "retreat.mp3");
		defend 	= getSound(name + "defend.mp3");
		
		if (die == null)
			die = getSound("miniondie.mp3");
	}
	
	public void playAttack(float volume)
	{
		try
		{
			if (Gdx.app.getType() == ApplicationType.Android)
				attack.stop();
			attack.play(volume);
		}
		catch (Exception e)
		{
			return;
		}
	}
	
	public void playDie(float volume)
	{
		try
		{
			die.play(volume);
		}
		catch (Exception e)
		{
			return;
		}
	}
	
	private Sound getSound(String name)
	{
		try
		{
			return Gdx.audio.newSound(Gdx.files.internal("audio/" + name));
		}
		catch (Exception e)
		{
			return null;
		}
			
	}
}
