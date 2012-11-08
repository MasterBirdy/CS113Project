package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.entity.Minion;
import com.me.mygdxgame.map.Coordinate;
import com.me.mygdxgame.map.Map;

public class MyGdxGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Texture circleTexture;
	private TextureRegion circle;
	private Sprite sprite;
	private Sprite circleSprite;
	private Minion circleMinion;
	private Map map;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		
		//texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture = new Texture(Gdx.files.internal("data/mockupmap.png"));
		circleTexture = new Texture(Gdx.files.internal("data/circleEntity.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		circleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 800, 600);
		circle = new TextureRegion(circleTexture, 0, 0, 41, 49);

		sprite = new Sprite(region);
		circleSprite = new Sprite(circle);
		
		map = new Map(new Coordinate(251, 200));
		map.add(new Coordinate(251, 0));
		map.add(new Coordinate(-270, 0));
		map.add(new Coordinate(-270, -230));
		map.add(new Coordinate(300, -230));
		
		circleMinion = new Minion(-300, 200, map.getPath(), circleSprite);
		circleSprite.setPosition((0 - (w / 2 - circleMinion.getPositionX())), (h / 2 - circleMinion.getPositionY()));
		//circleSprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		//circleSprite.setPosition(2f, 2f);
		
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		circleMinion.timeToChangePath();
		circleMinion.move();
		circleSprite.setPosition(circleMinion.getPositionX(), circleMinion.getPositionY());
		circleSprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
