package com.unknowngames.rainbowrage.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.EverythingHolder.TeamColor;
import com.unknowngames.rainbowrage.Player;
import com.unknowngames.rainbowrage.RainbowRage;
import com.unknowngames.rainbowrage.networking.Network.HeroSelectStatus;
import com.unknowngames.rainbowrage.networking.Network.LoginStatus;
import com.unknowngames.rainbowrage.networking.Network.ServerMessage;
import com.unknowngames.rainbowrage.networking.Network.StartGameInfo;
import com.unknowngames.rainbowrage.parser.ActorStructure;
import com.unknowngames.rainbowrage.parser.HeroStructure;
import com.unknowngames.rainbowrage.ui.ActorSkillDisplay;
import com.unknowngames.rainbowrage.ui.Button;
import com.unknowngames.rainbowrage.ui.RoundButton;

public class HeroSelectScreen extends BaseClass implements Screen 
{
//	EverythingHolder everything;
	SpriteBatch batch;// = new SpriteBatch();
	OrthographicCamera camera;
	TextureRegion background, selection;
	TextureRegion[] heroNames = new TextureRegion[3];
	Button[] buttons = new Button[11];
	int diameter; //= 40 * scale;
	int width = Gdx.graphics.getWidth();
	int height = Gdx.graphics.getHeight();
	int selected = 0;
	
//	String hero[] = {"swordface", "mrwizard"};
	//String color[] = {"red", "blue"};
//	TeamColors teamColors[] = new TeamColors[]{TeamColors.red, TeamColors.blue};
//	teamColors[0] = red;
	TeamColor teamColors[] = new TeamColor[]{TeamColor.red, TeamColor.blue, TeamColor.green, TeamColor.orange, TeamColor.purple, TeamColor.yellow};
	
	GL10 gl = Gdx.graphics.getGL10();
	Vector3 touchPoint = new Vector3();
	RainbowRage game;
	
	ActorSkillDisplay skillDisplay;
	ActorStructure selectedActor;
	
	Rectangle glViewport;
	float scale;
	boolean multiplayer;
	Player players[] = new Player[2];
	boolean ready[] = {false, false};
	int team = 0;
	
	Client client;
	
	String[] allHeroes;
	
	boolean start = false;
	
	public HeroSelectScreen(RainbowRage game, boolean multiplayer)
	{
		this.game = game;
		this.multiplayer = multiplayer;
		players[0] = everything.getPlayer(0);
		players[1] = everything.getPlayer(1);
		
		team = everything.team();
		allHeroes = everything.getAllHeroes();
		
		System.out.println(players[0].userName() + " vs. " + players[1].userName());
		
		if (multiplayer)
		{
			client = everything.getClient();
			
			client.addListener(new ThreadedListener(new Listener()
			{
				public void received(Connection connection, Object object)
				{	
					if (object instanceof HeroSelectStatus)
					{
						HeroSelectStatus status = (HeroSelectStatus)object;
						System.out.println(players[status.team].userName() + " : " + status.heroname);
						players[status.team].setColor(status.teamColor);
						players[status.team].setHero(status.heroname);
						ready[status.team]  = status.ready;
						System.out.println("READY: " + ready[0] + " : " + ready[1]);
						checkReady();
					}
				}

				public void disconnected(Connection connection)
				{
				}
			}));
		}
		else
		{
			team = 0;
		}
		
		resetDrawing();
	}
	
	public void resetDrawing()
	{
		scale = everything.getScreenScale();
		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(false);
		batch = new SpriteBatch();
		diameter = (int)(40 * scale);
//		batch.setProjectionMatrix(camera.combined);
//		this.batch = batch;
//		background = new TextureRegion(new Texture(Gdx.files.internal("images/mainmenubackground.jpg")), 0, 0, 800, 480);
		background = EverythingHolder.getObjectTexture("mainbackground");
		
		selection = EverythingHolder.getObjectTexture("heroselection");
		
		buttons[0] = new RoundButton(100 * scale, 80 * scale, 75 * scale, EverythingHolder.getObjectTexture("heroselectsword"));
		buttons[1] = new RoundButton(100 * scale, 240 * scale, 75 * scale, EverythingHolder.getObjectTexture("heroselectarrow"));
		buttons[2] = new RoundButton(100 * scale, 400 * scale, 75 * scale, EverythingHolder.getObjectTexture("heroselectwizard"));
		buttons[3] = new RoundButton(width - 130 * scale, 45 * scale, diameter, EverythingHolder.getObjectTexture("confirmbutton"));
		buttons[4] = new RoundButton(width - 45 * scale, 45 * scale, diameter, EverythingHolder.getObjectTexture("backbutton"));
		buttons[5] = new RoundButton(660 * scale, 150 * scale, diameter, EverythingHolder.getObjectTexture("redbutton"));
		buttons[6] = new RoundButton(750 * scale, 150 * scale, diameter, EverythingHolder.getObjectTexture("bluebutton"));
		buttons[7] = new RoundButton(660 * scale, 240 * scale, diameter, EverythingHolder.getObjectTexture("greenbutton"));
		buttons[8] = new RoundButton(750 * scale, 240 * scale, diameter, EverythingHolder.getObjectTexture("orangebutton"));
		buttons[9] = new RoundButton(660 * scale, 330 * scale, diameter, EverythingHolder.getObjectTexture("purplebutton"));
		buttons[10] = new RoundButton(750 * scale, 330 * scale, diameter, EverythingHolder.getObjectTexture("yellowbutton"));
		
		buttons[1].setClickable(false);
		buttons[2].setClickable(false);
//		buttons[5].setClickable(false);
		selectColor(5);
		
		heroNames[0] = EverythingHolder.getObjectTexture("heronamesword");
		heroNames[1] = EverythingHolder.getObjectTexture("heronamewizard");
		heroNames[2] = EverythingHolder.getObjectTexture("heronamearrow");
		
		skillDisplay = new ActorSkillDisplay((int)(200 * scale), (int)(125 * scale));
//		skillDisplay.setActor(selectedActor);
//		setSelectedHero(hero[0]);
		if (!multiplayer)
		{
			setSelectedHero(0);
		}
		
		glViewport = new Rectangle(0, 0, width, height);
	}
	
//	public void loadEverything(EverythingHolder e, SpriteBatch b)
//	{
//		everything = e;
//		batch = b;
//	}

	@Override
	public void render(float delta) 
	{
		update(delta);
		
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glViewport((int)glViewport.x, (int)glViewport.y, (int)glViewport.width, (int)glViewport.height);
		camera.update();
		camera.apply(gl);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		batch.draw(background, 0, 0, width, height);
		
		batch.setColor(1, 1, 1, .5f);
		batch.draw(EverythingHolder.getObjectTexture("upgradeBackground"), 0, 0, width, height);
		batch.setColor(Color.WHITE);
		
		EverythingHolder.font[2].draw(batch, "Choose your hero and team color!", 210 * scale, 450 * scale);
//		for (int i = 0; i < 3; i++)
//			batch.draw(heroNames[i], buttons[i].xCoord() - 70, 180, 151, 55);
		
		
//		batch.draw(heroNames[0], 30, 180, 151, 55);
//		batch.draw(heroNames[1], 230, 180, 151, 55);
//		batch.draw(heroNames[2], 430, 180, 151, 55);
		
		batch.draw(selection, buttons[selected].xCoord() - 85 * scale, buttons[selected].yCoord() - 93 * scale, selection.getRegionWidth() * scale, selection.getRegionWidth() * scale);//, 90, 98);
		for (int i = 0; i < 11; i++)
			buttons[i].draw(batch, delta);
		
		skillDisplay.render(batch);
		
		batch.end();
		
		if (start)
		{
			start = false;
			everything.loadTeams();
			game.gameScreen = new GameScreen(game, multiplayer);
			game.setScreen(game.gameScreen);
		}
	}
	
	public void update(float delta)
	{
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0f));
//			touchPoint.set(Gdx.input.getX(), Gdx.input.getY());
			System.out.println(touchPoint.x + " " + touchPoint.y);
			skillDisplay.hit(touchPoint.x, touchPoint.y);
			for (int i = 0; i < buttons.length; i++)
				if (buttons[i].hit(touchPoint.x, touchPoint.y))
				{
					buttonHit(i);
					return;
				}
		}
	}
	
	public void buttonHit(int h)
	{
		Gdx.input.vibrate(50);
		/*if (h == 0) // Swordface
		{
			setSelectedHero("swordface");
			return;
		}
		else if (h == 1) // Arroweyes
		{
			setSelectedHero("arroweyes");
			return;
		}
		else if (h == 2) // Mr. Wizard
		{
			setSelectedHero("mrwizard");
			return;
		}*/
		if (h >= 0 && h <= 2)
		{
			setSelectedHero(h);
			return;
		}
		else if (h == 3) // Start
		{
			System.out.println(players[0].getHero() + " : " + players[1].getHero());
			System.out.println(players[0].getColor().name() + " : " + players[1].getColor().name());
//			everything.loadTeams();
			ready();
//			everything.loadTeams(players);
//			everything.loadTeams(players[0].getColor(), players[1].getColor(), players[0].getHero(), players[1].getHero());
//			System.out.println(hero[0] + " : " + hero[1]);
//			everything.loadTeams(color[0], color[1], hero[0], hero[1]);
			/*game.gameScreen = new GameScreen(game, false);
			game.setScreen(game.gameScreen);*/
			return;
		}
		else if (h == 4) // Cancel
		{
			game.setScreen(game.mainMenuScreen);
			return;
		}
		/*else if(h == 5) // Red
		{
			color[0] = "red";
			color[1] = "blue";
		}
		else if (h == 6) // Blue
		{
			color[0] = "blue";
			color[1] = "green";
		}
		else if (h == 7) // Green
		{
			color[0] = "green";
			color[1] = "orange";
		}
		else if (h == 8) // Orange
		{
			color[0] = "orange";
			color[1] = "purple";
		}
		else if (h == 9) // Purple
		{
			color[0] = "purple";
			color[1] = "yellow";
		}
		else if (h == 10) // Yellow
		{
			color[0] = "yellow";
			color[1] = "red";
		}*/
		else if (h >= 5 && h <= 10)
			selectColor(h);
	}
	
	private void ready()
	{
		if (multiplayer)
		{
			HeroSelectStatus selectStatus = new HeroSelectStatus();
			selectStatus.heroname = everything.getSelfPlayer().getHero();
			selectStatus.teamColor = everything.getSelfPlayer().getColor();
			selectStatus.ready = true;
			selectStatus.team = (byte)team;
			System.out.println("Team: " + team);
			client.sendTCP(selectStatus);
//			start();
		}
		else
			start();
	}
	
	
	private void checkReady()
	{
		if (multiplayer)
		{
			for (boolean b : ready)
				if (!b)
					return;
			
			start();
		}
	}
	private void start()
	{
		start = true;
//		everything.loadTeams();
//		game.gameScreen = new GameScreen(game, multiplayer);
//		game.setScreen(game.gameScreen);
	}
	
	private void setSelectedHero(int h)
	{
		players[team].setHero(allHeroes[h]);
		selectedActor = everything.getHeroStats(allHeroes[h]);
		skillDisplay.setActor(selectedActor);
		for (int i = 0; i < 3; i++)
			buttons[i].setClickable(false);
		if (!multiplayer)
		{
			selectCounterHero(h);
		}
		selected = h;
		
		buttons[selected].setClickable(true);
	}
	
	private void selectCounterHero(int h)
	{
		players[1].setHero(allHeroes[(h + 1) % allHeroes.length]);
	}
	
	/*private void setSelectedHero(String h)
	{
		players[0].setHero(h);
		selectedActor = everything.getHeroStats(h);
		skillDisplay.setActor(selectedActor);
		for (int i = 0; i < 3; i++)
			buttons[i].setClickable(false);
		
		if (players[0].getHero() == "swordface")
		{
			selected = 0;
			players[1].setHero("mrwizard");
		}
		else if (players[0].getHero() == "arroweyes")
		{
			selected = 1;
			players[1].setHero("swordface");
		}
		else if (players[0].getHero() == "mrwizard")
		{
			selected = 2;
			players[1].setHero("arroweyes");
		}
		buttons[selected].setClickable(true);
		hero[0] = h;
		selectedActor = everything.getHeroStats(h);
		skillDisplay.setActor(selectedActor);
		for (int i = 0; i < 3; i++)
			buttons[i].setClickable(false);
		
		if (hero[0] == "swordface")
		{
			selected = 0;
			hero[1] = "mrwizard";
		}
		else if (hero[0] == "arroweyes")
		{
			selected = 1;
			hero[1] = "swordface";
		}
		else if (hero[0] == "mrwizard")
		{
			selected = 2;
			hero[1] = "arroweyes";
		}
		buttons[selected].setClickable(true);
	}*/
	
	private void selectColor(int c)
	{
		if (c < 5 || c > 10)
			return;
		System.out.println("Team: " + (team));
		players[team].setColor(teamColors[(c - 5) % teamColors.length]);
		
		if (!multiplayer)
			players[team % 2].setColor(teamColors[(team + 1) % teamColors.length]);
		
		/*else if(c == 5) // Red
		{
			players[0].setColor(TeamColor.red);
			players[1].setColor(TeamColor.blue);
		}
		else if (c == 6) // Blue
		{
			players[0].setColor(TeamColor.blue);
			players[1].setColor(TeamColor.green);
		}
		else if (c == 7) // Green
		{
			players[0].setColor(TeamColor.green);
			players[1].setColor(TeamColor.orange);
		}
		else if (c == 8) // Orange
		{
			players[0].setColor(TeamColor.orange);
			players[1].setColor(TeamColor.purple);
		}
		else if (c == 9) // Purple
		{
			players[0].setColor(TeamColor.purple);
			players[1].setColor(TeamColor.yellow);
		}
		else if (c == 10) // Yellow
		{
			players[0].setColor(TeamColor.yellow);
			players[1].setColor(TeamColor.red);
		}*/
		
		/*if (c < 5)
			return;
		
		else if(c == 5) // Red
		{
			color[0] = "red";
			color[1] = "blue";
		}
		else if (c == 6) // Blue
		{
			color[0] = "blue";
			color[1] = "green";
		}
		else if (c == 7) // Green
		{
			color[0] = "green";
			color[1] = "orange";
		}
		else if (c == 8) // Orange
		{
			color[0] = "orange";
			color[1] = "purple";
		}
		else if (c == 9) // Purple
		{
			color[0] = "purple";
			color[1] = "yellow";
		}
		else if (c == 10) // Yellow
		{
			color[0] = "yellow";
			color[1] = "red";
		}*/
		
		for (int i = 5; i < 11; i++)
			buttons[i].setClickable(false);
		buttons[c].setClickable(true);
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
