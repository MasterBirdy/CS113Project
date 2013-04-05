package com.awesomeincorporated.unknowndefense;

import com.awesomeincorporated.unknowndefense.ui.Button;
import com.awesomeincorporated.unknowndefense.ui.RoundButton;
import com.badlogic.gdx.Audio;
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

// Jason Rogers
// Mike Tang
// Matthew Ito
// Cecilia Bishton
// Michael Chen

public class MainMenuScreen implements Screen 
{

//	Rectangle highscoresBounds;
	UnknownDefense game;
	private OrthographicCamera camera;
	TextureRegion texture;
	Sprite sprite;
	Sprite titleSprite;
//	Sprite playSprite;
//	Sprite newGameSprite;
//	Sprite settingsSprite;
//	Sprite continueSprite;
//	Sprite continueFadedSprite;
//	Rectangle playRectangle;
//	Rectangle newGameRectangle;
//	Rectangle settingsRectangle;
//	Rectangle continueRectangle;
	Button[] buttons = new Button[4];
	private SpriteBatch batch;
	int width, height;
	Vector3 touchPoint;
	Audio tempMusic = Gdx.audio;
	Music startMusic;
	static ParticleEffect fire = new ParticleEffect();
	static ParticleEffect spark = new ParticleEffect();
	static ParticleEffect blood = new ParticleEffect();
	static ParticleEffect rainbow = new ParticleEffect();
	boolean newGameStarted;
	TextureRegion gameLogo, buttonFrame;
	
	EverythingHolder everything;

	public MainMenuScreen(UnknownDefense game, EverythingHolder everything)
	{
		this.game = game;
		Settings.getInstance();
		newGameStarted = false;
//		startMusic = tempMusic.newMusic(Gdx.files.internal("audio/523938_--MB---The-Black-Wi.mp3"));
//		startMusic.setLooping(true);
//		startMusic.play();
		this.everything = everything;
		startMusic = tempMusic.newMusic(Gdx.files.internal("audio/460436_trapped_in_dreams.mp3"));
		startMusic.setLooping(true);
		startMusic.setVolume(.5f);
		startMusic.play();
//		startMusic = tempMusic.newMusic(Gdx.files.internal("audio/Celeste.wav"));
//		startMusic.setLooping(true);
//		startMusic.play();
//		highscoresBounds = new Rectangle(160 - 150, 200 - 18, 300, 36);
//		float w = 800; //Gdx.graphics.getWidth();
//		float h = 480; //Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(width, height);
		texture = new TextureRegion(new Texture(Gdx.files.internal("images/mainmenubackground.jpg")), 0, 0, 800, 480);
		Texture textTexture = new Texture(Gdx.files.internal("images/textmenuscreen.png"));
//		TextureRegion region = new TextureRegion(texture, 0, 0, 800, 480);
		TextureRegion textRegion = new TextureRegion(textTexture, 0, 0, 451, 49);
//		TextureRegion textRegionPlay = new TextureRegion(textTexture, 0, 50, 126, 47);
//		TextureRegion textRegionNewGame = new TextureRegion(textTexture, 8, 98, 233, 34);
//		TextureRegion textRegionSettings = new TextureRegion(textTexture, 8, 141, 227, 38);
//		TextureRegion textRegionContinue = new TextureRegion(textTexture, 7, 508, 225, 34);
//		TextureRegion textRegionFadedContinue = new TextureRegion(textTexture, 256, 508, 225, 34);
		Texture icons = new Texture(Gdx.files.internal("images/buttons_sheet.png"));
//		gameLogo = new TextureRegion(icons, 1319, 0, 382, 175);
		gameLogo = new TextureRegion(icons, 0, 1514, 842, 467);
		buttonFrame = new TextureRegion(icons, 880, 422, 361, 572);
		
		int stackTopX = 680; //658;
		int stackTopY = 335; //355;
		int spaceX = 75;
		int spaceY = 54;
		int buttonRadius = (int) (width * .95f / 20);
		buttons[0] = new RoundButton(stackTopX, stackTopY - spaceY * 2 + 4, buttonRadius, 			
				new TextureRegion(icons, 367, 459, 152, 153));	// Sinlge-player
		buttons[1] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 3 + 4, buttonRadius, 			
				new TextureRegion(icons, 367, 612, 152, 153));	// Multi-player
		buttons[2] = new RoundButton(stackTopX, stackTopY - spaceY * 4 + 9, buttonRadius, 			
				new TextureRegion(icons, 367, 765, 152, 153));	// Settings
		buttons[3] = new RoundButton(stackTopX + spaceX, stackTopY - spaceY * 5 + 9, buttonRadius, 			
				new TextureRegion(icons, 367, 153, 152, 153));	// Quit		
		
		
//		sprite = new Sprite(region);
		titleSprite = new Sprite(textRegion);
//		playSprite = new Sprite(textRegionPlay);
//		newGameSprite = new Sprite(textRegionNewGame);
//		settingsSprite = new Sprite(textRegionSettings);
//		continueSprite = new Sprite(textRegionContinue);
//		continueFadedSprite = new Sprite(textRegionFadedContinue);
		titleSprite.setOrigin(0,0);
//		playSprite.setOrigin(0,0);
//		newGameSprite.setOrigin(0,0);
//		settingsSprite.setOrigin(0,0);
		titleSprite.setPosition((width - titleSprite.getWidth()) / 2, 370);
		System.out.println(width);
		System.out.println(height);
		System.out.println(width / 2 - titleSprite.getWidth());
		
//		playSprite.setPosition(width / 2 - playSprite.getWidth() / 2, 150);
//		playRectangle = new Rectangle(width / 2 - playSprite.getWidth() / 2 - 20 - width / 2, 150 - 20 - height / 2, playSprite.getWidth() + 40, playSprite.getHeight() + 40);
		touchPoint = new Vector3();
		
//		newGameSprite.setPosition(width / 2 - newGameSprite.getWidth() / 2, 155);
//		newGameRectangle = new Rectangle(width / 2 - newGameSprite.getWidth() / 2 - 5 - width / 2, 155 - 5 - height / 2, newGameSprite.getWidth() + 10, newGameSprite.getHeight() + 10);
//			
//		continueSprite.setPosition(width / 2 - continueSprite.getWidth() / 2, newGameSprite.getY() - 25 - newGameSprite.getHeight());
//		continueFadedSprite.setPosition(width / 2 - continueSprite.getWidth() / 2, newGameSprite.getY() - 25 - newGameSprite.getHeight());
//		continueRectangle = new Rectangle(width / 2 - continueSprite.getWidth() / 2 - 5 - width / 2, newGameSprite.getY() - 25 - newGameSprite.getHeight() - 5 - height/2, continueSprite.getWidth() + 10, continueSprite.getHeight() + 10);
//		
//		settingsSprite.setPosition(width / 2 - settingsSprite.getWidth() / 2, continueSprite.getY() - 30 - continueSprite.getHeight());
//		settingsRectangle = new Rectangle(width / 2 - settingsSprite.getWidth() / 2 - 5 - width / 2,  continueSprite.getY() - 30 - continueSprite.getHeight() - 5 - height / 2, settingsSprite.getWidth() + 10, settingsSprite.getHeight() + 10);
	
		fire.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
		fire.setPosition(610, 260);
		fire.start();
		
		//spark.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/SparkEffectAndroid.p" : "data/SparkEffect.p")), Gdx.files.internal("images"));
//		spark.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/HeroRegenAura.p" : "data/MinionRegenAura.p")), Gdx.files.internal("images"));
		spark.setPosition(400, 300);
		spark.start();
		
		blood.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/BloodEffectAndroid.p" : "data/BloodEffect.p")), Gdx.files.internal("images"));
		blood.setPosition(400, 300);
		for (ParticleEmitter pe : blood.getEmitters())
			pe.setContinuous(true);
		blood.start();
		
		
		rainbow = everything.getEffect("rainbowtrailsparkle");
		rainbow.setPosition(332, 423);
		rainbow.start();
		
		Gdx.graphics.setVSync(true);
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
		batch.draw(texture, 0, 0, width, height);
//		batch.drawdraw(texture, 0, 0, width, height);
		//sprite.draw(batch);
		//playSprite.draw(batch);
		//fire.setPosition(x, 480-y);
		//spark.draw(batch, delta);
//		titleSprite.draw(batch);
		//680 - 46, 335 - 355
		batch.draw(gameLogo, 155, 262, 382, 212);
//		batch.draw(gameLogo, 155, 300);//, buttonFrame.getRegionWidth() * .95f * 80 / 146, buttonFrame.getRegionHeight() * .95f * 80 / 146);
		batch.draw(buttonFrame, 634, -20, buttonFrame.getRegionWidth() * .95f * 80 / 146, buttonFrame.getRegionHeight() * .95f * 80 / 146);
//		
//		newGameSprite.draw(batch);
//		settingsSprite.draw(batch);
		
		for (Button button : buttons)
			button.draw(batch);
		
//		if (newGameStarted)
//			continueSprite.draw(batch);
//		else
//			continueFadedSprite.draw(batch);
		
		if (fire.isComplete())
			fire.start();
		if (spark.isComplete())
			spark.start();
		if (blood.isComplete())
			blood.start();
		if (rainbow.isComplete())
			rainbow.start();
		
//		fire.draw(batch, .01f);//, delta);
		blood.setPosition(x, height - y);
		blood.draw(batch, .01f);
		rainbow.draw(batch, 0.01f);
		
		batch.end();
	}

	public void update (float deltaTime) 
	{
		if (Gdx.input.justTouched()) {
//			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			
			touchPoint.set(Gdx.input.getX(), 480 - Gdx.input.getY(), 0);
			System.out.println(touchPoint.x + " " + touchPoint.y);
			for (int i = 0; i < buttons.length; i++)
				if (buttons[i].hit(touchPoint.x, touchPoint.y))
				{
					buttonHit(i);
					return;
				}
//			if (OverlapTester.pointInRectangle(newGameRectangle, touchPoint.x, touchPoint.y)) {
//				//System.out.println(true);
////				startMusic.stop();
//				game.gameScreen = new GameScreen(game);
//				newGameStarted = true;
//				game.setScreen(game.gameScreen);
//				return;
//			}
//			else if (OverlapTester.pointInRectangle(continueRectangle, touchPoint.x, touchPoint.y)) {
//				if (newGameStarted) {
////				startMusic.stop();
//				game.setScreen(game.gameScreen);
//				return;
//				}
//			}
//			else if (OverlapTester.pointInRectangle(settingsRectangle, touchPoint.x, touchPoint.y)) {
////				startMusic.stop();
//				game.setScreen(game.settingsScreen);
//				return;
//			}
		}
	}
	
	public void buttonHit(int h)
	{
		if (h == 0) // Single-player
		{
//			startMusic.stop();
			game.gameScreen = new GameScreen(game, false);
			newGameStarted = true;
			game.setScreen(game.gameScreen);
			return;
		}
		else if (h == 1) // Multi-player
		{
//			startMusic.stop();
			game.gameScreen = new GameScreen(game, true);
			newGameStarted = true;
			game.setScreen(game.gameScreen);
			return;
		}
		else if (h == 2) // Settings
		{
//			startMusic.stop();
			game.setScreen(game.settingsScreen);
			return;
		}
		else if (h == 3) // Quit
		{
//			startMusic.stop();
			System.exit(0);
		}
			
	}

	@Override
	public void resize(int width, int height) 
	{
		// TODO Auto-generated method stub
	
	}
	
	@Override
	public void show() 
	{
		startMusic.play();
		// TODO Auto-generated method stub
	//	if (Settings.getInstance().getSound() == SoundEnum.ON)
	//		startMusic.play();
	}
	
	@Override
	public void hide() 
	{
		startMusic.stop();
		// TODO Auto-generated method stub
	
	}
	
	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub
	
	}
	
	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub
	
	
	}
	
	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
	
	}
	
	public void gameWon() 
	{
		newGameStarted = false;
	}
}
