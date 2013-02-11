package com.me.mygdxgame;

import java.util.ArrayList;

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

public class SettingsScreen  implements Screen  {

	Sprite backSprite;
	Sprite backgroundSprite;
	Sprite difficultySprite;
	Sprite soundSprite;
	MyGdxGame game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	Vector3 touchPoint;
	private Sprite musicSprite;
	private Sprite graphicsSprite;
	private Button lowGraphicsButton;
	private Button highGraphicsButton;
	private Button lowHighlightedGraphicsButton;
	private Button highHighlightedGraphicsButton;
	private Button onSoundButton;
	private Button offSoundButton;
	private Button onHighlightedSoundButton;
	private Button offHighlightedSoundButton;
	private Button onButton;
	private Button offButton;
	private Button onHighlightedButton;
	private Button offHighlightedButton;
	private Button easyButton;
	private Button hardButton;
	private Button easyHighlightedButton;
	private Button hardHighlightedButton;
	private Button backButton;
	private Sprite hardSprite;


	public SettingsScreen(MyGdxGame game) {
		this.game = game;
		float w = 800; //Gdx.graphics.getWidth();
		float h = 480; //Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(w, h);
		touchPoint = new Vector3();
		Texture textTexture = new Texture(Gdx.files.internal("images/textmenuscreen.png"));
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/mainmenubackground.png"));
		backgroundSprite = new Sprite(new TextureRegion(backgroundTexture, 0, 0, 800, 480));

		backSprite = new Sprite(new TextureRegion(textTexture, 5, 179, 125, 40));
		hardSprite = new Sprite(new TextureRegion(textTexture, 9, 345, 118, 34));
		
		backButton = new Button(w - backSprite.getWidth() - 20, backSprite.getHeight() - 10, w, h, backSprite);

		difficultySprite = new Sprite(new TextureRegion(textTexture, 8, 224, 267, 42));
		difficultySprite.setPosition(w / 2 - 280 - 20, h / 2 + 141);
		
		easyButton = new Button(w / 2 + 43  - 20, h / 2 + 149, w, h, new Sprite(new TextureRegion(textTexture, 8, 282, 118, 34)));
		hardButton = new Button(w / 2 + hardSprite.getWidth() + 70  - 20, h / 2 + 149, w, h, hardSprite);
		easyHighlightedButton = new Button(w / 2 + 43  - 20 - 6, h / 2 + 149 - 6, w, h, new Sprite(new TextureRegion(textTexture, 149, 276, 130, 46)));
		hardHighlightedButton = new Button(w / 2 + hardSprite.getWidth() + 70 - 6  - 20, h / 2 + 143, h, w, new Sprite(new TextureRegion(textTexture, 154, 339, 131, 46)));
		easyButton.setVisibility(false);
		hardHighlightedButton.setVisibility(false);
		
		soundSprite = new Sprite(new TextureRegion(textTexture, 7, 399, 163, 34));
		soundSprite.setPosition(w / 2 - 280  - 20 + (difficultySprite.getWidth() - soundSprite.getWidth()), h / 2 + 19);

		musicSprite = new Sprite(new TextureRegion(textTexture, 14, 822, 151, 34));
		musicSprite.setPosition(w / 2 - 280  - 20 + (difficultySprite.getWidth() - musicSprite.getWidth()), h / 2 + 82);

		graphicsSprite = new Sprite(new TextureRegion(textTexture, 13, 873, 234, 38));
		graphicsSprite.setPosition(w / 2 - 280 - 20 + (difficultySprite.getWidth() - graphicsSprite.getWidth()), h / 2 + 19 - 62);

		onButton = new Button(w / 2 + 43  - 20, h / 2 + 79, w, h, new Sprite(new TextureRegion(textTexture, 8, 449, 56, 34)));
		offButton = new Button(w / 2 + hardSprite.getWidth() + 70 - 20, h / 2 + 79, w, h, new Sprite(new TextureRegion(textTexture, 190, 449, 87, 34)));
		onHighlightedButton = new Button(w / 2 + 37  - 20, h / 2 + 73, w, h, new Sprite(new TextureRegion(textTexture, 97, 443, 68, 46)));
		offHighlightedButton = new Button(w / 2 + hardSprite.getWidth() + 64 - 20, h / 2 + 73, w, h, new Sprite(new TextureRegion(textTexture, 302, 443, 99, 46)));
		onButton.setVisibility(false);
		offHighlightedButton.setVisibility(false);
		
		onSoundButton = new Button(w / 2 + 43 - 20, soundSprite.getY() - 1, w, h, new Sprite(new TextureRegion(textTexture, 8, 449, 56, 34)));
		offSoundButton = new Button(w / 2 + hardSprite.getWidth() + 70 - 20, soundSprite.getY() - 1, w, h, new Sprite(new TextureRegion(textTexture, 190, 449, 87, 34)));
		onHighlightedSoundButton = new Button(onSoundButton.getX() - 6, onSoundButton.getY() - 6, w, h, new Sprite(new TextureRegion(textTexture, 97, 443, 68, 46)));
		offHighlightedSoundButton = new Button(offSoundButton.getX() - 6, offSoundButton.getY() - 6, w, h,new Sprite(new TextureRegion(textTexture, 302, 443, 99, 46)));
		onSoundButton.setVisibility(false);
		offHighlightedSoundButton.setVisibility(false);
		
		lowGraphicsButton = new Button(w / 2 + 43 - 20, graphicsSprite.getY(), w, h, new Sprite(new TextureRegion(textTexture, 277, 875, 87, 34)));
		highGraphicsButton = new Button(w / 2 + hardSprite.getWidth() + 70 - 20, graphicsSprite.getY(), w, h, new Sprite(new TextureRegion(textTexture, 385, 876, 120, 34)));
		lowHighlightedGraphicsButton = new Button(lowGraphicsButton.getX() - 6, lowGraphicsButton.getY() - 6, w, h, new Sprite(new TextureRegion(textTexture, 219, 926, 99, 46)));
		highHighlightedGraphicsButton = new Button(highGraphicsButton.getX() - 6, highGraphicsButton.getY() - 6, w, h, new Sprite(new TextureRegion(textTexture, 346, 926, 132, 46)));
		highGraphicsButton.setVisibility(false);
		lowHighlightedGraphicsButton.setVisibility(false);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//batch.setProjectionMatrix(camera.combined);
		update(delta);
		batch.begin();
		
		backgroundSprite.draw(batch);
		backButton.draw(batch);
		difficultySprite.draw(batch);
		soundSprite.draw(batch);
		musicSprite.draw(batch);
		graphicsSprite.draw(batch);

		easyButton.draw(batch);
		hardButton.draw(batch);
		easyHighlightedButton.draw(batch);
		hardHighlightedButton.draw(batch);
		
		onButton.draw(batch);
		offButton.draw(batch);
		onHighlightedButton.draw(batch);
		offHighlightedButton.draw(batch);
		
		onSoundButton.draw(batch);
		offSoundButton.draw(batch);
		onHighlightedSoundButton.draw(batch);
		offHighlightedSoundButton.draw(batch);
		
		lowGraphicsButton.draw(batch);
		highGraphicsButton.draw(batch);
		lowHighlightedGraphicsButton.draw(batch);
		highHighlightedGraphicsButton.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	public void update (float deltaTime) 
	{
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(backButton.getRectangle(), touchPoint.x, touchPoint.y))
			{
				game.setScreen(game.mainMenuScreen);
			}
			else if (OverlapTester.pointInRectangle(easyButton.getRectangle(), touchPoint.x, touchPoint.y))
			{
				Settings.getInstance().setDifficulty(Difficulty.EASY);
				easyButton.setVisibility(false);
				easyHighlightedButton.setVisibility(true);
				hardButton.setVisibility(true);
				hardHighlightedButton.setVisibility(false);
			}
			else if (OverlapTester.pointInRectangle(hardButton.getRectangle(), touchPoint.x, touchPoint.y))
			{
				Settings.getInstance().setDifficulty(Difficulty.HARD);
				easyButton.setVisibility(true);
				easyHighlightedButton.setVisibility(false);
				hardButton.setVisibility(false);
				hardHighlightedButton.setVisibility(true);
			}
			else if (OverlapTester.pointInRectangle(onButton.getRectangle(), touchPoint.x, touchPoint.y))
			{
				Settings.getInstance().setMusic(MusicSound.ON);
				onButton.setVisibility(false);
				onHighlightedButton.setVisibility(true);
				offButton.setVisibility(true);
				offHighlightedButton.setVisibility(false);
			}
			else if (OverlapTester.pointInRectangle(offButton.getRectangle(), touchPoint.x, touchPoint.y))
			{
				Settings.getInstance().setMusic(MusicSound.OFF);
				onButton.setVisibility(true);
				onHighlightedButton.setVisibility(false);
				offButton.setVisibility(false);
				offHighlightedButton.setVisibility(true);
			}
			else if(OverlapTester.pointInRectangle(onSoundButton.getRectangle(), touchPoint.x, touchPoint.y)){
				Settings.getInstance().setSound(Sound.ON);
				onSoundButton.setVisibility(false);
				onHighlightedSoundButton.setVisibility(true);
				offSoundButton.setVisibility(true);
				offHighlightedSoundButton.setVisibility(false);
			}
			else if(OverlapTester.pointInRectangle(offSoundButton.getRectangle(), touchPoint.x, touchPoint.y)){
				Settings.getInstance().setSound(Sound.OFF);
				onSoundButton.setVisibility(true);
				onHighlightedSoundButton.setVisibility(false);
				offSoundButton.setVisibility(false);
				offHighlightedSoundButton.setVisibility(true);
			}
			else if (OverlapTester.pointInRectangle(lowGraphicsButton.getRectangle(), touchPoint.x, touchPoint.y))
			{
				Settings.getInstance().setGraphics(Graphics.LOW);
				lowGraphicsButton.setVisibility(false);
				lowHighlightedGraphicsButton.setVisibility(true);
				highGraphicsButton.setVisibility(true);
				highHighlightedGraphicsButton.setVisibility(false);
			}
			else if (OverlapTester.pointInRectangle(highGraphicsButton.getRectangle(), touchPoint.x, touchPoint.y)){
				Settings.getInstance().setGraphics(Graphics.HIGH);
				lowGraphicsButton.setVisibility(true);
				lowHighlightedGraphicsButton.setVisibility(false);
				highGraphicsButton.setVisibility(false);
				highHighlightedGraphicsButton.setVisibility(true);
			}
		}
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
