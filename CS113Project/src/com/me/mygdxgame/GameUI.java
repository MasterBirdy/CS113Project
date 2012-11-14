package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameUI 
{
	static SpriteBatch batch;
	private Texture sideUI;
	BitmapFont font;
	static EverythingHolder everything;
	
	public GameUI()
	{
		sideUI = new Texture(Gdx.files.internal("images/sideUI.png"));
		font = new BitmapFont();
	}
	
	static public void load(SpriteBatch b, EverythingHolder things)
	{
		batch = b;
		everything = things;
	}
	
	public void render()
	{
		batch.draw(sideUI, Gdx.graphics.getWidth() / 2 - sideUI.getWidth(), Gdx.graphics.getHeight() / 2 - sideUI.getHeight());
		font.draw(batch, "Total Units: " + (everything.team(1).size() + everything.team(2).size()), Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 20);
	}
	
	public int width()
	{
		return sideUI.getWidth();
	}
}
