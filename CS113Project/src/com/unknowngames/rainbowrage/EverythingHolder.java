package com.unknowngames.rainbowrage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Random;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
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
import com.badlogic.gdx.math.Vector3;
import com.unknowngames.rainbowrage.entity.*;
import com.unknowngames.rainbowrage.map.*;
import com.unknowngames.rainbowrage.parser.*;
import com.unknowngames.rainbowrage.skill.Skill;
import com.unknowngames.rainbowrage.skill.TravelingSkillContainer;

public class EverythingHolder 
{
	@SuppressWarnings("unchecked")
	String xmlVersion = "", gameVersion = "0.09_03_13";
	
	EntityComparator eCompare = new EntityComparator();
	static private SpriteBatch batch;
	static Map map;
	
	int waveInterval, waveTime, waveTimer, 
		previousTime, 
		spawnTimer1, spawnTimer2, 
		spawnInterval1, spawnInterval2;
	
	int nano = 1000000000;
	int income1 = 100, income2 = 100;
	int funds1, funds2;
	int turn, highestTurn = 10;
	int petType = 0;
	
	float stepTime = 0.02f;
	byte team = 1;

	static float musicVolume = 1f;
	
	static boolean showRange;
	boolean spawning;
	boolean running;
	int team1Towers = 0, team2Towers = 0, towerIncome = 0;
	
	//	You can call: mrwizard, swordface, or arroweyes
	String[] heroNames = {"mrwizard", "swordface"};
	
	Hero playerHeroes[];
	Building playerBases[];
	
	LinkedList<Integer>[] pools;			// Pools for spawning
	ArrayList<Entity> entities;		// Holds all units, towers, projectiles, and spells
//	static ArrayList<Projectile> projectiles;
	private ArrayList<ParticleEffect> effects;
	

	MinionStructure[][] playerUnits;// = new String[2][6]; 
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
	
	public BitmapFont[] font = new BitmapFont[4]; //, font2;
	private Texture teamTextures[] = new Texture[2];
	
	public String[] color = {"blue", "red"};
	
	boolean finished = false;
	
	Stats stats;
	
	Settings settings = new Settings();
	
//	private Map[] maps = new Map[2];
	
	private Music mainMusic, gameMusic, endMusic;
	
	Entity tempEnt = null;
	
	int[] sentUnits = new int[6];
	
	boolean top = false, bot = false;
	
	float sizeRatio, minZoom = 1, xRatio, yRatio;
	int screenSizeX = 0, screenSizeY = 0;
	
	List<TextEffect> textEffects = new LinkedList<TextEffect>(); 
	Iterator<TextEffect> textEffectItr;
	
	Random rand;
	
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
				
		spawning = false;
//		previousTime = System.nanoTime() / 1000000;
//		Actor.loadProjectiles(projectiles);
//		Entity.linkHolder(this);
		BaseClass.load(this);
		
		// Load all assets
		loadEffects();
		loadSounds();
		loadTextures();
		finished = true;
		
//		Entity.loadStatics(effects);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Kingthings Exeter.ttf"));
		font[0] = generator.generateFont(18);
		font[1] = generator.generateFont(32);
		font[2] = generator.generateFont(45);
		font[2].setColor(1, 1, 1, 1);
		font[3] = generator.generateFont(45);
//		font[3].setColor(0, 0, 0, 1);
		
		loadMusic();
		screenSizeX = Gdx.graphics.getWidth();
		screenSizeY = Gdx.graphics.getHeight();
		xRatio = (float)screenSizeX / 800;
		yRatio = (float)screenSizeY / 480;
		sizeRatio = (float)Gdx.graphics.getWidth() / 800;
	}
	
	public void setRandom(int i)
	{
		 rand = new Random(i);
	}
	
	public int getRandom(int i)
	{
		return rand.nextInt(i);
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
	
//			map = new Map(new Coordinate(100, 1030), sprite, 1600, 1200, 100, 1030, 1300, 170);
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
//			texture = new Texture(Gdx.files.internal("images/map1.png"));
			texture = new Texture(Gdx.files.internal("images/map1.jpg"));
			region = new TextureRegion(texture, 0, 0, 1200, 960);
			sprite = new Sprite(region);
//			sprite.scale(2);
			
//			map = new Map(new Coordinate(320, 870), sprite, 1200, 960, 310, 870, 1210, 480);
			map = new Map(sprite, 1200, 960, 310, 870, 1210, 480);
			map.add(new Coordinate(330, 870), 1);
			map.add(new Coordinate(1190, 870), 1);
			map.add(new Coordinate(1190, 680), 1);
			map.add(new Coordinate(365, 680), 1);
			map.add(new Coordinate(365, 480), 1);
			map.add(new Coordinate(1200, 480), 1);
			
//			map.add(new Coordinate(320, 885), 2);
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
			map.add(new Coordinate(1470, 1231), 2); // Tower 3 (1410, 1231)
			map.add(new Coordinate(1455, 1135), 2);
			map.add(new Coordinate(1401, 1091), 2); // Turn 2
			map.add(new Coordinate(1249, 1091), 2);
			map.add(new Coordinate(1192, 1066), 2); // Tower 4 (1192, 1104)
			map.add(new Coordinate(1135, 1091), 2);
			map.add(new Coordinate(848, 1091), 2);
			map.add(new Coordinate(791, 1066), 2);  // Tower 5 (791, 1104)
			map.add(new Coordinate(734, 1091), 2);
			map.add(new Coordinate(594, 1091), 2);
			map.add(new Coordinate(639, 978), 2);   // Tower 6 (571, 978)
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
		
		float xZoom = (float)map.width() / Gdx.graphics.getWidth();
		float yZoom = (float)map.height() / Gdx.graphics.getHeight();
		minZoom = (xZoom > yZoom ? yZoom : xZoom);
		
//		sprite.setSize(1600, 1200);
		
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
//		initializeHeroes()
		
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
	
//	public Animation getUnitAnimation(String name, int direction)
//	{
//		System.out.println("Name " + name);
//		return unitAnimations.get(name).getAnimation(direction);
//	}
	
	public Vector2 getUnitFeet(String name, int direction)
	{
		return unitAnimations.get(name).getFeet(direction);
	}
	
	public void loadUnitAnimations()
	{
//		System.out.println("Loading unit animations");
//		String[] color = {"blue", "red"};
//		ArrayList<Animation> unitAnimation;
		Animation[] animation = new Animation[8];
//		UnitAnimation unitAnimation = new UnitAnimation();
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
//			System.out.println("Puting: swordsman" + i + 1);
//			animationsR.add(unitAnimation);
			
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
//		System.out.println("Loaded unit animations");
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
	
	public BitmapFont getFont(int f)
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
	
//	public static void playUnitSounds(String name, int type)
//	{
//		unitSounds.get(name)
//	}
	
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
//		System.out.println("Looking for: " + name);
		return objectTextures.get(name);
	}
	
	public void loadTextures()
	{
//		objectTextures.put("cannonball", new TextureRegion(new Texture(Gdx.files.internal("images/cannonprojectile.png")), 0, 0, 16, 16));
//		objectTextures.put("arrow", new TextureRegion(new Texture(Gdx.files.internal("images/arrowprojectile.png")), 16, 0, 16, 16));
		Texture proj = new Texture(Gdx.files.internal("images/projectile_sheet.png"));
		proj.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		objectTextures.put("arrow", new TextureRegion(proj, 0, 0, 16, 16));
		objectTextures.put("arrowblue", new TextureRegion(proj, 0, 16, 16, 16));
		objectTextures.put("arrowred", new TextureRegion(proj, 16, 16, 16, 16));
		objectTextures.put("arrowgreen", new TextureRegion(proj, 0, 16, 16, 16));
		objectTextures.put("arroworange", new TextureRegion(proj, 16, 16, 16, 16));
		objectTextures.put("arrowpurple", new TextureRegion(proj, 0, 16, 16, 16));
		objectTextures.put("arrowyellow", new TextureRegion(proj, 16, 16, 16, 16));
		objectTextures.put("cannonball", new TextureRegion(proj, 16, 0, 16, 16));
		
		objectTextures.put("fireballred", new TextureRegion(proj, 0, 32, 16, 16));
		objectTextures.put("fireattackred", new TextureRegion(proj, 0, 48, 16, 16));
		objectTextures.put("fireballblue", new TextureRegion(proj, 16, 32, 16, 16));
		objectTextures.put("fireattackblue", new TextureRegion(proj, 16, 48, 16, 16));
		objectTextures.put("fireballgreen", new TextureRegion(proj, 0, 32, 16, 16));
		objectTextures.put("fireattackgreen", new TextureRegion(proj, 0, 48, 16, 16));
		objectTextures.put("fireballorange", new TextureRegion(proj, 16, 32, 16, 16));
		objectTextures.put("fireattackorange", new TextureRegion(proj, 16, 48, 16, 16));
		objectTextures.put("fireballpurple", new TextureRegion(proj, 0, 32, 16, 16));
		objectTextures.put("fireattackpurple", new TextureRegion(proj, 0, 48, 16, 16));
		objectTextures.put("fireballyellow", new TextureRegion(proj, 16, 32, 16, 16));
		objectTextures.put("fireattackyellow", new TextureRegion(proj, 16, 48, 16, 16));
		
		Texture icons = new Texture(Gdx.files.internal("images/buttons_sheet.png"));
		icons.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		objectTextures.put("fireball", new TextureRegion(icons, 1814, 0, 90, 90));
		objectTextures.put("fireattack", new TextureRegion(icons, 1814, 90, 73, 73));
		
		// New style hero icon
		objectTextures.put("swordfacebutton", new TextureRegion(icons, 1890, 787, 158, 155));
		objectTextures.put("mrwizardbutton", new TextureRegion(icons, 1890, 942, 158, 155));
		objectTextures.put("arroweyesbutton", new TextureRegion(icons, 1890, 1097, 158, 155));
		
		// Old style hero icon
//		objectTextures.put("swordfacebutton", new TextureRegion(icons, 0, 1377, 192, 137));
//		objectTextures.put("arroweyesbutton", new TextureRegion(icons, 192, 1377, 192, 137));
//		objectTextures.put("mrwizardbutton", new TextureRegion(icons, 384, 1377, 192, 137));
		
		objectTextures.put("swordbutton", new TextureRegion(icons, 0, 1071, 152, 153));
		objectTextures.put("archerbutton", new TextureRegion(icons, 0, 918, 152, 153));
		objectTextures.put("ninjabutton", new TextureRegion(icons, 0, 765, 152, 153));
		objectTextures.put("magebutton", new TextureRegion(icons, 0, 612, 152, 153));
		objectTextures.put("monkbutton", new TextureRegion(icons, 0, 459, 152, 153));
		objectTextures.put("petbutton", new TextureRegion(icons, 0, 1224, 152, 153));
		
		objectTextures.put("attackbutton", new TextureRegion(icons, 0, 0, 152, 153));
		objectTextures.put("defendbutton", new TextureRegion(icons, 0, 153, 152, 153));
		objectTextures.put("retreatbutton", new TextureRegion(icons, 0, 306, 152, 153));
		objectTextures.put("skillbutton", new TextureRegion(icons, 152, 0, 215, 209));
		
		// New new style buttons
		objectTextures.put("upgradebutton", new TextureRegion(icons, 1644, 787, 150, 149));
		objectTextures.put("storebutton", new TextureRegion(icons, 1644, 936, 150, 149));
		objectTextures.put("chatbutton", new TextureRegion(icons, 1644, 1085, 150, 149));
		
		// New style buttons
//		objectTextures.put("upgradebutton", new TextureRegion(icons, 1794, 787, 96, 95));
//		objectTextures.put("storebutton", new TextureRegion(icons, 1794, 882, 96, 95));
//		objectTextures.put("chatbutton", new TextureRegion(icons, 1794, 882, 96, 95));
		
		// Old style buttons
//		objectTextures.put("upgradebutton", new TextureRegion(icons, 1905, 109, 143, 109));
//		objectTextures.put("storebutton", new TextureRegion(icons, 1905, 0, 143, 109));
		
		objectTextures.put("confirmbutton", new TextureRegion(icons, 367, 0, 152, 153));
		objectTextures.put("cancelbutton", new TextureRegion(icons, 367, 153, 152, 153));
		objectTextures.put("backbutton", new TextureRegion(icons, 367, 306, 152, 153));
		objectTextures.put("forwardbutton", new TextureRegion(icons, 367, 459, 152, 153));
		objectTextures.put("singlebutton", new TextureRegion(icons, 367, 612, 152, 153));
		objectTextures.put("multibutton", new TextureRegion(icons, 367, 765, 152, 153));
		objectTextures.put("settingsbutton", new TextureRegion(icons, 367, 918, 152, 153));
		objectTextures.put("quitbutton", new TextureRegion(icons, 367, 1071, 152, 153));
		
		objectTextures.put("redbutton", new TextureRegion(icons, 152, 240, 127, 127));
		objectTextures.put("bluebutton", new TextureRegion(icons, 152, 367, 127, 127));
		objectTextures.put("greenbutton", new TextureRegion(icons, 152, 494, 127, 127));
		objectTextures.put("orangebutton", new TextureRegion(icons, 152, 621, 127, 127));
		objectTextures.put("purplebutton", new TextureRegion(icons, 152, 748, 127, 127));
		objectTextures.put("yellowbutton", new TextureRegion(icons, 152, 875, 127, 127));
		
		objectTextures.put("gamelogo", new TextureRegion(icons, 0, 1514, 842, 467));
		objectTextures.put("mainbuttonframe", new TextureRegion(icons, 880, 422, 361, 572));
		objectTextures.put("gamebuttonframe", new TextureRegion(icons, 519, 422, 361, 792));
		
		objectTextures.put("cashicon", new TextureRegion(icons, 152, 209, 33, 31));
		objectTextures.put("timeicon", new TextureRegion(icons, 185, 209, 33, 31));
		objectTextures.put("uniticon", new TextureRegion(icons, 218, 209, 33, 31));
		
		objectTextures.put("heroselectsword", new TextureRegion(icons, 1896, 328, 152, 153));
		objectTextures.put("heroselectwizard", new TextureRegion(icons, 1896, 481, 152, 153));
		objectTextures.put("heroselectarrow", new TextureRegion(icons, 1896, 634, 152, 153));
		objectTextures.put("heroselection", new TextureRegion(icons, 1722, 327, 174, 187));
		
		objectTextures.put("heronamesword", new TextureRegion(icons, 1333, 1939, 302, 109));
		objectTextures.put("heronamewizard", new TextureRegion(icons, 1333, 1830, 302, 109));
		objectTextures.put("heronamearrow", new TextureRegion(icons, 1333, 1721, 302, 109));
		
		objectTextures.put("endstatsbox", new TextureRegion(icons, 1635, 1824, 413, 224));
		objectTextures.put("endvictory", new TextureRegion(icons, 1687, 1711, 361, 62));
		objectTextures.put("enddefeat", new TextureRegion(icons, 1687, 1773, 360, 51));
		
		objectTextures.put("redrange", new TextureRegion(icons, 1604, 787, 40, 40));
		objectTextures.put("bluerange", new TextureRegion(icons, 1604, 827, 40, 40));
	}
	
	public void loadSounds()
	{
		String[] soundNames = {
				"thwp"};
		
//		Audio tempAudio = Gdx.audio;
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
//				if (name.equals("spark"))
//					System.out.println(e);
				temp.load(Gdx.files.internal("data/" + name + ".p"), Gdx.files.internal("images"));
			}
			particleEffects.put(name, temp);
		}
		
//		ParticleEffect temp = new ParticleEffect();
//		temp.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
//		particleEffects.put("fire", temp);
//		
//		temp = new ParticleEffect();
//		temp.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/BloodEffectAndroid.p" : "data/BloodEffect.p")), Gdx.files.internal("images"));
//		particleEffects.put("blood", temp);
//		
//		temp = new ParticleEffect();
//		temp.load(Gdx.files.internal("data/fireballeffect.p"), Gdx.files.internal("images"));
//		particleEffects.put("fireball", temp);
//		
//		temp = new ParticleEffect();
//		temp.load(Gdx.files.internal("data/fireballexplosioneffect.p"), Gdx.files.internal("images"));
//		particleEffects.put("fireballexplosion", temp);
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
		add(playerHeroes[0]); //, 1);
		add(playerHeroes[1]); //, 2);
//		System.out.println("PET " + playerHeroes[0].pet());
//		System.out.println("PET " + playerHeroes[1].pet());
		playerUnits = new MinionStructure[][]{
				{minionStats.get("swordsman"), minionStats.get("archer"), minionStats.get("ninja"), minionStats.get("mage"), minionStats.get("monk"), minionStats.get(playerHeroes[0].pet())}
			   ,{minionStats.get("swordsman"), minionStats.get("archer"), minionStats.get("ninja"), minionStats.get("mage"), minionStats.get("monk"), minionStats.get(playerHeroes[1].pet())}};

//		playerHeroes[1] = new Hero(map.start2().x(), map.start2().y(), 2, map().getPath().listIterator(map().getPath().size() - 1), heroUnits[1]);
//		add(playerHeroes[0], true, 1);
//		add(playerHeroes[1], true, 2);		
	}
	
	public void setHeroStance(int team, int s)
	{
		playerHeroes[team-1].stance(s);
		
//		if (team == this.team)
//			Gdx.input.vibrate(50);
	}
	
	public void activeHeroSkill(int team)
	{
		if (playerHeroes[team-1].activeSkill())
			if (team == this.team)
				Gdx.input.vibrate(50);
	}
	
	public int turn()
	{
		return turn;
	}
	
	public void setTeam(byte team)
	{
//		System.out.println("Team: " + team);
		this.team = team;
	}
	
	public void setRunning(boolean run)
	{
		running = run;
//		waveTimer = System.nanoTime() / 1000000; // Timer to keep track of waves
//		previousTime = System.nanoTime() / 1000000;
		waveTimer = 0;
		previousTime = 0;
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
	
	static public void setMusicVolume(float v)
	{
		musicVolume = v;
	}
	
	public Hero getHero()
	{
		return playerHeroes[team - 1];
	}
	
	public Actor atPoint(float x, float y)
	{
//		if (team == 0)
//			return null;
//		for (Actor a : teams[team - 1])// - 1])
//		{
//			if (a.getDistanceSquared(x, y) <= 1000)
//				return a;
//		}
		return null;
	}
	
	public int timeLeft()
	{
		if (!running)
			return 0;
		int left = (int) -((turn - waveTimer - waveInterval) * stepTime) + 1;
		return (left > 15 ? 15 : left);
//		return (int) ((waveInterval - waveTimer) / stepTime);
//		return (int) -((System.nanoTime() / 1000000 - waveTimer - waveInterval)/ 1000);
	}
	
	public String totalTime()
	{
		if (!running)
			return "00";
//		int time = (int) (totalTime / 1000);
//		int min = (int)(time / 60);
//		int sec = time % 60;
		
		int time = (int) (turn * stepTime);
		int min = (int)(time / 60);
		int sec = time % 60;
		return (min == 0 ? "00" : (min < 10 ? 0 + min : min)) + ":" + (sec == 0 ? "00" : (sec < 10 ? "0" + sec : sec));
		//return (int) totalTime / 1000;
	}
	
//	public void add(Actor a, boolean front, int team)
//	{
//		if (front)
//			teams[team - 1].add(0, a);
//		else
//			teams[team - 1].add(a);
//		if (a instanceof Building)
//		{
//			Building temp = (Building)a;
//			if (playerBases[temp.team()] == null)
//				playerBases[temp.team()] = temp;
//		}
//		entities.add(a);
//	}
	
	public void add(Entity a) //, int team)
	{
//		if (front)
//			teams[team - 1].add(0, a);
//		else
//			teams[team - 1].add(a);
		if (a instanceof Building)
		{
			if (playerBases[((Building)a).team() - 1] == null)
				playerBases[((Building)a).team() - 1] = (Building)a;
		}
//		int loc = entities.indexOf(null);
//		if (entities.isEmpty())
//			loc = 0;
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
			System.out.println("So many!");
			entities.set(loc, a);
		}
		else if (loc >= 0)
			entities.set(loc, a);
		else
			entities.add(a);
	}
	
	public void add(int unit, int team)
	{
		int cost = playerUnits[team - 1][unit].cost(0);
		
		if ((team == 1 ? funds1 : funds2) < cost)
			return;
		if (team == 1)
			funds1 -= cost;
		else
			funds2 -= cost;
		
		stats.minionSent[team - 1]++;
		
		if (team == this.team)
		{
			Gdx.input.vibrate(50);
			++sentUnits[unit];
		}
		
		pools[team - 1].add(unit);
	}
	
	public void spawnPools()
	{
		for (int m : pools[0])
			spawnMob(m, 1);
		for (int m : pools[1])
			spawnMob(m, 2);
		
		pools[0] = new LinkedList<Integer>();
		pools[1] = new LinkedList<Integer>();
		pools[2] = new LinkedList<Integer>();
		pools[3] = new LinkedList<Integer>();
	}
	
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
		add(new Minion(start.x(), start.y(), team, iter, playerUnits[team - 1][m], 0)); //, team);
		
//		iter = (team == 1 ? map.getPath(2).listIterator() : map.getReversePath(2).listIterator());
//		add(new Minion(start.x(), start.y(), team, iter, playerUnits[team - 1][m], 0), team);
		
//		ListIterator<Coordinate> iter = (team == 1 ? map.getPath().listIterator() : map.getPath().listIterator(map.getPath().size() - 1));
//		int randX = 0; //(int)(Math.random() * 10);
//		int randY = 0; //(int)(Math.random() * 5);
		
//		add(new Minion(start.x(), start.y(), team, iter, playerUnits[team-1][m], 0), true, team);
//		if (m == 1)
//			add(new Swordsman(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 2)
//			add(new Archer(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 3)
//			add(new Monk(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 4)
//			add(new Mage(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 5)
//			add(new Ninja(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 6)
//		{
//			petType++;
//			if (petType > 2)
//				petType = 0;
//			if (petType == 0)
//				add(new Eagle(start.x() + randX, start.y() + randY, team, iter), true, team);
//			else if (petType == 1)
//				add(new Wolf(start.x() + randX, start.y() + randY, team, iter), true, team);
//			else
//				add(new Elemental(start.x() + randX, start.y() + randY, team, iter), true, team);
//		}
	}
	
	public ArrayList<Actor> team(int t)
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
		for (Entity a : entities)
			if (a instanceof Actor && a.isAlive() && (t == 0 || ((Actor)a).team() == t))
					temp.add((Actor)a);
		return temp;
//		if (t == 0)
//		if (t == 1)
//			return teams[0];
//		return teams[1];
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
//		if (t == 0)
//		if (t == 1)
//			return teams[0];
//		return teams[1];
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
//					center.getDistanceSquared(a) < radius * radius)
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
//			ArrayList<Actor> temp1 = new ArrayList<Actor>();
//			for (int i = 0; i < targetCount && i < temp.size(); i++)
//				temp1.add(temp.get(i));
//			ArrayList<Actor> temp2 = new ArrayList<Actor>(temp.subList(0, targetCount));
//			return temp1;
			//temp = (ArrayList<Actor>)temp.subList(0, (targetCount > temp.size() ? temp.size() - 1 : targetCount - 1));
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
//					s.getDistanceSquared(a) < radius * radius)
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
//		ArrayList<Actor> temp = new ArrayList<Actor>();
//		
//		int target = 0;
//		
//		if (targeting > 0)
//			target = center.team();
//		else if (targeting < 0)
//			target = (center.team() == 1 ? 2 : 1);
//		
////		System.out.println("Range: " + range);
//		
//		for (Entity a : entities)
//			if (a instanceof Actor && a.isAlive() && 
//					(target == 0 || ((Actor)a).team() == target) && 
//					center.getDistanceSquared(a) < (range + ((Actor)a).getRadius()) * (range + ((Actor)a).getRadius()))
////					center.getDistanceSquared(a) < range * range + (((Actor)a).getRadius() * ((Actor)a).getRadius()))
//				temp.add((Actor)a);
//		
//		if (temp.isEmpty())
//			return null;
//		else if (temp.size() > 1)
//		{
//			switch(Math.abs(targeting))
//			{
//			case 4:
//			case 5:
//				Collections.sort(temp, Actor.HealthComparator);
//				break;
//			}
//		}
//		System.out.println("Target Found");
//		return temp.get(0);
	}
	
	public int teamSize()
	{
		int temp = 0;
		for (Entity a : entities)
			if (a instanceof Actor && a.isAlive() && !(a instanceof Building) && a.team() == team)
//			if (a instanceof Actor && a.isAlive() && (t == 0 || ((Actor)a).team() == t))
				temp++;
		return temp;
	}
	
	public void sortEntities()
	{
		Collections.sort(entities, eCompare);
	}
	
	public void render(float delta)
	{
//		for (Actor a : teams[0])
//		{
//			if (a instanceof Stronghold)
//				a.draw(batch);
//		}
//		for (Actor a : teams[1])
//		{
//			if (a instanceof Stronghold)
//				a.draw(batch);
//		}
		
		
//		Collections.sort(entities, eCompare);
//		int g = 1;
//		System.out.println(g);
//		Entity e;
		for (int i = 0; i < entities.size(); i++)
		{
//			e = entities.get(i);
			tempEnt = entities.get(i);
//			if (e == null)
//			if (tempEnt == null)
//				break;
			if (tempEnt instanceof Building || (tempEnt instanceof Actor && ((Entity)tempEnt).isAlive()) || (tempEnt instanceof TravelingSkillContainer && ((Entity)tempEnt).isAlive()) ||
				(tempEnt instanceof Unit && ((Unit)tempEnt).deathCountdown() > 0))
				tempEnt.draw(batch, delta);
//			if (e instanceof Building || (e instanceof Actor && ((Entity)e).isAlive()) || (e instanceof Skill && ((Entity)e).isAlive()))
//				e.draw(batch, delta);
			else if (!(tempEnt instanceof Hero))
			{
//				System.out.println("DEAD");
//				if (e instanceof Minion)
//				{
//					System.out.println("Minion " + e.team());
//					stats.minionDeaths[e.team() - 1]++;
//					stats.minionKills[(e.team() == 1 ? 1 : 0)]++;
//				}
				
				entities.set(i, null);
//				entities.get(i).setRemove();
			}
//			else if (e instanceof Hero)
//			{
//				
//				System.out.println("Hero " + e.team());
//				stats.heroDeaths[e.team() - 1]++;
//				stats.heroKills[(e.team() == 1 ? 1 : 0)]++;
//			}
		}
		
//		for (Projectile p : projectiles)
//		{
//			try
//			{
//				p.draw(batch, delta);
//			}
//			catch (Exception ex)
//			{
//				ex.printStackTrace();
//			}
//		}
		
//		for (ParticleEffect pe : effects)
//		{
//			if (pe != null)
//				pe.draw(batch, delta * 0.5f);
//		}
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
//		Iterator<ParticleEffect> iter = effects.iterator();
//
//		while (iter.hasNext())
//		{
//			if ((iter.next()).isComplete())
//				iter.remove();
//		}
		if (false)//showRange)
		{
			for (Entity en : entities)
			{
				if (en instanceof Actor)
				{
					if (((Actor)en).isAlive())
						((Actor)en).rangeIndicator(batch);
				}
			}
		}
		
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
//		System.out.println("Hero " + team);
		stats.heroDeaths[team - 1]++;
		stats.heroKills[(team == 1 ? 1 : 0)]++;
	}
	
	public void minionDeath(int team)
	{
//		System.out.println("Minion " + team);
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
		
//		music.setVolume(musicVolume);
		++turn;
//		if (++turn % 10 == 0)
//			System.out.println("Turn: " + turn);
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
//		int currentTurn = turn++;
//		long currentTime = System.nanoTime() / 1000000;
		//totalTime += currentTime - previousTime;
		previousTime = turn;
		int timeDiff = turn - waveTimer;
		if (timeDiff > waveInterval)
//		if (currentTurn % waveInterval == 0)
		{
			spawning = true;
			waveTimer = turn;
			spawnTimer1 = waveTimer;
			
//			spawnInterval1 = (!pools[0].isEmpty() ? waveTime / pools[0].size() : waveInterval);
			spawnInterval1 = 20;	
			spawnTimer2 = waveTimer;
//			spawnInterval2 = (!pools[1].isEmpty() ? waveTime / pools[1].size() : waveInterval);
			spawnInterval2 = 20;
			pools[2].addAll(pools[0]);
			pools[3].addAll(pools[1]);
			pools[0] = new LinkedList<Integer>();
			pools[1] = new LinkedList<Integer>();
			if (pools[2] == null)
				pools[2] = new LinkedList<Integer>();
			if (pools[3] == null)
				pools[3] = new LinkedList<Integer>();			
			funds1 += income1 + team1Towers * towerIncome;
			funds2 += income2 + team2Towers * towerIncome;
			
			sentUnits = new int[6];
		}

		if (!pools[2].isEmpty() && turn - spawnTimer1 > spawnInterval1)
		{
			spawnPool(1);
			spawnTimer1 = turn;
		}
		
		if (!pools[3].isEmpty() && turn - spawnTimer2 > spawnInterval2)
		{
			spawnPool(2);
			spawnTimer2 = turn;
		}
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
	
	public void load(SpriteBatch b) //, Map m)
	{
		batch = b;
		
//		map = m;
//		initializeHeroes();
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
				buildingStats.get("stronghold"));//new Stronghold(maps[level].start1().x() + 20, maps[level].start1().y(), 1);
		tower.upgrade();
		add(tower); //, 1);
		//everything.add(tower, true, 1);
		//tower = new Stronghold(maps[level].start2().x() - 20, maps[level].start2().y(), 2);
		tower = new Building(map.start2().x(), map.start2().y() - 2, 2, 
		buildingStats.get("stronghold"));
		tower.upgrade();
		add(tower); //, 2);
		
		int towerNumber = 1;
		for (Coordinate c : map.buildSites(1))
		{
			tower = new Building(c.x(), c.y(), 1, buildingStats.get("arrowtower"));
//			tower = new ArrowTower(c.x(), c.y(), 1, towerNumber++);
			tower.upgrade();
			add(tower); //, 1);
//			everything.add(tower, true, 1);
		}
		
		towerNumber = 1;
		for (Coordinate c : map.buildSites(2))
		{
			tower = new Building(c.x(), c.y(), 2, buildingStats.get("arrowtower"));
//			tower = new ArrowTower(c.x(), c.y(), 2, towerNumber++);
			tower.upgrade();
			add(tower); //, 2);
//			everything.add(tower, true, 2);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void reset()
	{
		pools = new LinkedList[4];
		pools[0] = new LinkedList<Integer>();
		pools[1] = new LinkedList<Integer>();
		pools[2] = new LinkedList<Integer>();
		pools[3] = new LinkedList<Integer>();
		
//		playerHeroes = new Hero[2];
		playerBases = new Building[2];
		
		spawning = false;
		running = false;
		
//		entities = new ArrayList<Entity>(50);
		entities = new ArrayList<Entity>();
//		projectiles = new ArrayList<Projectile>();
		effects = new ArrayList<ParticleEffect>();
//		Actor.loadProjectiles(projectiles);
		
		funds1 = 100;
		funds2 = 100;
		turn = 0;
		highestTurn = 10;
		
//		Entity.loadStatics(effects);
		
//		font[0] = new BitmapFont();
//		font[1] = new BitmapFont();
//		font[1].scale(2);
		
		
//		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BaroqueScript.ttf"));
//		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Kingthings Exeter.ttf"));
////		font[2] = generator.generateFont(36);
//		font[0] = generator.generateFont(18);
//		font[1] = generator.generateFont(32);
//		font[2] = generator.generateFont(45);
//		font[2].setColor(1, 1, 1, 1);
		
//		font[3] = generator.generateFont(36);
		
		stats = new Stats();
		setRandom(100);
//		font[2] = 
	}
}
