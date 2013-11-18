package com.unknowngames.rainbowrage.screens;

//import com.badlogic.gdx.ApplicationListener;
import java.io.IOException;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.PriorityQueue;

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
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.esotericsoftware.minlog.Log;
import com.unknowngames.rainbowrage.Difficulty;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.RainbowRage;
import com.unknowngames.rainbowrage.TextEffect;
import com.unknowngames.rainbowrage.entity.*;
import com.unknowngames.rainbowrage.input.GameInput;
import com.unknowngames.rainbowrage.map.Coordinate;
import com.unknowngames.rainbowrage.map.Map;
import com.unknowngames.rainbowrage.networking.Network;
import com.unknowngames.rainbowrage.networking.User;
import com.unknowngames.rainbowrage.networking.Network.*;
import com.unknowngames.rainbowrage.parser.HeroStructure;
import com.unknowngames.rainbowrage.ui.GameMenu;
import com.unknowngames.rainbowrage.ui.GameUI;
import com.unknowngames.rainbowrage.ui.ScoreBoard;

public class GameScreen implements Screen
{
	private OrthographicCamera camera, uiCamera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	int counter1, counter2;
	private boolean isPaused;
	static boolean showRange;
	GameInput inputProcessor;
	static EverythingHolder everything;
	TextureRegion pauseRegion;
	int pauseCooldown;
	GameUI gameUI;
	Hero[] heroes = new Hero[2];
	RainbowRage game;
	Vector3 touchPoint;
	Vector3 gameTouchPoint;
	Difficulty difficulty;
	int level = 2;
	int income, resources;
	Audio tempMusic = Gdx.audio;
	Music startMusic;

//	boolean sentTurn = false;

	float cameraW, cameraH, width, height, screenH;

	float timeAccumulator = 0;
	float volume = .2f;
	boolean following = true;
	boolean multiplayer = false; // True with multiplayer
	boolean running = false; // False with Multiplayer
	boolean connected = false;
	byte team; // Team 0 is top and Team 1 is bottom
	Client client;
	ScoreBoard scoreBoard;

	String serverIp = "unknowngamestudio.com";
//	String serverIp = "localhost"; // Local Host
//	String serverIp = "ec2-204-236-164-26.us-west-1.compute.amazonaws.com";// "10.170.103.156";
																			// //
																			// EC2
																			// Server

	// String serverIp = "evil-monkey.ics.uci.edu";//"128.195.6.172";
	// String serverIp =
	// "ernie-the-giant-chicken.ics.uci.edu";//"128.195.6.172";
	// String serverIp = "169.234.242.202"; // My desktop

	float stepTime = 0.02f;
	Comparator<CommandIn> comparator = new MessageCompare();
	PriorityQueue<CommandIn> commandQueue = new PriorityQueue<CommandIn>(20,
			comparator);

	boolean ready = false, sentReady = false;

	GL10 gl;
	
	int currentDisplayMode = 0;
	
	Screen previousScreen;
	
	ThreadedListener threadedListener;

	public GameScreen(RainbowRage game, boolean multiplayer, Screen previous)
	{
		System.out.println("Starting game screen");
		long startTime = System.currentTimeMillis();
		
		everything.setMultiplayerGame(multiplayer);
		
		client = everything.getClient();

		gl = Gdx.graphics.getGL10();
		this.game = game;
		this.multiplayer = multiplayer;
		previousScreen = previous;
		everything.reset();
		EverythingHolder.registerGameScreen(this);
		isPaused = false;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		counter1 = 0;
		counter2 = 0;
		pauseCooldown = 0;

		camera = new OrthographicCamera(w, h);

		camera.zoom = 1.5f;
		uiCamera = new OrthographicCamera(w, h);
		uiCamera.setToOrtho(false);
		batch = new SpriteBatch();

		everything.loadMap(level);
		TextEffect.loadFonts();

		everything.load(batch);

		EverythingHolder.showRange = true;
		inputProcessor = new GameInput();

		Gdx.input.setInputProcessor(inputProcessor);
		GameUI.load(this);
		gameUI = new GameUI();

		GameInput.loadCamera(camera, uiCamera);
		GameInput.loadGame(this, gameUI);

		everything.initializeHeroes();
//		gameUI.setMessage("This is only a test");
		gameUI.setMessage(everything.getHero().getPhrase("start"));
		
		touchPoint = new Vector3();
		gameTouchPoint = new Vector3();

		width = everything.map().width();
		height = everything.map().height();
		screenH = Gdx.graphics.getHeight() / 2;

		everything.setGameMusic();
		everything.playGameMusic();

		if (!connected && multiplayer)
			networkSetup();
		if (!multiplayer)
		{
			running = true;
			everything.setRunning(true);
			team = 0;
			everything.setTeam((byte) team);
//			everything.add(0, 2);
//			everything.add(0, 2);
//			everything.add(1, 2);
//			everything.add(1, 2);
//			everything.add(3, 2);
		}
		else
		{
			team = (byte)everything.team();
		}
		
		for (int i = 0; i < 2; i++)
		{
			everything.add(0, i);
			everything.add(0, i);
			everything.add(1, i);
			everything.add(1, i);
			everything.add(3, i);
		}
		
		timeAccumulator = 0;

		System.out.println("That took: "
				+ (System.currentTimeMillis() - startTime) + " ms");
	}

	public boolean getMultiplayer()
	{
		return multiplayer;
	}

	public boolean getConnected()
	{
		return connected;
	}

	public Vector3 getPointCoord(int x, int y)
	{
		Vector3 newPoint = new Vector3();
		uiCamera.unproject(newPoint.set(x, y, 0));
		return newPoint;
	}

	public void toggleIsPaused()
	{
		isPaused = !isPaused;
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
		
		if (multiplayer && !sentReady)
		{
			sentReady = true;
			Command cmd = new Command();
			cmd.team = team;
			client.sendTCP(cmd);
		}
		if (ready && !isPaused && (everything.moreTurns() || !multiplayer))
			timeAccumulator += delta;

		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (following)
			centerOnHero();

		camera.update();
		camera.apply(gl);
		boundCamera();
		camera.update();
		camera.apply(gl);

		uiCamera.update();
		uiCamera.apply(gl);
		int maxLoops = 5;
		if (multiplayer)
			maxLoops = 1;
		while (!isPaused && timeAccumulator > stepTime && running && ready
				&& (everything.moreTurns() || !multiplayer) && maxLoops-- > 0)
		{
//			sentTurn = false;
			sendTurn();
			update();
			timeAccumulator -= stepTime;
		}

		if (scoreBoard == null)
		{
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			everything.map().background().draw(batch);
			everything.render((isPaused ? 0 : delta));

			batch.end();
		}

		if (scoreBoard == null && EverythingHolder.getSettings().showPath())
			everything.map().drawPaths(batch, camera);

		batch.setProjectionMatrix(uiCamera.combined);
		batch.begin();
		if (scoreBoard == null)
		{
			gameUI.render(batch, delta);
			pauseCooldown++;

			if (multiplayer == true && connected == false)
				EverythingHolder.getFont(2).draw(batch,
						"Waiting for a challenger...", 100, 300);
		}
		else
			scoreBoard.draw(batch);

		batch.end();
	}

	private void scoreBoards(int t)
	{
		if (t == -1)
			return;
		Gdx.input.setInputProcessor(null);
		if (t == team)
			scoreBoard = new ScoreBoard(0, this);
		else
			scoreBoard = new ScoreBoard(1, this);
		everything.stopGameMusic();
		if (multiplayer)
			this.sendFinished(t);

		startMusic = tempMusic.newMusic(Gdx.files
				.internal("audio/526296_In-My-Final-Hours.mp3"));
		startMusic.setLooping(true);
		startMusic.setVolume(EverythingHolder.getSettings().getMusicSound());
		startMusic.play();

		isPaused = true;
	}

	public void endGame()
	{
		System.out.println("Ending game");
		everything.end();
		everything.stopGameMusic();
		if (startMusic != null)
			startMusic.stop();
		game.mainMenuScreen.gameWon();
		game.setScreen(previousScreen);
//		game.setScreen(game.mainMenuScreen);
	}

	public void pullCommand()
	{
		Object object;

		while (true)
		{
			if (commandQueue.isEmpty())
				return;
			System.out.println(((CommandIn) commandQueue.peek()).turn + " : "
					+ everything.getTurn());
			if (everything.getTurn() != ((CommandIn) commandQueue.peek()).turn)
				return;

			object = commandQueue.remove();

			if (object instanceof CommandIn)
			{
				CommandIn command = (CommandIn) object;
				if (command.command >= 0 && command.command < 6)
				{
					System.out.println("Pulled unit send command.");
					everything.add(((CommandIn) object).command,
							((CommandIn) object).team);
				}
				else if (command.command > 6 && command.command < 10)
				{
					System.out.println("Pulled hero command.");
					everything.setHeroStance(command.team, command.command - 8);
				}
				else if (command.command == 10)
				{
					System.out.println("Pulled active cast command.");
					everything.activeHeroSkill(command.team);
				}
				else if (command.command == 11)
				{
					System.out.println("Pulled clear send command.");
					everything.clearSend(command.team);
				}
				else if (command.command >= 20)
				{
					System.out.println("Pulled upgrade command.");
					everything.buyUpgrade(command.command, command.team);
				}
			}
		}
	}

	public void update()
	{
		handleInput();
//		if (multiplayer)
			pullCommand();
		everything.update();
//		if (!multiplayer)
//			randomSpawner();
		scoreBoards(everything.winCondition());
		everything.sortEntities();
	}

	public void randomSpawner()
	{
		if (--counter2 < 0)
		{
			float rand = (float) Math.random();
			if (rand < 0.20f)
				everything.add(0, 2);
			else if (rand < 0.4f)
				everything.add(1, 2);
			else if (rand < 0.6f)
				everything.add(2, 2);
			else if (rand < 0.8f)
				everything.add(3, 2);
			else if (rand < 0.9f)
				everything.add(4, 2);
			else
				everything.add(5, 2);

			counter2 = (int) (Math.random() * 60) + 60;
		}
	}
	
	public void sendCommand(int command)
	{
		if (multiplayer)
		{
			Command cmd = new Command();
			cmd.type = (byte) command;
			cmd.team = team;
			cmd.turn = everything.turn();
			client.sendTCP(cmd);
		}
		else
		{
			CommandIn cmd = new CommandIn();
			cmd.command = (byte) command;
			cmd.team = (byte) everything.team();
			cmd.turn = everything.turn() + 1;
			commandQueue.add((CommandIn) cmd);
		}
	}
	
	public void sendFinished(int winner)
	{
		FinishedGame f = new FinishedGame();
		f.team = team;
		f.winner = winner;
		client.sendTCP(f);
		if (everything.getPlayer(0).getUsername().substring(0, 5).equals("Guest") &&
			everything.getPlayer(1).getUsername().substring(0, 5).equals("Guest"))
		{
			everything.getPrivatePlayerInfo().wins++;
			everything.getPrivatePlayerInfo().losses++;
		}
		else
		{
			if (winner == team)
				everything.getPrivatePlayerInfo().wins++;
			else
				everything.getPrivatePlayerInfo().losses++;
		}
	}

	public void sendMessage(String message)
	{
		if (multiplayer)
		{
			UserMessage msg = new UserMessage();
			msg.message = message;
			msg.team = team;
			msg.turn = everything.turn();
			client.sendTCP(msg);
		}
	}

	public void sendTurn()
	{
		if (multiplayer && //sentTurn == false && 
			everything.getTurn() == everything.getHighestTurn() - 9)
		{
			Command cmd = new Command();
			cmd.team = team;
			cmd.type = -2;
			cmd.turn = everything.turn();
			client.sendTCP(cmd);
//			sentTurn = true;
		}
	}
	
	public void buyUpgrade(int unit, int skill, int level, int team)
	{
		if (multiplayer && !connected)
			return;
		
		sendCommand(20 + level + skill * 3 + unit * 12);
		
//		System.out.println("Sending upgrade");
//		if (multiplayer && connected)
//		{
//			System.out.println("Sending upgrade!");
//			if (everything.funds() < everything.unitCost(unit, team))
//			{
//				return;
//			}
//			Command cmd = new Command();
//			cmd.team = (byte) team;
//			cmd.type = (byte)(20 + level + skill * 3 + unit * 12);
////			cmd.type = (byte)(20 + team + level * 2 + skill * 6 + unit * 18);
//			cmd.turn = everything.turn();
//			client.sendTCP(cmd);
//		}
//		else
//		{
//			CommandIn cmd = new CommandIn();
//			cmd.team = (byte) team;
//			cmd.command = (byte) (20 + level + skill * 3 + unit * 12);
//			cmd.turn = everything.turn() + 1;
//			commandQueue.add((CommandIn) cmd);
////			everything.buyUpgrade(unit, skill, level, 1);
//		}
	}

	public void buyUnit(int unit)
	{
		if (multiplayer && !connected)
			return;
		
		sendCommand(unit);
		
		/*if (multiplayer && connected)
		{
			System.out.println("Sending unit");
			if (everything.funds() < everything.unitCost(unit, team))
			{
				return;
			}
			Command cmd = new Command();
			cmd.team = team;
			cmd.type = (byte) unit;
			cmd.turn = everything.turn();
			client.sendTCP(cmd);
		}
		else
		{
			CommandIn cmd = new CommandIn();
			cmd.team = team;
			cmd.command = (byte) unit;
			cmd.turn = everything.turn() + 1;
			commandQueue.add((CommandIn) cmd);
//			everything.add(unit, 1);
		}*/
	}

	public void setHeroStance(int stance)
	{
		if (multiplayer && !connected)
			return;
		
		sendCommand(stance + 8);
		/*if (multiplayer && connected)
		{
			Command cmd = new Command();
			cmd.type = (byte) (stance + 8);
			cmd.team = team;
			cmd.turn = everything.turn();
			client.sendTCP(cmd);
		}
		else
		{
			CommandIn cmd = new CommandIn();
			cmd.team = team;
			cmd.command = (byte) (stance + 8);
			cmd.turn = everything.turn() + 1;
			commandQueue.add((CommandIn) cmd);
			//everything.setHeroStance(1, stance);
		}*/
	}
	
	public void clearSend()
	{
		if (multiplayer && !connected)
			return;
		
		sendCommand(11);
	}

	public void castHeroActive()
	{
		if (multiplayer && !connected)
			return;
		
		sendCommand(10);
		
//		if (++currentDisplayMode == 1)
//			Gdx.graphics.setDisplayMode(1680, 1050, false);
//		else if (currentDisplayMode == 2)
//			Gdx.graphics.setDisplayMode(1920, 1080, false);
//		else
//		{
//			currentDisplayMode = 0;
//			Gdx.graphics.setDisplayMode(800, 480, false);
//		}
//		
//		everything.rescale();
//		if (Gdx.graphics.isFullscreen())
//			Gdx.graphics.setDisplayMode(800, 480, false);
//		else
//			Gdx.graphics.setDisplayMode(1920, 1200, true);
		/*if (multiplayer && connected)
		{
			Command cmd = new Command();
			cmd.type = 10;
			cmd.team = team;
			cmd.turn = everything.turn();
			client.sendTCP(cmd);
		}
		else
		{
			CommandIn cmd = new CommandIn();
			cmd.command = 10;
			cmd.team = (byte) everything.team();
			cmd.turn = everything.turn() + 1;
			commandQueue.add((CommandIn) cmd);
			// System.out.println("CASTING SPELL");
//			everything.activeHeroSkill(1);
		}*/
//		if (multiplayer)
//		{
//			Command cmd = new Command();
//			cmd.type = 10;
//			cmd.team = team;
//			cmd.turn = everything.turn();
//			client.sendTCP(cmd);
//		}
//		else
//		{
//			// System.out.println("CASTING SPELL");
//			everything.activeHeroSkill(1);
//		}
	}

	public void upgradeTower(int tower)
	{
		// if (multiplayer)
		// {
		// System.out.println("Trying to upgrade tower " + tower + " from team "
		// + team);
		// Command cmd = new Command();
		// cmd.team = team;
		// cmd.type = (byte)(tower + 10);
		// client.sendTCP(cmd);
		// }
		// else
		// {
		everything.upgradeTower(tower, team);
		// everything.funds -= 40;
		System.out.println("Trying to upgrade towers " + tower + " from team "
				+ team);
		// }
	}

	private void handleInput()
	{
		if (Gdx.input.isKeyPressed(Input.Keys.UP)
				|| Gdx.input.isKeyPressed(Input.Keys.W))
			camera.translate(0, 10);
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
				|| Gdx.input.isKeyPressed(Input.Keys.S))
			camera.translate(0, -10);
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
				|| Gdx.input.isKeyPressed(Input.Keys.D))
			camera.translate(10, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
				|| Gdx.input.isKeyPressed(Input.Keys.A))
			camera.translate(-10, 0);

		if ((Gdx.input.isKeyPressed(Input.Keys.P)) && (pauseCooldown > 100 || isPaused))
		{
//			toggleIsPaused();
			pauseCooldown = 0;
		}
		if (Gdx.input.justTouched())
		{
			uiCamera.unproject(touchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));
			camera.unproject(gameTouchPoint.set(Gdx.input.getX(),
					Gdx.input.getY(), 0));

			Actor selected = everything.atPoint(gameTouchPoint.x,
					gameTouchPoint.y);
			if (selected instanceof Building)
			{
				if (selected.team() == team && !selected.isAlive()
						&& everything.funds() >= 40)
				{
					upgradeTower(((Building) selected).towerNumber());
				}
			}
		}
	}

	public void centerOnHero()
	{
		// System.out.println("CENTERING");
		if (!everything.getHero().isAlive())
			return;

		camera.position.x = everything.getHero().xCoord();
		camera.position.y = everything.getHero().yCoord() + 40;
		// boundCamera();
	}

	public void quit()
	{
		System.out.println("Quitting");
		if (client != null)
			client.close();
		Gdx.app.exit();
	}

	public void boundCamera()
	{
		if (camera.zoom > everything.getMinZoom())
			camera.zoom = everything.getMinZoom();
		// if (camera.zoom > 2.25/ everything.getSizeRatio())
		// camera.zoom = 2.25f / everything.getSizeRatio();
		if (camera.zoom < .35f / everything.getSizeRatio())
			camera.zoom = .35f / everything.getSizeRatio();
		/*
		 * float width = camera.viewportWidth; int w = Gdx.graphics.getWidth() /
		 * 2; int h = Gdx.graphics.getHeight() / 2;
		 */

		/*
		 * float w = camera.viewportWidth / 2; float h = camera.viewportHeight /
		 * 2;
		 */
		cameraW = (camera.frustum.planePoints[1].x - camera.frustum.planePoints[0].x) / 2;
		cameraH = (camera.frustum.planePoints[3].y - camera.frustum.planePoints[0].y) / 2;

		// if (camera.position.y > everything.map().height() * (1 / (2 -
		// camera.zoom)))
		// camera.position.y = everything.map().height() * (1 / (2 -
		// camera.zoom));
		if (camera.position.y > height - cameraH)// + 240)
		{
			// System.out.println("Cam y: " + camera.position.y);
			// System.out.println("height: " + height);
			// System.out.println("h: " + cameraH);
			// System.out.println("screenH: " + screenH);

			camera.position.y = height - cameraH;// + 240;
		}

		if (camera.position.y < cameraH)
		{
			// System.out.println("Cam y: " + camera.position.y);
			// System.out.println("height: " + height);
			// System.out.println("h: " + cameraH);
			camera.position.y = cameraH;
		}

		// if (camera.position.x > everything.map().width() + gameUI.width())
		// camera.position.x = everything.map().width() + gameUI.width();
		float temp = width - cameraW;// + 400;
		if (camera.position.x > temp) // (width - (w / 2) + 400))//(w / 4) - w +
										// 400)
			camera.position.x = temp; // width - (w / 2) + 400; //(w / 4) - w +
										// 400;

		if (camera.position.x < cameraW)
			camera.position.x = cameraW;
	}

	private void networkSetup()
	{
		if (client == null || !client.isConnected())
		{
			client = new Client();
			client.start();
		}
		
		Network.register(client);
		
		threadedListener = new ThreadedListener(new Listener()
		{
			public void connected(Connection connection)
			{
			}

			public void received(Connection connection, Object object)
			{
				/*if (object instanceof LoginStatus)
				{
					LoginStatus status = (LoginStatus) object;
					if (status.status >= 0)
					{
						System.out.println("Loginstatus");
						team = (byte) status.status;
						everything.setTeam(team);
						gameUI.setup();
						// System.out.println("Entering as player " + team);
						connected = true;
						return;
					}
					else if (status.status == -1)
					{
						// System.out.println("Server is full.");
						System.exit(-1);
					}
					return;
				}*/

				if (object instanceof CommandIn)
				{
					System.out.println("CommandIn");
					if (((CommandIn) object).command == -2)
					{
						// System.out.println("Highest now " +
						// ((CommandIn)object).turn);
						System.out.println("New highest " + ((CommandIn)object).turn);
						everything.setHighestTurn(((CommandIn) object).turn);
						if (!connected)
							connected = true;
					}
					else
						commandQueue.add((CommandIn) object);
				}

				if (object instanceof ServerMessage)
				{
					// ServerMessage msg = (ServerMessage)object;

					if (((ServerMessage) object).message == 1)
					{
						// System.out.println("Game is starting!");
						running = true;
						timeAccumulator = 0;
						everything.setRunning(true);
					}
					// else if (((ServerMessage)object).message > 10)
					// {
					// everything.highestTurn = ((ServerMessage)object).message;
					// }
					return;
				}

				if (object instanceof UserMessage)
				{
					receivedMessage((UserMessage) object);
//					gameUI.setMessage(((UserMessage) object).message);
				}
			}

			public void disconnected(Connection connection)
			{
				endGame();
				// System.out.println("Disconnected");
				// System.exit(0);
			}
		});
		
		client.addListener(threadedListener);
		
		// ThreadedListener runs the listener methods on a different thread.
		/*client.addListener(new ThreadedListener(new Listener()
		{
			public void connected(Connection connection)
			{
			}

			public void received(Connection connection, Object object)
			{
				if (object instanceof LoginStatus)
				{
					LoginStatus status = (LoginStatus) object;
					if (status.status >= 0)
					{
						System.out.println("Loginstatus");
						team = (byte) status.status;
						everything.setTeam(team);
						gameUI.setup();
						// System.out.println("Entering as player " + team);
						connected = true;
						return;
					}
					else if (status.status == -1)
					{
						// System.out.println("Server is full.");
						System.exit(-1);
					}
					return;
				}

				if (object instanceof CommandIn)
				{
					System.out.println("CommandIn");
					if (((CommandIn) object).command == -2)
					{
						// System.out.println("Highest now " +
						// ((CommandIn)object).turn);
						System.out.println("New highest " + ((CommandIn)object).turn);
						everything.setHighestTurn(((CommandIn) object).turn);
						if (!connected)
							connected = true;
					}
					else
						commandQueue.add((CommandIn) object);
				}

				if (object instanceof ServerMessage)
				{
					// ServerMessage msg = (ServerMessage)object;

					if (((ServerMessage) object).message == 1)
					{
						// System.out.println("Game is starting!");
						running = true;
						timeAccumulator = 0;
						everything.setRunning(true);
					}
					// else if (((ServerMessage)object).message > 10)
					// {
					// everything.highestTurn = ((ServerMessage)object).message;
					// }
					return;
				}

				if (object instanceof UserMessage)
				{
					receivedMessage((UserMessage) object);
//					gameUI.setMessage(((UserMessage) object).message);
				}
			}

			public void disconnected(Connection connection)
			{
				endGame();
				// System.out.println("Disconnected");
				// System.exit(0);
			}
		}));*/

		// ui = new UI();
		//
		// String host = ui.inputHost();
		try
		{
			if (!client.isConnected())
				client.connect(5000, serverIp, Network.port);
			// Server communication after connection can go here, or in
			// Listener#connected().
		}
		catch (IOException ex)
		{
			System.out.println(ex.getMessage());
			// System.exit(1);
			// throw new FailedConnectionException(ex.getMessage());
			// ex.printStackTrace();
		}

		// name = ui.inputName();
		/*Login login = new Login();
		login.name = "Player";
		client.sendTCP(login);*/
	}
	
	private void receivedMessage(UserMessage m)
	{
		gameUI.setMessage(everything.getHero(m.team).getPhrase(m.message));
	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
		// startMusic.stop();
		// client.close();
		// client.stop();
	}

	@Override
	public void resume()
	{
		Gdx.input.setInputProcessor(inputProcessor);
		if (multiplayer)
			client.addListener(threadedListener);
		// startMusic.play();
	}

	@Override
	public void show()
	{
		// if (Settings.getInstance().getSound() == SoundEnum.ON)
		everything.musicPlay();
		ready = true;
		// camera.translate(everything.map().width() / 2,
		// everything.map().height() / 2);
	}

	@Override
	public void hide()
	{
		System.out.println("Hiding");
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(null);// setInputProcessor(inputProcessor);
		if (multiplayer)
			client.removeListener(threadedListener);
		everything.end();
		/*if (client != null)
		{
			client.close();
			client.stop();
		}*/
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
	
	public void refreshButtons()
	{
		gameUI.refreshButtons();
	}
}
