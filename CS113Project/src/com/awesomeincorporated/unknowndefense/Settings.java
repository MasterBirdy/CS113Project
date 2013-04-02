package com.awesomeincorporated.unknowndefense;

public class Settings {
	
	Difficulty difficulty;
	SoundEnum sound;

    // Private constructor prevents instantiation from other classes
    private Settings() {
    	difficulty = Difficulty.EASY;
    	sound = SoundEnum.ON;
    }

    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class SingletonHolder { 
            public static final Settings INSTANCE = new Settings();
    }

    public static Settings getInstance() {
            return SingletonHolder.INSTANCE;
    }
    
    public void setDifficulty(Difficulty d)
    {
    	difficulty = d;
    }
    
    public Difficulty getDifficulty()
    {
    	return difficulty;
    }
    
    public void setSound(SoundEnum s)
    {
    	sound = s;
    }
    
    public SoundEnum getSound()
    {
    	return sound;
    }
}
