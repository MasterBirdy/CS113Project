package com.me.mygdxgame;

import java.util.Iterator;
import java.util.LinkedList;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.entity.*;
import com.me.mygdxgame.input.MyInputProcessor;
import com.me.mygdxgame.map.Coordinate;
import com.me.mygdxgame.map.Map;

public class MyGdxGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private Map map1;
	LinkedList<Actor> team1 = new LinkedList<Actor>();
	LinkedList<Actor> team2 = new LinkedList<Actor>();
	int counter1, counter2;
	BitmapFont font;
	static boolean showRange;
	MyInputProcessor inputProcessor;
	
	@Override
	public void create() 
	{
		Texture.setEnforcePotImages(false);
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		counter1 = 0;
		counter2 = 0;
		
		camera = new OrthographicCamera(w, h);
		camera.translate(400, 300);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/mockupmap.png"));
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 800, 600);

		sprite = new Sprite(region);
		
		map1 = new Map(new Coordinate(100, 1030), sprite, 1600, 1200, 100, 1030, 1300, 170);
		map1.add(new Coordinate(1300, 1000));
		map1.add(new Coordinate(1300, 600));
		map1.add(new Coordinate(300, 600));
		map1.add(new Coordinate(300, 170));
		map1.add(new Coordinate(1300, 170));
		
		Actor.linkActors(team1, team2);
		Actor.loadRange();
		Entity.loadSheet(new Texture(Gdx.files.internal("images/sprite_sheet.png")));
		Unit.loadAnimations();
		sprite.setSize(1600, 1200);
		font = new BitmapFont();
		showRange = true;
		inputProcessor = new MyInputProcessor();
		MyInputProcessor.loadCamera(camera);
		Gdx.input.setInputProcessor(inputProcessor);
	}
	
	static public void toggleShowRange()
	{
		showRange = showRange ? false : true;
	}

	@Override
	public void dispose() 
	{
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() 
	{
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		camera.apply(gl);
		
		update();
		handleInput();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		map1.background().draw(batch);
		
		if (showRange)
		{
			for (Actor a : team1)
				a.rangeIndicator(batch);
			for (Actor a : team2)
				a.rangeIndicator(batch);
		}
		
		Iterator<Actor> actorIter = team1.iterator();
		Actor a;
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a.isAlive())
				a.draw(batch);
			else
				actorIter.remove();
		}
		actorIter = team2.iterator();
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a.isAlive())
				a.draw(batch);
			else
				actorIter.remove();
		}
		
		font.draw(batch, "Total Units: " + (team1.size() + team2.size()), 800, 555);
		
		batch.end();
	}
	
	public void update()
	{
		for (Actor a : team1)
			a.checkAlive();
		for (Actor a : team2)
			a.checkAlive();
		Coordinate start1 = map1.start1();
		Coordinate start2 = map1.start2();
		
		if (--counter1 < 0)
		{
			boolean sword = Math.random() < 0.6;
			if (sword)
				team1.add(0, new Swordsman(start1.x(), start1.y(), 1, map1.getPath().iterator()));
			else
				team1.add(0, new Archer(start1.x(), start1.y(), 1, map1.getPath().iterator()));
			counter1 = (int)(Math.random() * 60) + 40;
		}
		if (--counter2 < 0)
		{
			boolean sword = Math.random() < 0.6;
			if (sword)
				team2.add(new Swordsman(start2.x(), start2.y(), 2, map1.getPath().descendingIterator()));
			else
				team2.add(new Archer(start2.x(), start2.y(), 2, map1.getPath().descendingIterator()));
			counter2 = (int)(Math.random() * 60) + 40;
		}

		for (Actor a : team2)
			a.update();
		for (Actor a : team1)
			a.update();
	}
	
	private void handleInput()
	{
		int w = sprite.getRegionWidth() / 2;
		int h = sprite.getRegionHeight() / 2;
		
		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
		{
			camera.translate(0, 10);
			if (camera.position.y > map1.height() - h)
				camera.position.y = map1.height() - h;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
		{
			camera.translate(0, -10);
			if (camera.position.y < h)
				camera.position.y = h;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
		{
			camera.translate(10, 0);
			if (camera.position.x > map1.width() - w)
				camera.position.x = map1.width() - w;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
		{
			camera.translate(-10, 0);
			if (camera.position.x < w)
				camera.position.x = w;
		}
	}

	@Override
	public void resize(int width, int height) 
	{
	}

	@Override
	public void pause() 
	{
	}

	@Override
	public void resume() 
	{
	}
}
