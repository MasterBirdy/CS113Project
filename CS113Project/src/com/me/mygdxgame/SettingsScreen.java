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

public class SettingsScreen  implements Screen  {
	
	Sprite backSprite, backgroundSprite, creditsSprite;
	Sprite difficultySprite;
	Sprite easySprite;
	Sprite easyHighlightedSprite;
	Sprite hardSprite;
	Sprite hardHighlightedSprite;
	Sprite soundSprite;
	Sprite onSprite;
	Sprite offSprite;
	Sprite onHighlightedSprite;
	Sprite offHighlightedSprite;
	MyGdxGame game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	Rectangle backRectangle;
	Rectangle easyRectangle;
	Rectangle hardRectangle;
	Rectangle onRectangle;
	Rectangle offRectangle;
	Rectangle creditsRectangle;
	Vector3 touchPoint;
	
	
	public SettingsScreen(MyGdxGame game) {
		this.game = game;
		float w = 800; //Gdx.graphics.getWidth();
		float h = 480; //Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(w, h);
		touchPoint = new Vector3();
		Texture textTexture = new Texture(Gdx.files.internal("images/textmenuscreen.png"));
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/mainmenubackground.png"));
		TextureRegion backRegion = new TextureRegion(textTexture, 5, 179, 125, 40);
		TextureRegion difficultyRegion = new TextureRegion(textTexture, 8, 224, 267, 42);
		TextureRegion backgroundRegion = new TextureRegion(backgroundTexture, 0, 0, 800, 480);
		TextureRegion easyRegion = new TextureRegion(textTexture, 8, 282, 118, 34);
		TextureRegion easyHighlightedRegion = new TextureRegion(textTexture, 149, 276, 130, 46);
		TextureRegion hardRegion = new TextureRegion(textTexture, 9, 345, 118, 34);
		TextureRegion hardHighlightedRegion = new TextureRegion(textTexture, 154, 339, 131, 46);
		TextureRegion soundRegion = new TextureRegion(textTexture, 7, 399, 163, 34);
		TextureRegion onRegion = new TextureRegion(textTexture, 8, 449, 56, 34);
		TextureRegion onHighlightedRegion = new TextureRegion(textTexture, 97, 443, 68, 46);
		TextureRegion offRegion = new TextureRegion(textTexture, 190, 449, 87, 34);
		TextureRegion offHighlightedRegion = new TextureRegion(textTexture, 302, 443, 99, 46);
		TextureRegion creditsRegion = new TextureRegion(textTexture, 8, 569, 190, 31);
		backSprite = new Sprite(backRegion);
		backRectangle = new Rectangle(w - backSprite.getWidth() - 20 - 20 - w / 2, backSprite.getHeight() - 10 - 20 - h / 2, backSprite.getWidth() + 40, backSprite.getHeight() + 40);
		difficultySprite = new Sprite(difficultyRegion);
		backgroundSprite = new Sprite(backgroundRegion);
		easySprite = new Sprite(easyRegion);
		easyHighlightedSprite = new Sprite(easyHighlightedRegion);
		hardSprite = new Sprite(hardRegion);
		hardHighlightedSprite = new Sprite(hardHighlightedRegion);
		soundSprite = new Sprite(soundRegion);
		onSprite = new Sprite(onRegion);
		onHighlightedSprite = new Sprite(onHighlightedRegion);
		offSprite = new Sprite(offRegion);
		offHighlightedSprite = new Sprite(offHighlightedRegion);
		creditsSprite = new Sprite(creditsRegion);
		backSprite.setPosition(w - backSprite.getWidth() - 20, backSprite.getHeight() - 10);
		difficultySprite.setPosition(w / 2 - 280 - 20, h / 2 + 141);
		easySprite.setPosition(w / 2 + 43  - 20, h / 2 + 149);
		easyHighlightedSprite.setPosition(w / 2 + 37  - 20, h / 2 + 143);
		hardSprite.setPosition(w / 2 + hardSprite.getWidth() + 70  - 20, h / 2 + 149);
		hardHighlightedSprite.setPosition(w / 2 + hardSprite.getWidth() + 70 - 6  - 20, h / 2 + 143);
		soundSprite.setPosition(w / 2 - 280  - 20 + (difficultySprite.getWidth() - soundSprite.getWidth()), h / 2 + 79);
		onSprite.setPosition(w / 2 + 43  - 20, h / 2 + 79);
		onHighlightedSprite.setPosition(w / 2 + 37  - 20, h / 2 + 73);
		offSprite.setPosition(w / 2 + hardSprite.getWidth() + 70 - 20, h / 2 + 79);
		offHighlightedSprite.setPosition(w / 2 + hardSprite.getWidth() + 64 - 20, h / 2 + 73);
		creditsSprite.setPosition(w / 2 - creditsSprite.getWidth() / 2, h / 2 + 7);
		easyRectangle = new Rectangle(w / 2 + 43 - 20 - w / 2, h / 2 + 149 - 20 - h / 2  - 20, easySprite.getWidth() + 40, easySprite.getHeight() + 40);
		hardRectangle = new Rectangle(w / 2 + hardSprite.getWidth() + 70 - 20 - w / 2  - 20, h / 2 + 149 - 20 - h / 2, hardSprite.getWidth() + 40, hardSprite.getHeight() + 40);
		onRectangle = new Rectangle(w / 2 + 43 - 20 - 20 - w / 2, h / 2 + 79 - 20 - h / 2, onSprite.getWidth() + 40, onSprite.getHeight()+ 40);
		offRectangle = new Rectangle(w / 2 + offSprite.getWidth() + 70- 20 - 20 - w / 2, h / 2 + 79 - 20 - h / 2, offSprite.getWidth() + 40, offSprite.getHeight() + 40);
		creditsRectangle = new Rectangle (w / 2 - creditsSprite.getWidth() / 2 - 5 - w / 2, h / 2 + 7 - 5 - h / 2, creditsSprite.getWidth() + 10, creditsSprite.getHeight() + 10);
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
		backSprite.draw(batch);
		difficultySprite.draw(batch);
		soundSprite.draw(batch);
		if (Settings.getInstance().getDifficulty() == Difficulty.HARD)
		{
		easySprite.draw(batch);
		hardHighlightedSprite.draw(batch);
		}
		else if (Settings.getInstance().getDifficulty() == Difficulty.EASY)
		{
		easyHighlightedSprite.draw(batch);
		hardSprite.draw(batch);
		}
		if (Settings.getInstance().getSound() == Sound.ON)
		{
			onHighlightedSprite.draw(batch);
			offSprite.draw(batch);
		}
		else if (Settings.getInstance().getSound() == Sound.OFF)
		{
			onSprite.draw(batch);
			offHighlightedSprite.draw(batch);
		}
		creditsSprite.draw(batch);
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
			//System.out.println(touchPoint.x + " " + touchPoint.y);
			if (OverlapTester.pointInRectangle(backRectangle, touchPoint.x, touchPoint.y))
			{
				game.setScreen(game.mainMenuScreen);
			}
			else if (OverlapTester.pointInRectangle(easyRectangle, touchPoint.x, touchPoint.y))
			{
				Settings.getInstance().setDifficulty(Difficulty.EASY);
			}
			else if (OverlapTester.pointInRectangle(hardRectangle, touchPoint.x, touchPoint.y))
			{
				Settings.getInstance().setDifficulty(Difficulty.HARD);
			}
			else if (OverlapTester.pointInRectangle(onRectangle, touchPoint.x, touchPoint.y))
			{
				Settings.getInstance().setSound(Sound.ON);
			}
			else if (OverlapTester.pointInRectangle(offRectangle, touchPoint.x, touchPoint.y))
			{
				Settings.getInstance().setSound(Sound.OFF);
			}
			else if (OverlapTester.pointInRectangle(creditsRectangle, touchPoint.x, touchPoint.y))
			{
				game.setScreen(game.creditsScreen);
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
