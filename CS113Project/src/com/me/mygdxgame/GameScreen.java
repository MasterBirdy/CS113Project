package com.me.mygdxgame;

//import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
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
	MyGdxGame game;
	Rectangle pauseRectangle;
	Rectangle pauseRectangle2;
	Rectangle swordRectangle;
	Rectangle bowRectangle;
	Rectangle serfRectangle;
	Rectangle magicRectangle;
	Rectangle petRectangle;
	Rectangle spiralRectangle;
	Rectangle attackRectangle;
	Rectangle defendRectangle;
	Rectangle retreatRectangle;
	Vector3 touchPoint;
	Difficulty difficulty;
	int level = 1;
	int income, resources;


	public GameScreen(MyGdxGame game)
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
			
			Coordinate[] sites = new Coordinate[3];
			sites[0] = new Coordinate(1230, 750);
			sites[1] = new Coordinate(600, 910);
			sites[2] = new Coordinate(900, 910);
			maps[1].buildSites(sites, 1);
			
			sites = new Coordinate[3];
			sites[0] = new Coordinate(300, 550);
			sites[1] = new Coordinate(600, 430);
			sites[2] = new Coordinate(900, 430);
			maps[1].buildSites(sites, 2);
		}

		EverythingHolder.load(batch, maps[level]);

		hero = new SwordFace(maps[level].start1().x(), maps[level].start1().y(), 1, everything.map().getPath().listIterator());

		Texture sheet = new Texture(Gdx.files.internal("images/sprite_sheet.png"));

		Actor.linkActors(everything.team(1), everything.team(2));
		//<<<<<<< HEAD
		Entity.loadStatics(sheet);
		Actor.loadRange();
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
		MyInputProcessor.loadGame(this);
		
		Building.loadAnimations();

		Building tower = new ArrowTower(maps[level].start1().x() + 20, maps[level].start1().y(), 1);
		tower.upgrade();
		everything.add(tower, true, 1);
		tower = new ArrowTower(maps[level].start2().x() - 20, maps[level].start2().y(), 2);
		tower.upgrade();
		everything.add(tower, true, 2);
		
		for (Coordinate c : everything.map().buildSites(1))
		{
			tower = new ArrowTower(c.x(), c.y(), 1);
			everything.add(tower, true, 1);
		}
		
		for (Coordinate c : everything.map().buildSites(2))
		{
			tower = new ArrowTower(c.x(), c.y(), 2);
			tower.upgrade();
			everything.add(tower, true, 2);
		}
		
		hero.takeDamage(1000);
		everything.add(hero, true, 1);
		
//		tower = new ArrowTower(300, 400, 1);
//		everything.add(tower, true, 1);
//		tower= new ArrowTower(1000, 1000, 2);
//		everything.add(tower, true, 2);

		pauseRectangle   = new Rectangle(-68, -32, 133, 33);
		pauseRectangle2  = new Rectangle(-76, -76, 156, 27);
		swordRectangle   = new Rectangle(221, -29, 69, 80);
		bowRectangle     = new Rectangle(311, -29, 69, 80);
		serfRectangle    = new Rectangle(221, -127, 69, 80);
		magicRectangle   = new Rectangle(311, -127, 69, 80);
		petRectangle     = new Rectangle(221, -227, 69, 80);
		spiralRectangle  = new Rectangle(311, -227, 69, 80);
		attackRectangle  = new Rectangle(-50, -200, 40, 40);
		defendRectangle  = new Rectangle(-100, -200, 40, 40);
		retreatRectangle = new Rectangle(-150, -200, 40, 40);

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
		boundCamera();
		
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();
		camera.apply(gl);
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
		/*if (--counter1 < 0)
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
		}*/
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
	
	public void buyUnit(int unit)
	{
		everything.add(unit, 1);
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
		if ((Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.MENU)) && pauseCooldown > 10)
		{
			isPaused = !isPaused;
			pauseCooldown = 0;
		}
		if ((Gdx.input.isKeyPressed(Input.Keys.Q)) && pauseCooldown > 100)
		{
			for (Actor a : everything.team(1))
			{
				if (a instanceof ArrowTower)
					((ArrowTower) a).upgrade();
			}
			pauseCooldown = 0;
		}
		if (Gdx.input.justTouched()) {
			uiCamera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			System.out.println("X: " + touchPoint.x + " Y: " + touchPoint.y);
			if (OverlapTester.pointInRectangle(swordRectangle, touchPoint.x, touchPoint.y))
			{
				buyUnit(1);
			}
			if (OverlapTester.pointInRectangle(bowRectangle, touchPoint.x, touchPoint.y))
			{
				buyUnit(2);
			}
			if (OverlapTester.pointInRectangle(serfRectangle, touchPoint.x, touchPoint.y))
			{
				/*
				 * INSERT CODE HERE
				 */
			}
			if (OverlapTester.pointInRectangle(magicRectangle, touchPoint.x, touchPoint.y))
			{
				/*
				 * INSERT CODE HERE
				 */
			}
			if (OverlapTester.pointInRectangle(petRectangle, touchPoint.x, touchPoint.y))
			{
				/*
				 * INSERT CODE HERE
				 */
			}
			if (OverlapTester.pointInRectangle(spiralRectangle, touchPoint.x, touchPoint.y))
			{
				/*
				 * INSERT CODE HERE
				 */
			}
			if (OverlapTester.pointInRectangle(attackRectangle, touchPoint.x, touchPoint.y))
			{
				hero.stance(1);
				Gdx.input.vibrate(50);
			}
			if (OverlapTester.pointInRectangle(defendRectangle, touchPoint.x, touchPoint.y))
			{
				hero.stance(0);
				Gdx.input.vibrate(50);
			}
			if (OverlapTester.pointInRectangle(retreatRectangle, touchPoint.x, touchPoint.y))
			{
				hero.stance(-1);
				Gdx.input.vibrate(50);
			}
			Actor a = everything.team(1).getLast();
			if (OverlapTester.pointInRectangle(new Rectangle(a.xCoord(), a.yCoord(), 40, 40), touchPoint.x, touchPoint.y))
			{
				hero.stance(0);
			}
			if (isPaused){
				//System.out.println(touchPoint.x + " " + touchPoint.y);
				if (OverlapTester.pointInRectangle(pauseRectangle, touchPoint.x, touchPoint.y)) {
					//System.out.println(true);
					game.setScreen(game.mainMenuScreen);
					return;
				}
				
				if (OverlapTester.pointInRectangle(pauseRectangle2, touchPoint.x, touchPoint.y)) {
					//System.out.println(true);
					game.setScreen(game.settingsScreen);
					return;
				}
			}
		}
	}

	public void boundCamera()
	{
		if (camera.zoom > 2)
			camera.zoom = 2;
		if (camera.zoom < .5)
			camera.zoom = .5f;
		/*float width = camera.viewportWidth;
		int w = Gdx.graphics.getWidth() / 2;
		int h = Gdx.graphics.getHeight() / 2;*/
		
		/*float w = camera.viewportWidth / 2;
		float h = camera.viewportHeight / 2;*/
		float w = (camera.frustum.planePoints[1].x - camera.frustum.planePoints[0].x) / 2;
		float h = (camera.frustum.planePoints[3].y - camera.frustum.planePoints[0].y) / 2;
		
		float width = everything.map().width();
		float height = everything.map().height();

		//if (camera.position.y > everything.map().height() * (1 / (2 - camera.zoom)))
		//	camera.position.y = everything.map().height() * (1 / (2 - camera.zoom));
		if (camera.position.y > everything.map().height() - h + 240)
			camera.position.y = everything.map().height() - h + 240;

		if (camera.position.y < h)
			camera.position.y = h;

		//if (camera.position.x > everything.map().width() + gameUI.width())
		//	camera.position.x = everything.map().width() + gameUI.width();
		float temp = everything.map().width() - (w / 2) + 400;
		if (camera.position.x > (everything.map().width() - (w / 2) + 400))//(w / 4) - w + 400)
			camera.position.x = everything.map().width() - (w / 2) + 400; //(w / 4) - w + 400;

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
