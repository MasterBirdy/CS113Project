package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class CreditsScreen implements Screen{

	MyGdxGame game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	Vector3 touchPoint;
	Rectangle wholeScreen;
	Sprite creditsSprite;
	Sprite backgroundSprite;

	public CreditsScreen(MyGdxGame game)
	{
		this.game = game;
		float w = 800; //Gdx.graphics.getWidth();
		float h = 480; //Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(w, h);
		touchPoint = new Vector3();
		Texture credits = new Texture(Gdx.files.internal("images/credits.png"));
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/mainmenubackground.png"));
		TextureRegion backgroundRegion = new TextureRegion(backgroundTexture, 0, 0, 800, 480);
		TextureRegion creditsRegion = new TextureRegion(credits, 0, 0, 664, 345);
		creditsSprite = new Sprite(creditsRegion);
		creditsSprite.setPosition(w / 2 - creditsSprite.getWidth() / 2, h / 2 - creditsSprite.getHeight() / 2);
		wholeScreen = new Rectangle(0, 0, w, h);
		backgroundSprite = new Sprite(backgroundRegion);
		
	}
	@Override
	public void render(float delta) {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//batch.setProjectionMatrix(camera.combined);
		update(delta);
		batch.begin();
		backgroundSprite.draw(batch);
		creditsSprite.draw(batch);
		batch.end();
	}
	
	public void update (float deltaTime) 
	{
		if (Gdx.input.justTouched()) {
				game.setScreen(game.settingsScreen);
		}
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
