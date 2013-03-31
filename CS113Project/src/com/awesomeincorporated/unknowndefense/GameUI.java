package com.awesomeincorporated.unknowndefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameUI 
{
	static SpriteBatch batch;
	private Texture icons; 
	private TextureRegion sideUI;
	private TextureRegion swordIcon;
	private TextureRegion bowIcon;
	private TextureRegion serfIcon;
	private TextureRegion magicIcon;
	private TextureRegion petIcon;
	private TextureRegion spiralIcon;
	private TextureRegion attackIcon;
	private TextureRegion defendIcon;
	private TextureRegion retreatIcon;
	BitmapFont font;
	static EverythingHolder everything;
	
	float screenX, screenY;
	
	public GameUI()
	{
		sideUI = new TextureRegion(new Texture(Gdx.files.internal("images/sideui.png")), 200, 480);
		
		icons = new Texture(Gdx.files.internal("images/icons.jpg"));
		
		swordIcon = new TextureRegion(icons, 0, 0, 69, 80);
		bowIcon = new TextureRegion(icons, 69, 0, 69, 80);
		serfIcon = new TextureRegion(icons, 138, 0, 69, 80);
		magicIcon = new TextureRegion(icons, 0, 80, 69, 80);
		petIcon = new TextureRegion(icons, 69, 80, 69, 80);
		spiralIcon = new TextureRegion(icons, 138, 80, 69, 80);
		attackIcon = new TextureRegion(icons, 207, 0, 40, 40);
		defendIcon = new TextureRegion(icons, 207, 40, 40, 40);
		retreatIcon = new TextureRegion(icons, 207, 80, 40, 40);
		
//		swordIcon = new TextureRegion(new Texture(Gdx.files.internal("images/swordicon.png")), 69, 80);
//		bowIcon = new TextureRegion(new Texture(Gdx.files.internal("images/bowicon.png")), 69, 80);
//		serfIcon = new TextureRegion(new Texture(Gdx.files.internal("images/serficon.png")), 69, 80);
//		magicIcon = new TextureRegion(new Texture(Gdx.files.internal("images/magiciconbnw.png")), 69, 80);
//		petIcon = new TextureRegion(new Texture(Gdx.files.internal("images/peticonbnw.png")), 69, 80);
//		spiralIcon = new TextureRegion(new Texture(Gdx.files.internal("images/spiraliconbnw.png")), 69, 80);
//		attackIcon = new TextureRegion(new Texture(Gdx.files.internal("images/attackicon.png")), 40, 40);
//		defendIcon = new TextureRegion(new Texture(Gdx.files.internal("images/holdicon.png")), 40, 40);
//		retreatIcon = new TextureRegion(new Texture(Gdx.files.internal("images/retreaticon.png")), 40, 40);
		font = new BitmapFont();
		screenX = Gdx.graphics.getWidth() / 2;
		screenY = Gdx.graphics.getHeight() / 2;
	}
	
	static public void load(SpriteBatch b, EverythingHolder things)
	{
		batch = b;
		everything = things;
	}
	
	public void render()
	{
		batch.draw(sideUI, screenX - sideUI.getRegionWidth(), screenY - sideUI.getRegionHeight());
//		batch.draw(swordIcon, Gdx.graphics.getWidth() / 2 - sideUI.getRegionWidth() + 10 + 5, Gdx.graphics.getHeight() / 2 - swordIcon.getRegionHeight() * 2 - 10);
//		batch.draw(bowIcon, Gdx.graphics.getWidth() / 2 - sideUI.getRegionWidth() + 10 * 2 + swordIcon.getRegionWidth() + 5, Gdx.graphics.getHeight() / 2 - swordIcon.getRegionHeight() * 2 - 10);
//		batch.draw(serfIcon, Gdx.graphics.getWidth() / 2 - sideUI.getRegionWidth() + 10 * 3 + swordIcon.getRegionWidth() * 2 + 5, Gdx.graphics.getHeight() / 2 - swordIcon.getRegionHeight() * 2 - 10);
//<<<<<<< HEAD
//		batch.draw(swordIcon, 221, -29, 68, 80);
//		batch.draw(bowIcon, 310, -29, 68, 80);
//		batch.draw(serfIcon, 221, -125, 68, 80);
//		batch.draw(magicIcon, 310, -125, 68, 80);
//		batch.draw(petIcon, 221, -226, 68, 80);
//		batch.draw(spiralIcon, 310, -226, 68, 80);
//=======
		batch.draw(swordIcon, 221, -29);
		batch.draw(bowIcon, 311, -29);
		batch.draw(serfIcon, 221, -127);
		batch.draw(magicIcon, 311, -127);
		batch.draw(petIcon, 221, -227);
		batch.draw(spiralIcon, 311, -227);
//>>>>>>> Matthew-Branch-2
		batch.draw(attackIcon, -50, -200);
		batch.draw(defendIcon, -100, -200);
		batch.draw(retreatIcon, -150, -200);
		//font.draw(batch, "Total Units: " + (everything.team(1).size() + everything.team(2).size()), Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 20);
		font.draw(batch, "Next wave: " + everything.timeLeft(), screenX - 150, screenY - 20);
		font.draw(batch, "Time: " + everything.totalTime(), screenX - 150, screenY - 45);
		font.draw(batch, "Funds: " + everything.funds(), screenX - 150, screenY - 70);
	}
	
	public int width()
	{
		return sideUI.getRegionWidth();
	}
}