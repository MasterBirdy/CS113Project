package com.unknowngames.rainbowrage;

public class Settings 
{
	
//	Difficulty difficulty;
	int difficulty = 1, particleEffects = 0;
	float gameSound = .1f, //.5f, 
		  musicSound = .01f; //.1f;
	boolean showPath 		= false, 
			showRange 		= false, 
			showRadius 		= false,
			showTextEffect 	= true;
	
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
	}
	
	public void setParticleEffects(int p)
	{
		particleEffects = p;
	}
	
	public void setGameSound(float s)
	{
		gameSound = s;
	}
	
	public void setMusicSound(float s)
	{
		musicSound = s;
	}
	
	public String getParticleEffects()
	{
		if (particleEffects == 1)
			return "low";
		return "";
	}
	
	public float getGameSound()
	{
		return gameSound;
	}
	
	public float getMusicSound()
	{
		return musicSound;
	}
	
	
	
//	SoundEnum sound;

    // Private constructor prevents instantiation from other classes
//    private Settings() 
//    {
//    	difficulty = Difficulty.EASY;
//    	sound = SoundEnum.ON;
//    }

    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
//    private static class SingletonHolder { 
//            public static final Settings INSTANCE = new Settings();
//    }
//
//    public static Settings getInstance() {
//            return SingletonHolder.INSTANCE;
//    }
//    
//    public void setDifficulty(Difficulty d)
//    {
//    	difficulty = d;
//    }
//    
//    public Difficulty getDifficulty()
//    {
//    	return difficulty;
//    }
//    
//    public void setSound(SoundEnum s)
//    {
//    	sound = s;
//    }
//    
//    public SoundEnum getSound()
//    {
//    	return sound;
//    }
}
