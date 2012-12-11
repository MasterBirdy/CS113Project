package com.me.mygdxgame;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen 
{

	Rectangle highscoresBounds;
	MyGdxGame game;
	private OrthographicCamera camera;
	Sprite sprite;
	Sprite titleSprite;
	Sprite playSprite;
	Sprite newGameSprite;
	Sprite settingsSprite;
	Sprite continueSprite;
	Sprite continueFadedSprite;
	Rectangle playRectangle;
	Rectangle newGameRectangle;
	Rectangle settingsRectangle;
	Rectangle continueRectangle;
	private SpriteBatch batch;
	Vector3 touchPoint;
	Audio tempMusic = Gdx.audio;
	Music startMusic;
	static ParticleEffect fire = new ParticleEffect();
	static ParticleEffect spark = new ParticleEffect();
	static ParticleEffect blood = new ParticleEffect();
	boolean newGameStarted;

	public MainMenuScreen(MyGdxGame game)
	{
		this.game = game;
		Settings.getInstance();
		newGameStarted = false;
		startMusic = tempMusic.newMusic(Gdx.files.internal("audio/Celeste.wav"));
		startMusic.setLooping(true);
		startMusic.play();
		highscoresBounds = new Rectangle(160 - 150, 200 - 18, 300, 36);
		float w = 800; //Gdx.graphics.getWidth();
		float h = 480; //Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(w, h);
		Texture texture = new Texture(Gdx.files.internal("images/mainmenubackground.png"));
		Texture textTexture = new Texture(Gdx.files.internal("images/textmenuscreen.png"));
		TextureRegion region = new TextureRegion(texture, 0, 0, 800, 480);
		TextureRegion textRegion = new TextureRegion(textTexture, 0, 0, 451, 49);
		TextureRegion textRegionPlay = new TextureRegion(textTexture, 0, 50, 126, 47);
		TextureRegion textRegionNewGame = new TextureRegion(textTexture, 8, 98, 233, 34);
		TextureRegion textRegionSettings = new TextureRegion(textTexture, 8, 141, 227, 38);
		TextureRegion textRegionContinue = new TextureRegion(textTexture, 7, 508, 225, 34);
		TextureRegion textRegionFadedContinue = new TextureRegion(textTexture, 256, 508, 225, 34);
		sprite = new Sprite(region);
		titleSprite = new Sprite(textRegion);
		playSprite = new Sprite(textRegionPlay);
		newGameSprite = new Sprite(textRegionNewGame);
		settingsSprite = new Sprite(textRegionSettings);
		continueSprite = new Sprite(textRegionContinue);
		continueFadedSprite = new Sprite(textRegionFadedContinue);
		titleSprite.setOrigin(0,0);
		playSprite.setOrigin(0,0);
		newGameSprite.setOrigin(0,0);
		settingsSprite.setOrigin(0,0);
		titleSprite.setPosition((w - titleSprite.getWidth()) / 2, 370);
		System.out.println(w);
		System.out.println(h);
		System.out.println(w / 2 - titleSprite.getWidth());
		
		playSprite.setPosition(w / 2 - playSprite.getWidth() / 2, 150);
		playRectangle = new Rectangle(w / 2 - playSprite.getWidth() / 2 - 20 - w / 2, 150 - 20 - h / 2, playSprite.getWidth() + 40, playSprite.getHeight() + 40);
		touchPoint = new Vector3();
		
		newGameSprite.setPosition(w / 2 - newGameSprite.getWidth() / 2, 155);
		newGameRectangle = new Rectangle(w / 2 - newGameSprite.getWidth() / 2 - 5 - w / 2, 155 - 5 - h / 2, newGameSprite.getWidth() + 10, newGameSprite.getHeight() + 10);
			
		continueSprite.setPosition(w / 2 - continueSprite.getWidth() / 2, newGameSprite.getY() - 25 - newGameSprite.getHeight());
		continueFadedSprite.setPosition(w / 2 - continueSprite.getWidth() / 2, newGameSprite.getY() - 25 - newGameSprite.getHeight());
		continueRectangle = new Rectangle(w / 2 - continueSprite.getWidth() / 2 - 5 - w / 2, newGameSprite.getY() - 25 - newGameSprite.getHeight() - 5 - h/2, continueSprite.getWidth() + 10, continueSprite.getHeight() + 10);
		
		settingsSprite.setPosition(w / 2 - settingsSprite.getWidth() / 2, continueSprite.getY() - 30 - continueSprite.getHeight());
		settingsRectangle = new Rectangle(w / 2 - settingsSprite.getWidth() / 2 - 5 - w / 2,  continueSprite.getY() - 30 - continueSprite.getHeight() - 5 - h / 2, settingsSprite.getWidth() + 10, settingsSprite.getHeight() + 10);
	
		fire.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
		fire.setPosition(400, 10);
		fire.start();
		
		spark.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/SparkEffectAndroid.p" : "data/SparkEffect.p")), Gdx.files.internal("images"));
		spark.setPosition(400, 300);
		spark.start();
		
		blood.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/BloodEffectAndroid.p" : "data/BloodEffect.p")), Gdx.files.internal("images"));
		blood.setPosition(400, 300);
		for (ParticleEmitter pe : blood.getEmitters())
			pe.setContinuous(true);
		
		blood.start();
		
		//Gdx.input.setCursorCatched(false);
	}

	@Override
	public void render(float delta) 
	{
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		/*if (x < 0 || x > 800 || y < 0 || y > 480)
			Gdx.input.setCursorCatched(false);
		else
			Gdx.input.setCursorCatched(true);*/
		update(delta);
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		titleSprite.draw(batch);
		//playSprite.draw(batch);
		if (fire.isComplete())
			fire.start();
		if (spark.isComplete())
			spark.start();
		if (blood.isComplete())
			blood.start();
		//fire.setPosition(x, 480-y);
		fire.draw(batch, delta);//, delta);
		//spark.draw(batch, delta);
		blood.setPosition(x, 480-y);
		newGameSprite.draw(batch);
		settingsSprite.draw(batch);
		if (newGameStarted)
			continueSprite.draw(batch);
		else
			continueFadedSprite.draw(batch);
		blood.draw(batch, .01f);
		batch.end();
	}

	public void update (float deltaTime) 
	{
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			//System.out.println(touchPoint.x + " " + touchPoint.y);
			if (OverlapTester.pointInRectangle(newGameRectangle, touchPoint.x, touchPoint.y)) {
				//System.out.println(true);
				startMusic.stop();
				game.gameScreen = new GameScreen(game);
				newGameStarted = true;
				game.setScreen(game.gameScreen);
				return;
			}
			else if (OverlapTester.pointInRectangle(continueRectangle, touchPoint.x, touchPoint.y)) {
				if (newGameStarted) {
				startMusic.stop();
				game.setScreen(game.gameScreen);
				return;
				}
			}
			else if (OverlapTester.pointInRectangle(settingsRectangle, touchPoint.x, touchPoint.y)) {
				startMusic.stop();
				game.setScreen(game.settingsScreen);
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
	if (Settings.getInstance().getSound() == Sound.ON)
		startMusic.play();
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

public void gameWon() {
	newGameStarted = false;
}

}
