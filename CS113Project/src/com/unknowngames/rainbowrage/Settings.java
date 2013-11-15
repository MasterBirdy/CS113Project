package com.unknowngames.rainbowrage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Settings
{

	// Difficulty difficulty;
	int 	difficulty 		= 1, 
			particleEffects = 1, 
			graphics 		= 1, 
			resolution 		= 1;
	float 	gameVolume 		= .1f, // .5f,
			musicVolume 	= .01f; // .1f;
	boolean showPath 		= false, 
			showRange 		= false, 
			showRadius 		= false,
			showTextEffect 	= true, 
			showShadows 	= true;

	public Settings()
	{
		try
		{
			FileHandle handle = Gdx.files.local("save/settings.txt");
			if (handle.exists())
			{
				String fileContent = handle.readString();
	
				particleEffects = Integer.parseInt(getSetting("particleEffects", fileContent));
				graphics = Integer.parseInt(getSetting("graphics", fileContent));
				resolution = Integer.parseInt(getSetting("resolution", fileContent));
				musicVolume = Float.parseFloat(getSetting("musicVolume", fileContent));
				gameVolume = Float.parseFloat(getSetting("gameVolume", fileContent));
				showTextEffect = Boolean.parseBoolean(getSetting("textEffect", fileContent));
				showShadows = Boolean.parseBoolean(getSetting("shadows", fileContent));
			}
			else
			{				
				createNew();
			}
		}
		catch (GdxRuntimeException e)
		{
			e.printStackTrace();
		}
	}

	public String getSetting(String name, String fileContent)
	{
		if (name == null || fileContent == null)
			return null;

		int loc = fileContent.indexOf(name) + name.length();
		while (fileContent.charAt(loc) == ' ' || fileContent.charAt(loc) == '=')
			loc++;
		return fileContent.substring(loc, fileContent.indexOf('\n', loc));
	}
	
	public void writeSetting(String output, FileHandle handle)
	{
		handle.writeString(output, false);
	}

	public void createNew()
	{
		difficulty = 1;
		particleEffects = 1;
		graphics = 1;
		resolution = 1;
		gameVolume = .1f;
		musicVolume = .01f;
		showPath = false;
		showRange = false;
		showRadius = false;
		showTextEffect = true;
		showShadows = true;
		
		saveFile();
	}
	
//	graphics", fileContent));
//	resolution = Integer
//			.parseInt(getSetting("resolution", fileContent));
//	musicSound = Float
//			.parseFloat(getSetting("musicVolume", fileContent));
//	gameSound = Float.parseFloat(getSetting("gameVolume", fileContent));
//	showTextEffect = Boolean.parseBoolean(getSetting("textEffect",
//			fileContent));
//	showTextEffect = Boolean.parseBoolean(getSetting("shadows

	public void saveFile()
	{
		String output = "";
		output += "particleEffects = " + particleEffects + "\n";
		output += "graphics = " + graphics + "\n";
		output += "resolution = " + resolution + "\n";
		output += "musicVolume = " + musicVolume + "\n";
		output += "gameVolume = " + gameVolume + "\n";
		output += "textEffect = " + showTextEffect + "\n";
		output += "shadows = " + showShadows + "\n";
		
		try
		{
			FileHandle handle = Gdx.files.local("save/settings.txt");
			handle.writeString(output, false);
		}
		catch (GdxRuntimeException e)
		{
			e.printStackTrace();
		}
	}

	public int getResolution()
	{
		return resolution;
	}

	public void setResolution(int r)
	{
		resolution = r;
	}

	public boolean getShadows()
	{
		return showShadows;
	}

	public int graphics()
	{
		return graphics;
	}

	public void setShadows(boolean s)
	{
		showShadows = s;
	}

	public void setGraphics(int g)
	{
		if (g >= 0 && g <= 1)
			graphics = g;
	}

	public boolean showPath()
	{
		return showPath;
	}

	public void togglePath()
	{
		showPath = !showPath;
	}

	public void showPath(boolean show)
	{
		showPath = show;
	}

	public boolean showRange()
	{
		return showRange;
	}

	public void toggleRange()
	{
		showRange = !showRange;
	}

	public void showRange(boolean show)
	{
		showRange = show;
	}

	public boolean showRadius()
	{
		return showRadius;
	}

	public void toggleRadius()
	{
		showRadius = !showRadius;
	}

	public void showRadius(boolean show)
	{
		showRadius = show;
	}

	public boolean showTextEffect()
	{
		return showTextEffect;
	}

	public void toggleTextEffect()
	{
		showTextEffect = !showTextEffect;
	}

	public void showTextEffect(boolean show)
	{
		showTextEffect = show;
		System.out.println("text " + (showTextEffect ? "enabled" : "disabled"));
	}

	public void setParticleEffects(int p)
	{
		particleEffects = p;
	}

	public void setGameSound(float s)
	{
		gameVolume = s;
	}

	public void setMusicSound(float s)
	{
		musicVolume = s;
	}

	public int getParticleEffects()
	{
		return particleEffects;
	}

	public float getGameSound()
	{
		return gameVolume;
	}

	public float getMusicSound()
	{
		return musicVolume;
	}
}
