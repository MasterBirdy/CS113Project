package com.unknowngames.rainbowrage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.unknowngames.rainbowrage.entity.*;
import com.unknowngames.rainbowrage.map.*;
import com.unknowngames.rainbowrage.parser.*;
import com.unknowngames.rainbowrage.skill.Skill;

public class EverythingHolder 
{
	@SuppressWarnings("unchecked")
	EntityComparator eCompare = new EntityComparator();
	static private SpriteBatch batch;
	static Map map;
	
	int waveInterval, waveTime, waveTimer, 
		previousTime, 
		spawnTimer1, spawnTimer2, 
		spawnInterval1, spawnInterval2;
	
	int nano = 1000000000;
	int income = 100;
	int funds1, funds2;
	int turn, highestTurn = 10;
	int petType = 0;
	
	float stepTime = 0.02f;
	byte team = 1;

	private Music music;
	Audio tempMusic = Gdx.audio;

	static float musicVolume = 1f;
	
	static boolean showRange;
	boolean spawning;
	boolean running;
	
	//	mrwizard swordface arroweyes
	String[] heroNames = {"mrwizard", "swordface"};
	
//	heroNames[0] = "mrwizard";
//	heroNames[1] = "arroweyes";
	
	Hero playerHeroes[];
	Building playerBases[];
	
	LinkedList<Integer>[] pools;			// Pools for spawning
	ArrayList<Entity> entities;		// Holds all units, towers, projectiles, and spells
	static ArrayList<Projectile> projectiles;
	private ArrayList<ParticleEffect> effects;
	

	MinionStructure[][] playerUnits;// = new String[2][6]; 
	HeroStructure[] heroUnits;
	
	HashMap<String, MinionStructure> minionStats = new HashMap<String, MinionStructure>();
	HashMap<String, BuildingStructure> buildingStats = new HashMap<String, BuildingStructure>();
	HashMap<String, HeroStructure> heroStats = new HashMap<String, HeroStructure>();
	HashMap<String, ParticleEffect> particleEffects = new HashMap<String, ParticleEffect>();
	HashMap<String, SkillStructure> skillStats = new HashMap<String, SkillStructure>();
	HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	HashMap<String, SoundPack> unitSounds = new HashMap<String, SoundPack>();
	HashMap<String, TextureRegion> objectTextures = new HashMap<String, TextureRegion>();
	HashMap<String, UnitAnimation> unitAnimations = new HashMap<String, UnitAnimation>();
	public HashMap<String, BuildingAnimation> buildingAnimations = new HashMap<String, BuildingAnimation>();
	
	public BitmapFont[] font = new BitmapFont[3]; //, font2;
	private Texture teamTextures[] = new Texture[2];
	
	String[] color = {"blue", "red"};
	
	boolean finished = false;
	
	Settings settings = new Settings();
		
	public EverythingHolder()
	{
		reset();
		
//		pools[0] = new LinkedList<Integer>();
//		pools[1] = new LinkedList<Integer>();
//		pools[2] = new LinkedList<Integer>();
//		pools[3] = new LinkedList<Integer>();
		
		UnitParser unitParser = new UnitParser();
		minionStats = unitParser.getMinionStats();
		buildingStats = unitParser.getBuildingStats();
		heroStats = unitParser.getHeroStats();
		skillStats = unitParser.getSkillStats();
		
//		initializeHeroes();
		
//		Entity.loadStatics(effects);
		// Wave control
//		waveTimer = System.nanoTime() / 1000000; // Timer to keep track of waves
		waveInterval = 	(int) (10 / stepTime); 	// Turns (15 seconds)
		waveTime = 		(int) (3 / stepTime);	// Turns (2 seconds)
				
		spawning = false;
//		previousTime = System.nanoTime() / 1000000;
//		Actor.loadProjectiles(projectiles);
		Entity.linkHolder(this);
		
		// Load all assets
		loadEffects();
		loadSounds();
		loadTextures();
//		loadTeams(color[0], color[1]);
//		loadUnitAnimations();
		
//		font = new BitmapFont();
//		font2 = new BitmapFont();
//		font2.scale(2);
		finished = true;
//		music = tempMusic.newMusic(Gdx.files.internal("audio/506819_Xanax-amp-Bluebird3.wav"));
//		music.setLooping(true);
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
		teamTextures[0] = new Texture(Gdx.files.internal("images/sprite_sheet_" + one + ".png"));
		teamTextures[1] = new Texture(Gdx.files.internal("images/sprite_sheet_" + two + ".png"));
		loadUnitAnimations();
		heroNames[0] = hone;
		heroNames[1] = htwo;
//		initializeHeroes()
		
	}
	
	public UnitAnimation getUnitAnimation(String name)
	{
		return unitAnimations.get(name);
	}
	
	public BuildingAnimation getBuildingAnimation(String name)
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
		System.out.println("Loading unit animations");
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
			
			animation[0] = loadAnimation(0, 0, 29, 47, 5, false, false, i);	// Down walk
			animation[1] = loadAnimation(0, 47, 34, 43, 5, false, false, i);	// Left walk
			animation[2] = loadAnimation(0, 47, 34, 43, 5, true, false, i);	// Right walk
			animation[3] = loadAnimation(0, 90, 31, 44, 5, false, false, i);	// Up walk
			animation[4] = loadAnimation(0, 134, 40, 46, 3, false, true, i);	// Down attack
			animation[5] = loadAnimation(0, 180, 38, 45, 3, false, true, i);	// Left attack
			animation[6] = loadAnimation(0, 180, 38, 45, 3, true, true, i);	// Right attack
			animation[7] = loadAnimation(0, 225, 46, 42, 3, false, true, i);	// Up attack
			
			points[0] = new Vector2(15, 5);
			points[1] = new Vector2(19, 2);
			points[2] = new Vector2(16, 2);
			points[3] = new Vector2(16, 2);
			points[4] = new Vector2(26, 8);
			points[5] = new Vector2(24, 4);
			points[6] = new Vector2(15, 4);
			points[7] = new Vector2(16, 1);
			
			unitAnimations.put("swordsman" + (i + 1), new UnitAnimation(animation, points));
			System.out.println("Puting: swordsman" + i + 1);
//			animationsR.add(unitAnimation);
			
			// Archer			
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(169, 0, 35, 46, 5, false, false, i);		// Down walk
			animation[1] = loadAnimation(169, 46, 35, 42, 5, false, false, i);		// Left walk
			animation[2] = loadAnimation(169, 46, 35, 42, 5, true, false, i);		// Right walk
			animation[3] = loadAnimation(169, 88, 41, 46, 5, false, false, i);		// Up walk
			animation[4] = loadAnimation(169, 134, 33, 44, 4, false, true, i);	// Down attack
			animation[5] = loadAnimation(169, 178, 39, 46, 4, false, true, i);	// Left attack
			animation[6] = loadAnimation(169, 178, 39, 46, 4, true, true, i);		// Right attack
			animation[7] = loadAnimation(169, 224, 40, 45, 4, false, true, i);	// Up attack
			
			points[0] = new Vector2(15, 2);
			points[1] = new Vector2(17, 1);
			points[2] = new Vector2(19, 1);
			points[3] = new Vector2(22, 2);
			points[4] = new Vector2(15, 1);
			points[5] = new Vector2(20, 5);
			points[6] = new Vector2(19, 5);
			points[7] = new Vector2(18, 1);
			
			unitAnimations.put("archer" + (i + 1), new UnitAnimation(animation, points));
			
			// Monk
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(0, 267, 30, 41, 5, false, false, i);
			animation[1] = loadAnimation(0, 308, 27, 42, 5, false, false, i);
			animation[2] = loadAnimation(0, 308, 27, 42, 5, true, false, i);
			animation[3] = loadAnimation(0, 350, 30, 41, 5, false, false, i);
			animation[4] = loadAnimation(0, 391, 30, 41, 4, false, true, i);
			animation[5] = loadAnimation(0, 432, 27, 41, 4, false, true, i);
			animation[6] = loadAnimation(0, 432, 27, 41, 4, true, true, i);
			animation[7] = loadAnimation(0, 474, 39, 41, 4, false, true, i);
			
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
			
			animation[0] = loadAnimation(169, 269, 33, 48, 5, false, false, i);
			animation[1] = loadAnimation(169, 317, 30, 43, 5, false, false, i);
			animation[2] = loadAnimation(169, 317, 30, 43, 5, true, false, i);
			animation[3] = loadAnimation(169, 360, 33, 45, 5, false, false, i);
			animation[4] = loadAnimation(169, 405, 45, 46, 4, false, true, i);
			animation[5] = loadAnimation(169, 451, 36, 43, 4, false, true, i);
			animation[6] = loadAnimation(169, 451, 36, 43, 4, true, true, i);
			animation[7] = loadAnimation(169, 494, 40, 45, 4, false, true, i);
			
			points[0] = new Vector2(16, 5);
			points[1] = new Vector2(14, 1);
			points[2] = new Vector2(17, 1);
			points[3] = new Vector2(16, 5);
			points[4] = new Vector2(28, 4);
			points[5] = new Vector2(20, 1);
			points[6] = new Vector2(17, 1);
			points[7] = new Vector2(16, 3);
			
			unitAnimations.put("mage" + (i + 1), new UnitAnimation(animation, points));
			
			/////////////////////////// STIL NEED TO DO POINTS FOR NINJA TO ELEMENTAL ////////////////
			// Ninja
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(374, 0, 31, 42, 5, false, false, i);
			animation[1] = loadAnimation(374, 42, 29, 41, 5, false, false, i);
			animation[2] = loadAnimation(374, 42, 29, 41, 5, true, false, i);
			animation[3] = loadAnimation(374, 83, 31, 41, 5, false, false, i);
			animation[4] = loadAnimation(374, 124, 45, 41, 4, false, true, i);
			animation[5] = loadAnimation(374, 165, 30, 41, 4, false, true, i);
			animation[6] = loadAnimation(374, 165, 30, 41, 4, true, true, i);
			animation[7] = loadAnimation(374, 206, 42, 40, 4, false, true, i);
			
			points[0] = new Vector2(15, 5);
			points[1] = new Vector2(19, 2);
			points[2] = new Vector2(16, 2);
			points[3] = new Vector2(16, 2);
			points[4] = new Vector2(26, 8);
			points[5] = new Vector2(24, 4);
			points[6] = new Vector2(15, 4);
			points[7] = new Vector2(16, 1);
			
			unitAnimations.put("ninja" + (i + 1), new UnitAnimation(animation, points));
			
			// Eagle
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(374, 246, 44, 84, 4, false, false, i);
			animation[1] = loadAnimation(374, 330, 45, 84, 4, false, false, i);
			animation[2] = loadAnimation(374, 330, 45, 84, 4, true, false, i);
			animation[3] = loadAnimation(374, 414, 43, 78, 4, false, false, i);
			animation[4] = loadAnimation(374, 492, 44, 59, 4, false, true, i);
			animation[5] = loadAnimation(374, 551, 44, 59, 4, false, true, i);
			animation[6] = loadAnimation(374, 551, 44, 59, 4, true, true, i);
			animation[7] = loadAnimation(374, 610, 43, 57, 4, false, true, i);
			
			points[0] = new Vector2(15, 5);
			points[1] = new Vector2(19, 2);
			points[2] = new Vector2(16, 2);
			points[3] = new Vector2(16, 2);
			points[4] = new Vector2(26, 8);
			points[5] = new Vector2(24, 4);
			points[6] = new Vector2(15, 4);
			points[7] = new Vector2(16, 1);
			
			unitAnimations.put("eagle" + (i + 1), new UnitAnimation(animation, points));
			
			// Wolf
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(554, 0, 18, 53, 4, false, false, i);
			animation[1] = loadAnimation(554, 53, 56, 31, 4, false, false, i);
			animation[2] = loadAnimation(554, 53, 56, 31, 4, true, false, i);
			animation[3] = loadAnimation(554, 84, 20, 50, 4, false, false, i);
			animation[4] = loadAnimation(554, 134, 24, 55, 4, false, true, i);
			animation[5] = loadAnimation(554, 189, 56, 35, 4, false, true, i);
			animation[6] = loadAnimation(554, 189, 56, 35, 4, true, true, i);
			animation[7] = loadAnimation(554, 224, 22, 48, 4, false, true, i);
			
			points[0] = new Vector2(15, 5);
			points[1] = new Vector2(19, 2);
			points[2] = new Vector2(16, 2);
			points[3] = new Vector2(16, 2);
			points[4] = new Vector2(26, 8);
			points[5] = new Vector2(24, 4);
			points[6] = new Vector2(15, 4);
			points[7] = new Vector2(16, 1);
			
			unitAnimations.put("wolf" + (i + 1), new UnitAnimation(animation, points));
	
			// Elemental
			animation = new Animation[8];
			points = new Vector2[8];
			
			animation[0] = loadAnimation(554, 272, 51, 38, 5, false, false, i);
			animation[1] = loadAnimation(554, 310, 32, 38, 5, false, false, i);
			animation[2] = loadAnimation(554, 310, 32, 38, 5, true, false, i);
			animation[3] = loadAnimation(554, 348, 50, 39, 5, false, false, i);
			animation[4] = loadAnimation(554, 387, 51, 38, 4, false, true, i);
			animation[5] = loadAnimation(554, 425, 32, 37, 4, false, true, i);
			animation[6] = loadAnimation(554, 425, 32, 37, 4, true, true, i);
			animation[7] = loadAnimation(554, 462, 56, 39, 4, false, true, i);
			
			points[0] = new Vector2(15, 5);
			points[1] = new Vector2(19, 2);
			points[2] = new Vector2(16, 2);
			points[3] = new Vector2(16, 2);
			points[4] = new Vector2(26, 8);
			points[5] = new Vector2(24, 4);
			points[6] = new Vector2(15, 4);
			points[7] = new Vector2(16, 1);
			
			unitAnimations.put("elemental" + (i + 1), new UnitAnimation(animation, points));
			
			// Stronghold
			animation = new Animation[1];
			points = new Vector2[1];
			
			animation[0] = loadAnimation(0, 670, 72, 128, 1, false, false, i);
			points[0] = new Vector2(42, 8);
			
			buildingAnimations.put("stronghold" + (i + 1), new BuildingAnimation(animation, points));
			
			// Arrowtower
			animation = new Animation[1];
			points = new Vector2[1];
			
			animation[0] = loadAnimation(0, 580, 56, 90, 3, false, false, i);
			points[0] = new Vector2(28, 2);
			
			buildingAnimations.put("arrowtower" + (i + 1), new BuildingAnimation(animation, points));
			
			// Rubble
			animation = new Animation[1];
			points = new Vector2[1];
			
			animation[0] = loadAnimation(0, 515, 47, 65, 3, false, false, i);
			points[0] = new Vector2(23, 7);
			
			buildingAnimations.put("rubble" + (i + 1), new BuildingAnimation(animation, points));
		}
		System.out.println("Loaded unit animations");
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
	
	public SoundPack getUnitSounds(String name)
	{
		return unitSounds.get(name);
	}
	
	public Sound getSound(String sound)
	{
		return sounds.get(sound);
	}
	
	public SkillStructure getSkill(String skill)
	{
		return skillStats.get(skill);
	}
	
	public float heroHealthRatio()
	{
		return playerHeroes[team - 1].getHealthRatio();
	}
	
	public float baseHealthRatio()
	{
		return playerBases[team - 1].getHealthRatio();
	}
	
	public TextureRegion getObjectTexture(String name)
	{
		return objectTextures.get(name);
	}
	
	public void loadTextures()
	{
		objectTextures.put("cannonball", new TextureRegion(new Texture(Gdx.files.internal("images/cannonprojectile.png")), 0, 0, 16, 16));
		Texture icons = new Texture(Gdx.files.internal("images/buttons_sheet.png"));
		objectTextures.put("fireball", new TextureRegion(icons, 1814, 0, 90, 90));
		objectTextures.put("fireattack", new TextureRegion(icons, 1814, 90, 73, 73));
		
		objectTextures.put("swordfacebutton", new TextureRegion(icons, 0, 1377, 192, 137));
		objectTextures.put("arroweyesbutton", new TextureRegion(icons, 192, 1377, 192, 137));
		objectTextures.put("mrwizardbutton", new TextureRegion(icons, 384, 1377, 192, 137));
		
		objectTextures.put("upgradebutton", new TextureRegion(icons, 1905, 109, 143, 109));
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
		
		objectTextures.put("gamelogo", new TextureRegion(icons, 0, 1514, 842, 467));
		objectTextures.put("mainbuttonframe", new TextureRegion(icons, 880, 422, 361, 572));
		
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
				"stunattack",
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
	
	public ParticleEffect getEffect(String e)
	{
//		System.out.println("Get");
//		if (Gdx.app.getType() == ApplicationType.Android)
//			return null;
		if (particleEffects.containsKey(e))
			return new ParticleEffect(particleEffects.get(e));
		System.out.println("Missing Effect");
		return null;
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
		playerHeroes[0] = new Hero(map.start1().x(), map.start1().y(), 1, map().getPath().listIterator(), heroUnits[0]);
		playerHeroes[1] = new Hero(map.start2().x(), map.start2().y(), 2, map().getReversePath().listIterator(), heroUnits[1]);
		add(playerHeroes[0], 1);
		add(playerHeroes[1], 2);
		System.out.println("PET " + playerHeroes[0].pet());
		System.out.println("PET " + playerHeroes[1].pet());
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
	}
	
	public void activeHeroSkill(int team)
	{
		playerHeroes[team-1].activeSkill();
	}
	
	public int turn()
	{
		return turn;
	}
	
	public void setTeam(byte team)
	{
		System.out.println("Team: " + team);
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
		return (left > 10 ? 10 : left);
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
	
	public void add(Entity a, int team)
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
		int loc = entities.indexOf(null);
		if (loc >= 0)
			entities.add(loc, a);
		else
			entities.add(a);
	}
	
	/*public void addHero(Hero h, int team)
	{
		if (team == 1)
			hero1 = h;
		else
			hero2 = h;
	}*/
	
	public void add(int unit, int team)
	{
//		System.out.println("Trying to add " + unit + " on team " + team);
//		if (team == this.team)//(team == 0)
//		{
		int cost = playerUnits[team - 1][unit].cost(0);
			if ((team == 1 ? funds1 : funds2) < cost)
				return;
			if (team == 1)
				funds1 -= cost;
			else
				funds2 -= cost;
			
			if (team == this.team)
				Gdx.input.vibrate(50);
//		}
//		else
//		{
//			if ((team == 1 ? funds2 : funds) < 20)
//				return;
//			if (team == 1)
//				funds1 -= 20;
//			else
//				funds2 -= 20;
//		}
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
		ListIterator<Coordinate> iter = (team == 1 ? map.getPath().listIterator() : map.getReversePath().listIterator());
		add(new Minion(start.x(), start.y(), team, iter, playerUnits[team - 1][m], 0), team);
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
	
	public int teamSize()
	{
		int temp = 0;
		for (Entity a : entities)
			if (a instanceof Actor && a.isAlive() && !(a instanceof Stronghold) && a.team() == team)
//			if (a instanceof Actor && a.isAlive() && (t == 0 || ((Actor)a).team() == t))
				temp++;
		return temp;
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
		for (Projectile p : projectiles)
			p.draw(batch, delta);
		
		// Reordering changed entities for proper depth rendering
//		Iterator<Entity> entityIter = entities.iterator();
//		changedEntity = new ArrayList<Entity>();
//		for (Entity e : entities)
//			if (e.yCoordChanged())
//				changedEntity.add(e);
//		for (Entity e : changedEntity)
//		{
//			entities.remove(e);
//			entities.add(e);
//		}
		
		/*entityDraw.clear();
		for (Entity e : entities)
			entityDraw.add(e);
		
		while (entityDraw.peek() != null)
		{
			Entity e = entityDraw.remove();*/
		Collections.sort(entities, eCompare);
		
		Entity e;
		for (int i = 0; i < entities.size(); i++)
		{
			e = entities.get(i);
			if (e instanceof Building || (e instanceof Actor && ((Entity)e).isAlive()) || (e instanceof Skill && ((Entity)e).isAlive()))
				e.draw(batch, delta);
			else if (!(e instanceof Hero))
			{
//				System.out.println("DEAD");
				entities.set(i, null);
			}
		}
		
//		while (entityIter.hasNext())
//		{
//			e = entityIter.next();
//			if (e instanceof Building || (e instanceof Actor && ((Actor)e).isAlive()))
//			{
//				e.draw(batch);
//			}
//		}
		
		
		
//		for (Entity e : entities)
//		{
////			if (e instanceof Actor && ((Actor)e).isAlive())
//			if (e instanceof Building || (e instanceof Actor && ((Actor)e).isAlive()))
//			{
//				e.draw(batch);
//			}
//			else if (!(e instanceof Hero))
//			{
//				
//			}
//		}
		
//		Iterator<Actor> actorIter = teams[0].iterator();
//		Actor a;
//		while (actorIter.hasNext())
//		{
//			a = actorIter.next();
//			if (a instanceof Stronghold)
//				continue;
//			if (a.isAlive() || (a instanceof ArrowTower))
//				a.draw(batch);
//			else if (!(a instanceof Hero))
//			{
//				//effects.add(a.blood());
//				a.destroy();
//				actorIter.remove();
//			}
//		}
		
		/*if (hero1 != null && hero1.isAlive())
			hero1.draw(batch);*/
		
//		actorIter = teams[1].iterator();
//		while (actorIter.hasNext())
//		{
//			a = actorIter.next();
//			if (a instanceof Stronghold)
//				continue;
//			if (a.isAlive() || (a instanceof ArrowTower))
//			{
//				a.draw(batch);
//			}
//			else if (!(a instanceof Hero))
//			{
//				//effects.add(a.blood());
//				a.destroy();
//				actorIter.remove();
//			}
//		}
		for (ParticleEffect pe : effects)
		{
			pe.draw(batch, delta * 0.5f);
		}
		Iterator<ParticleEffect> iter = effects.iterator();
//		ParticleEffect e = new ParticleEffect();
		while (iter.hasNext())
		{
			if ((iter.next()).isComplete())
				iter.remove();
//			e = (ParticleEffect) iter.next();
//			if (e.isComplete())
//				iter.remove();
		}
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
//			for (Actor ac : teams[0])
//				if (ac.isAlive())
//					ac.rangeIndicator(batch);
//			for (Actor ac : teams[1])
//				if (ac.isAlive())
//					ac.rangeIndicator(batch);
//		}
	}
	
	public int unitCost(int unit, int team)
	{
		return playerUnits[team - 1][unit].cost(0);
	}
	
	public void update()
	{
//		for (Actor a : teams[0])
//			a.checkAlive();
//		for (Actor a : teams[1])
//			a.checkAlive();
		for (Entity e : entities)
		{
			if (e instanceof Actor)
				((Actor)e).checkAlive();
		}
		
		ArrayList<Projectile> removeList = new ArrayList<Projectile>();
		for (Projectile p : projectiles)
		{
				//if (p.xCoord == this.target.xCoord() || )
			p.update();
			if (p.getxSpeed() > 0 && p.xCoord() > p.target().xCoord())
				removeList.add(p);
			else if (p.getxSpeed() < 0 && p.xCoord() < p.target().xCoord())
				removeList.add(p);
			else if (p.getySpeed() > 0 && p.yCoord() > p.target().yCoord())
				removeList.add(p);
			else if (p.getySpeed() < 0 && p.yCoord() < p.target().yCoord())
				removeList.add(p);
		}
		
		projectiles.removeAll(removeList);
		
		/*if (hero1 != null)
			hero1.isAlive();
		if (hero2 != null)
			hero2.isAlive();*/
		
//		for (Actor a : teams[0])
//			if (a.isAlive())
//				a.update();
//		for (Actor a : teams[1])
//			if (a.isAlive())
//				a.update();
		for (int i = 0; i < entities.size(); i++)
		{
			if (entities.get(i) != null && (entities.get(i).isAlive() || entities.get(i) instanceof Hero))
				entities.get(i).update();
		}
//		for (Entity e : entities)
//		{
//			if (e != null)
//			{
//				try
//				{
//				e.update();
//			}
//		}
		
		/*if (hero1 != null)
			hero1.update();
		if (hero2 != null)
			hero2.update();*/
		
//		music.setVolume(musicVolume);
		if (++turn % 10 == 0)
			System.out.println("Turn: " + turn);
		spawnTimers();
		if (!playerHeroes[0].isAlive() && playerHeroes[0].canRespawn())
			playerHeroes[0].respawn(map.start1().x(), map.start1().y(), map.getPath().listIterator());
		if (!playerHeroes[1].isAlive() && playerHeroes[1].canRespawn())
			playerHeroes[1].respawn(map.start2().x(), map.start2().y(), map.getReversePath().listIterator());
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
		if ((timeDiff) > waveInterval)
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
			pools[2] = pools[0];
			pools[3] = pools[1];
			pools[0] = new LinkedList<Integer>();
			pools[1] = new LinkedList<Integer>();
			if (pools[2] == null)
				pools[2] = new LinkedList<Integer>();
			if (pools[3] == null)
				pools[3] = new LinkedList<Integer>();
			
//			if (!playerHeroes[0].isAlive())
//				playerHeroes[0].respawn(map.start1().x(), map.start1().y(), map.getPath().listIterator());
//			if (!playerHeroes[1].isAlive())
//				playerHeroes[1].respawn(map.start2().x(), map.start2().y(), map.getReversePath().listIterator());
			
//			for (Actor a : teams[0])
//				if (a instanceof Hero && !a.isAlive())
//					((Hero)a).respawn(map.start1().x(), map.start1().y(), map.getPath().listIterator());
//			for (Actor a : teams[1])
//				if (a instanceof Hero && !a.isAlive())
//					((Hero)a).respawn(map.start2().x(), map.start2().y(), map.getReversePath().listIterator());
//					((Hero)a).respawn(map.start2().x(), map.start2().y(), map.getPath().listIterator(map.getPath().size() - 1));
//			if (Settings.getInstance().getDifficulty() == Difficulty.EASY)
//				funds += income;
//			else if (Settings.getInstance().getDifficulty() == Difficulty.HARD)
//				funds += income * .75;
			
			funds1 += income;
			funds2 += income;
			//Gdx.input.vibrate(1000);
		}
		
		/*if (spawning && pools[2].isEmpty() && pools[3].isEmpty())
			spawning = false;
		
		if (spawning)
		{*/
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
		//}
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
	
	public void load(SpriteBatch b, Map m)
	{
		batch = b;
		map = m;
//		initializeHeroes();
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
		
		entities = new ArrayList<Entity>(50);
		projectiles = new ArrayList<Projectile>();
		effects = new ArrayList<ParticleEffect>();
		Actor.loadProjectiles(projectiles);
		
		Entity.loadStatics(effects);
		funds1 = 200;
		funds2 = 200;
		turn = 0;
		
		font[0] = new BitmapFont();
		font[1] = new BitmapFont();
		font[1].scale(2);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BaroqueScript.ttf"));
		font[2] = generator.generateFont(36);
		font[2].setColor(1, 1, 1, 1);
//		font[2] = 
	}
}
