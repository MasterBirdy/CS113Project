package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.MyTextInputListener;

public class LoginUI extends BaseClass
{
	int x = 100, y = 100;
	float scale = everything.getScreenScale();
	boolean shown = true;
	MyTextInputListener listener = new MyTextInputListener();
	TextField userNameField;
	TextFieldStyle style;
	
	public LoginUI()
	{
//		Gdx.input.getTextInput(listener, "Dialog title", "Initial Value");
//		style = new TextFieldStyle();
//		style.fontColor = Color.WHITE;
//		style.font = EverythingHolder.getFont(0);
//		userNameField = new TextField("Testing!", style);
//		userNameField.setX(x);
//		userNameField.setY(y);
//		userNameField.setBounds(x, y, x + 100, y + 20);
//		userNameField.setSize(100, 20);
	}
	
	public void hit(float x, float y)
	{
		
	}
	
	public void show(SpriteBatch batch, float delta)
	{
//		userNameField.draw(batch, 1f);
//		userNameField.act(delta);
		
//		Gdx.input.getTextInput(listener, "Dialog title", "Initial Value");
	}
}
