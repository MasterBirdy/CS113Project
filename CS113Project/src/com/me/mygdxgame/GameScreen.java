package com.me.mygdxgame;

//import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entity.*;
import com.me.mygdxgame.input.MyInputProcessor;
import com.me.mygdxgame.map.Coordinate;
import com.me.mygdxgame.map.Map;

public class GameScreen implements Screen {
	private OrthographicCamera camera, uiCamera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private Map[] maps = new Map[2];
	int counter1, counter2;
	private boolean isPaused;
	BitmapFont font;
	static boolean showRange;
	MyInputProcessor inputProcessor;
	EverythingHolder everything = new EverythingHolder();
	TextureRegion pauseRegion;
	int pauseCooldown;
	GameUI gameUI;
	Hero hero;
	Game game;
	Rectangle pauseRectangle;
	Vector3 touchPoint;
	int level = 1;


	public GameScreen(Game game)
	{

		this.game = game;
		Texture.setEnforcePotImages(false);
		isPaused = false;


		//Gdx.graphics.setDisplayMode(800, 480, false);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();


		counter1 = 0;
		counter2 = 0;
		pauseCooldown = 0;

		camera = new OrthographicCamera(w, h);
		uiCamera = new OrthographicCamera(w, h);
		camera.translate(400, 300);
		batch = new SpriteBatch();

		Texture pauseTexture = new Texture(Gdx.files.internal("images/pausemenu.png"));
		pauseRegion = new TextureRegion(pauseTexture, 0, 0, 270, 190);
		TextureRegion region;
		
		if (level == 0)
		{
			texture = new Texture(Gdx.files.internal("data/mockupmap.png"));
			region = new TextureRegion(texture, 0, 0, 800, 600);
			sprite = new Sprite(region);
	
			maps[0] = new Map(new Coordinate(100, 1030), sprite, 1600, 1200, 100, 1030, 1300, 170);
			maps[0].add(new Coordinate(1300, 1030));
			maps[0].add(new Coordinate(1300, 600));
			maps[0].add(new Coordinate(300, 600));
			maps[0].add(new Coordinate(300, 170));
			maps[0].add(new Coordinate(1300, 170));
		}
		else
		{
			texture = new Texture(Gdx.files.internal("images/map1.png"));
			region = new TextureRegion(texture, 0, 0, 1200, 960);
			sprite = new Sprite(region);
			
			maps[1] = new Map(new Coordinate(280, 870), sprite, 1200, 960, 270, 870, 1210, 480);
			maps[1].add(new Coordinate(1190, 870));
			maps[1].add(new Coordinate(1190, 665));
			maps[1].add(new Coordinate(360, 665));
			maps[1].add(new Coordinate(360, 480));
			maps[1].add(new Coordinate(1200, 480));
		}

		EverythingHolder.load(batch, maps[level]);

		hero = new SwordFace(maps[level].start1().x(), maps[level].start1().y(), 1, everything.map().getPath().iterator());

		Texture sheet = new Texture(Gdx.files.internal("images/sprite_sheet.png"));

		Actor.linkActors(everything.team(1), everything.team(2));
		//<<<<<<< HEAD
		Actor.loadRange(sheet);
		Entity.loadSheet(sheet);
		Unit.loadAnimations();
		Projectile.loadProjectiles();
		Building.loadSprites();
		sprite.setSize(1600, 1200);
		//=======
		//		Actor.loadRange();
		//		Entity.loadSheet(new Texture(Gdx.files.internal("images/sprite_sheet.png")));
		//		Unit.loadAnimations();
		//		Projectile.loadProjectiles();
		//		sprite.setSize(1600, 1200);
		//>>>>>>> Jason-Split-Branch
		font = new BitmapFont();
		EverythingHolder.showRange = true;
		inputProcessor = new MyInputProcessor();

		MyInputProcessor.loadCamera(camera);
		MyInputProcessor.loadHero(hero);
		Building tower = new ArrowTower(maps[level].start1().x() + 20, maps[level].start1().y(), 1);
		everything.add(tower, true, 1);
		tower = new ArrowTower(maps[level].start2().x() - 20, maps[level].start2().y(), 2);
		everything.add(tower, true, 2);
		
		everything.add(hero, true, 1);

		pauseRectangle = new Rectangle(-68, -32, 133, 33);

		Gdx.input.setInputProcessor(inputProcessor);
		gameUI = new GameUI();
		GameUI.load(batch, everything);
		touchPoint = new Vector3();
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
	public void render(float delta) 
	{
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		boundCamera();

		camera.update();
		camera.apply(gl);

		uiCamera.update();
		uiCamera.apply(gl);

		if (!isPaused)
			update();
		handleInput();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		everything.map().background().draw(batch);
		everything.render();
		font.draw(batch, "Total Units: " + (everything.team(1).size() + everything.team(2).size()), 800, 555);

		batch.end();
		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();
		gameUI.render();
		pauseCooldown++;
		if (isPaused)
			batch.draw(pauseRegion, 0-pauseRegion.getRegionWidth() / 2 , 0-pauseRegion.getRegionHeight() / 2);
		batch.end();
	}

	public void update()
	{
		everything.update();
		randomSpawner();
		if (!everything.team(1).getLast().isAlive())
			game.setScreen(new MainMenuScreen(game));
		else if (!everything.team(2).getLast().isAlive())
			game.setScreen(new MainMenuScreen(game));
	}

	public void randomSpawner()
	{
		Coordinate start1 = everything.map().start1();
		Coordinate start2 = everything.map().start2();

		if (--counter1 < 0)
		{
			// decides to add either a swordsman or an archer
			boolean sword = Math.random() < 0.6;
			if (sword)
				//everything.add(new Swordsman(start1.x(), start1.y(), 1, everything.map().getPath().iterator()), true, 1);
				everything.add(1, 1);
			else
				//everything.add(new Archer(start1.x(), start1.y(), 1, everything.map().getPath().iterator()), true, 1);
				everything.add(2, 1);
			counter1 = (int)(Math.random() * 60) + 40;
		}
		if (--counter2 < 0)
		{
			boolean sword = Math.random() < 0.6;
			if (sword)
				//everything.add(new Swordsman(start2.x(), start2.y(), 2, everything.map().getPath().descendingIterator()), false, 2);
				everything.add(1, 2);
			else
				//everything.add(new Archer(start2.x(), start2.y(), 2, everything.map().getPath().descendingIterator()), false, 2);
				everything.add(2, 2);
			counter2 = (int)(Math.random() * 60) + 40;
		}
	}	

	private void handleInput()
	{
		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
			camera.translate(0, 10);
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
			camera.translate(0, -10);
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
			camera.translate(10, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
			camera.translate(-10, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && pauseCooldown > 10)
		{
			isPaused = !isPaused;
			pauseCooldown = 0;
		}
		if (isPaused){
			if (Gdx.input.justTouched()) {
				uiCamera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				//System.out.println(touchPoint.x + " " + touchPoint.y);
				if (OverlapTester.pointInRectangle(pauseRectangle, touchPoint.x, touchPoint.y)) {
					//System.out.println(true);
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}

	public void boundCamera()
	{
		int w = Gdx.graphics.getWidth() / 2;
		int h = Gdx.graphics.getHeight() / 2;

		if (camera.position.y > everything.map().height())
			camera.position.y = everything.map().height();

		if (camera.position.y < h)
			camera.position.y = h;

		if (camera.position.x > everything.map().width() + gameUI.width())
			camera.position.x = everything.map().width() + gameUI.width();

		if (camera.position.x < w)
			camera.position.x = w;
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


	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
}
