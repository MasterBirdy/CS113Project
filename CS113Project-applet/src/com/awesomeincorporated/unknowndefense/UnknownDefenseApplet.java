package com.awesomeincorporated.unknowndefense;

import com.awesomeincorporated.unknowndefense.UnknownDefense;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplet;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class UnknownDefenseApplet extends LwjglApplet
{
	private static final long serialVersionUID = 1L;
	
	public UnknownDefenseApplet()
	{
		super(new UnknownDefense(), false);
	}
	
//	public UnknownDefenseApplet(ApplicationListener listener,
//			LwjglApplicationConfiguration config) 
//	{
//		super(listener, config);
//	}
}
