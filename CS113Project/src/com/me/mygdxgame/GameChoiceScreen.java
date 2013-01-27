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

public class GameChoiceScreen implements Screen{

	private MyGdxGame game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Vector3 touchPoint;
	private Sprite titleSprite;
	private Sprite survivalSprite;
	private Sprite quickSprite;
	private Rectangle survivalRectangle;
	private Rectangle quickRectangle;
	private Sprite sprite;
	private Sprite backSprite;
	private Rectangle backRectangle;
	private Sprite singlePlayerSprite;
	private Sprite multiPlayerSprite;

	public GameChoiceScreen (MyGdxGame game)
	{
		this.game = game;
		float w = 800; //Gdx.graphics.getWidth();
		float h = 480; //Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(w, h);
		touchPoint = new Vector3();
		Texture textTexture = new Texture(Gdx.files.internal("images/textmenuscreen.png"));
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/mainmenubackground.png"));
		
		sprite = new Sprite(new TextureRegion(backgroundTexture, 0, 0, 800, 480));
		titleSprite = new Sprite(new TextureRegion(textTexture, 0, 0, 451, 49));
		titleSprite.setPosition((w - titleSprite.getWidth()) / 2, 370);

		survivalSprite = new Sprite(new TextureRegion(textTexture, 10, 621, 342, 34));
		survivalSprite.setPosition((w - survivalSprite.getWidth()) / 2, 200);
		survivalRectangle = new Rectangle((w - survivalSprite.getWidth()) / 2 - 20 - w / 2, 180 - 20 - h / 2, survivalSprite.getWidth() + 40, survivalSprite.getHeight() + 40);

		quickSprite = new Sprite(new TextureRegion(textTexture, 12, 674, 306, 34));
		quickSprite.setPosition((w - quickSprite.getWidth()) / 2, 110);
		quickRectangle = new Rectangle((w - quickSprite.getWidth()) / 2 - 20 - w / 2, 80 - 20 - h / 2, quickSprite.getWidth() + 40, quickSprite.getHeight() + 40);
		
		backSprite = new Sprite(new TextureRegion(textTexture, 5, 179, 125, 40));
		backSprite.setPosition(w - backSprite.getWidth() - 20, backSprite.getHeight() - 10);
		backRectangle = new Rectangle(w - backSprite.getWidth() - 20 - 20 - w / 2, backSprite.getHeight() - 10 - 20 - h / 2, backSprite.getWidth() + 40, backSprite.getHeight() + 40);

		singlePlayerSprite = new Sprite(new TextureRegion(textTexture, 14, 792, 156, 21));
		singlePlayerSprite.setPosition(survivalSprite.getX() + survivalSprite.getWidth() / 2 - singlePlayerSprite.getWidth() / 2, survivalSprite.getY() - survivalSprite.getHeight() + 5);
		
		multiPlayerSprite = new Sprite(new TextureRegion(textTexture, 234, 794, 136, 21));
		multiPlayerSprite.setPosition(quickSprite.getX() +quickSprite.getWidth() / 2 - multiPlayerSprite.getWidth() / 2, quickSprite.getY() - quickSprite.getHeight() + 5);
	}

	public void update (float deltaTime) 
	{
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			//System.out.println(touchPoint.x + " " + touchPoint.y);
			if (OverlapTester.pointInRectangle(survivalRectangle, touchPoint.x, touchPoint.y)) {
				game.gameScreen = new GameScreen(game);
				game.mainMenuScreen.gameStarted();
				game.mainMenuScreen.startMusic.stop();
				game.setScreen(game.gameScreen);
				return;
			}
			else if (OverlapTester.pointInRectangle(quickRectangle, touchPoint.x, touchPoint.y)) {

				return;
			}
			
			else if (OverlapTester.pointInRectangle(backRectangle, touchPoint.x, touchPoint.y)){
				
				game.setScreen(game.mainMenuScreen);
				return;
				
			}
		}
	}


	@Override
	public void render(float delta) {
		update(delta);
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		sprite.draw(batch);
		titleSprite.draw(batch);
		survivalSprite.draw(batch);
		quickSprite.draw(batch);
		backSprite.draw(batch);
		singlePlayerSprite.draw(batch);
		multiPlayerSprite.draw(batch);
		batch.end();
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
