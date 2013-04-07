package com.awesomeincorporated.unknowndefense;

//import com.badlogic.gdx.ApplicationListener;
import java.io.IOException;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.PriorityQueue;

import com.awesomeincorporated.unknowndefense.entity.*;
import com.awesomeincorporated.unknowndefense.input.MyInputProcessor;
import com.awesomeincorporated.unknowndefense.map.Coordinate;
import com.awesomeincorporated.unknowndefense.map.Map;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.minlog.Log;
import com.awesomeincorporated.unknowndefense.networking.Network.*;
import com.awesomeincorporated.unknowndefense.networking.Network;
import com.awesomeincorporated.unknowndefense.networking.User;
import com.awesomeincorporated.unknowndefense.parser.HeroStructure;
import com.awesomeincorporated.unknowndefense.ui.GameUI;

public class GameScreen implements Screen 
{
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
	static EverythingHolder everything;// = new EverythingHolder();
	TextureRegion pauseRegion;
	int pauseCooldown;
	GameUI gameUI3;
	Hero[] heroes = new Hero[2];
	UnknownDefense game;
	Vector3 touchPoint;
	Vector3 gameTouchPoint;
	Difficulty difficulty;
	int level = 1;
	int income, resources;
	Audio tempMusic = Gdx.audio;
	Music startMusic;
	
	float cameraW, cameraH, width, height, screenH;
	
	float timeAccumulator = 0;
	float volume = .2f;
	boolean following = false;
	boolean multiplayer = false; 	// True with multiplayer
	boolean running = false;		// False with Multiplayer
	boolean connected = false;
	byte team;						// Team 1 is top and Team 2 is bottom
	Client client;
//	String serverIp = "localhost"; 	// Local Host
//	String serverIp = "evil-monkey.ics.uci.edu";//"128.195.6.172";
//	String serverIp = "ernie-the-giant-chicken.ics.uci.edu";//"128.195.6.172";
//	String serverIp = "169.234.242.202"; 	// My desktop
	
	String serverIp = "ec2-204-236-164-26.us-west-1.compute.amazonaws.com";//"10.170.103.156"; 	// EC2 Server
	float stepTime = 0.02f;
	Comparator<CommandIn> comparator = new MessageCompare();
	PriorityQueue<CommandIn> commandQueue = new PriorityQueue<CommandIn>(20, comparator);
	
	boolean ready = false;
	
	GL10 gl;

	public GameScreen(UnknownDefense game, boolean multiplayer)
	{
		gl = Gdx.graphics.getGL10();
		this.game = game;
		this.multiplayer = multiplayer;
		everything.reset();
		Texture.setEnforcePotImages(true);
		isPaused = false;
		
//		startMusic = tempMusic.newMusic(Gdx.files.internal("audio/373780_The_Devil_On_A_Bicy.mp3"));
//		startMusic.setLooping(true);
//		startMusic.setVolume(volume);
//		startMusic.play();
//		startMusic = tempMusic.newMusic(Gdx.files.internal("audio/506819_Xanax-amp-Bluebird3.wav"));
//		startMusic.setLooping(true);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();


		counter1 = 0;
		counter2 = 0;
		pauseCooldown = 0;

		camera = new OrthographicCamera(w, h);
		camera.translate(950, 700);
		camera.zoom = 1.9f;
		uiCamera = new OrthographicCamera(w, h);
		uiCamera.setToOrtho(false);
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
//			texture = new Texture(Gdx.files.internal("images/map1.png"));
			texture = new Texture(Gdx.files.internal("images/map1.jpg"));
			region = new TextureRegion(texture, 0, 0, 1200, 960);
			sprite = new Sprite(region);
			
			maps[1] = new Map(new Coordinate(280, 870), sprite, 1200, 960, 270, 870, 1210, 480);
			maps[1].add(new Coordinate(1190, 870));
			maps[1].add(new Coordinate(1190, 665));
			maps[1].add(new Coordinate(360, 665));
			maps[1].add(new Coordinate(360, 480));
			maps[1].add(new Coordinate(1200, 480));
			
			Coordinate[] sites = new Coordinate[3];
			sites[0] = new Coordinate(600, 910);
			sites[1] = new Coordinate(900, 910);
			sites[2] = new Coordinate(1230, 750);
			maps[1].buildSites(sites, 1);
			
			sites = new Coordinate[3];
			sites[0] = new Coordinate(900, 430);
			sites[1] = new Coordinate(600, 430);
			sites[2] = new Coordinate(300, 550);
			maps[1].buildSites(sites, 2);
		}
		
		everything.load(batch, maps[level]);
//		Texture sheetR = new Texture(Gdx.files.internal("images/sprite_sheet_red.png"));
//		Texture sheetB = new Texture(Gdx.files.internal("images/sprite_sheet_blue.png"));

		//<<<<<<< HEAD
//		Entity.loadStatics(sheetR, sheetB);
//		Actor.loadRange();
//		Unit.loadAnimations();
		Projectile.loadProjectiles();
//		Building.loadSprites();
		sprite.setSize(1600, 1200);
		font = new BitmapFont();
		EverythingHolder.showRange = true;
		inputProcessor = new MyInputProcessor();

		Gdx.input.setInputProcessor(inputProcessor);
		GameUI.load(batch, everything);
		gameUI3 = new GameUI();
		
		MyInputProcessor.loadCamera(camera, uiCamera);
		MyInputProcessor.loadGame(this, gameUI3);
		
//		Building.loadAnimations();

		Building tower = new Building(maps[level].start1().x() + 20, maps[level].start1().y(), 1, 
									everything.buildingStats.get("stronghold"));//new Stronghold(maps[level].start1().x() + 20, maps[level].start1().y(), 1);
		tower.upgrade();
		everything.add(tower, 1);
//		everything.add(tower, true, 1);
//		tower = new Stronghold(maps[level].start2().x() - 20, maps[level].start2().y(), 2);
		tower = new Building(maps[level].start2().x() - 20, maps[level].start2().y(), 2, 
				everything.buildingStats.get("stronghold"));
		tower.upgrade();
		everything.add(tower, 2);
//		everything.add(tower, true, 2);
		
		int towerNumber = 1;
		for (Coordinate c : everything.map().buildSites(1))
		{
			tower = new Building(c.x(), c.y(), 1, everything.buildingStats.get("arrowtower"));
//			tower = new ArrowTower(c.x(), c.y(), 1, towerNumber++);
			tower.upgrade();
			everything.add(tower, 1);
//			everything.add(tower, true, 1);
		}
		
		towerNumber = 1;
		for (Coordinate c : everything.map().buildSites(2))
		{
			tower = new Building(c.x(), c.y(), 2, everything.buildingStats.get("arrowtower"));
//			tower = new ArrowTower(c.x(), c.y(), 2, towerNumber++);
			tower.upgrade();
			everything.add(tower, 2);
//			everything.add(tower, true, 2);
		}
		
		everything.initializeHeroes();

		touchPoint = new Vector3();
		gameTouchPoint = new Vector3();
		
		
		width = everything.map().width();
		height = everything.map().height();
		screenH = Gdx.graphics.getHeight() / 2;
		
		startMusic = tempMusic.newMusic(Gdx.files.internal("audio/373780_The_Devil_On_A_Bicy.mp3"));
		startMusic.setLooping(true);
		startMusic.setVolume(volume);
		startMusic.play();	

		if (!connected && multiplayer)
			networkSetup();
		if (!multiplayer)
		{
			running = true;
			everything.setRunning(true);
			team = 1;
			everything.setTeam((byte)1);
		}
		
//		ready = true;
	}
	
	public void toggleFollowing()
	{
		following = !following;
	}
	
	public void diasbleFollowing()
	{
		following = false;
	}
	
	static public void setEverything(EverythingHolder e)
	{
		everything = e;
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
		
		if (ready)
			timeAccumulator += delta;
		
//		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		if (gl.glGetError() != 0)
//			Log.debug("GL ERROR " + gl.glGetError());
//			System.out.println("GL ERROR " + gl.glGetError());
		
		if (following)
			centerOnHero();
		
		camera.update();
		camera.apply(gl);
		boundCamera();
		camera.update();
		camera.apply(gl);
		
		uiCamera.update();
		uiCamera.apply(gl);

		if (!isPaused && timeAccumulator > stepTime && running && ready)
		{
			while (timeAccumulator > stepTime)
			{
				update();
				timeAccumulator -= stepTime;
			}
		}
//		handleInput();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		everything.map().background().draw(batch);
		everything.render(delta);
		//font.draw(batch, "Total Units: " + (everything.team(1).size() + everything.team(2).size()), 800, 555);
		//font.draw(batch, "delta: " + delta, 800, 555);
//		font.draw(batch, "fps: " + (1 / delta), 800, 555);
//		font.draw(batch, "team: " + team, 800, 555);

		batch.end();
		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();
		gameUI3.render(delta);
		pauseCooldown++;
		if (isPaused)
			batch.draw(pauseRegion, 400 - pauseRegion.getRegionWidth() / 2 , 240 - pauseRegion.getRegionHeight() / 2);
		batch.end();
	}
	
	private void endGame(int t)
	{
		if (t == 0) return;
		everything.end();
		startMusic.stop();
		game.mainMenuScreen.gameWon();
		game.setScreen(game.mainMenuScreen);
	}
	
	public void pullCommand()
	{
		Object object;
		
		while(true)
		{
			if (commandQueue.isEmpty())
				return;
			System.out.println(((CommandIn)commandQueue.peek()).turn + " : " + everything.turn);
			if (everything.turn != ((CommandIn)commandQueue.peek()).turn)
				return;
			
			object = commandQueue.remove();
			
	//		if (object instanceof AddUnit)
	//		{
	//			System.out.println("Adding unit");
	//			everything.add(((AddUnit)object).unit, ((AddUnit)object).team);
	//			return;
	//		}
			
			if (object instanceof CommandIn)
			{
				CommandIn command = (CommandIn)object;
				if (command.command >= 0 && command.command < 6)
				{
					System.out.println("Pulled unit send command.");
					everything.add(((CommandIn)object).command, ((CommandIn)object).team);
					return;
				}
				if (command.command > 6 && command.command < 10)
				{
					System.out.println("Pulled hero command.");
					everything.setHeroStance(command.team, command.command - 8);
//					heroes[command.team - 1].stance(command.command - 8);
	//				heroes[0].stance(command.command - 8);
	//				heroes[1].stance(command.command - 8);
					return;
				}
				if (command.command > 9 && command.command < 13)
				{
					System.out.println("Pulled tower command.");
					everything.upgradeTower(command.command - 10, team);
//					everything.funds -= 40;
					System.out.println("Trying to upgrade tower " + (command.command - 10) + " from team " + team);
	//				heroes[command.team].stance(command.command - 8);
	//				heroes[0].stance(command.command - 8);
	//				heroes[1].stance(command.command - 8);
					return;
				}
			}
		}
	}

	public void update()
	{
		handleInput();
		if (multiplayer)
			pullCommand();
		everything.update();
		if (!multiplayer)
			randomSpawner();
		endGame(everything.winCondition());
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
			float rand = (float) Math.random();
			if (rand < 0.20f)
				//everything.add(new Swordsman(start2.x(), start2.y(), 2, everything.map().getPath().descendingIterator()), false, 2);
				everything.add(0, 2);
			else if (rand < 0.4f)
				//everything.add(new Archer(start2.x(), start2.y(), 2, everything.map().getPath().descendingIterator()), false, 2);
				everything.add(1, 2);
			else if (rand < 0.6f)
				everything.add(2, 2);
			else if (rand < 0.8f)
				everything.add(3, 2);
			else if (rand < 0.9f)
				everything.add(4, 2);
			else
				everything.add(5, 2);
			
			counter2 = (int)(Math.random() * 60) + 60;
		}
	}
	
	public void buyUnit(int unit)
	{
		if (multiplayer)
		{
			System.out.println("Trying to send unit " + unit + " from team " + team);
			if (everything.funds() < 20)
			{
				System.out.println("Out of money.");
				return;
			}
			Command cmd = new Command();
			cmd.team = team;
			cmd.type = (byte)unit;
			cmd.turn = everything.turn();
			client.sendTCP(cmd);
		}
		else
		{
			System.out.println("Trying to send unit " + unit + " from team " + team);
			everything.add(unit, 1);
		}
	}
	
	public void setHeroStance(int stance)
	{
		if (multiplayer)
		{
			Command cmd = new Command();
			cmd.type = (byte)(stance + 8);
			cmd.team = team;
			cmd.turn = everything.turn();
			System.out.println("Setting hero " + cmd.team + " to stance " + (cmd.type - 8));
			client.sendTCP(cmd);
		}
		else
			everything.setHeroStance(1, stance);
	}
	
	public void castHeroActive()
	{
		if (multiplayer)
		{
			
		}
		else
		{
			System.out.println("CASTING SPELL");
			everything.activeHeroSkill(1);
		}
	}
	
	public void upgradeTower(int tower)
	{
//		if (multiplayer)
//		{
//			System.out.println("Trying to upgrade tower " + tower + " from team " + team);
//			Command cmd = new Command();
//			cmd.team = team;
//			cmd.type = (byte)(tower + 10);
//			client.sendTCP(cmd);
//		}
//		else
//		{
			everything.upgradeTower(tower, team);
//			everything.funds -= 40;
			System.out.println("Trying to upgrade towers " + tower + " from team " + team);
//		}
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
//		if ((Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.MENU)) && pauseCooldown > 10)
//		{
//			isPaused = !isPaused;
//			pauseCooldown = 0;
//		}
		if (Gdx.input.isKeyPressed(Input.Keys.HOME))
		{
			client.close();
			Gdx.app.exit();
		}
		if ((Gdx.input.isKeyPressed(Input.Keys.Q)) && pauseCooldown > 100)
		{
//			for (Actor a : (team == 1 ? everything.team(1) : everything.team(2)))
//			{
//				if (a instanceof ArrowTower)
//					((ArrowTower) a).upgrade();
//			}
			pauseCooldown = 0;
		}
		if (Gdx.input.justTouched()) 
		{
			uiCamera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			camera.unproject(gameTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			
			Actor selected = everything.atPoint(gameTouchPoint.x, gameTouchPoint.y);
			if (selected instanceof Building)
			{
				System.out.println(selected.team() + " Tower number: " + ((Building)selected).towerNumber() + " by team " + team);
				if (selected.team() == team && !selected.isAlive() && everything.funds() >= 40)
				{
					System.out.println("Tower number: " + ((Building)selected).towerNumber() + " by team " + team);
					upgradeTower(((Building)selected).towerNumber());
//					((Building) selected).upgrade();
//					everything.funds -= 40;
				}	
			}
			
//			System.out.println("X: " + touchPoint.x + " Y: " + touchPoint.y);
//			System.out.println("X1: " + gameTouchPoint.x + " Y1: " + gameTouchPoint.y);
		}
	}
	
	public void centerOnHero()
	{
		System.out.println("CENTERING");
		if (!everything.getHero().isAlive())
			return;
		
		camera.position.x = everything.getHero().xCoord() + 40;
		camera.position.y = everything.getHero().yCoord() + 40;
//		boundCamera();
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
		cameraW = (camera.frustum.planePoints[1].x - camera.frustum.planePoints[0].x) / 2;
		cameraH = (camera.frustum.planePoints[3].y - camera.frustum.planePoints[0].y) / 2;

		//if (camera.position.y > everything.map().height() * (1 / (2 - camera.zoom)))
		//	camera.position.y = everything.map().height() * (1 / (2 - camera.zoom));
		if (camera.position.y > height - cameraH + 240)
		{
//			System.out.println("Cam y: " + camera.position.y);
//			System.out.println("height: " + height);
//			System.out.println("h: " + cameraH);
//			System.out.println("screenH: " + screenH);
			
			camera.position.y = height - cameraH + 240;
		}

		if (camera.position.y < cameraH)
		{
//			System.out.println("Cam y: " + camera.position.y);
//			System.out.println("height: " + height);
//			System.out.println("h: " + cameraH);
			camera.position.y = cameraH;
		}

		//if (camera.position.x > everything.map().width() + gameUI.width())
		//	camera.position.x = everything.map().width() + gameUI.width();
		float temp = width - cameraW + 400;
		if (camera.position.x > temp) //(width - (w / 2) + 400))//(w / 4) - w + 400)
			camera.position.x = temp; //width - (w / 2) + 400; //(w / 4) - w + 400;

		if (camera.position.x < cameraW)
			camera.position.x = cameraW;
	}
	
	private void networkSetup()
	{
		client = new Client();
		client.start();
		Network.register(client);
		
		// ThreadedListener runs the listener methods on a different thread.
        client.addListener(new ThreadedListener(new Listener() 
        {
        	public void connected (Connection connection) 
        	{
        	}
        	
        	public void received (Connection connection, Object object) 
        	{
        		if (object instanceof LoginStatus)
        		{
        			LoginStatus status = (LoginStatus)object;
        			if (status.status >= 0)
        			{
        				team = (byte)status.status;
        				everything.setTeam(team);
        				
        				System.out.println("Entering as player " + team);
        				connected = true;
        				return;
        			}
        			else if (status.status == -1)
        			{
        				System.out.println("Server is full.");
        				System.exit(-1);
        			}
        			return;
        		}
        		
        		if (object instanceof CommandIn)
        		{
        			System.out.println("CommandIn");
        			commandQueue.add((CommandIn)object);
        		}
        		
        		if (object instanceof ServerMessage)
        		{
//        			ServerMessage msg = (ServerMessage)object;
        			
        			if (((ServerMessage)object).message == 1)
        			{
        				System.out.println("Game is starting!");
        				running = true;
        				timeAccumulator = 0;
        				everything.setRunning(true);
        			}
        			return;
        		}
        	}
        	
        	public void disconnected (Connection connection) 
        	{
        		System.out.println("Disconnected");
        		System.exit(0);
        	}
        }));
        
//        ui = new UI();
//
//        String host = ui.inputHost();
        try 
        {
                client.connect(5000, serverIp, Network.port);
                // Server communication after connection can go here, or in Listener#connected().
        } 
        catch (IOException ex) 
        {
        	System.out.println(ex.getMessage());
        	//System.exit(1);
//        	throw new FailedConnectionException(ex.getMessage());
                //ex.printStackTrace();
        }

//        name = ui.inputName();
        Login login = new Login();
        login.name = "Player";
        client.sendTCP(login);
	}

	@Override
	public void resize(int width, int height) 
	{
	}

	@Override
	public void pause() 
	{
//		startMusic.stop();
//		client.close();
//		client.stop();
	}

	@Override
	public void resume() 
	{
//		startMusic.play();
	}


	@Override
	public void show() 
	{
		if (Settings.getInstance().getSound() == SoundEnum.ON)
			everything.musicPlay();
		ready = true;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		everything.end();
		if (client != null)
		{
			client.close();
			client.stop();
		}
	}
	
	public class MessageCompare implements Comparator<CommandIn>
	{
		public int compare(CommandIn msg1, CommandIn msg2)
		{
			if (msg1.turn > msg2.turn)
				return 1;
			else if (msg1.turn < msg2.turn)
				return -1;
			return 0;
		}
	}
}
