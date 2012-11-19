package com.me.mygdxgame;

import com.badlogic.gdx.Game;
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

public class MainMenuScreen implements Screen {

	Rectangle highscoresBounds;
	Game game;
	private OrthographicCamera camera;
	Sprite sprite;
	Sprite titleSprite;
	Sprite playSprite;
	Rectangle playRectangle;
	private SpriteBatch batch;
	Vector3 touchPoint;

	public MainMenuScreen(Game game)
	{
		this.game = game;
		highscoresBounds = new Rectangle(160 - 150, 200 - 18, 300, 36);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(w, h);
		Texture texture = new Texture(Gdx.files.internal("images/mainmenubackground.png"));
		Texture textTexture = new Texture(Gdx.files.internal("images/textmenuscreen.png"));
		TextureRegion region = new TextureRegion(texture, 0, 0, 800, 600);
		TextureRegion textRegion = new TextureRegion(textTexture, 0, 0, 451, 49);
		TextureRegion textRegionPlay = new TextureRegion(textTexture, 0, 50, 126, 47);
		sprite = new Sprite(region);
		titleSprite = new Sprite(textRegion);
		playSprite = new Sprite(textRegionPlay);
		titleSprite.setOrigin(0,0);
		playSprite.setOrigin(0,0);
		titleSprite.setPosition(w / 2 - titleSprite.getWidth() / 2, 370);
		System.out.println(w);
		System.out.println(h);
		System.out.println(w / 2 - titleSprite.getWidth());
		playSprite.setPosition(w / 2 - playSprite.getWidth() / 2, 100);
		playRectangle = new Rectangle(w / 2 - playSprite.getWidth() / 2 - 20 - w / 2, 100 - 20 - h / 2, playSprite.getWidth() + 40, playSprite.getHeight() + 40);
		touchPoint = new Vector3();
	}

	@Override
	public void render(float delta) {
		update(delta);
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		titleSprite.draw(batch);
		playSprite.draw(batch);
		batch.end();
	}

	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			//System.out.println(touchPoint.x + " " + touchPoint.y);
			if (OverlapTester.pointInRectangle(playRectangle, touchPoint.x, touchPoint.y)) {
				//System.out.println(true);
				game.setScreen(new GameScreen(game));
				return;
			}
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
