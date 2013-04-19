package com.unknowngames.rainbowrage;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplet;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.unknowngames.rainbowrage.RainbowRage;

public class RainbowRageApplet extends LwjglApplet
{
	private static final long serialVersionUID = 1L;
	
	public RainbowRageApplet()
	{
		super(new RainbowRage(), false);
	}
	
//	public UnknownDefenseApplet(ApplicationListener listener,
//			LwjglApplicationConfiguration config) 
//	{
//		super(listener, config);
//	}
}
