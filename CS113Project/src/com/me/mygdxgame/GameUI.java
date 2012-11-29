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
	private TextureRegion swordIcon;
	private TextureRegion bowIcon;
	private TextureRegion serfIcon;
	private TextureRegion magicIcon;
	private TextureRegion petIcon;
	private TextureRegion spiralIcon;
	BitmapFont font;
	static EverythingHolder everything;
	
	public GameUI()
	{
		sideUI = new TextureRegion(new Texture(Gdx.files.internal("images/sideui.png")), 200, 480);
		swordIcon = new TextureRegion(new Texture(Gdx.files.internal("images/swordicon.png")), 40, 40);
		bowIcon = new TextureRegion(new Texture(Gdx.files.internal("images/bowicon.png")), 40, 40);
		serfIcon = new TextureRegion(new Texture(Gdx.files.internal("images/serficon.png")), 40, 40);
		magicIcon = new TextureRegion(new Texture(Gdx.files.internal("images/magiciconbnw.png")), 40, 40);
		petIcon = new TextureRegion(new Texture(Gdx.files.internal("images/peticonbnw.png")), 40, 40);
		spiralIcon = new TextureRegion(new Texture(Gdx.files.internal("images/spiraliconbnw.png")), 40, 40);
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
//		batch.draw(swordIcon, Gdx.graphics.getWidth() / 2 - sideUI.getRegionWidth() + 10 + 5, Gdx.graphics.getHeight() / 2 - swordIcon.getRegionHeight() * 2 - 10);
//		batch.draw(bowIcon, Gdx.graphics.getWidth() / 2 - sideUI.getRegionWidth() + 10 * 2 + swordIcon.getRegionWidth() + 5, Gdx.graphics.getHeight() / 2 - swordIcon.getRegionHeight() * 2 - 10);
//		batch.draw(serfIcon, Gdx.graphics.getWidth() / 2 - sideUI.getRegionWidth() + 10 * 3 + swordIcon.getRegionWidth() * 2 + 5, Gdx.graphics.getHeight() / 2 - swordIcon.getRegionHeight() * 2 - 10);
		batch.draw(swordIcon, 225, 8);
		batch.draw(bowIcon, 280, 8);
		batch.draw(serfIcon, 335, 8);
		batch.draw(magicIcon, 225, -41);
		batch.draw(petIcon, 280, -41);
		batch.draw(spiralIcon, 335, -41);
		font.draw(batch, "Total Units: " + (everything.team(1).size() + everything.team(2).size()), Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 20);
	}
	
	public int width()
	{
		return sideUI.getRegionWidth();
	}
}
