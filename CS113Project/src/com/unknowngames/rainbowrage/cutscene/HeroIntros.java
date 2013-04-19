package com.unknowngames.rainbowrage.cutscene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.RainbowRage;

public class HeroIntros implements Screen
{
	HeroPortrait heroes[] = new HeroPortrait[4];
	OrthographicCamera camera;
	SpriteBatch batch;
	int turn = 0;
	float fade = 0;
	EverythingHolder everything;
	BitmapFont font = new BitmapFont();
	int capturedelay = 600;
	
	public HeroIntros(RainbowRage game)//, EverythingHolder everything)
	{
		everything = new EverythingHolder(); // TEMP
		
		int width = Gdx.graphics.getWidth(),
			height = Gdx.graphics.getHeight();
		float ratio = height / 835;
		heroes[0] = new HeroPortrait(new TextureRegion(new Texture(Gdx.files.internal("images/portrait_swordface.png")), 0, 0, 1024, 835));
		heroes[1] = new HeroPortrait(new TextureRegion(new Texture(Gdx.files.internal("images/portrait_arroweyes.png")), 0, 0, 1024, 835));
		heroes[2] = new HeroPortrait(new TextureRegion(new Texture(Gdx.files.internal("images/portrait_mrwizard.png")), 0, 0, 1024, 835));
		heroes[3] = new HeroPortrait(new TextureRegion(new Texture(Gdx.files.internal("images/buttons_sheet.png")), 0, 1514, 842, 467));
		
//		gameLogo = new TextureRegion(icons, 1319, 0, 382, 175);
		
//		heroes[0].setPosition(211, 0);
//		heroes[1].setPosition(0, 0);
//		heroes[2].setPosition(104, 0);
		for (int i = 0; i < 3; i++)
			heroes[i].setSize(589, height);
		heroes[3].setSize(500, 277);
		
		heroes[0].slide(800, 0, 211, 0, -7, 0);
		heroes[1].slide(-589, 0, 0, 0, 7, 0);
		heroes[2].slide(104, -height, 104, 0, 0, 7);
		heroes[3].slide(140, height + 10, 140, 115, 0, -9);
		heroes[3].setHold(450);
		batch = new SpriteBatch();
		camera = new OrthographicCamera(width, height);
		
		font = everything.getFont(2);
	}
	
	@Override
	public void render(float delta) 
	{
		if (--capturedelay > 0)
			return;
		update();
		GL10 gl = Gdx.graphics.getGL10();
		if (turn < 3)
			gl.glClearColor(0, 0, 0, 1);
		else
		{
			fade += 0.03f;
			gl.glClearColor(fade, fade, fade, 1);
		}
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		heroes[turn].draw(batch);
		if (turn == 0)
			font.draw(batch, "Swordface", 20, 200);
		else if (turn == 1)
			font.draw(batch, "Arroweyes", 550, 200);
		else if (turn == 2)
			font.draw(batch, "Mr. Wizard", 30, 440);
//		for (int i = 0; i < heroes.length; i++)
//			heroes[i].draw(batch);
		batch.end();
	}
	
	public void update()
	{
		if (heroes[turn].update())
			turn++;
		if (turn > 3)
			turn = 3;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
