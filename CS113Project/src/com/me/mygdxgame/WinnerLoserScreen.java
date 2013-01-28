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

public class WinnerLoserScreen implements Screen {
	
	private MyGdxGame game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Vector3 touchPoint;
	private Sprite loserSprite;
	private Sprite winnerSprite;
	private boolean whoWon;
	private Sprite continueSprite;
	private Rectangle continueRectangle;
	private Sprite backgroundSprite;
	
	public WinnerLoserScreen (MyGdxGame game, boolean whoWon)
	{
		this.game = game;
		float w = 800; //Gdx.graphics.getWidth();
		float h = 480; //Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(w, h);
		touchPoint = new Vector3();
		Texture winnerLoserTexture = new Texture(Gdx.files.internal("images/youlose.png"));
		Texture textTexture = new Texture(Gdx.files.internal("images/textmenuscreen.png"));
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/mainmenubackground.png"));
		backgroundSprite = new Sprite(new TextureRegion(backgroundTexture, 0, 0, 800, 480));
		
		loserSprite = new Sprite(new TextureRegion(winnerLoserTexture, 0, 1, 474, 180));
		loserSprite.setPosition((w - loserSprite.getWidth()) / 2, 270);
		
		winnerSprite = new Sprite(new TextureRegion(winnerLoserTexture, 0, 192, 474, 180));
		winnerSprite.setPosition((w - winnerSprite.getWidth()) / 2, 270);
		
		continueSprite = new Sprite(new TextureRegion(textTexture, 7, 508, 225, 34));
		continueSprite.setPosition(w / 2 - continueSprite.getWidth() / 2, 100);
		continueRectangle = new Rectangle(w / 2 - continueSprite.getWidth() / 2 - w / 2 - 20, 100 - 20 - h/2, continueSprite.getWidth() + 40, continueSprite.getHeight() + 40);
		
		this.whoWon = whoWon;
	}

	@Override
	public void render(float delta) {
		update(delta);
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		backgroundSprite.draw(batch);
		if (whoWon)
			winnerSprite.draw(batch);
		else
			loserSprite.draw(batch);
		continueSprite.draw(batch);
		batch.end();
	}

	private void update(float delta) {
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			//System.out.println(touchPoint.x + " " + touchPoint.y);
			if (OverlapTester.pointInRectangle(continueRectangle, touchPoint.x, touchPoint.y)) {
				game.setScreen(game.mainMenuScreen);
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
