package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameUI 
{
	static SpriteBatch batch;
	private TextureRegion sideUI;
	BitmapFont font;
	static EverythingHolder everything;
	
	public GameUI()
	{
		sideUI = new TextureRegion(new Texture(Gdx.files.internal("images/sideui.png")), 200, 480);
		font = new BitmapFont();
	}
	
	static public void load(SpriteBatch b, EverythingHolder things)
	{
		batch = b;
		everything = things;
	}
	
	public void render()
	{
		batch.draw(sideUI, Gdx.graphics.getWidth() / 2 - sideUI.getRegionWidth(), Gdx.graphics.getHeight() / 2 - sideUI.getRegionHeight());
		font.draw(batch, "Total Units: " + (everything.team(1).size() + everything.team(2).size()), Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 20);
	}
	
	public int width()
	{
		return sideUI.getRegionWidth();
	}
}
