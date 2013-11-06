package com.unknowngames.rainbowrage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.unknowngames.rainbowrage.entity.*;
import com.unknowngames.rainbowrage.map.*;
import com.unknowngames.rainbowrage.parser.*;
import com.unknowngames.rainbowrage.screens.GameScreen;
import com.unknowngames.rainbowrage.skill.Skill;
import com.unknowngames.rainbowrage.skill.TravelingSkillContainer;

public class EverythingHolder 
{
	@SuppressWarnings("unchecked")
	String xmlVersion = "", gameVersion = "0.11_3_13";
	
	EntityComparator eCompare = new EntityComparator();
	static private SpriteBatch batch;
	static Map map;
	
	int waveInterval, waveTime, waveTimer, 
//		previousTime, 
		spawnTimer1, spawnTimer2, 
		spawnInterval1, spawnInterval2;
	
	int nano = 1000000000;
	int income1 = 100, income2 = 100;
	int funds1, funds2;
	int turn, highestTurn = 10;
	int petType = 0;
	
	float stepTime = 0.02f;
	byte team = 1;

//	static float musicVolume = 1f;
	
	public static boolean showRange;
	boolean spawning;
	boolean running;
	int team1Towers = 0, team2Towers = 0, towerIncome = 0;
	
	//	You can call: mrwizard, swordface, or arroweyes
	String[] heroNames = {"mrwizard", "swordface"};
	
	Hero playerHeroes[];
	Building playerBases[];
	
	LinkedList<Integer>[] pools;			// Pools for spawning
	ArrayList<Entity> entities;		// Holds all units, towers, projectiles, and spells
	private ArrayList<ParticleEffect> effects;
	

	MinionStructure[][] playerUnits;
	HeroStructure[] heroUnits;
	
	HashMap<String, MinionStructure> minionStats = new HashMap<String, MinionStructure>();
	HashMap<String, BuildingStructure> buildingStats = new HashMap<String, BuildingStructure>();
	HashMap<String, HeroStructure> heroStats = new HashMap<String, HeroStructure>();
	static HashMap<String, ParticleEffect> particleEffects = new HashMap<String, ParticleEffect>();
	HashMap<String, SkillStructure> skillStats = new HashMap<String, SkillStructure>();
	HashMap<String, SkillContainerStructure> skillContainerStats = new HashMap<String, SkillContainerStructure>();
	static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	static HashMap<String, SoundPack> unitSounds = new HashMap<String, SoundPack>();
	static HashMap<String, TextureRegion> objectTextures = new HashMap<String, TextureRegion>();
	static HashMap<String, UnitAnimation> unitAnimations = new HashMap<String, UnitAnimation>();
	static HashMap<String, BuildingAnimation> buildingAnimations = new HashMap<String, BuildingAnimation>();
	
	public static BitmapFont[] font = new BitmapFont[7]; //, font2;
	private Texture teamTextures[] = new Texture[2];
	
	public String[] color = {"blue", "red"};
	
	boolean finished = false;
	
	Stats stats;
	
	Settings settings = new Settings();
	
	private Music mainMusic, gameMusic, endMusic;
	
	Entity tempEnt = null;
	
	int[] sentUnits = new int[6];
	
	boolean top, bot;
	
	float sizeRatio, minZoom = 1, xRatio, yRatio;
	int screenSizeX = 0, screenSizeY = 0;
	
	List<TextEffect> textEffects = new LinkedList<TextEffect>(); 
	Iterator<TextEffect> textEffectItr;
	
	Random rand;
	
	float scale;
	
	Player[] players = new Player[2];
	
	static GameScreen gameScreen;
	
	public EverythingHolder()
	{
		Texture.setEnforcePotImages(true);
		
		reset();
				
		UnitParser unitParser = new UnitParser();
		xmlVersion = unitParser.getVersion();
		minionStats = unitParser.getMinionStats();
		buildingStats = unitParser.getBuildingStats();
		heroStats = unitParser.getHeroStats();
		skillStats = unitParser.getSkillStats();
		skillContainerStats = unitParser.getSkillContainerStats();
		
		// Wave control
//		waveTimer = System.nanoTime() / 1000000; // Timer to keep track of waves
		waveInterval = 	(int) (15 / stepTime); 	// Turns (15 seconds)
		waveTime = 		(int) (3 / stepTime);	// Turns (3 seconds)
				
//		spawning = false;
		BaseClass.load(this);
		
		// Load all assets
		loadEffects();
		loadSounds();
		loadTextures();
		finished = true;
		
		
		
		loadMusic();
		screenSizeX = Gdx.graphics.getWidth();
		screenSizeY = Gdx.graphics.getHeight();
		xRatio = (float)screenSizeX / 800;
		yRatio = (float)screenSizeY / 480;
		scale = (xRatio < yRatio ? xRatio : yRatio);
		sizeRatio = (float)Gdx.graphics.getWidth() / 800;
		
		loadFonts();
		
		System.out.println("Heap: " + Gdx.app.getJavaHeap());
	}
	
	public void rescale()
	{
		screenSizeX = Gdx.graphics.getWidth();
		screenSizeY = Gdx.graphics.getHeight();
		xRatio = (float)screenSizeX / 800;
		yRatio = (float)screenSizeY / 480;
		scale = (xRatio < yRatio ? xRatio : yRatio);
		sizeRatio = (float)Gdx.graphics.getWidth() / 800;
		loadFonts();
	}
	
	private void loadFonts()
	{
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Kingthings Exeter.ttf"));
		font[0] = generator.generateFont(18);
		font[1] = generator.generateFont(32);
		font[2] = generator.generateFont(45);
		font[2].setColor(1, 1, 1, 1);
		font[3] = generator.generateFont(45);
//		font[3].setColor(0, 0, 0, 1);
		font[4] = new BitmapFont(Gdx.files.internal("fonts/myfont.fnt"), Gdx.files.internal("fonts/myfont.png"), false);
		font[4].scale(-.45f);
		font[5] = new BitmapFont(Gdx.files.internal("fonts/myfont.fnt"), Gdx.files.internal("fonts/myfont.png"), false);
		font[5].scale(-.6f);
//		font[5].scale(-.5f);
		font[6] = new BitmapFont(Gdx.files.internal("fonts/myfont.fnt"), Gdx.files.internal("fonts/myfont.png"), false);
//		font[6].scale(-.5f);
//		font[4].setScale(scale);
		
		for (int i = 0; i < font.length; i++)
			font[i].scale(scale - 1f);
	}
	
	public static GameScreen getGameScreen()
	{
		return gameScreen;
	}
	
	public static void registerGameScreen(GameScreen gs)
	{
		gameScreen = gs;
	}
	
	public Player getSelfPlayer()
	{
		return getPlayer(team() - 1);
	}
	
	public Player getPlayer(int t)
	{
		return players[t];
	}
	
	public BuildingStructure getBuildingStructure(int i)
	{
		return buildingStats.get("arrowtower");
	}
	
	public ActorStructure getActorStructure(int i, int t)
	{
		return playerUnits[t - 1][i];
	}
	
	public HeroStructure getHeroStructure(int t)
	{
		return heroUnits[t - 1];
	}
	
	public void setRandom(int i)
	{
		 rand = new Random(i);
	}
	
	public int getRandom(int i)
	{
		int temp = rand.nextInt(i);
		System.out.println("Turn:" + turn + " Rand: " + temp);
		return temp;
//		return rand.nextInt(i);
	}
	
	public HeroStructure getHeroStats(String hero)
	{
		return heroStats.get(hero);
	}
	
	public void addTextEffect(float x, float y, String text, int cause)
	{
		textEffects.add(new TextEffect(x, y, text, cause));
	}
	
	public float getMinZoom()
	{
		return minZoom;
	}
	
	public float getXRatio()
	{
		return xRatio;
	}
	
	public float getYRatio()
	{
		return yRatio;
	}
	
	public float getSizeRatio()
	{
		return sizeRatio;
	}
	
	public float getScreenSizeX()
	{
		return screenSizeX;
	}
	
	public float getScreenSizeY()
	{
		return screenSizeY;
	}
	
	public String getXmlVersion()
	{
		return xmlVersion;
	}
	
	public String getGameVersion()
	{
		return gameVersion;
	}
	
	public void loadMusic()
	{
		Audio tempMusic = Gdx.audio;
		gameMusic = tempMusic.newMusic(Gdx.files.internal("audio/373780_The_Devil_On_A_Bicy.mp3"));
		gameMusic.setVolume(settings.musicSound);
		gameMusic.setLooping(true);
	}
	
	public void playGameMusic()
	{
		gameMusic.play();
	}
	
	public void pauseGameMusic()
	{
		gameMusic.pause();
	}
	
	public void stopGameMusic()
	{
		gameMusic.stop();
	}
	
	public void setGameMusic()
	{
		gameMusic.setVolume(settings.musicSound);
	}
	
	public int getSentUnit(int unit)
	{
		return sentUnits[unit];
	}
	
	public void loadMap(int level)
	{
		Texture texture;
		TextureRegion region;
		Sprite sprite;
		
		if (level == 0)
		{
			texture = new Texture(Gdx.files.internal("data/mockupmap.png"));
			region = new TextureRegion(texture, 0, 0, 800, 600);
			sprite = new Sprite(region);
	
			map = new Map(sprite, 800, 600, 100, 1030, 1300, 170);
			map.add(new Coordinate(100, 1030), 1);
			map.add(new Coordinate(1300, 1030), 1);
			map.add(new Coordinate(1300, 600), 1);
			map.add(new Coordinate(300, 600), 1);
			map.add(new Coordinate(300, 170), 1);
			map.add(new Coordinate(1300, 170), 1);
			
			map.add(new Coordinate(100, 1030), 2);
			map.add(new Coordinate(1300, 1030), 2);
			map.add(new Coordinate(1300, 600), 2);
			map.add(new Coordinate(300, 600), 2);
			map.add(new Coordinate(300, 170), 2);
			map.add(new Coordinate(1300, 170), 2);
		}
		else if (level == 1)
		{
			texture = new Texture(Gdx.files.internal("images/map1.jpg"));
			region = new TextureRegion(texture, 0, 0, 1200, 960);
			sprite = new Sprite(region);

			map = new Map(sprite, 1200, 960, 310, 870, 1210, 480);
			map.add(new Coordinate(330, 870), 1);
			map.add(new Coordinate(1190, 870), 1);
			map.add(new Coordinate(1190, 680), 1);
			map.add(new Coordinate(365, 680), 1);
			map.add(new Coordinate(365, 480), 1);
			map.add(new Coordinate(1200, 480), 1);
			
			map.add(new Coordinate(330, 885), 2);
			map.add(new Coordinate(1220, 885), 2);
			map.add(new Coordinate(1220, 665), 2);
			map.add(new Coordinate(395, 665), 2);
			map.add(new Coordinate(395, 495), 2);
			map.add(new Coordinate(1200, 495), 2);
			
			Coordinate[] sites = new Coordinate[3];
			sites[0] = new Coordinate(600, 910);
			sites[1] = new Coordinate(900, 910);
			sites[2] = new Coordinate(1270, 750);
			map.buildSites(sites, 1);
			
			sites = new Coordinate[3];
			sites[0] = new Coordinate(900, 445);
			sites[1] = new Coordinate(600, 445);
			sites[2] = new Coordinate(300, 550);
			map.buildSites(sites, 2);
		}
		else
		{
			texture = new Texture(Gdx.files.internal("images/mapBeach.jpg"));
			region = new TextureRegion(texture, 0, 0, 2048, 2048);
			sprite = new Sprite(region);
			sprite.setOrigin(0, 0);
			
			map = new Map(sprite, 2048, 2048, 462, 1363, 1546, 821);
			
			// Balanced map
			// Yellow
			map.add(new Coordinate(441, 1345), 1);  // Base 1  (441, 1358)
			map.add(new Coordinate(734, 1345), 1);
			map.add(new Coordinate(791, 1315), 1);  // Tower 1 (791, 1358)
			map.add(new Coordinate(848, 1345), 1);
			map.add(new Coordinate(1135, 1345), 1);
			map.add(new Coordinate(1192, 1315), 1); // Tower 2 (1192, 1358)
			map.add(new Coordinate(1255, 1345), 1);
			map.add(new Coordinate(1355, 1345), 1);
			map.add(new Coordinate(1390, 1305), 1); // Turn 1
			map.add(new Coordinate(1350, 1231), 1); // Tower 3 (1410, 1231)
			map.add(new Coordinate(1395, 1117), 1); // Turn 2
			map.add(new Coordinate(1249, 1117), 1);
			map.add(new Coordinate(1192, 1142), 1); // Tower 4 (1192, 1104)
			map.add(new Coordinate(1135, 1117), 1);
			map.add(new Coordinate(848, 1117), 1);
			map.add(new Coordinate(791, 1142), 1);  // Tower 5 (791, 1104)
			map.add(new Coordinate(734, 1117), 1);
			map.add(new Coordinate(582, 1117), 1);
			map.add(new Coordinate(528, 1073), 1);  // Turn 3
			map.add(new Coordinate(513, 978), 1);   // Tower 6 (571, 978)
			map.add(new Coordinate(573, 887), 1);
			map.add(new Coordinate(618, 838), 1);
			map.add(new Coordinate(734, 838), 1);
			map.add(new Coordinate(791, 808), 1);   // Tower 7 (791, 851)
			map.add(new Coordinate(848, 838), 1);
			map.add(new Coordinate(1135, 838), 1);
			map.add(new Coordinate(1192, 808), 1);  // Tower 8 (1192, 851)
			map.add(new Coordinate(1249, 838), 1);
			map.add(new Coordinate(1542, 838), 1);  // Base 2  (1542, 851)
			
			// Blue
			map.add(new Coordinate(441, 1371), 2);  // Base 1  (441, 1358)
			map.add(new Coordinate(734, 1371), 2);
			map.add(new Coordinate(791, 1401), 2);  // Tower 1 (791, 1358)
			map.add(new Coordinate(848, 1371), 2);
			map.add(new Coordinate(1135, 1371), 2);
			map.add(new Coordinate(1192, 1401), 2); // Tower 2 (1192, 1358)
			map.add(new Coordinate(1249, 1371), 2);
			map.add(new Coordinate(1365, 1371), 2);
			map.add(new Coordinate(1410, 1322), 2); // Turn 1
			map.add(new Coordinate(1468, 1231), 2); // Tower 3 (1410, 1231)
			map.add(new Coordinate(1455, 1135), 2);
			map.add(new Coordinate(1401, 1091), 2); // Turn 2
			map.add(new Coordinate(1249, 1091), 2);
			map.add(new Coordinate(1192, 1066), 2); // Tower 4 (1192, 1104)
			map.add(new Coordinate(1135, 1091), 2);
			map.add(new Coordinate(848, 1091), 2);
			map.add(new Coordinate(791, 1066), 2);  // Tower 5 (791, 1104)
			map.add(new Coordinate(734, 1091), 2);
			map.add(new Coordinate(594, 1091), 2);
			map.add(new Coordinate(631, 978), 2);   // Tower 6 (571, 978)
			map.add(new Coordinate(599, 904), 2);
			map.add(new Coordinate(634, 864), 2);
			map.add(new Coordinate(734, 864), 2);
			map.add(new Coordinate(791, 894), 2);   // Tower 7 (791, 851)
			map.add(new Coordinate(848, 864), 2);
			map.add(new Coordinate(1135, 864), 2);
			map.add(new Coordinate(1192, 894), 2);  // Tower 8 (1192, 851)
			map.add(new Coordinate(1249, 864), 2);
			map.add(new Coordinate(1542, 864), 2);  // Base 2  (1542, 851)
			
			Coordinate[] sites = new Coordinate[4];
			sites[0] = new Coordinate(791, 1358);
			sites[1] = new Coordinate(1192, 1358);
			sites[2] = new Coordinate(1410, 1231);
			sites[3] = new Coordinate(1192, 1104);
			map.buildSites(sites, 1);
			
			sites = new Coordinate[4];
			sites[0] = new Coordinate(1192, 851);
			sites[1] = new Coordinate(791, 851);
			sites[2] = new Coordinate(571, 978);
			sites[3] = new Coordinate(791, 1104);
			map.buildSites(sites, 2);
			
			team1Towers = 4;
			team2Towers = 4;
			towerIncome = 10;
		}

		minZoom = Math.min((float)map.width() / Gdx.graphics.getWidth(), 
						   (float)map.height() / Gdx.graphics.getHeight());
		
		buildMap();
	}
	
	public void shiftTowerCount(int shift)
	{
		team1Towers += shift;
		team2Towers -= shift;
	}
	
	public Stats Stats()
	{
		return stats;
	}
	
	public float getMusicLevel()
	{
		return settings.getMusicSound();
	}
	
	public float getSoundLevel()
	{
		return settings.getGameSound();
	}
	
	public int getHighestTurn()
	{
		return highestTurn;
	}
	
	public void setHighestTurn(int turn)
	{
		highestTurn = turn;
	}
	
	public int getTurn()
	{
		return turn;
	}
	
	public int activeCooldown()
	{
		return playerHeroes[team - 1].activeCooldown();
	}
	
	public boolean moreTurns()
	{
		if (turn <= highestTurn)
			return true;
		return false;
	}
	
	public void loadTeams(String one, String two, String hone, String htwo)
	{
		teamTextures[0] = new Texture(Gdx.files.internal("images/sprite" + one + ".png"));
		teamTextures[1] = new Texture(Gdx.files.internal("images/sprite" + two + ".png"));
		teamTextures[0].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		teamTextures[1].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		color[0] = one;
		color[1] = two;
		loadUnitAnimations();
		heroNames[0] = hone;
		heroNames[1] = htwo;
		System.out.println("Heap 2: " + Gdx.app.getJavaHeap());
	}
	
	public String teamColor(int team)
	{
		return color[team - 1];
	}
	
	public static UnitAnimation getUnitAnimation(String name)
	{
		return unitAnimations.get(name);
	}
	
	public static BuildingAnimation getBuildingAnimation(String name)
	{
		return buildingAnimations.get(name);
	}
	
	public Vector2 getUnitFeet(String name, int direction)
	{
		return unitAnimations.get(name).getFeet(direction);
	}
	
	public void loadUnitAnimations()
	{
		Animation[] animation = new Animation[8];
		Vector2[] points = new Vector2[8];
		for (int i = 0; i < color.length; i++)
		{
			// Swordsman
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(0, 0, 58, 94, 5, false, false, i);	// Down walk
			animation[1] = loadAnimation(0, 94, 68, 86, 5, false, false, i);	// Left walk
			animation[2] = loadAnimation(0, 94, 68, 86, 5, true, false, i);	// Right walk
			animation[3] = loadAnimation(0, 180, 62, 88, 5, false, false, i);	// Up walk
			animation[4] = loadAnimation(0, 268, 80, 92, 3, false, true, i);	// Down attack
			animation[5] = loadAnimation(0, 360, 76, 90, 3, false, true, i);	// Left attack
			animation[6] = loadAnimation(0, 360, 76, 90, 3, true, true, i);	// Right attack
			animation[7] = loadAnimation(0, 450, 92, 84, 3, false, true, i);	// Up attack
			
			points[0] = new Vector2(30, 10);
			points[1] = new Vector2(38, 4);
			points[2] = new Vector2(32, 4);
			points[3] = new Vector2(32, 4);
			points[4] = new Vector2(52, 16);
			points[5] = new Vector2(48, 8);
			points[6] = new Vector2(30, 8);
			points[7] = new Vector2(32, 2);
			
			unitAnimations.put("swordsman" + (i + 1), new UnitAnimation(animation, points));
			
			// Archer			
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(338, 0, 70, 92, 5, false, false, i);		// Down walk
			animation[1] = loadAnimation(338, 92, 70, 84, 5, false, false, i);		// Left walk
			animation[2] = loadAnimation(338, 92, 70, 84, 5, true, false, i);		// Right walk
			animation[3] = loadAnimation(338, 176, 82, 92, 5, false, false, i);		// Up walk
			animation[4] = loadAnimation(338, 268, 66, 88, 4, false, true, i);	// Down attack
			animation[5] = loadAnimation(338, 356, 78, 92, 4, false, true, i);	// Left attack
			animation[6] = loadAnimation(338, 356, 78, 92, 4, true, true, i);		// Right attack
			animation[7] = loadAnimation(338, 448, 80, 90, 4, false, true, i);	// Up attack
			
			points[0] = new Vector2(30, 4);
			points[1] = new Vector2(34, 2);
			points[2] = new Vector2(38, 2);
			points[3] = new Vector2(44, 4);
			points[4] = new Vector2(30, 2);
			points[5] = new Vector2(40, 10);
			points[6] = new Vector2(38, 10);
			points[7] = new Vector2(36, 2);
			
			unitAnimations.put("archer" + (i + 1), new UnitAnimation(animation, points));
			
			// Monk
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(0, 534, 60, 82, 5, false, false, i);
			animation[1] = loadAnimation(0, 616, 54, 84, 5, false, false, i);
			animation[2] = loadAnimation(0, 616, 54, 84, 5, true, false, i);
			animation[3] = loadAnimation(0, 700, 60, 82, 5, false, false, i);
			animation[4] = loadAnimation(0, 782, 60, 82, 4, false, true, i);
			animation[5] = loadAnimation(0, 864, 54, 82, 4, false, true, i);
			animation[6] = loadAnimation(0, 864, 54, 82, 4, true, true, i);
			animation[7] = loadAnimation(0, 946, 78, 82, 4, false, true, i);
			
			points[0] = new Vector2(16, 2);
			points[1] = new Vector2(12, 2);
			points[2] = new Vector2(16, 2);
			points[3] = new Vector2(16, 2);
			points[4] = new Vector2(16, 2);
			points[5] = new Vector2(12, 1);
			points[6] = new Vector2(16, 1);
			points[7] = new Vector2(19, 2);
			
			unitAnimations.put("monk" + (i + 1), new UnitAnimation(animation, points));
			
			// Mage
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(338, 538, 66, 96, 5, false, false, i);
			animation[1] = loadAnimation(338, 634, 60, 86, 5, false, false, i);
			animation[2] = loadAnimation(338, 634, 60, 86, 5, true, false, i);
			animation[3] = loadAnimation(338, 720, 66, 90, 5, false, false, i);
			animation[4] = loadAnimation(338, 810, 90, 92, 4, false, true, i);
			animation[5] = loadAnimation(338, 902, 72, 86, 4, false, true, i);
			animation[6] = loadAnimation(338, 902, 72, 86, 4, true, true, i);
			animation[7] = loadAnimation(338, 988, 80, 90, 4, false, true, i);
			
			points[0] = new Vector2(32, 10);
			points[1] = new Vector2(28, 2);
			points[2] = new Vector2(34, 2);
			points[3] = new Vector2(32, 10);
			points[4] = new Vector2(56, 8);
			points[5] = new Vector2(40, 2);
			points[6] = new Vector2(34, 2);
			points[7] = new Vector2(32, 6);
			
			unitAnimations.put("mage" + (i + 1), new UnitAnimation(animation, points));
			
			/////////////////////////// STIL NEED TO DO POINTS FOR NINJA TO ELEMENTAL ////////////////
			// Ninja
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(748, 0, 62, 84, 5, false, false, i);
			animation[1] = loadAnimation(748, 84, 58, 82, 5, false, false, i);
			animation[2] = loadAnimation(748, 84, 58, 82, 5, true, false, i);
			animation[3] = loadAnimation(748, 166, 62, 82, 5, false, false, i);
			animation[4] = loadAnimation(748, 248, 90, 82, 4, false, true, i);
			animation[5] = loadAnimation(748, 330, 60, 82, 4, false, true, i);
			animation[6] = loadAnimation(748, 330, 60, 82, 4, true, true, i);
			animation[7] = loadAnimation(748, 412, 84, 80, 4, false, true, i);
			
			points[0] = new Vector2(32, 8);
			points[1] = new Vector2(34, 4);
			points[2] = new Vector2(24, 4);
			points[3] = new Vector2(30, 6);
			points[4] = new Vector2(54, 4);
			points[5] = new Vector2(36, 4);
			points[6] = new Vector2(24, 4);
			points[7] = new Vector2(44, 4);
			
			unitAnimations.put("ninja" + (i + 1), new UnitAnimation(animation, points));
			
			// Eagle
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(748, 492, 88, 168, 4, false, false, i);
			animation[1] = loadAnimation(748, 660, 90, 168, 4, false, false, i);
			animation[2] = loadAnimation(748, 660, 90, 168, 4, true, false, i);
			animation[3] = loadAnimation(748, 828, 86, 156, 4, false, false, i);
			animation[4] = loadAnimation(748, 984, 88, 118, 4, false, true, i);
			animation[5] = loadAnimation(748, 1102, 88, 118, 4, false, true, i);
			animation[6] = loadAnimation(748, 1102, 88, 118, 4, true, true, i);
			animation[7] = loadAnimation(748, 1220, 86, 114, 4, false, true, i);
			
			points[0] = new Vector2(44, 0);
			points[1] = new Vector2(44, 0);
			points[2] = new Vector2(46, 0);
			points[3] = new Vector2(42, 0);
			points[4] = new Vector2(44, 0);
			points[5] = new Vector2(44, 0);
			points[6] = new Vector2(44, 0);
			points[7] = new Vector2(42, 0);
			
			unitAnimations.put("eagle" + (i + 1), new UnitAnimation(animation, points));
			
			// Wolf
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(1108, 0, 36, 106, 4, false, false, i);
			animation[1] = loadAnimation(1108, 106, 112, 62, 4, false, false, i);
			animation[2] = loadAnimation(1108, 106, 112, 62, 4, true, false, i);
			animation[3] = loadAnimation(1108, 168, 40, 100, 4, false, false, i);
			animation[4] = loadAnimation(1108, 268, 48, 110, 4, false, true, i);
			animation[5] = loadAnimation(1108, 380, 112, 70, 4, false, true, i);
			animation[6] = loadAnimation(1108, 380, 112, 70, 4, true, true, i);
			animation[7] = loadAnimation(1108, 450, 44, 96, 4, false, true, i);
			
			points[0] = new Vector2(30, 18);
			points[1] = new Vector2(50, 4);
			points[2] = new Vector2(62, 4);
			points[3] = new Vector2(20, 46);
			points[4] = new Vector2(18, 26);
			points[5] = new Vector2(50, 4);
			points[6] = new Vector2(62, 4);
			points[7] = new Vector2(18, 30);
			
			unitAnimations.put("wolf" + (i + 1), new UnitAnimation(animation, points));
	
			// Elemental
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(1108, 544, 102, 76, 5, false, false, i);
			animation[1] = loadAnimation(1108, 620, 64, 76, 5, false, false, i);
			animation[2] = loadAnimation(1108, 620, 64, 76, 5, true, false, i);
			animation[3] = loadAnimation(1108, 696, 100, 78, 5, false, false, i);
			animation[4] = loadAnimation(1108, 774, 102, 76, 4, false, true, i);
			animation[5] = loadAnimation(1108, 850, 64, 74, 4, false, true, i);
			animation[6] = loadAnimation(1108, 850, 64, 74, 4, true, true, i);
			animation[7] = loadAnimation(1108, 924, 112, 78, 4, false, true, i);
			
			points[0] = new Vector2(30, 10);
			points[1] = new Vector2(38, 4);
			points[2] = new Vector2(32, 4);
			points[3] = new Vector2(32, 4);
			points[4] = new Vector2(52, 16);
			points[5] = new Vector2(48, 8);
			points[6] = new Vector2(30, 8);
			points[7] = new Vector2(32, 2);
			
			unitAnimations.put("elemental" + (i + 1), new UnitAnimation(animation, points));
			
			// Stronghold
			animation = new Animation[1];
			points = new Vector2[1];
			
			animation[0] = loadAnimation(0, 1340, 144, 256, 1, false, false, i);
			points[0] = new Vector2(84, 16);
			
			buildingAnimations.put("stronghold" + (i + 1), new BuildingAnimation(animation, points));
			
			// Arrowtower
			animation = new Animation[1];
			points = new Vector2[1];
			
			animation[0] = loadAnimation(0, 1160, 112, 180, 3, false, false, i);
			points[0] = new Vector2(56, 4);
			
			buildingAnimations.put("arrowtower" + (i + 1), new BuildingAnimation(animation, points));
			
			// Rubble
			animation = new Animation[1];
			points = new Vector2[1];
			
			animation[0] = loadAnimation(0, 1030, 94, 130, 3, false, false, i);
			points[0] = new Vector2(46, 14);
			
			buildingAnimations.put("rubble" + (i + 1), new BuildingAnimation(animation, points));
			
			// Control Point
			animation = new Animation[1];
			points = new Vector2[1];
			
			animation[0] = loadAnimation(48, 893, 40, 40, 1, false, false, i);
			points[0] = new Vector2(20, 20);
			
			buildingAnimations.put("controlpoint" + (i + 1), new BuildingAnimation(animation, points));
		}
	}
	
	private Animation loadAnimation(int x, int y, int w, int h, int count, boolean flipX, boolean attacking, int team) // team0 = red, team1 = blue
	{
		TextureRegion[] frames = new TextureRegion[count];
		
		TextureRegion temp = new TextureRegion(teamTextures[team], x, y, w * count, h);
		TextureRegion[][] tmp = temp.split(w, h);		
		
		for (int i = 0; i < count; i++)
		{
			frames[i] = tmp[0][i];
			if (flipX)// || flipY)
				frames[i].flip(flipX, false);//flipY);
		}
		
		Animation tempAnimation = new Animation(.05f, frames);
		if (attacking)
			tempAnimation.setPlayMode(Animation.NORMAL);
		else
			tempAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		return tempAnimation;
	}
	
	public String getHeroName()
	{
		return heroNames[team - 1];
	}
	
	public static BitmapFont getFont(int f)
	{
		if (font[f] == null)
			font[f] = new BitmapFont();
		
		return font[f];
	}
	
	public boolean finished()
	{
		return finished;
	}
	
	public static SoundPack getUnitSounds(String name)
	{
		return unitSounds.get(name);
	}
	
	public static Sound getSound(String sound)
	{
		return sounds.get(sound);
	}
	
	public SkillStructure getSkill(String skill)
	{
		return skillStats.get(skill);
	}
	
	public SkillContainerStructure getSkillContainer(String skill)
	{
//		System.out.println("Skill Container: " + skill);
		return skillContainerStats.get(skill);
	}
	
	public float heroHealthRatio()
	{
		return playerHeroes[team - 1].getHealthRatio();
	}
	
	public float baseHealthRatio()
	{
		return playerBases[team - 1].getHealthRatio();
	}
	
	public static TextureRegion getObjectTexture(String name)
	{
		return objectTextures.get(name);
	}
	
	public void loadTextures()
	{
		Texture textures = new Texture(Gdx.files.internal("images/projectile_sheet.png"));
		textures.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		objectTextures.put("arrow", new TextureRegion(textures, 0, 0, 16, 16));
		objectTextures.put("arrowblue", new TextureRegion(textures, 0, 16, 16, 16));
		objectTextures.put("arrowred", new TextureRegion(textures, 16, 16, 16, 16));
		objectTextures.put("arrowgreen", new TextureRegion(textures, 0, 16, 16, 16));
		objectTextures.put("arroworange", new TextureRegion(textures, 16, 16, 16, 16));
		objectTextures.put("arrowpurple", new TextureRegion(textures, 0, 16, 16, 16));
		objectTextures.put("arrowyellow", new TextureRegion(textures, 16, 16, 16, 16));
		objectTextures.put("cannonball", new TextureRegion(textures, 16, 0, 16, 16));
		
		objectTextures.put("fireballred", new TextureRegion(textures, 0, 32, 16, 16));
		objectTextures.put("fireattackred", new TextureRegion(textures, 0, 48, 16, 16));
		objectTextures.put("fireballblue", new TextureRegion(textures, 16, 32, 16, 16));
		objectTextures.put("fireattackblue", new TextureRegion(textures, 16, 48, 16, 16));
		objectTextures.put("fireballgreen", new TextureRegion(textures, 0, 32, 16, 16));
		objectTextures.put("fireattackgreen", new TextureRegion(textures, 0, 48, 16, 16));
		objectTextures.put("fireballorange", new TextureRegion(textures, 16, 32, 16, 16));
		objectTextures.put("fireattackorange", new TextureRegion(textures, 16, 48, 16, 16));
		objectTextures.put("fireballpurple", new TextureRegion(textures, 0, 32, 16, 16));
		objectTextures.put("fireattackpurple", new TextureRegion(textures, 0, 48, 16, 16));
		objectTextures.put("fireballyellow", new TextureRegion(textures, 16, 32, 16, 16));
		objectTextures.put("fireattackyellow", new TextureRegion(textures, 16, 48, 16, 16));
		
		
		textures = new Texture(Gdx.files.internal("images/buttons_sheet.png"));
		textures.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		objectTextures.put("fireball", new TextureRegion(textures, 1814, 0, 90, 90));
		objectTextures.put("fireattack", new TextureRegion(textures, 1814, 90, 73, 73));
		
		// New style hero icon
		objectTextures.put("swordfacebutton", new TextureRegion(textures, 1890, 787, 158, 155));
		objectTextures.put("mrwizardbutton", new TextureRegion(textures, 1890, 942, 158, 155));
		objectTextures.put("arroweyesbutton", new TextureRegion(textures, 1890, 1097, 158, 155));
		
		objectTextures.put("swordbutton", new TextureRegion(textures, 0, 1071, 152, 153));
		objectTextures.put("archerbutton", new TextureRegion(textures, 0, 918, 152, 153));
		objectTextures.put("ninjabutton", new TextureRegion(textures, 0, 765, 152, 153));
		objectTextures.put("magebutton", new TextureRegion(textures, 0, 612, 152, 153));
		objectTextures.put("monkbutton", new TextureRegion(textures, 0, 459, 152, 153));
		objectTextures.put("petbutton", new TextureRegion(textures, 0, 1224, 152, 153));
		
		objectTextures.put("attackbutton", new TextureRegion(textures, 0, 0, 152, 153));
		objectTextures.put("defendbutton", new TextureRegion(textures, 0, 153, 152, 153));
		objectTextures.put("retreatbutton", new TextureRegion(textures, 0, 306, 152, 153));
		objectTextures.put("skillbutton", new TextureRegion(textures, 152, 0, 215, 209));
		
		// New new style buttons
		objectTextures.put("storebutton", new TextureRegion(textures, 1644, 787, 150, 149));
		objectTextures.put("upgradebutton", new TextureRegion(textures, 1644, 936, 150, 149));
		objectTextures.put("chatbutton", new TextureRegion(textures, 1644, 1085, 150, 149));
		
		objectTextures.put("confirmbutton", new TextureRegion(textures, 367, 0, 152, 153));
		objectTextures.put("cancelbutton", new TextureRegion(textures, 367, 153, 152, 153));
		objectTextures.put("backbutton", new TextureRegion(textures, 367, 306, 152, 153));
		objectTextures.put("forwardbutton", new TextureRegion(textures, 367, 459, 152, 153));
		objectTextures.put("singlebutton", new TextureRegion(textures, 367, 612, 152, 153));
		objectTextures.put("multibutton", new TextureRegion(textures, 367, 765, 152, 153));
		objectTextures.put("settingsbutton", new TextureRegion(textures, 367, 918, 152, 153));
		objectTextures.put("quitbutton", new TextureRegion(textures, 367, 1071, 152, 153));
		
		objectTextures.put("redbutton", new TextureRegion(textures, 152, 240, 127, 127));
		objectTextures.put("bluebutton", new TextureRegion(textures, 152, 367, 127, 127));
		objectTextures.put("greenbutton", new TextureRegion(textures, 152, 494, 127, 127));
		objectTextures.put("orangebutton", new TextureRegion(textures, 152, 621, 127, 127));
		objectTextures.put("purplebutton", new TextureRegion(textures, 152, 748, 127, 127));
		objectTextures.put("yellowbutton", new TextureRegion(textures, 152, 875, 127, 127));
		
		objectTextures.put("gamelogo", new TextureRegion(textures, 0, 1514, 842, 467));
		objectTextures.put("mainbuttonframe", new TextureRegion(textures, 880, 422, 361, 572));
		objectTextures.put("gamebuttonframe", new TextureRegion(textures, 519, 422, 361, 792));
		
		objectTextures.put("cashicon", new TextureRegion(textures, 152, 209, 33, 31));
		objectTextures.put("timeicon", new TextureRegion(textures, 185, 209, 33, 31));
		objectTextures.put("uniticon", new TextureRegion(textures, 218, 209, 33, 31));
		
		objectTextures.put("heroselectsword", new TextureRegion(textures, 1896, 328, 152, 153));
		objectTextures.put("heroselectwizard", new TextureRegion(textures, 1896, 481, 152, 153));
		objectTextures.put("heroselectarrow", new TextureRegion(textures, 1896, 634, 152, 153));
		objectTextures.put("heroselection", new TextureRegion(textures, 1722, 327, 174, 187));
		
		objectTextures.put("swordfaceimage", new TextureRegion(textures, 1294, 1170, 350, 323));
		objectTextures.put("arroweyesimage", new TextureRegion(textures, 1304, 1493, 340, 325));
		objectTextures.put("mrwizardimage", new TextureRegion(textures, 1294, 867, 350, 303));
		
		objectTextures.put("heronamesword", new TextureRegion(textures, 1333, 1939, 302, 109));
		objectTextures.put("heronamewizard", new TextureRegion(textures, 1333, 1830, 302, 109));
		objectTextures.put("heronamearrow", new TextureRegion(textures, 1333, 1721, 302, 109));
		
		objectTextures.put("endstatsbox", new TextureRegion(textures, 1635, 1824, 413, 224));
		objectTextures.put("endvictory", new TextureRegion(textures, 1687, 1711, 361, 62));
		objectTextures.put("enddefeat", new TextureRegion(textures, 1687, 1773, 360, 51));
		
		objectTextures.put("redrange", new TextureRegion(textures, 1604, 787, 40, 40));
		objectTextures.put("bluerange", new TextureRegion(textures, 1604, 827, 40, 40));
		
		objectTextures.put("armoricon", new TextureRegion(textures, 1644, 1085, 150, 149));
		objectTextures.put("defaulticon", new TextureRegion(textures, 1644, 936, 150, 149));
		
		objectTextures.put("healthFrame", new TextureRegion(textures, 519, 0, 431, 87));
		objectTextures.put("emptyHealth", new TextureRegion(textures, 598, 200, 345, 20));
		objectTextures.put("fullHealth", new TextureRegion(textures, 598, 287, 345, 20));
		objectTextures.put("nextWave", new TextureRegion(textures, 519, 348, 166, 74));
		
		objectTextures.put("upgradeBackground", new TextureRegion(textures, 598, 200, 1, 1));
		
		objectTextures.put("chatbox", new TextureRegion(textures, 1105, 182, 800, 87));
		objectTextures.put("heroface", new TextureRegion(textures, 1237, 1952, 96, 96));
		
		
		
		textures = new Texture(Gdx.files.internal("images/spriteblue.png"));
		textures.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		objectTextures.put("swordsmanimage", new TextureRegion(textures, 0, 94, 68, 86));
		objectTextures.put("archerimage", new TextureRegion(textures, 338, 92, 70, 84));
		objectTextures.put("monkimage", new TextureRegion(textures, 0, 616, 54, 84));
		objectTextures.put("mageimage", new TextureRegion(textures, 338, 634, 60, 86));
		objectTextures.put("ninjaimage", new TextureRegion(textures, 748, 84, 58, 82));
		objectTextures.put("wolfimage", new TextureRegion(textures, 1108, 106, 112, 62));
		objectTextures.put("eagleimage", new TextureRegion(textures, 748, 660, 90, 168));
		objectTextures.put("elementalimage", new TextureRegion(textures, 1108, 620, 64, 76));
		objectTextures.put("arrowtowerimage", new TextureRegion(textures, 0, 1160, 112, 180));
		
		String[] skillIconNames = {"armoricon", "berzerkicon", "parryicon", "reducedamageicon", "knockbackicon", 					// Sword
								   "arrowdamageicon", "arrowslowicon", "arrowpoisonicon", "arrowpoisonstackicon", "arrowrapidicon",	// Archer
								   "cloakicon", "passthroughdamageicon", "invincibleicon", "splashdeathicon", "backstabicon",		// Ninja
								   "fireballicon", "firebufficon", "deathexplodicon", "spelleffectivenessicon", "refreshdebufficon",// Mage
								   "healicon", "shieldicon", "dodgeicon", "selfhealicon", "removedebufficon",						// Monk
								   "herostunicon", "herostundurationicon", "herostunknockbackicon", "heroreducedamageicon", "woundicon", // Swordface
								   "herorapidfireicon", "herofirearrowsicon", "heresplitshowicon", "herotrapsicon", "blankicon",		 // Arroweyes
								   "herofireballicon", "herofiredoticon", "herofireballpassicon", "heroreflecticon", "heroincspeedicon", // MrWizard
		};
		TextureRegion[] skillIcons = spliteSpriteSheet("images/skillicons.png", 128, 128, 8, 8);
		
		for (int i = 0; i < skillIconNames.length; i++)
		{
			objectTextures.put(skillIconNames[i], skillIcons[i]);
		}
		
		
		/*textures = new Texture(Gdx.files.internal("images/skillicons.png"));
		textures.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
//		objectTextures.put("selfhealicon", new TextureRegion(textures, 0, 0, 128, 128));
		objectTextures.put("arrowpoisonicon", new TextureRegion(textures, 128, 0, 128, 128));
		objectTextures.put("arrowpoisonstackicon", new TextureRegion(textures, 256, 0, 128, 128));
		objectTextures.put("arrowrapidicon", new TextureRegion(textures, 384, 0, 128, 128));
		
		objectTextures.put("selfhealicon", new TextureRegion(textures, 0, 128, 128, 128));
		objectTextures.put("removedebufficon", new TextureRegion(textures, 128, 128, 128, 128));
		objectTextures.put("arrowdamageicon", new TextureRegion(textures, 256, 128, 128, 128));
		objectTextures.put("arrowslowicon", new TextureRegion(textures, 384, 128, 128, 128));
		
		objectTextures.put("knockbackicon", new TextureRegion(textures, 0, 256, 128, 128));
		objectTextures.put("healicon", new TextureRegion(textures, 128, 256, 128, 128));
		objectTextures.put("shieldicon", new TextureRegion(textures, 256, 256, 128, 128));
		objectTextures.put("dodgeicon", new TextureRegion(textures, 384, 256, 128, 128));
		
		objectTextures.put("reducedamageicon", new TextureRegion(textures, 0, 384, 128, 128));
		objectTextures.put("parryicon", new TextureRegion(textures, 128, 384, 128, 128));
		objectTextures.put("berzerkicon", new TextureRegion(textures, 256, 384, 128, 128));
		objectTextures.put("armoricon", new TextureRegion(textures, 384, 384, 128, 128));
		
		objectTextures.put("fireballicon", new TextureRegion(textures, 0, 512, 128, 128));
		objectTextures.put("firebufficon", new TextureRegion(textures, 128, 512, 128, 128));
		objectTextures.put("deathexplodicon", new TextureRegion(textures, 256, 512, 128, 128));
		objectTextures.put("spelleffectivenessicon", new TextureRegion(textures, 384, 512, 128, 128));
		
		objectTextures.put("refreshdebufficon", new TextureRegion(textures, 0, 640, 128, 128));
		objectTextures.put("cloakicon", new TextureRegion(textures, 128, 640, 128, 128));
		objectTextures.put("passthroughdamageicon", new TextureRegion(textures, 256, 640, 128, 128));
		objectTextures.put("invincibleicon", new TextureRegion(textures, 384, 640, 128, 128));
		
		objectTextures.put("splashdeathicon", new TextureRegion(textures, 0, 768, 128, 128));
		objectTextures.put("backstabicon", new TextureRegion(textures, 128, 768, 128, 128));
//		objectTextures.put("passthroughdamageicon", new TextureRegion(textures, 256, 768, 128, 128));
//		objectTextures.put("invincibleicon", new TextureRegion(textures, 384, 768, 128, 128));*/
		
//		textures.dispose();
	}
	
	public TextureRegion[] spliteSpriteSheet(String filename, int width, int height, int xCount, int yCount)
	{
		TextureRegion[] tempTexture = new TextureRegion[xCount * yCount];
		Texture temp = new Texture(Gdx.files.internal(filename));
		temp.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		for (int i = 0; i < xCount; i++)
			for (int j = 0; j < yCount; j++)
				tempTexture[i + j * xCount] = new TextureRegion(temp, i * width, j * height, width, height);
		
		
		return tempTexture;
	}
	
	public void loadSounds()
	{
		String[] soundNames = {
				"thwp"};
		
		for (String name : soundNames)
			sounds.put(name, Gdx.audio.newSound(Gdx.files.internal("audio/" + name + ".wav")));
		
		String[] unitNames = {
				"herowarrior",
				"heroarcher",
				"herowizard",
				"minionwarrior",
				"minionarcher",
				"minionninja",
				"minionwizard",
				"minionmonk",
				"minionwolf",
				"minionelemental",
				"minioneagle"};
		for (String name : unitNames)
			unitSounds.put(name, new SoundPack(name));
	}
	
	public void loadEffects()
	{
		String[] particleNames = {
				"fireball", "fireballexplosion", 
				"heal", 
				"largefireball", "largefireballexplosion", 
				"mediumfireball", "mediumfireballexplosion",
				"smallfireball", "smallfireballexplosion",
				"smokebomb",
				"stunattack", "stunattackeffect", 
				"attackspeedbuff",
				"increasearrowspeed",
				"fire",
				"spark",
				"blood",
				"rainbowtrailsparkle"};
		
		ParticleEffect temp;
		for (String name : particleNames)
		{
			temp = new ParticleEffect();
			try
			{
				temp.load(Gdx.files.internal("data/" + name + settings.getParticleEffects() + ".p"), Gdx.files.internal("images"));
			}
			catch (Exception e)
			{
				System.out.println("Don't have " + name + settings.getParticleEffects() + ".p");
				temp.load(Gdx.files.internal("data/" + name + ".p"), Gdx.files.internal("images"));
			}
			particleEffects.put(name, temp);
		}
	}
	
	public float getScreenScale()
	{
		return scale;
	}
	
	public static ParticleEffect getEffect(String e)
	{
//		System.out.println("Get");
//		if (Gdx.app.getType() == ApplicationType.Android)
//			return null;
		
		if (e.equals("empty"))// || Gdx.app.getType() == ApplicationType.Android)
			return new ParticleEffect();
		else
		{
			if (particleEffects.containsKey(e))
				return new ParticleEffect(particleEffects.get(e));
		}
		System.out.println("Missing Effect: " + e);
		return new ParticleEffect();
	}
	
	public int team()
	{
		return team;
	}
	
	public int winCondition()
	{
		if (!playerBases[0].isAlive())
			return 2;
		if (!playerBases[1].isAlive())
			return 1;
		return 0;
	}
	
	public void initializeHeroes()
	{
		playerHeroes = new Hero[2];
		
		heroUnits = new HeroStructure[]{heroStats.get(heroNames[0]), heroStats.get(heroNames[1])};
		playerHeroes[0] = new Hero(map.start1().x(), map.start1().y(), 1, map().getPath(1).listIterator(), heroUnits[0]);
		playerHeroes[1] = new Hero(map.start2().x(), map.start2().y(), 2, map().getReversePath(1).listIterator(), heroUnits[1]);
		add(playerHeroes[0]);
		add(playerHeroes[1]);
		playerUnits = new MinionStructure[][]{
				{minionStats.get("swordsman"), minionStats.get("archer"), minionStats.get("ninja"), minionStats.get("mage"), minionStats.get("monk"), minionStats.get(playerHeroes[0].pet())}
			   ,{minionStats.get("swordsman"), minionStats.get("archer"), minionStats.get("ninja"), minionStats.get("mage"), minionStats.get("monk"), minionStats.get(playerHeroes[1].pet())}};	
	}
	
	public void setHeroStance(int team, int s)
	{
		playerHeroes[team-1].stance(s);
	}
	
	public void activeHeroSkill(int team)
	{
		if (playerHeroes[team-1].activeSkill())
			if (team == this.team)
				Gdx.input.vibrate(50);
	}
	
	public void clearSend(int team)
	{
		pools[team - 1] = new LinkedList<Integer>();
		
		if (team == this.team)
			sentUnits = new int[6];
	}
	
	public int turn()
	{
		return turn;
	}
	
	public void setTeam(byte team)
	{
		this.team = team;
	}
	
	public void setRunning(boolean run)
	{
		running = run;
		waveTimer = 0;
//		previousTime = 0;
	}
	
	public void upgradeTower(int tower, int team)
	{
//		for (Actor b : teams[team - 1])
//		{
//			if (b instanceof Building)
//			{
//				Building buil = (Building)b;
//				System.out.println("Building: " + tower + " Team: " + team);
//			}
//		}
		//((Building)teams[team].get(tower)).upgrade();
	}
	
	public void end()
	{
//		music.stop();
	}
	
	public void musicPlay()
	{
//		music.play();
	}
	
	public Hero getHero()
	{
		return playerHeroes[team - 1];
	}
	
	public Actor atPoint(float x, float y)
	{
		for (Entity a : entities)
		{
			if (a instanceof Actor && a.getDistanceSquared(x, y) < ((Actor)a).getRadius() * ((Actor)a).getRadius())
				return (Actor)a;
		}
		return null;
	}
	
	public int timeLeft()
	{
		if (!running)
			return 0;
		int left = (int) -((turn - waveTimer - waveInterval) * stepTime) + 1;
		return (left > 15 ? 15 : left);
	}
	
	public String totalTime()
	{
		if (!running)
			return "00";
		
		int time = (int) (turn * stepTime);
		int min = (int)(time / 60);
		int sec = time % 60;
		return (min == 0 ? "00" : (min < 10 ? 0 + min : min)) + ":" + (sec == 0 ? "00" : (sec < 10 ? "0" + sec : sec));
	}
	
	public SkillContainerStructure getSkillStructure(ActorStructure a, int skill, int level)
	{
		if (skill >= 0 && skill < 3)
			return getSkillContainer(a.getSkill(skill, level));
//		if (skill == 0)
//			return getSkillContainer(a.firstSkill(level));
//		else if (skill == 1)
//			return getSkillContainer(a.secondSkill(level));
//		else if (skill == 2)
//			return getSkillContainer(a.thirdSkill(level));
		else if (skill == 3)
			return getSkillContainer(((HeroStructure)a).activeSkill(level));
		return null;
	}
	
	public void buyUpgrade(int command, int team)
	{
//		System.out.println("Upgrade Command: " + command);
		command -= 20;
		int level = command % 3;
		int skill = ((command - level) / 3) % 4;
		int unit = (command - level - skill * 3) / 12;
//		int team = command % 2;
//		int level = (command - team) % 3;
//		int skill = (command - team - level) % 3;
//		int unit = (command - team - level - skill);
		
		buyUpgrade(unit, skill, level, team);
	}
	
	public void buyUpgrade(int unit, int skill, int level, int team)
	{
		if (players[team - 1].upgrades[unit][skill] != -1)
		{
//			System.out.println("Already bought it!");
			return;
		}
		int cost;
		if (unit < 6)
			cost = getSkillStructure(getActorStructure(unit, team), skill, level).cost;
		else
			cost = getSkillStructure(getHeroStructure(team), skill, level).cost;
		if (cost == -1)
			return;
//		int cost = playerUnits[team - 1][unit].getSkill(skill, level);
		if ((team == 1 ? funds1 : funds2) < cost)
			return;
		if (team == 1)
			funds1 -= cost;
		else
			funds2 -= cost;
		
		players[team - 1].upgrades[unit][skill] = level;
		
		if (team == this.team)
		{
//			System.out.println("Upgrade!");
			Gdx.input.vibrate(50);
		}
		gameScreen.refreshButtons();
//		System.out.println("Upgrade!?");
	}
	
	public void add(Entity a)
	{
//		System.out.println("Starting to add");
		if (a instanceof Building)
		{
			if (playerBases[((Building)a).team() - 1] == null)
				playerBases[((Building)a).team() - 1] = (Building)a;
		}

		int loc = -1;
		for (int i = 0; i < entities.size(); i++)
		{
			if (entities.get(i) == null)
			{
				loc = i;
				break;
			}
		}
		if (loc > 100)
		{
//			System.out.println("So many!");
			entities.set(loc, a);
		}
		else if (loc >= 0)
			entities.set(loc, a);
		else
			entities.add(a);
//		System.out.println("Added at " + loc);
//		if (loc != -1)
//			System.out.println("It's team " + entities.get(loc).team());
//		if (loc == 12)
//			System.out.println("Should work");
	}
	
	public void add(int unit, int team)
	{
		int cost = playerUnits[team - 1][unit].cost(0);
		
		if ((team == 1 ? funds1 : funds2) < cost)
			return;
		
		if (unit != 5 && pools[team - 1].size() >= 5)
			return;
		
		if (team == 1)
			funds1 -= cost;
		else
			funds2 -= cost;
		
		if (unit == 5)
		{
			pools[team + 1].add(5);
			return;
		}
			      
		stats.minionSent[team - 1]++;
		
		if (team == this.team)
		{
			Gdx.input.vibrate(50);
			++sentUnits[unit];
		}
		
		pools[team - 1].add(unit);
	}
	
	/*public void spawnPools()
	{
		for (int m : pools[0])
			spawnMob(m, 1);
		for (int m : pools[1])
			spawnMob(m, 2);
		
		pools[0] = new LinkedList<Integer>();
		pools[1] = new LinkedList<Integer>();
		pools[2] = new LinkedList<Integer>();
		pools[3] = new LinkedList<Integer>();
	}*/
	
	private void spawnPool(int team)
	{
		if (team == 1)
			spawnMob(pools[2].pop(), 1);
		else
			spawnMob(pools[3].pop(), 2);
	}
	
	private void spawnMob(int m, int team)
	{
		Coordinate start = (team == 1 ? map.start1() : map.start2());
		ListIterator<Coordinate> iter = (team == 1 ? map.getPath(((top = !top) == false ? 1 : 2)).listIterator() : 
													 map.getReversePath(((bot = !bot) == false ? 1 : 2)).listIterator());
		//add(new Minion(start.x(), start.y(), team, iter, playerUnits[team - 1][m], 0));
		add(new Minion(start.x(), start.y(), team, iter, playerUnits[team - 1][m], players[team - 1].upgrades[m]));
	}
	
	public ArrayList<Actor> team(int t)
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
		for (Entity a : entities)
			if (a instanceof Actor && a.isAlive() && (t == 0 || ((Actor)a).team() == t))
					temp.add((Actor)a);
		return temp;
	}
	
	public ArrayList<Actor> team(int t, int targeting)
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
		int target = 0;
		
		if (targeting > 0)
			target = t;
		else if (targeting < 0)
			target = (t == 1 ? 2 : 1);
			
		for (Entity a : entities)
			if (a instanceof Actor && a.isAlive() && (target == 0 || ((Actor)a).team() == target))
					temp.add((Actor)a);
		return temp;
	}
	
	public ArrayList<Actor> actorsInRange(Actor center, int radius)
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
			
		for (Entity a : entities)
			if (a instanceof Actor && a.isAlive() && center.getDistanceSquared(a) < radius * radius)
					temp.add((Actor)a);
		return temp;
	}
	
	public ArrayList<Actor> actorsInRange(Actor center, int radius, int targeting)
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
		
		int target = 0;
		
		if (targeting > 0)
			target = center.team();
		else if (targeting < 0)
			target = (center.team() == 1 ? 2 : 1);
		
		for (Entity a : entities)
			if (a instanceof Actor && a.isAlive() && 
					(target == 0 || ((Actor)a).team() == target) &&
					center.getDistanceSquared(a) < (radius + center.getRadius() + ((Actor)a).getRadius()) * (radius + center.getRadius() + ((Actor)a).getRadius()))
				temp.add((Actor)a);
		
		if (temp.size() > 1)
		{
			switch(Math.abs(targeting))
			{
			case -1:
				Actor.setCenterActor(center);
				Collections.sort(temp, Actor.TargetedComparator);
				break;
			case 4:
			case 5:
				Collections.sort(temp, Actor.HealthComparator);
				break;
			}
		}
		
		return temp;
	}
	
	public ArrayList<Actor> actorsInRange(Skill s, int radius, int targeting)
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
		
		int target = 0;
		
		if (targeting > 0)
			target = s.team();
		else if (targeting < 0)
			target = (s.team() == 1 ? 2 : 1);
		
		for (Entity a : entities)
			if (a instanceof Actor && a.isAlive() && 
					(target == 0 || a.team() == target) && 
					s.getDistanceSquared(a) < (radius + ((Actor)a).getRadius()) * (radius + ((Actor)a).getRadius()))
				temp.add((Actor)a);
		
		if (temp.size() > 1)
		{
			switch(Math.abs(targeting))
			{
			case -1:
				Actor.setCenterActor(s);
				Collections.sort(temp, Actor.TargetedComparator);
				break;
			case 4:
			case 5:
				Collections.sort(temp, Actor.HealthComparator);
				break;
			}
		}
		
		return temp;
	}
	
	public Actor actorInRange(Actor center, float range, int targeting)
	{
		for (Actor a : actorsInRange(center, (int)range, targeting))
			return a;
		return null;
	}
	
	public int teamSize()
	{
		int temp = 0;
		for (Entity a : entities)
			if (a instanceof Actor && a.isAlive() && !(a instanceof Building) && a.team() == team)
				temp++;
		return temp;
	}
	
	public void sortEntities()
	{
		Collections.sort(entities, eCompare);
	}
	
	public void render(float delta)
	{
		for (int i = 0; i < entities.size(); i++)
		{
			tempEnt = entities.get(i);
			if (tempEnt instanceof Building || (tempEnt instanceof Actor && ((Entity)tempEnt).isAlive()) || (tempEnt instanceof TravelingSkillContainer && ((Entity)tempEnt).isAlive()) ||
				(tempEnt instanceof Unit && ((Unit)tempEnt).deathCountdown() > 0))
				tempEnt.draw(batch, delta);
			else if (!(tempEnt instanceof Hero))
			{				
				entities.set(i, null);
			}
		}

		for (int i = 0; i < effects.size(); i++)
		{
			if (effects.get(i) != null)
			{
				if (effects.get(i).isComplete())
					effects.set(i, null);
				else
					effects.get(i).draw(batch, delta * 0.5f);
			}
		}

//		if (false)//showRange)
//		{
//			for (Entity en : entities)
//			{
//				if (en instanceof Actor)
//				{
//					if (((Actor)en).isAlive())
//						((Actor)en).rangeIndicator(batch);
//				}
//			}
//		}
		
		if (settings.showTextEffect())
		{
			textEffectItr = textEffects.iterator();
			while (textEffectItr.hasNext())
			{
				textEffectItr.next().draw(batch, delta);
			}
		}
	}
	
	public Settings getSettings()
	{
		return settings;
	}
	
	public void heroDeath(int team)
	{
		stats.heroDeaths[team - 1]++;
		stats.heroKills[(team == 1 ? 1 : 0)]++;
	}
	
	public void minionDeath(int team)
	{
		stats.minionDeaths[team - 1]++;
		stats.minionKills[(team == 1 ? 1 : 0)]++;
	}
	
	public int unitCost(int unit, int team)
	{
		return playerUnits[team - 1][unit].cost(0);
	}
	
	public void update()
	{
		for (Entity e : entities)
		{
			if (e instanceof Actor)
				((Actor)e).checkAlive();
		}
		
		for (int i = 0; i < entities.size(); i++)
		{
			if (entities.get(i) != null && ((entities.get(i).isAlive() || entities.get(i) instanceof Hero || entities.get(i) instanceof Building) || (entities.get(i) instanceof Unit && ((Unit)entities.get(i)).deathCountdown() > 0)))
				entities.get(i).update();
		}
//		if (entities.size() >= 13 && entities.get(12) != null)
//			System.out.println("Should have updated: " + (entities.get(12).isAlive() ? "True" : "False"));
		
		++turn;
		spawnTimers();
		if (!playerHeroes[0].isAlive() && playerHeroes[0].canRespawn())
			playerHeroes[0].respawn(map.start1().x(), map.start1().y(), map.getPath(1).listIterator());
		if (!playerHeroes[1].isAlive() && playerHeroes[1].canRespawn())
			playerHeroes[1].respawn(map.start2().x(), map.start2().y(), map.getReversePath(1).listIterator());
		
		textEffectItr = textEffects.iterator();
		TextEffect tEffect;
		while (textEffectItr.hasNext())
		{
			tEffect = textEffectItr.next();
			tEffect.update();
			if (!tEffect.isAlive())
				textEffectItr.remove();
		}
	}
	
	private void spawnTimers()
	{
		if (!running)
			return;
//		previousTime = turn;
		int timeDiff = turn - waveTimer;
		if (timeDiff > waveInterval)
		{
			spawning = true;
			waveTimer = turn;
			spawnTimer1 = waveTimer;
			
			spawnInterval1 = 20;	
			spawnTimer2 = waveTimer;
			spawnInterval2 = 20;
			pools[2].addAll(pools[0]);
			pools[3].addAll(pools[1]);
//			pools[0] = new LinkedList<Integer>();
//			pools[1] = new LinkedList<Integer>();
			if (pools[2] == null)
				pools[2] = new LinkedList<Integer>();
			if (pools[3] == null)
				pools[3] = new LinkedList<Integer>();			
			funds1 += income1 + team1Towers * towerIncome;
			funds2 += income2 + team2Towers * towerIncome;
			
			for (Entity e : entities)
				if (e != null && e instanceof Building && e.team() == team)
					((Building)e).generateIncome();
			
//			sentUnits = new int[6];
		}
		
		if (turn - spawnTimer1 > spawnInterval1)
		{
			if (!pools[2].isEmpty())
				spawnPool(1);
			if (!pools[3].isEmpty())
				spawnPool(2);
			spawnTimer1 = turn;
		}

//		if (!pools[2].isEmpty() && turn - spawnTimer1 > spawnInterval1)
//		{
//			spawnPool(1);
//			spawnTimer1 = turn;
//		}
//		
//		if (!pools[3].isEmpty() && turn - spawnTimer2 > spawnInterval2)
//		{
//			spawnPool(2);
//			spawnTimer2 = turn;
//		}
	}
	
	public Map map()
	{
		return map;
	}
	
	public int funds()
	{
		return team == 1 ? funds1 : funds2;
	}
	
	static public void toggleShowRange()
	{
		showRange = (showRange ? false : true);
	}
	
	public void load(SpriteBatch b)
	{
		batch = b;
	}
	
	public void addEffect(ParticleEffect e)
	{
		int i = effects.indexOf(null);
		if (i >= 0)
			effects.set(i, e);
		else
			effects.add(e);
	}
	
	public void buildMap()
	{
		Building tower = new Building(map.start1().x(), map.start1().y() - 2, 1, 
				buildingStats.get("stronghold"));
		tower.upgrade();
		add(tower);
		tower = new Building(map.start2().x(), map.start2().y() - 2, 2, 
		buildingStats.get("stronghold"));
		tower.upgrade();
		add(tower);
		
//		int towerNumber = 1;
		for (Coordinate c : map.buildSites(1))
		{
			tower = new Building(c.x(), c.y(), 1, buildingStats.get("arrowtower"));
			tower.upgrade();
			add(tower);
		}
		
//		towerNumber = 1;
		for (Coordinate c : map.buildSites(2))
		{
			tower = new Building(c.x(), c.y(), 2, buildingStats.get("arrowtower"));
			tower.upgrade();
			add(tower);
		}
	}
	
//	@SuppressWarnings("unchecked")
	public void reset()
	{
		pools = new LinkedList[4];
		pools[0] = new LinkedList<Integer>();
		pools[1] = new LinkedList<Integer>();
		pools[2] = new LinkedList<Integer>();
		pools[3] = new LinkedList<Integer>();
		
		playerBases = new Building[2];
		
		spawning = false;
		running = false;
		
		entities = new ArrayList<Entity>();
		effects = new ArrayList<ParticleEffect>();
		
		funds1 = 100;
		funds2 = 100;
		turn = 0;
		highestTurn = 10;
		
		stats = new Stats();
		setRandom(100);
		
		players[0] = new Player("Player1");
		players[1] = new Player("Computer");
		
		waveTimer = 0;
		spawnTimer1 = 0;
		spawnTimer2 = 0;
		
		top = false;
		bot = false;
		
		textEffects = new LinkedList<TextEffect>(); 
		sentUnits = new int[6];
	}
}
