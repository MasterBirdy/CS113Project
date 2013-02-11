package com.me.mygdxgame;

public class Settings {
	
	Difficulty difficulty;
	MusicSound music;
	Sound sound;
	Graphics graphics;

    // Private constructor prevents instantiation from other classes
    private Settings() {
    	difficulty = Difficulty.EASY;
    	music = MusicSound.ON;
    	sound = Sound.ON;
    	graphics = Graphics.HIGH;
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
    
    public void setMusic(MusicSound s)
    {
    	music = s;
    }
    
    public MusicSound getMusic()
    {
    	return music;
    }
    
    public void setSound(Sound s)
    {
    	sound = s;
    }
    	
    public Sound getSound()
    {
    	return sound;
    }
    
    public void setGraphics(Graphics g)
    {
    	graphics = g;
    }
    
    public Graphics getGraphics()
    {
    	return graphics;
    }
}
