package com.unknowngames.rainbowrage.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
//import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.utils.Clipboard;
import com.unknowngames.rainbowrage.RainbowRage;

public class RainbowRageGwt extends GwtApplication 
{
	@Override
	public GwtApplicationConfiguration getConfig () 
	{
		System.out.println("Testing");
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(800, 480);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () 
	{
		System.out.println("Test");
		return new RainbowRage();
	}

	@Override
	public Net getNet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clipboard getClipboard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log(String tag, String message, Throwable exception)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLogLevel()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}