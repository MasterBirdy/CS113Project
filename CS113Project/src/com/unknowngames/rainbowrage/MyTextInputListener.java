package com.unknowngames.rainbowrage;
import com.badlogic.gdx.Input.TextInputListener;


public class MyTextInputListener implements TextInputListener
{
	String text = "";

	@Override
	public void input(String text)
	{
		this.text = text;
	}

	@Override
	public void canceled()
	{
	}
	
	public String getText()
	{
		return text;
	}

}
